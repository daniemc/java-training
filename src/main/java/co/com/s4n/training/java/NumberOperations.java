package co.com.s4n.training.java;

import io.vavr.control.Option;
import static io.vavr.API.None;

public class NumberOperations {

    public static Option<Double> add(Double num1, Double num2){
        return Option.of(num1 + num2);
    }

    public static Option<Double> subtract(Double num1, Double num2){
        return num1 - num2 > 0 ? Option.of(num1 - num2) : None();
    }

    public static Option<Double> miltiply(Double num1, Double num2){
        return Option.of(num1 + num2);
    }

    public static Option<Double> divide(Double num1, Double num2){
        return num2 == 0.0 ? None() : Option.of(num1/num2);
    }

    public static Option<Double> factorial(Double num1){
        double factorialNumber = 1;
        for (double i = 1.0; i <= num1; i++) {
            factorialNumber *= i;
        }
        return Option.of(factorialNumber);
    }

    public static Option<Double> power(Double base, int pow){
        return Option.of(Math.pow(base, pow));
    }
}
