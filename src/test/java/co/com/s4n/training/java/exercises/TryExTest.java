package co.com.s4n.training.java.exercises;

import co.com.s4n.training.java.NumberOperations;
import co.com.s4n.training.java.NumberOperationsTry;
import io.vavr.control.Try;
import org.junit.Test;

import static io.vavr.API.For;
import static io.vavr.API.Some;
import static io.vavr.API.Success;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TryExTest {

    @Test
    public void factorialFlatMap(){

        Try<Double> result = NumberOperationsTry.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperationsTry.add(r1, 2.0)
                        .flatMap(r2 -> NumberOperationsTry.add(r2, 1.0)
                                .flatMap(r3 -> NumberOperationsTry.factorial(r3))));

        assertEquals(Success(120.0), result);
    }

    @Test
    public void factorialFlatMapFor(){

        Try<Double> result =
                For(NumberOperationsTry.add(1.0, 1.0), r1 ->
                        For(NumberOperationsTry.add(r1, 2.0), r2 ->
                                For(NumberOperationsTry.add(r2, 1.0), r3 ->
                                        NumberOperationsTry.factorial(r3)))).toTry();

        assertEquals(Success(120.0), result);
    }

    @Test
    public void divisionByZero(){
        Try<Double> result = NumberOperationsTry.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperationsTry.add(r1, 1.0)
                        .flatMap(r2 -> NumberOperationsTry.add(r2, 1.0)
                                .flatMap(r3 -> NumberOperationsTry.factorial(r3)
                                        .flatMap(r4 -> NumberOperationsTry.divide(r4, 0.0)
                                                .recoverWith(Exception.class, Try.of(() -> -1.0)
                                                )))));

        assertEquals(Success(-1.0),result);
    }

    @Test
    public void divisionByZeroFor(){

        Try<Double> result =
                For(NumberOperationsTry.add(1.0, 1.0), r1 ->
                        For(NumberOperationsTry.add(r1, 2.0), r2 ->
                                For(NumberOperationsTry.add(r2, 1.0), r3 ->
                                        For(NumberOperationsTry.factorial(r3), r4 ->
                                                For(NumberOperationsTry.divide(r4, 0.0), r5 ->
                                                        NumberOperationsTry.add(r5, 5.0)))))).toTry();

        assertTrue(result.isFailure());
    }

    @Test
    public void powerTest(){

        Try<Double> result = NumberOperationsTry.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperationsTry.power(r1, 5));

        assertEquals(Success(32.0), result);
    }

    @Test
    public void subsTest(){
        Try<Double> result =
                For(NumberOperationsTry.add(1.0, 1.0), num ->
                        For(NumberOperationsTry.add(num, 1.0), num2 ->
                                For(NumberOperationsTry.add(num2, 1.0), num3 ->
                                        NumberOperationsTry.subtract(num3, 3.0)))).toTry();

        assertEquals(Success(1.0), result);
    }

    @Test
    public void subsFailTest(){
        Try<Double> result =
                For(NumberOperationsTry.add(1.0, 1.0), num ->
                        For(NumberOperationsTry.add(num, 1.0), num2 ->
                                For(NumberOperationsTry.add(num2, 1.0), num3 ->
                                        NumberOperationsTry.subtract(num3, 5.0)))).toTry();

        assertTrue(result.isFailure());
    }

    @Test
    public void subsFailTest2(){
        Try<Double> result =
                For(NumberOperationsTry.add(1.0, 1.0), num ->
                        For(NumberOperationsTry.add(num, 1.0), num2 ->
                                For(NumberOperationsTry.add(num2, 1.0), num3 ->
                                        NumberOperationsTry.subtract(num3, 5.0).recover(Exception.class, e -> -1.0) ))).toTry();

        assertEquals(Success(-1.0) ,result);
    }

    @Test
    public void failInMiddleTest(){
        Try<Double> result = NumberOperationsTry.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperationsTry.divide(r1, 0.0).recover(Exception.class, 5.0)
                    .flatMap(r2 -> NumberOperationsTry.add(r2, 5.0)));

        assertEquals(Success(10.0), result);
    }

    @Test
    public void noFailInMiddleTest(){
        Try<Double> result = NumberOperationsTry.add(1.0, 1.0)
                .flatMap(r1 -> NumberOperationsTry.divide(r1, 1.0).recover(Exception.class, 5.0)
                        .flatMap(r2 -> NumberOperationsTry.add(r2, 5.0)));

        assertEquals(Success(7.0), result);
    }
}
