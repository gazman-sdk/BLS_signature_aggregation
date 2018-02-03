package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractMillerPairingPreProcessing implements PairingPreProcessing {

    protected final AbstractMillerPairingMap.MillerPreProcessingInfo processingInfo;


    protected AbstractMillerPairingPreProcessing(int processingInfoSize) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(processingInfoSize);
    }

    protected AbstractMillerPairingPreProcessing(Pairing pairing, byte[] source, int offset) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(pairing, source, offset);
    }

}
