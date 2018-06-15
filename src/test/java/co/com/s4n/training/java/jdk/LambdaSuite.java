package co.com.s4n.training.java.jdk;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

public class LambdaSuite {

    @FunctionalInterface
    interface InterfaceDeEjemplo{
        int metodoDeEjemplo(int x, int y);
    }

    class ClaseDeEjemplo{
        public int metodoDeEjemplo1(int z, InterfaceDeEjemplo i){
            return z + i.metodoDeEjemplo(1,2);
        }

        public int metodoDeEjemplo2(int z, BiFunction<Integer, Integer, Integer> fn){
            return z + fn.apply(1,2);
        }
    }

    @Test
    public void smokeTest() {
        assertTrue(true);
    }

    @Test
    public void usarUnaInterfaceFuncional1(){

        InterfaceDeEjemplo i = (x,y)->x+y;

        ClaseDeEjemplo instancia = new ClaseDeEjemplo();

        int resultado = instancia.metodoDeEjemplo1(1,i);

        assertTrue(resultado==4);
    }

    @Test
    public void usarUnaInterfaceFuncional2(){

        BiFunction<Integer, Integer, Integer> f = (x, y) -> new Integer(x.intValue()+y.intValue());

        ClaseDeEjemplo instancia = new ClaseDeEjemplo();

        int resultado = instancia.metodoDeEjemplo2(1,f);

        assertTrue(resultado==4);
    }

    @Test
    public void usarUnaInterfaceFuncional3(){
        InterfaceDeEjemplo i = (x,y)->x-y;

        ClaseDeEjemplo instancia = new ClaseDeEjemplo();

        int resultado = instancia.metodoDeEjemplo1(1,i);

        assertTrue(resultado==0);
    }

    @Test
    public void usarUnaInterfaceFuncional4(){

        BiFunction<Integer, Integer, Integer> f = (x, y) -> new Integer((int) Math.pow(x, y));

        ClaseDeEjemplo instancia = new ClaseDeEjemplo();

        int resultado = instancia.metodoDeEjemplo2(1,f);

        assertTrue(resultado==2);
    }

    class ClaseDeEjemplo2{

        public int metodoDeEjemplo2(int x, int y, IntBinaryOperator fn){
            return fn.applyAsInt(x,y);
        }
    }
    @Test
    public void usarUnaFuncionConTiposPrimitivos(){
        IntBinaryOperator f = (x, y) -> x + y;

        ClaseDeEjemplo2 instancia = new ClaseDeEjemplo2();

        int resultado = instancia.metodoDeEjemplo2(1,2,f);

        assertEquals(3,resultado);
    }

    @Test
    public void usarUnaFuncionConTiposPrimitivos2(){
        IntBinaryOperator f = (x, y) -> (int) Math.pow(x,y);

        ClaseDeEjemplo2 instancia = new ClaseDeEjemplo2();

        int resultado = instancia.metodoDeEjemplo2(1,2,f);

        assertEquals(1,resultado);
    }

    class ClaseDeEjemplo3{

        public String operarConSupplier(Supplier<Integer> s){
            return "El int que me han entregado es: " + s.get();
        }
    }

    @Test
    public void usarUnaFuncionConSupplier(){
        Supplier s1 = () -> {
            System.out.println("Cuándo se evalúa esto? (1)");
            return 4;
        };

        Supplier s2 = () -> {
            System.out.println("Cuándo se evalúa esto? (2)");
            return 4;
        };

        ClaseDeEjemplo3 instancia = new ClaseDeEjemplo3();

        String resultado = instancia.operarConSupplier(s2);

        assertEquals("El int que me han entregado es: 4",resultado);
    }

    class ClaseDeEjemplo4{

        private int i = 0;

        public void operarConConsumer(Consumer<Integer> c){
            c.accept(i);
        }
    }

    @Test
    public void usarUnaFuncionConConsumer(){
        Consumer<Integer> c1 = x -> {
            System.out.println("Me han entregado este valor: "+x);

        };

        ClaseDeEjemplo4 instancia = new ClaseDeEjemplo4();

        instancia.operarConConsumer(c1);


    }

    class ClaseDeEjemplo5{
        public void operarConconsumer(Consumer<Integer> c, int i) {
            c.accept(i);
        }
    }

    @Test
    public void usarUnaFuncionConConsumer1() {

        Consumer<Integer> c1 = x -> {
            System.out.println("Me han entregado el valor: " + x);
        };

        ClaseDeEjemplo5 consumer = new ClaseDeEjemplo5();

        consumer.operarConconsumer(c1, 5);
    }

    class ClaseDeEjemploSuplier{

        public Integer operarConSupplier(Supplier<Integer> s1, Supplier<Integer> s2, Supplier<Integer> s3){
            ClaseDeEjemploConsumer consumerIinstance = new ClaseDeEjemploConsumer();

            return s1.get() + s2.get() + s3.get();

        }
    }

    class ClaseDeEjemploConsumer{
        public void operarConconsumer(Consumer<Integer> c, int i) {

            c.accept(i);
        }
    }

    @Test
    public void SupplierAndConsumer() {
        Supplier s1 = () -> {
            return 1;
        };

        Supplier s2 = () -> {
            return 2;
        };

        Supplier s3 = () -> {
            return 3;
        };

        Consumer<Integer> c1 = x -> {
            System.out.println("La suma de los valores (Supplier + Consumer) es " + (x + 4));
        };

        ClaseDeEjemploSuplier supplierInstance = new ClaseDeEjemploSuplier();
        int supplierSum = supplierInstance.operarConSupplier(s1, s2, s3);

        ClaseDeEjemploConsumer consumerIinstance = new ClaseDeEjemploConsumer();

        consumerIinstance.operarConconsumer(c1, supplierSum);

    }

    @FunctionalInterface
    interface InterfaceSupplier{
        public Consumer<Integer> sumaSupplier(
                Supplier<Integer> s1,
                Supplier<Integer> s2,
                Supplier<Integer> s3
        );
    }

    @Test
    public void supplierConsumerTest() {
        InterfaceSupplier i = (x, y, z) -> {
            Integer sum = x.get() + y.get() + z.get();
            Consumer<Integer> c = n -> {
                System.out.println("Suma con lambda: " + (sum + n));
            };

            return c;
        };

        Supplier x = () -> 1;
        Supplier y = () -> 2;
        Supplier z = () -> 3;

        Consumer<Integer> cons = i.sumaSupplier(x, y, z);

        cons.accept(new Integer(4));
    }

    class Family{
        private String name;
        private Integer age;

        public Family(String name, Integer age){
            this.name = name;
            this.age = age;
        }

        public String getName(){
            return this.name;
        }

        public Integer getAge() {
            return this.age;
        }
    }

    @Test
    public void exampleListTest() {
        List<Family> myFamily = new ArrayList<Family>();

        myFamily.add(new Family("Leticia", 45));
        myFamily.add(new Family("Albeiro", 55));
        myFamily.add(new Family("Daniel", 26));
        myFamily.add(new Family("Emilio", 3));


        myFamily.sort((mf1, mf2)->mf1.getName().compareTo(mf2.getName()));
        // myFamily.sort(Comparator.comparing(Family::getName));

        myFamily.forEach((human)-> System.out.println(human.getName()));

        // assertThat(humans.get(0), equalTo(new Human("Albeiro", 55)));
    }
}
