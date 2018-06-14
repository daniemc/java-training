package co.com.s4n.training.java.jdk;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class CompletableFutureSuite {

    public void printMessage(String message){
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        String formatedDate = dateFormat.format(date);

        System.out.println(formatedDate + " " + message);
    }

    private void sleep(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        }catch(Exception e){
            System.out.println("Problemas durmiendo hilo");
        }
    }

    @Test
    public void t1() {

        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();


        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> {
            Thread.sleep(300);

            completableFuture.complete("Hello");
            return null;
        });
            System.out.println(Thread.currentThread().getName());

        try {
            String s = completableFuture.get(500, TimeUnit.MILLISECONDS);
            assertEquals(s, "Hello");
        }catch(Exception e){
            assertTrue(false);
        }finally{
            executorService.shutdown();

        }

    }

    @Test
    public void t2(){
        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> {
            Thread.sleep(300);

            completableFuture.complete("Hello");
            return null;
        });

        try {
            String s = completableFuture.get(500, TimeUnit.MILLISECONDS);
            assertEquals(s, "Hello");
        }catch(Exception e){
            assertTrue(false);
        }finally{
            executorService.shutdown();
        }
    }

    @Test
    public void t3(){
        // Se puede construir un CompletableFuture a partir de una lambda Supplier (que no recibe parámetros pero sí tiene retorno)
        // con supplyAsync
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "Hello";
        });

        try {
            String s = future.get(500, TimeUnit.MILLISECONDS);
            assertEquals(s, "Hello");
        }catch(Exception e){

            assertTrue(false);
        }
    }

    @Test
    public void t4(){

        int i = 0;
        // Se puede construir un CompletableFuture a partir de una lambda (Supplier)
        // con runAsync
        Runnable r = () -> {
            sleep(300);
            System.out.println("Soy impuro y no merezco existir");
        };

        // Note el tipo de retorno de runAsync. Siempre es un CompletableFuture<Void> asi que
        // no tenemos manera de determinar el retorno al completar el computo
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(r);

        try {
            voidCompletableFuture.get(500, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void t5(){

        String testName = "t5";

        System.out.println(testName + " - El test (hilo ppal) esta corriendo en: "+Thread.currentThread().getName());

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            System.out.println(testName + " - completbleFuture corriendo en el thread: "+Thread.currentThread().getName());
            return "Hello";
        });

        //thenApply acepta lambdas de aridad 1 con retorno
        CompletableFuture<String> future = completableFuture
                .thenApply(s -> {
                    printMessage(testName + " - future corriendo en el thread: "+Thread.currentThread().getName());
                    sleep(500);
                    return s + " World";
                })
                .thenApply(s -> {
                    printMessage(testName + " - future corriendo en el thread: "+Thread.currentThread().getName());

                    return s + "!";
                });

        try {
            assertEquals("Hello World!", future.get());
        }catch(Exception e){
            assertTrue(false);
        }
    }


    @Test
    public void t6(){

        String testName = "t6";

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            System.out.println(testName + " - completbleFuture corriendo en el thread: "+Thread.currentThread().getName());
            return "Hello";
        });

        // thenAccept solo acepta Consumer (lambdas de aridad 1 que no tienen retorno)
        // analice el segundo thenAccept ¿Tiene sentido?
        CompletableFuture<Void> future = completableFuture
                .thenAccept(s -> {
                    printMessage(testName + " - future corriendo en el thread: " + Thread.currentThread().getName() + " lo que viene del futuro es: "+s);
                    sleep(500);
                })
                .thenAccept(s -> {
                    printMessage(testName + " - future corriendo en el thread: " + Thread.currentThread().getName() + " lo que viene del futuro es: "+s);
                });

    }

    @Test
    public void t7(){

        String testName = "t7";

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(testName + " - completbleFuture corriendo en el thread: "+Thread.currentThread().getName());
            return "Hello";
        });

        //thenAccept solo acepta Consumer (lambdas de aridad 1 que no tienen retorno)
        CompletableFuture<Void> future = completableFuture
                .thenRun(() -> {
                    printMessage(testName + " - future corriendo en el thread: " + Thread.currentThread().getName());
                    sleep(500);
                })
                .thenRun(() -> {
                    printMessage(testName + " - future corriendo en el thread: " + Thread.currentThread().getName());
                });

    }

    @Test
    public void t8(){

        String testName = "t8";

        CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(testName + " - future corriendo en el thread: " + Thread.currentThread().getName());
                    return "Hello";
                })
                .thenCompose(s -> {
                    System.out.println(testName + " - compose corriendo en el thread: " + Thread.currentThread().getName());
                    return CompletableFuture.supplyAsync(() ->{
                        System.out.println(testName + " - CompletableFuture interno corriendo en el thread: " + Thread.currentThread().getName());
                        return s + " World"  ;
                    } );
                });

        try {
            assertEquals("Hello World", completableFuture.get());
        }catch(Exception e){
            assertTrue(false);
        }
    }

    class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    public void t88(){

        String testName = "t8";

        CompletableFuture<Person> completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(testName + " - person future corriendo en el thread: " + Thread.currentThread().getName());
                    return "Daniel.26";
                })
                .thenCompose(s -> {
                    System.out.println(testName + " - person compose corriendo en el thread: " + Thread.currentThread().getName());
                    return CompletableFuture.supplyAsync(() ->{
                        System.out.println(testName + " - Person CompletableFuture interno corriendo en el thread: " + Thread.currentThread().getName());
                        String[] splitedPerson = s.split(Pattern.quote("."));
                        Person person = new Person(splitedPerson[0], Integer.parseInt(splitedPerson[1]));
                        return person;

                    } );
                });

        try {
            assertEquals("Daniel", completableFuture.get().name);
            assertEquals(26, completableFuture.get().age);
        }catch(Exception e){
            printMessage(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void t9(){

        String testName = "t9";


        // El segundo parametro de thenCombina es un BiFunction la cual sí tiene que tener retorno.
        CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    printMessage(testName + " completable corriendo en " + Thread.currentThread().getName());
                    return "Hello";
                })
                .thenCombine(
                        CompletableFuture.supplyAsync(() -> {
                            printMessage(testName + " combine corriendo en " + Thread.currentThread().getName());
                            return " World";
                        }),
                        (s1, s2) -> {
                            printMessage(testName + " lambda combine corriendo en " + Thread.currentThread().getName());
                            return s1 + s2;
                        }
                );

        try {
            assertEquals("Hello World", completableFuture.get());
        }catch(Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void t10(){

        String testName = "t10";

        // El segundo parametro de thenAcceptBoth debe ser un BiConsumer. No puede tener retorno.
        CompletableFuture future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptBoth(
                        CompletableFuture.supplyAsync(() -> " World"),
                        (s1, s2) -> System.out.println(testName + " corriendo en thread: "+Thread.currentThread().getName()+ " : " +s1 + s2));

        try{
            Object o = future.get();
        }catch(Exception e){
            assertTrue(false);

        }
    }

    @Test
    public void linkToSupplyAsync(){
        String testName = "T9";

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello", executorService);

        CompletableFuture<String> anotherFuture = future.supplyAsync(()-> {
            printMessage(testName + " Ejecutando 1");
            sleep(500);
            return "1";
        }).supplyAsync(()-> {
            printMessage(testName + " Ejecutando 2");
            return "2";
        });

        try {
            assertEquals(anotherFuture.get(), "2");
        } catch (Exception e) {

        }
    }

    @Test
    public void linkToSupplyAsync2(){
        String testName = "T9-2";

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello", executorService);

        CompletableFuture<String> anotherFuture = future.supplyAsync(()-> {
            printMessage(testName + " Ejecutando 1");
            sleep(500);
            return "1";
        }, executorService).supplyAsync(()-> {
            printMessage(testName + " Ejecutando 2");
            return "2";
        }, executorService);

        try {
            assertEquals(anotherFuture.get(), "2");
        } catch (Exception e) {

        }
    }

    @Test
    public void t11(){

        String testName = "t11";

        ExecutorService es = Executors.newFixedThreadPool(1);
        CompletableFuture f = CompletableFuture.supplyAsync(()->"Hello",es);

        f.supplyAsync(() -> "Hello")
                .thenCombineAsync(
                    CompletableFuture.supplyAsync(() -> {
                        System.out.println(testName + " thenCombineAsync en Thread (1): " + Thread.currentThread().getName());
                        return " World";
                    }),
                    (s1, s2) -> {
                        System.out.println(testName + " thenCombineAsync en Thread (2): " + Thread.currentThread().getName());
                        return s1 + s2;
                    },
                    es
                );

    }

    @Test
    public void t12(){
        printMessage("hi message");
    }

    @Test
    public void applyAsincTestSameExS(){
        String testName = "applyAsincTestSameExS";

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello", executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-1 " + Thread.currentThread().getName());
                    return s + " I'm Async 1";
                }, executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-2 " + Thread.currentThread().getName());
                    return s + " I'm Async 2";
                }, executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-3 " + Thread.currentThread().getName());
                    return s + " I'm Async 3";
                }, executorService);

        try {
            printMessage(""+future.get());
        }catch (Exception e) {

        }
    }

    @Test
    public void applyAsincTestDifExS(){
        String testName = "applyAsincTestDifExS";

        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello")
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-1 " + Thread.currentThread().getName());
                    return s + " I'm Async 1";
                })
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-2 " + Thread.currentThread().getName());
                    return s + " I'm Async 2";
                })
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-3 " + Thread.currentThread().getName());
                    return s + " I'm Async 3";
                });

        try {
            printMessage(""+future.get());
        }catch (Exception e) {

        }
    }

    @Test
    public void applyAsincTestSameExS_Sleep(){
        String testName = "applyAsincTestSameExS_Sleep";

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello", executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-1 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 1";
                }, executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-2 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 2";
                }, executorService)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-3 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 3";
                }, executorService);

        try {
            printMessage(""+future.get());
        }catch (Exception e) {

        }
    }

    @Test
    public void applyAsincTestDifExS_Sleep(){
        String testName = "applyAsincTestDifExS_Sleep";

        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello")
                .thenApplyAsync((s)-> {
                    sleep(500);
                    printMessage(testName + " As-1 " + Thread.currentThread().getName());
                    return s + " I'm Async 1";
                })
                .thenApplyAsync((s)-> {
                    sleep(500);
                    printMessage(testName + " As-2 " + Thread.currentThread().getName());
                    return s + " I'm Async 2";
                })
                .thenApplyAsync((s)-> {
                    sleep(500);
                    printMessage(testName + " As-3 " + Thread.currentThread().getName());
                    return s + " I'm Async 3";
                });

        try {
            printMessage(""+future.get());
        }catch (Exception e) {

        }
    }

    @Test
    public void applyAsincTestMultiExS_Sleep(){
        String testName = "applyAsincTestSameExS_Sleep";

        ExecutorService executorService1 = Executors.newFixedThreadPool(1);
        ExecutorService executorService2 = Executors.newFixedThreadPool(1);
        ExecutorService executorService3 = Executors.newFixedThreadPool(1);
        ExecutorService executorService4 = Executors.newFixedThreadPool(1);
        CompletableFuture future = CompletableFuture.supplyAsync(()-> "Hello", executorService1)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-1 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 1";
                }, executorService2)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-2 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 2";
                }, executorService3)
                .thenApplyAsync((s)-> {
                    printMessage(testName + " As-3 " + Thread.currentThread().getName());
                    sleep(500);
                    return s + " I'm Async 3";
                }, executorService4);

        try {
            printMessage(""+future.get());
        }catch (Exception e) {

        }
    }

}
