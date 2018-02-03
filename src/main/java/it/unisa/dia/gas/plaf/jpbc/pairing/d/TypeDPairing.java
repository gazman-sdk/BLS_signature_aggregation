package it.unisa.dia.gas.plaf.jpbc.pairing.d;

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
public class TypeDPairing extends AbstractPairing {
    private final PairingParameters curveParams;

    int k;

    private BigInteger q;
    private BigInteger n;
    BigInteger r;
    private BigInteger h;
    private BigInteger a;
    private BigInteger b;

    PolyModElement xPowq;
    PolyModElement xPowq2;
    Element nqrInverse;
    Element nqrInverseSquare;
    BigInteger tateExp;
    BigInteger phikOnr;

    Field Fq;
    Field<? extends Point<Polynomial>> Fqk;
    PolyModField Fqd;
    private CurveField Eq;


    public TypeDPairing(PairingParameters curveParams) {
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
        if (type == null || !"d".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'd'.");

        // load params
        k = curveParams.getInt("k");
        if (k % 2 != 0)
            throw new IllegalArgumentException("odd k not implemented anymore");

        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");
        n = curveParams.getBigInteger("n");

        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
    }


    private void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init Eq
        Eq = initEq();

        // Init Fqx
        PolyField polyField = initPoly();

        // Init the irreducible polynomial
        int d = k / 2;

        PolyElement<Element> irreduciblePoly = polyField.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        for (int i = 0; i < d; i++) {
            irreduciblePolyCoeff.add(polyField.getTargetField().newElement().set(curveParams.getBigIntegerAt("coeff", i)));
        }
        irreduciblePolyCoeff.add(polyField.getTargetField().newElement().setToOne());

        // init Fqd
        Fqd = initPolyMod(irreduciblePoly);

        // init Fqk
        Fqk = initQuadratic();

        // Compute constants involved in the final powering.
        if (k == 6) {
            phikOnr = q.multiply(q).subtract(q).add(BigInteger.ONE).divide(r);

            PolyModElement polyModElement = Fqd.newElement();
            polyModElement.getCoefficient(1).setToOne();

            polyModElement.pow(q);

            xPowq = polyModElement;
            xPowq2 = polyModElement.duplicate().square();
        } else {
            tateExp = Fqk.getOrder().subtract(BigInteger.ONE).divide(r);
        }

        // init etwist
        CurveField etwist;
        if (curveParams.containsKey("twist.a")) {
            // load the twist
            Element twistA = Fqd.newElementFromBytes(curveParams.getBytes("twist.a"));
            Element twistB = Fqd.newElementFromBytes(curveParams.getBytes("twist.b"));

            etwist = new CurveField(twistA, twistB, r, curveParams.getBytes("twist.gen"));
        } else {
            etwist = initEqMap().twist();
        }

        // ndonr temporarily holds the trace.
        BigInteger ndonr = q.subtract(n).add(BigInteger.ONE);

        // Negate it because we want the trace of the twist.
        ndonr = ndonr.negate();
        ndonr = BigIntegerUtils.pbc_mpz_curve_order_extn(q, ndonr, d);
        ndonr = ndonr.divide(r);
        etwist.setQuotientCmp(ndonr);

        nqrInverse = Fqd.getNqr().duplicate().invert();
        nqrInverseSquare = nqrInverse.duplicate().square();

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
        return new PolyModField(irred, curveParams.getBigInteger("nqr"));
    }

    private QuadraticField initQuadratic() {
        return new QuadraticField(Fqd);
    }

    private Field initGT() {
        return new GTFiniteField(r, pairingMap, Fqk);
    }

    private void initMap() {
        pairingMap = new TypeDTateAffineNoDenomMillerPairingMap(this);
    }

}