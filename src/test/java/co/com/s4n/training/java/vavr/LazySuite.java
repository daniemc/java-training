package co.com.s4n.training.java.vavr;

import io.vavr.Lazy;
import io.vavr.concurrent.Future;
import org.junit.platform.runner.IncludeEngines;
import org.junit.platform.runner.JUnitPlatform;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@RunWith(JUnitPlatform.class)
@IncludeEngines("junit-jupiter")
public class LazySuite {

    public void sleep(Integer ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLazy(){

        Long start = System.nanoTime();

        Lazy<String> f1 = Lazy.of(() -> {
            sleep(500);
            return "1";
        });

        Lazy<String> f2 = Lazy.of(() -> {
            sleep(800);
            return "2";
        });

        Lazy<String> f3 = Lazy.of(() -> {
            sleep(300);
            return "3";
        });


        String lr1 = f1.get();
        String lr2 = f2.get();
        String lr3 = f3.get();


        Long finish = System.nanoTime();
        long durationInMs = TimeUnit.MILLISECONDS.convert(finish - start, TimeUnit.NANOSECONDS);
        System.out.println(durationInMs);
        assertTrue(durationInMs >= 1600);
    }

    @Test
    public void lazySupplier() {


        Lazy<String> f1 = Lazy.of(() -> {
            sleep(500);
            return "LAZ";
        });

        Supplier<String> s1 = () -> {
            sleep(500);
            return "SUP";
        };

        // INIT LAZY 1
        Long startL1 = System.nanoTime();
        f1.get();
        Long finishL1 = System.nanoTime();

        // INIT LAZY 2
        Long startL2 = System.nanoTime();
        f1.get();
        Long finishL2 = System.nanoTime();

        // INIT SUPP 1
        Long startS1 = System.nanoTime();
        s1.get();
        Long finishS1 = System.nanoTime();

        // INIT SUPP 2
        Long startS2 = System.nanoTime();
        s1.get();
        Long finishS2 = System.nanoTime();

        long durationL1InMs = TimeUnit.MILLISECONDS.convert(finishL1 - startL1, TimeUnit.NANOSECONDS);
        long durationL2InMs = TimeUnit.MILLISECONDS.convert(finishL2 - startL2, TimeUnit.NANOSECONDS);
        long durationS1InMs = TimeUnit.MILLISECONDS.convert(finishS1 - startS1, TimeUnit.NANOSECONDS);
        long durationS2InMs = TimeUnit.MILLISECONDS.convert(finishS2 - startS2, TimeUnit.NANOSECONDS);

        System.out.println(durationL1InMs);
        System.out.println(durationL2InMs);
        System.out.println(durationS1InMs);
        System.out.println(durationS2InMs);

        assertTrue(durationL1InMs >= 500);
        assertTrue(durationL2InMs < 500);
        assertTrue(durationS1InMs >= 500);
        assertTrue(durationS2InMs >= 500);

    }
}
