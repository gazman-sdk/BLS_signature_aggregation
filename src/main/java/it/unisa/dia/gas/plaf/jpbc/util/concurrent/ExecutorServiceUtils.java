package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ExecutorServiceUtils {

    private static final ExecutorService fixedThreadPool;
    private static final ExecutorService cachedThreadPool;

    static {
        fixedThreadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 4
        );
        cachedThreadPool = Executors.newCachedThreadPool();
    }


    private ExecutorServiceUtils() {
    }


    public static ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public static ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }


}
