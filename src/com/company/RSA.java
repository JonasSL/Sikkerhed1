package com.company;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RSA {

    BigInteger e;
    BigInteger p;
    BigInteger q;
    BigInteger d;
    BigInteger n;
    MessageDigest digest;
    int k = 2000;

    public RSA(BigInteger e) {
        this.e = e;
        generatePrimes();
        d = keygen();
        System.out.println("for " + e);
        System.out.println("d : " + d);
        System.out.println("n: " + n);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            System.out.println(e1);
        }
    }

    public RSA(BigInteger p, BigInteger q, BigInteger e) {
        this.e = e;
        this.p = p;
        this.q = q;
        n = q.multiply(p);
        this.d = keygen();

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Generates valid primes p and q
     */
    private void generatePrimes() {
        p = BigInteger.probablePrime(k/2, new Random());
        q = BigInteger.probablePrime(k/2, new Random());

        BigInteger smallP = p.subtract(BigInteger.ONE);
        BigInteger smallQ = q.subtract(BigInteger.ONE);

        //Generate a new p until p-1 is relative prime to 3
        while (!smallP.gcd(e).equals(BigInteger.ONE)) {
            p = BigInteger.probablePrime(k/2, new Random());
            smallP = p.subtract(BigInteger.ONE);
        }

        //Generate a new q until q-1 is relative prime to 3
        while (!smallQ.gcd(e).equals(BigInteger.ONE)) {
            q = BigInteger.probablePrime(k/2, new Random());
            smallQ = q.subtract(BigInteger.ONE);
        }

        n = q.multiply(p);
        if (n.bitLength() != k) {
            generatePrimes();
            return;
        }
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
    public BigInteger encrypt(BigInteger message, BigInteger pk) {
        return message.modPow(pk, n);
    }

    /**
     * Decrypts the ciphertext
     * @param ciphertext
     * @return
     */
    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d,n);
    }

    /**
     * Signs a message by hashing and then signs it with the private key.
     * @param message the message
     * @return the signed message
     */
    public BigInteger sign(BigInteger message) {
        long startTime = System.nanoTime();
        digest.update(message.byteValue());
        BigInteger hashedMessage = new BigInteger(1,digest.digest());
        long endTime = System.nanoTime();

        double duration = ((endTime - startTime)/1000000000.0);

        //System.out.println("Duration for hashing " + message.bitLength() + " bits: " + duration + " seconds");
        //System.out.println("Bits pr second: " + message.bitLength()/duration);
        return decrypt(hashedMessage);

    }

    /**
     * Verifies a signed text and a plain text
     * @param signedText
     * @param plainText
     * @return boolean answer for the verification
     */
    public boolean verify(BigInteger signedText, BigInteger plainText, BigInteger pk) {
        BigInteger hashedMessage = encrypt(signedText, pk);
        digest.update(plainText.byteValue());
        BigInteger hashedPlainText = new BigInteger(1,digest.digest());
        return hashedMessage.equals(hashedPlainText);
    }
}

