package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class SequentialMulPairingAccumulator implements PairingAccumulator {

    private final Pairing pairing;
    private final Element value;


    public SequentialMulPairingAccumulator(Pairing pairing) {
        this.pairing = pairing;
        this.value = pairing.getGT().newOneElement();
    }

    public Element getResult() {
        return value;
    }

    public void addPairing(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2));

    }

    public Element awaitResult() {
        return value;
    }

}
