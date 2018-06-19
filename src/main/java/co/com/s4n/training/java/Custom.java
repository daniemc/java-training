package co.com.s4n.training.java;

import io.vavr.collection.List;
import io.vavr.concurrent.Future;

import java.util.function.BiFunction;

public class Custom {
    // PT = Param Type
    // RT = Return Type

    /**
     *
     * @param futures Lista de futuros a iterar
     * @param zero valor inicial para la operacion (f)
     * @param function funcion a aplicar a elementos iterados
     * @param <PT> tipo de parametro entrada
     * @param <RT> tipo de respuesta
     * @return retorna un futuro de la lista iterada y habiendo aplicado la funcion BiFunction(f)
     */
    public static <PT, RT> Future<RT> myFold(List<Future<PT>> futures, RT zero, BiFunction<RT, RT, RT> function) {
        RT res = zero;

        for(Future<PT> future : futures){
            res = function.apply(res, (RT) future.get());
        }
        RT finalRes = res;
        return Future.of(() -> finalRes);
    }
}
