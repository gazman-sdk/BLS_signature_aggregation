package it.unisa.dia.gas.plaf.jpbc.util.concurrent.context;

import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ContextExecutor extends PoolExecutor implements MutablePairingParameters {

    private final MutablePairingParameters parameters;


    public ContextExecutor(MutablePairingParameters parameters) {
        super(ExecutorServiceUtils.getCachedThreadPool());
        this.parameters = parameters;
    }

    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    public String getString(String key) {
        return parameters.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return parameters.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return parameters.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return parameters.getInt(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return parameters.getBigInteger(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return parameters.getBigIntegerAt(key, index);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return parameters.getBigInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return parameters.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return parameters.getLong(key, defaultValue);
    }

    public byte[] getBytes(String key) {
        return parameters.getBytes(key);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return parameters.getBytes(key, defaultValue);
    }

    public String toString(String separator) {
        return parameters.toString(separator);
    }

    public Object getObject(String key) {
        return parameters.getObject(key);
    }

}
