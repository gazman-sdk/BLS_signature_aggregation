package com.gazman.bls;

import com.gazman.bls.model.BlsModel;
import com.gazman.bls.model.Signature;
import com.gazman.bls.utils.Sha256Hash;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ilya Gazman on 2/3/2018.
 */
public class BlsSignaturesTest {

    private BlsSignatures blsSignatures;
    private Pairing pairing = BlsModel.instance.pairing;
    private ArrayList<Signature> signatures = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        blsSignatures = new BlsSignatures();
        for (int i = 0; i < 10; i++) {
            byte[] secretKey = pairing.getZr().newRandomElement().toBytes();
            Signature signature = blsSignatures.sign(("cool " + i).getBytes(), secretKey);
            signatures.add(signature);
        }
    }

    @Test
    public void baseTest() throws Exception {
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertTrue(blsSignatures.validate());
    }

    @Test
    public void changeMessage() throws Exception {
        signatures.get(1).message = "dummy".getBytes();
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertFalse(blsSignatures.validate());
    }

    @Test
    public void changePublicKey() throws Exception {
        signatures.get(1).publicKey = pairing.getG1().newRandomElement();
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertFalse(blsSignatures.validate());
    }

    @Test
    public void changeSignature() throws Exception {
        signatures.get(1).signature = pairing.getG1().newRandomElement();
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertFalse(blsSignatures.validate());
    }

    @Test
    public void duplicateSignature() throws Exception {
        Element message1 = signatures.get(1).signature;
        signatures.get(2).signature = message1;
        signatures.get(1).signature = message1;

        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }

        assertFalse(blsSignatures.validate());
    }

    @Test
    public void swapMessages() throws Exception {
        byte[] message1 = signatures.get(1).message;
        byte[] message2 = signatures.get(2).message;
        signatures.get(2).message = message1;
        signatures.get(1).message = message2;

        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertFalse(blsSignatures.validate());
    }

    @Test
    public void swapPublicKeys() throws Exception {
        Element message1 = signatures.get(1).publicKey;
        Element message2 = signatures.get(2).publicKey;
        signatures.get(2).publicKey = message1;
        signatures.get(1).publicKey = message2;

        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        assertFalse(blsSignatures.validate());
    }

    @Test
    public void swapSignatures() throws Exception {
        Element message1 = signatures.get(1).signature;
        Element message2 = signatures.get(2).signature;
        signatures.get(2).signature = message1;
        signatures.get(1).signature = message2;

        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }
        /*
         * This one is funny because it's actually allowed. When signatures are aggregated
         * the order information is getting lost, so it does not meter what signature came first.
         * What matters is that there is exactly one message that was signed by that signature.
         *
         * The below two test should convince you
         */
        assertTrue(blsSignatures.validate());
    }

    @Test
    public void signRandomWallet() throws Exception {
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }

        Element secretKey = pairing.getZr().newRandomElement();
        Element publicKey = BlsModel.instance.systemParameters.duplicate().powZn(secretKey);

        Element fakeSecretKey = pairing.getZr().newRandomElement();
        Element fakePublicKey = BlsModel.instance.systemParameters.duplicate().powZn(fakeSecretKey);

        Signature signature = constructSignature("cool 90".getBytes(), secretKey, publicKey, fakePublicKey);
        blsSignatures.addSignature(signature);

        assertFalse(blsSignatures.validate());
    }

    @Test
    public void signSomeoneElseWallet() throws Exception {
        for (Signature signature : signatures) {
            blsSignatures.addSignature(signature);
        }

        Element secretKey = pairing.getZr().newRandomElement();
        Element publicKey = BlsModel.instance.systemParameters.duplicate().powZn(secretKey);

        Element fakePublicKey = signatures.get(1).publicKey.duplicate();

        Signature signature = constructSignature("cool 90".getBytes(), secretKey, publicKey, fakePublicKey);
        blsSignatures.addSignature(signature);

        assertFalse(blsSignatures.validate());
    }

    private Signature constructSignature(byte[] message, Element secretKey, Element publicKey, Element fakePublicKey) {
        byte[] hash = hash(message, fakePublicKey);
        Element messageHashElement = pairing.getG1().newElementFromHash(hash, 0, hash.length);
        Element signatureElement = messageHashElement.powZn(secretKey);

        Signature signature = new Signature();
        signature.message = message;
        signature.publicKey = publicKey;
        signature.signature = signatureElement;
        return signature;
    }

    private byte[] hash(byte[] message, Element publicKey) {
        byte[] bytes1 = Sha256Hash.hash(message);
        byte[] bytes2 = publicKey.toBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes1.length + bytes2.length);
        buffer.put(bytes1);
        buffer.put(bytes2);

        return Sha256Hash.hash(buffer.array());
    }

}