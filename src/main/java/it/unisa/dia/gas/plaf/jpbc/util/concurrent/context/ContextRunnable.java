package it.unisa.dia.gas.plaf.jpbc.util.concurrent.context;

import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class ContextRunnable implements Runnable, MutablePairingParameters {

    private ContextExecutor executor;

    void setExecutor(ContextExecutor executor) {
        this.executor = executor;
    }

    public boolean containsKey(String key) {
        return executor.containsKey(key);
    }

    public String getString(String key) {
        return executor.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return executor.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return executor.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return executor.getInt(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return executor.getBigInteger(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return executor.getBigIntegerAt(key, index);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return executor.getBigInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return executor.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return executor.getLong(key, defaultValue);
    }

    public byte[] getBytes(String key) {
        return executor.getBytes(key);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return executor.getBytes(key, defaultValue);
    }

    public String toString(String separator) {
        return executor.toString(separator);
    }

    public Object getObject(String key) {
        return executor.getObject(key);
    }
}
