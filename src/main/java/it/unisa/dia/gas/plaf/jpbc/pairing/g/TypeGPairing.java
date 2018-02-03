package it.unisa.dia.gas.plaf.jpbc.pairing.g;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeGPairing extends AbstractPairing {
    private final PairingParameters curveParams;

    private BigInteger q;
    private BigInteger n;
    BigInteger r;
    private BigInteger h;
    private BigInteger a;
    private BigInteger b;
    private BigInteger nqr;

    PolyModElement xPowq;
    PolyModElement xPowq2;
    PolyModElement xPowq3;
    PolyModElement xPowq4;
    Element nqrInverse;
    Element nqrInverseSquare;
    BigInteger phikOnr;

    private Field Fq;
    Field<? extends Point<Polynomial>> Fqk;
    PolyModField Fqd;
    private CurveField Eq;


    public TypeGPairing(PairingParameters curveParams) {
        this.curveParams = curveParams;

        initParams();
        initMap();
        initFields();
    }

    public boolean isSymmetric() {
        return false;
    }


    private void initParams() {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !type.equalsIgnoreCase("g"))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'g'.");

        // load params
        q = curveParams.getBigInteger("q");
        n = curveParams.getBigInteger("n");
        h = curveParams.getBigInteger("h");
        r = curveParams.getBigInteger("r");
        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
//        k = curveParams.getBigInteger("k");
//        nk = curveParams.getBigInteger("nk");
//        hk = curveParams.getBigInteger("hk");
        nqr = curveParams.getBigInteger("nqr");
    }


    private void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init the curve
        Eq = initEq();

        // Init Fqx
        Field fqx = initPoly();

        // Init polymod
        // First set the coefficient of x^5 to 1 so we can call element_item()
        // for the other coefficients.
        PolyElement irreduciblePoly = (PolyElement) fqx.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        for (int i = 0; i < 5; i++)
            irreduciblePolyCoeff.add(Fq.newElement().set(curveParams.getBigIntegerAt("coeff", i)));
        irreduciblePolyCoeff.add(Fq.newOneElement());

        // init Fq12
        Fqd = initPolyMod(irreduciblePoly);

        Fqk = initQuadratic();

        CurveField etwist = initEqMap().twist();

        nqrInverse = Fqd.getNqr().duplicate().invert();
        nqrInverseSquare = nqrInverse.duplicate().square();

        // ndonr temporarily holds the trace.
        BigInteger ndonr = q.subtract(n).add(BigInteger.ONE);

        // Negate because we want the order of the twist.
        ndonr = ndonr.negate();
        ndonr = BigIntegerUtils.pbc_mpz_curve_order_extn(q, ndonr, 5);
        ndonr = ndonr.divide(r);
        etwist.setQuotientCmp(ndonr);

        // Compute phi(k)/r = (q^4 - q^3 + ... + 1)/r.
        phikOnr = BigInteger.ONE;
        phikOnr = phikOnr.subtract(q);
        BigInteger z0 = q.multiply(q);
        phikOnr = phikOnr.add(z0);
        z0 = z0.multiply(q);
        phikOnr = phikOnr.subtract(z0);
        z0 = z0.multiply(q);
        phikOnr = phikOnr.add(z0);
        phikOnr = phikOnr.divide(r);

        // Compute xPowq's
        xPowq = Fqd.newElement();
        xPowq.getCoefficient(1).setToOne();
        xPowq.pow(q);
        xPowq2 = xPowq.duplicate().square();
        xPowq4 = xPowq2.duplicate().square();
        xPowq3 = xPowq2.duplicate().mul(xPowq);

        // Init G1, G2, GT
        G1 = Eq;
        G2 = etwist;
        GT = initGT();
    }

    private Field initFp(BigInteger order) {
        return new ZrField(order);
    }

    private CurveField initEq() {
        return new CurveField(Fq.newElement().set(a), Fq.newElement().set(b), r, h);
    }

    private CurveField initEqMap() {
        return new CurveField(Fqd.newElement().map(Eq.getA()), Fqd.newElement().map(Eq.getB()), r);
    }

    private PolyField initPoly() {
        return new PolyField(Fq);
    }

    private PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(irred, nqr);
    }

    private QuadraticField initQuadratic() {
        return new QuadraticField(Fqd);
    }

    private Field initGT() {
        return new GTFiniteField(r, pairingMap, Fqk);
    }

    private void initMap() {
        pairingMap = new TypeGTateAffineNoDenomMillerPairingMap(this);
    }
}