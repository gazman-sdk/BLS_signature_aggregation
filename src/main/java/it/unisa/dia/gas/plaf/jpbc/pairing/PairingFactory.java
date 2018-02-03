package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.g.TypeGPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.immutable.ImmutableParing;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
@SuppressWarnings("unused")
public class PairingFactory {
    private static final PairingFactory INSTANCE = new PairingFactory();

    public static Pairing getPairing(String parametersPath) {
        return INSTANCE.initPairing(parametersPath);
    }

    private boolean usePBCWhenPossible = false;
    private boolean reuseInstance = true;
    private boolean pbcAvailable = false;
    private boolean immutable = false;

    private Map<PairingParameters, Pairing> instances;
    private EllipticCurvesPairingCreator defaultCreator = new EllipticCurvesPairingCreator();


    private PairingFactory() {
        this.instances = new HashMap<>();
    }


    private Pairing initPairing(String parametersPath) {
        return initPairing(loadParameters(parametersPath));
    }

    private Pairing initPairing(PairingParameters parameters) {
        if (parameters == null)
            throw new IllegalArgumentException("parameters cannot be null.");

        Pairing pairing;
        if (reuseInstance) {
            pairing = instances.get(parameters);
            if (pairing != null)
                return pairing;
        }

        String type = parameters.getString("type");

        pairing = defaultCreator.create(type, parameters);
        if (pairing == null)
            throw new IllegalArgumentException("Cannot create pairing instance. Type = " + type);

        if (immutable)
            pairing = new ImmutableParing(pairing);

        if (reuseInstance)
            instances.put(parameters, pairing);

        return pairing;
    }


    private PairingParameters loadParameters(String path) {
        PropertiesParameters curveParams = new PropertiesParameters();
        curveParams.load(path);

        return curveParams;
    }


    public boolean isPBCAvailable() {
        return pbcAvailable;
    }

    public boolean isUsePBCWhenPossible() {
        return usePBCWhenPossible;
    }

    public void setUsePBCWhenPossible(boolean usePBCWhenPossible) {
        this.usePBCWhenPossible = usePBCWhenPossible;
    }

    public boolean isReuseInstance() {
        return reuseInstance;
    }

    public void setReuseInstance(boolean reuseInstance) {
        this.reuseInstance = reuseInstance;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public class EllipticCurvesPairingCreator {

        private Method getPairingMethod;
        private Throwable pbcPairingFailure;

        @SuppressWarnings("unchecked")
        EllipticCurvesPairingCreator() {
            // Try to load jpbc-pbc factory
            try {
                Class pbcPairingFactoryClass = Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairingFactory");
                Method isPBCAvailable = pbcPairingFactoryClass.getMethod("isPBCAvailable");

                pbcAvailable = ((Boolean) isPBCAvailable.invoke(null));
                if (pbcAvailable)
                    getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", PairingParameters.class);
            } catch (Exception e) {
                pbcAvailable = false;
                pbcPairingFailure = e;
            }

        }

        Pairing create(String type, PairingParameters parameters) {
            Pairing pairing = null;

            // Handle bilinear maps parameters
            if (usePBCWhenPossible && pbcAvailable)
                pairing = getPBCPairing(parameters);

            if (pairing == null) {
                if ("a".equalsIgnoreCase(type))
                    pairing = new TypeAPairing(parameters);
                else if ("a1".equalsIgnoreCase(type))
                    pairing = new TypeA1Pairing(parameters);
                else if ("d".equalsIgnoreCase(type))
                    pairing = new TypeDPairing(parameters);
                else if ("e".equalsIgnoreCase(type))
                    pairing = new TypeEPairing(parameters);
                else if ("f".equalsIgnoreCase(type))
                    return new TypeFPairing(parameters);
                else if ("g".equalsIgnoreCase(type))
                    return new TypeGPairing(parameters);
                else
                    throw new IllegalArgumentException("Type not supported. Type = " + type);
            }

            return pairing;
        }

        Pairing getPBCPairing(PairingParameters parameters) {
            try {
                return (Pairing) getPairingMethod.invoke(null, parameters);
            } catch (Exception e) {
                // Ignore
                e.printStackTrace();
            }
            return null;
        }

        public Throwable getPbcPairingFailure() {
            return pbcPairingFailure;
        }
    }

}
