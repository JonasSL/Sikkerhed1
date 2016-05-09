package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
        //Client
        BigInteger pk_mine = BigInteger.valueOf(3);
        BigInteger pk_other = BigInteger.valueOf(5);
        RSA rsa = new RSA(pk_mine);


        QueueHandler qh = new QueueHandler(pk_mine,pk_other,rsa);


        DistributedClient client = new DistributedClient(40123, qh);
        System.out.println("After Client Run");
        new Thread(new Runnable() {
            public void run() {
                client.run("10.192.114.243");
            }
        }).start();

        qh.sendCertificate();

        new Thread(new Runnable() {
            @Override
            public void run() {
               while(true) {
                   qh.checkResponses();
               }
            }
        }).start();



        /*BigInteger message = BigInteger.valueOf(2134);

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
        long startTime = System.nanoTime();
        rsa.sign(message2);
        long endTime = System.nanoTime();
        double duration = ((endTime - startTime)/1000000000.0);
        System.out.println("Took " + duration + " seconds to sign message using hash function and RSA.");

        startTime = System.nanoTime();
        rsa.decrypt(message2);
        endTime = System.nanoTime();
        duration = ((endTime - startTime)/1000000000.0);
        System.out.println("Took " + duration + " seconds to sign message using RSA");*/

    }

}
