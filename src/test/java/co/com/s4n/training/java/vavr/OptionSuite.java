package co.com.s4n.training.java.vavr;

import org.junit.platform.runner.IncludeEngines;
import org.junit.platform.runner.JUnitPlatform;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;


import io.vavr.PartialFunction;
import io.vavr.control.Option;

import static io.vavr.API.None;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.ArrayList;

import static io.vavr.API.*;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import java.util.List;
import java.util.Optional;

import static io.vavr.API.Some;

@RunWith(JUnitPlatform.class)
@IncludeEngines("junit-jupiter")
public class OptionSuite {

    @Test
    public void testOptionConstruction1(){
        Option<Integer> opt = Option(1);
        assertTrue(opt.isDefined());
        assertEquals(opt, Some(1));
    }

    @Test
    public void testOptionConstruction2(){
        Option<Integer> opt = Option(null);
        assertEquals(opt, Option.none());
    }

    private Boolean isEvenOrNull(Integer num){
        return num%2 == 0 ? true : null;
    }

    @Test
    public void testOptionConstruction3(){
        Option<Boolean> opt = Option(isEvenOrNull(1));
        assertEquals(opt, Option.none());
    }

    private Integer identityOrNull(Integer num){
        return num%2 == 0 ? new Integer(num) : null;
    }

    @Test
    public void testOptionFilter(){
        Option<Integer> opt = Option(identityOrNull(2));
        Option<Integer> optFiltered = opt.filter(num -> num.intValue() < 4);
        assertEquals(optFiltered.getOrElse(0).intValue(), 2);
    }

    @Test
    public void testOptionNoneFilter(){
        Option<Integer> opt = Option(identityOrNull(1));
        Option<Integer> optFiltered = opt.filter(num -> num.intValue() < 4);
        assertEquals(optFiltered, Option.none());
    }

    @Test
    public void testOptionWithMap(){
        Option<Integer> opt = Option(identityOrNull(2));
        Option<Integer> optFiltered = opt.map(num -> num * 4);
        assertEquals(optFiltered.getOrElse(0).intValue(), 8);
    }

    @Test
    public void testOptionWithMapOnNone(){
        Option<Integer> opt = Option(identityOrNull(3));
        Option<Integer> optFiltered = opt.map(num -> num * 4);
        assertEquals(optFiltered, Option.none());
    }

    /**
     * Un option se puede filtar, y mostrar un some() o un none si no encuentra el resultado
     */
    @Test
    public void testOptionWithFilter() {
        Option<Integer> o = Option(3);

        assertEquals(
                Some(3),
                o.filter(it -> it >= 3));

        assertEquals(
                None(),
                o.filter(it -> it > 3));
    }

    /**
     * Se puede hacer pattern matching a un option y comparar entre Some y None.
     */
    private String patternMatchSimple(Option<Integer> number) {
        String result = Match(number).of(
                Case($Some($()),"Existe"),
                Case($None(),"Imaginario")
        );
        return result;
    }

    @Test
    public void testOptionWithPatternMatching() {
        Option<Integer> o1 = Option(1);
        Option<Integer> o2 = None();

        //Comparacion de Some o None()
        assertEquals("Existe", patternMatchSimple(o1));
        assertEquals( "Imaginario", patternMatchSimple(o2));
    }
    /**
     *
     * el metodo peek aplica una funcion lambda o un metodo con el valor de Option cuando esta definido
     * este metodo se usa para efectos colaterales y retorna el mismo Option que lo llamó
     */
    @Test
    public void testPeekMethod(){
        Option<String> defined_option = Option.of("Hello!");
        /* Se debe utilizar una variable mutable para reflejar los efectos colaterales*/
        final List<String> list = new ArrayList<>();
        Option<String> peek = defined_option.peek(list::add);// the same as defined_option.peek(s -> list.add(s))

        System.out.println("peek: "+ peek);

        assertEquals(
                Option.of("Hello!"),
                defined_option);

        assertEquals(
                "Hello!",
                list.get(0));
    }

    /**
     * Un option se puede transformar dada una función
     */
    @Test
    public void testOptionTransform() {
        String textToCount = "Count this text";
        Option<String> text = Option.of(textToCount);
        Option<Integer> count = text.transform(s -> Option.of(s.getOrElse("DEFAULT").length()));

        assertEquals(
                Option.of(textToCount.length()),
                count);

        Option<String> hello = Option.of("Hello");
        Tuple2<String, String> result = hello.transform(s -> Tuple.of("OK", s.getOrElse("DEFAULT")));

        assertEquals(
                Tuple.of("OK", "Hello"),
                result);

    }

    /**
     * el metodo getOrElse permite obtener el valor de un Option o un sustituto en caso de ser None
     */
    @Test
    public void testGetOrElse(){
        Option<String> defined_option = Option.of("Hello!");
        Option<String> none = None();
        assertEquals( "Hello!", defined_option.getOrElse("Goodbye!"));
        assertEquals( "Goodbye!", none.getOrElse("Goodbye!"));
    }

    /**
     * el metodo 'when' permite crear un Some(valor) o None utilizando condicionales booleanos
     */
    @Test
    public void testWhenMethod(){
        Option<String> valid = Option.when(true, "Good!");
        Option<String> invalid = Option.when(false, "Bad!");
        assertEquals( Some("Good!"), valid);
        assertEquals( None(), invalid);
    }

    @Test
    public void testOptionCollect() {
        final PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>() {
            @Override
            public String apply(Integer i) {
                return String.valueOf(i);
            }

            @Override
            public boolean isDefinedAt(Integer i) {
                return i % 2 == 1;
            }
        };
        assertEquals( None(),Option.of(2).collect(pf));
        assertEquals( None(),Option.<Integer>none().collect(pf));
    }
    /**
     * En este test se prueba la funcionalidad para el manejo de Null en Option con FlatMap
     */
    @Test
    public void testMananagementNull(){
        Option<String> valor = Option.of("pepe");
        Option<String> someN = valor.map(v -> null);

        /* Se valida que devuelve un Some null lo cual podria ocasionar en una Excepcion de JavanullPointerExcepcion*/
        assertEquals(
                someN.get(),
                null);

        Option<String> buenUso = someN
                .flatMap(v -> {
                    System.out.println("testManagementNull - Esto se imprime? (flatMap)");
                    return Option.of(v);
                })
                .map(x -> {
                    System.out.println("testManagementNull - Esto se imprime? (map)");
                    return x.toUpperCase() +"Validacion";
                });

        assertEquals(
                None(),
                buenUso);
    }

    /**
     * En este test se prueba la funcionalidad para transformar un Option por medio de Map y flatMap
     */
    @Test
    public void testMapAndFlatMapToOption() {
        Option<String> myMap = Option.of("mi mapa");

        Option<String> myResultMapOne = myMap.map(s -> s + " es bonito");

        assertEquals(
                Option.of("mi mapa es bonito"),
                myResultMapOne);

        Option<String> myResultMapTwo = myMap
                .flatMap(s -> Option.of(s + " es bonito"))
                .map(v -> v + " con flat map");


        assertEquals(
                Option.of("mi mapa es bonito con flat map"),
                myResultMapTwo);
    }

    @Test
    public void optionFromNull(){
        Option<Object> of = Option.of(null);
        assertEquals(of, None());
    }

    @Test
    public void optionFromOptional(){
        Optional optional = Optional.of(1);
        Option option = Option.ofOptional(optional);
    }

    Option<Integer> esPar(int i){
        return (i%2==0)?Some(i):None();
    }

    @Test
    public void forCompEnOption1(){
        Option<Integer> integers = For(esPar(2), d -> Option(d)).toOption();
        assertEquals(integers,Some(2));
    }

    @Test
    public void forCompEnOption2(){
        Option<Integer> integers = For(esPar(2), d ->
                                   For(esPar(4), c -> Option(d+c))).toOption();
        assertEquals(integers,Some(6));
    }

    @Test
    public void testOptionMapFlat(){
        Option<Integer> opt = Option.of(1);
        Option<Option<Integer>> optOpt = opt.map(f -> Option.of(identityOrNull(f - 3)));
        Option<Integer> flatOpt = opt.flatMap(f -> Option.of(identityOrNull(f - 3)));

    }

    private Option<Integer> add(int num1, int num2) {
        System.out.println("Adding: " + num1 + " + " + num2);
        return Option.of(num1 + num2);
    }

    private Option<Integer> subtract(int num1, int num2) {
        System.out.println("Subtracting: "+ num1 + " - " + num2);
        return num1 - num2 > 0 ? Option.of(num1 + num2) : None();
    }

    @Test
    public void testOptionFlatMap(){
        //When there's not need of previous parameters (can be same parameter)
        Option<Integer> res1 = add(1, 1)
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1));

        //When there's need of previous parameters (must use different var)
        Option<Integer> res2 = add(1, 1)
                .flatMap(num1 -> add(num1 , 1)
                    .flatMap(num2 -> add(num2 , 1)
                        .flatMap(num3 -> add(num3 , 1)
                            .flatMap(num4 -> add(num4 , 1)))));

        assertEquals(res1.getOrElse(0).intValue(), 6);
        assertEquals(res2.getOrElse(0).intValue(), 6);

    }

    @Test
    public void testOptionFlatMapNone(){
        //When there's not need of previous parameters (can be same var)
        Option<Integer> res1 = add(1, 1)
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> subtract(num, 10))
                .flatMap(num -> add(num , 1))
                .flatMap(num -> add(num , 1));

        //When there's need of previous parameters (must use different var)
        Option<Integer> res2 = add(1, 1)
                .flatMap(num1 -> add(num1 , 1)
                    .flatMap(num2 -> add(num2 , 1)
                        .flatMap(num3 -> add(num3 , 1)
                            .flatMap(num4 -> add(num4 , 1)
                                .flatMap(num5 -> subtract(num5, 10)
                                    .flatMap(num6 -> add(num6 , 1)
                                        .flatMap(num7 -> add(num7 , 1))))))));

        assertEquals(res1, Option.none());
        assertEquals(res2, Option.none());

    }

    @Test
    public void testFlatMapFor(){

        // sintactic suggar for flatMap
        Option<Integer> res =
                For(add(1, 1), num ->
                For(add(num, 1), num2 ->
                For(add(num2, 1), num3 ->
                    add(num3, 1)))).toOption();

        assertEquals(res.getOrElse(0).intValue(), 5);
    }
}
