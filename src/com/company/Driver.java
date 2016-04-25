package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
        RSA rsa = new RSA();

        BigInteger message = BigInteger.valueOf(2134);
        BigInteger signedMessage = rsa.sign(message);

        System.out.println("Signed " + message);

        //magic

        //Real test
        System.out.println("Verified " + message + " to be " + rsa.verify(signedMessage,message));

        //False test
        BigInteger fakeMessage = BigInteger.valueOf(1337);
        System.out.println("Verified " + fakeMessage + " to be " + rsa.verify(signedMessage,fakeMessage));

        //Measure hash function
        BigInteger message2 = new BigInteger(80000, new Random());
        rsa.sign(message2);
    }

}
