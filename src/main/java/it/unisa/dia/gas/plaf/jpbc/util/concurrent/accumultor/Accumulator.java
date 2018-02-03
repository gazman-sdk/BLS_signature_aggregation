package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface Accumulator<T> extends Pool<T> {

    T awaitResult();

    T getResult();

}
