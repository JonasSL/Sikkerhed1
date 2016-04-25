package com.company;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

    BigInteger e;
    BigInteger p;
    BigInteger q;
    BigInteger d;
    BigInteger n;

    public RSA() {
        e = BigInteger.valueOf(3);
        generatePrimes();
        d = keygen();
        System.out.println("Your secret key: " + d);
    }

    public RSA(BigInteger d, BigInteger n) {
        e = BigInteger.valueOf(3);
        this.d = d;
        this.n = n;
    }

    /**
     * Generates valid primes p and q
     */
    private void generatePrimes() {
        int k = 2000;
        p = BigInteger.probablePrime(k/2, new Random());
        q = BigInteger.probablePrime(k/2, new Random());

        BigInteger smallP = p.subtract(BigInteger.ONE);
        BigInteger smallQ = q.subtract(BigInteger.ONE);

        //Generate a new p until p-1 is relative prime to 3
        while (!smallP.gcd(e).equals(BigInteger.ONE)) {
            p = BigInteger.probablePrime(k/2, new Random());
            smallP = p.subtract(BigInteger.ONE);
        }

        //Generate a new p until q-1 is relative prime to 3
        while (!smallQ.gcd(e).equals(BigInteger.ONE)) {
            q = BigInteger.probablePrime(k/2, new Random());
            smallQ = q.subtract(BigInteger.ONE);
        }

        n = q.multiply(p);
        if (n.bitLength() != k) {
            generatePrimes();
            System.out.println("n was not k bits long");
            return;
        }
        System.out.println("Generated primes.");
    }

    /**
     * Generates a d.
     * @return private key value, d
     */
    public BigInteger keygen() {
        BigInteger smallP = p.subtract(BigInteger.ONE);
        BigInteger smallQ = q.subtract(BigInteger.ONE);
        return e.modInverse(smallP.multiply(smallQ));
    }

    /**
     * Encrypts the message with public key (e = 3)
     * @param message the message we want to encrypt
     * @return
     */
    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    /**
     * Decrypts the ciphertext
     * @param ciphertext
     * @return
     */
    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d,n);
    }
}

