package com.gazman.bls.model;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * Created by Ilya Gazman on 2/3/2018.
 */
public enum BlsModel {
    instance;

    public final Pairing pairing;
    public final Element systemParameters;

    BlsModel(){
        pairing = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
        systemParameters = pairing.getG2().newRandomElement(); // this will be a hardcoded value in the future
    }
}
