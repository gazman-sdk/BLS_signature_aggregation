package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import com.gazman.bls.utils.RandomHolder;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeA1CurveGenerator implements PairingParametersGenerator {
    private final int numPrimes;
    private final int bits;


    private TypeA1CurveGenerator() {
        this.numPrimes = 3;
        this.bits = 512;
    }

    public PairingParameters generate() {
        BigInteger[] primes = new BigInteger[numPrimes];
        BigInteger order, n, p;
        long l;

        SecureRandom random = RandomHolder.RANDOM;
        order = BigInteger.ONE;
        for (int i = 0; i < numPrimes; i++) {

            boolean isNew = false;
            while (!isNew) {
                primes[i] = BigInteger.probablePrime(bits, random);
                isNew = true;
                for (int j = 0; j < i; j++) {
                    if (primes[i].equals(primes[j])) {
                        isNew = false;
                        break;
                    }
                }
            }

            order = order.multiply(primes[i]);
        }
        l = 4;
        n = order.multiply(BigIntegerUtils.FOUR);

        p = n.subtract(BigInteger.ONE);
        while (!p.isProbablePrime(10)) {
            p = p.add(n);
            l += 4;
        }

        PropertiesParameters params = new PropertiesParameters();
        params.put("type", "a1");
        params.put("p", p.toString());
        params.put("n", order.toString());
        for (int i = 0; i < primes.length; i++) {
            params.put("n" + i, primes[i].toString());
        }
        params.put("l", String.valueOf(l));


        return params;
    }

    public static void main(String[] args) {
        TypeA1CurveGenerator generator = new TypeA1CurveGenerator();
        PairingParameters curveParams = generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}
