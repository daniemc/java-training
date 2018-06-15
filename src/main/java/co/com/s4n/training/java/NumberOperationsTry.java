package co.com.s4n.training.java;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.EmptyStackException;

import static io.vavr.API.None;

public class NumberOperationsTry {

    public static Try<Double> add(Double num1, Double num2){
        return Try.of(() -> num1 + num2);
    }

    public static Try<Double> subtract(Double num1, Double num2){
        return num1 - num2 > 0.0 ? Try.of(() -> num1 - num2) : Try.failure(new Exception("Bad"));
    }

    public static Try<Double> miltiply(Double num1, Double num2){
        return Try.of(() -> num1 * num2);
    }

    public static Try<Double> divide(Double num1, Double num2){
        return num2 > 0.0 ? Try.of(() -> num1/num2) : Try.failure(new Exception("Bad"));
    }

    public static Try<Double> factorial(Double num1){
        double factorialNumber = 1.0;
        for (double i = 1.0; i <= num1; i++) {
            factorialNumber *= i;
        }
        double finalFactorialNumber = factorialNumber;
        return Try.of(() -> finalFactorialNumber);
    }

    public static Try<Double> power(Double base, int pow){
        return Try.of(() -> Math.pow(base, pow));
    }
}
