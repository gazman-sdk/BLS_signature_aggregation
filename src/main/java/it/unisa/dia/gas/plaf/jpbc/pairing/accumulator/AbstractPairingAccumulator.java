package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.AbstractAccumulator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class AbstractPairingAccumulator extends AbstractAccumulator<Element> implements PairingAccumulator {

    private final Pairing pairing;


    AbstractPairingAccumulator(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    private AbstractPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.result = value;
    }


    public void addPairing(final Element e1, final Element e2) {
        super.accumulate(() -> pairing.pairing(e1, e2));

    }

}
