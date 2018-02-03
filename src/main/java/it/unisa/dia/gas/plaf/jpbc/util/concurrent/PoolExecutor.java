package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PoolExecutor<T> implements Pool<T> {

    protected final CompletionService<T> pool;
    protected int counter;


    protected PoolExecutor(Executor executor) {
        this.pool = new ExecutorCompletionService<>(executor);
        this.counter = 0;
    }


    protected void submit(Callable<T> callable) {
        counter++;
        pool.submit(callable);

    }

}
