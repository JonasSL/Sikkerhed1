package com.company;

import java.math.BigInteger;

public class Driver {

    public static void main(String[] args) {
        RSA rsa = new RSA();

        BigInteger message = BigInteger.valueOf(2134);
        BigInteger cipher = rsa.encrypt(message);

        System.out.println("Encrypted " + message + " to this cipher: " + cipher);

        //magic

        BigInteger messageDecrypt = rsa.decrypt(cipher);
        System.out.println("Decrypted " + message + " to be " + messageDecrypt);
    }

}
