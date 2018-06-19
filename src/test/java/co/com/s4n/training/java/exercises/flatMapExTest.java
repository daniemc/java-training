package co.com.s4n.training.java.exercises;
import io.vavr.control.Option;

import static io.vavr.API.For;
import static io.vavr.API.None;
import co.com.s4n.training.java.NumberOperations;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static io.vavr.API.Some;
import java.util.Optional;

public class flatMapExTest {

    @Test
    public void factorialFlatMap(){

        Option<Double> result = NumberOperations.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperations.add(r1, 2.0)
                    .flatMap(r2 -> NumberOperations.add(r2, 1.0)
                        .flatMap(r3 -> NumberOperations.factorial(r3))));

        assertEquals(Some(120.0), result);
    }

    @Test
    public void factorialFlatMapFor(){

        Option<Double> result =
                For(NumberOperations.add(1.0, 1.0), r1 ->
                For(NumberOperations.add(r1, 2.0), r2 ->
                For(NumberOperations.add(r2, 1.0), r3 ->
                    NumberOperations.factorial(r3)))).toOption();

        assertEquals(Some(120.0), result);
    }

    @Test
    public void divisionByZero(){
        Option<Double> result = NumberOperations.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperations.add(r1, 1.0)
                        .flatMap(r2 -> NumberOperations.add(r2, 1.0)
                                .flatMap(r3 -> NumberOperations.factorial(r3)
                                    .flatMap(r4 -> NumberOperations.divide(r4, 0.0)
                                        .flatMap(r5 -> NumberOperations.add(r5, 5.0))))));

        assertEquals(Option.none(), result);
    }

    @Test
    public void divisionByZeroFor(){

        Option<Double> result =
                For(NumberOperations.add(1.0, 1.0), r1 ->
                For(NumberOperations.add(r1, 2.0), r2 ->
                For(NumberOperations.add(r2, 1.0), r3 ->
                For(NumberOperations.factorial(r3), r4 ->
                For(NumberOperations.divide(r4, 0.0), r5 ->
                    NumberOperations.add(r5, 5.0)))))).toOption();

        assertEquals(Option.none(), result);
    }

    @Test
    public void powerTest(){

        Option<Double> result = NumberOperations.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperations.power(r1, 5));

        assertEquals(Some(32.0), result);
    }

    @Test
    public void subsTest(){
        Option<Double> result =
                For(NumberOperations.add(1.0, 1.0), num ->
                For(NumberOperations.add(num, 1.0), num2 ->
                For(NumberOperations.add(num2, 1.0), num3 ->
                    NumberOperations.subtract(num3, 3.0)))).toOption();

        assertEquals(Some(1.0), result);
    }

    @Test
    public void subsNoneTest(){
        Option<Double> result =
                For(NumberOperations.add(1.0, 1.0), num ->
                For(NumberOperations.add(num, 1.0), num2 ->
                For(NumberOperations.add(num2, 1.0), num3 ->
                    NumberOperations.subtract(num3, 5.0)))).toOption();

        assertEquals(Option.none(), result);
    }
}
