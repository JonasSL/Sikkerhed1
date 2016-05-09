package com.company;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by jonaslarsen on 09/05/2016.
 */
public class Certificate implements Serializable {

    BigInteger pk;
    BigInteger number;

    public Certificate(BigInteger cert, BigInteger number) {
        this.pk = cert;
        this.number = number;
    }
}
