package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class AbstractAccumulator<T> extends PoolExecutor<T> implements Accumulator<T> {


    protected T result;


    protected AbstractAccumulator() {
        super(ExecutorServiceUtils.getFixedThreadPool());
    }


    public void accumulate(Callable<T> callable) {
        submit(callable);

    }

    private Accumulator<T> awaitTermination() {
        try {
            for (int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public T awaitResult() {
        return awaitTermination().getResult();
    }


    protected abstract void reduce(T value);

}
