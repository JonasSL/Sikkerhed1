package com.company;

import java.math.BigInteger;
import java.util.Random;

public class Driver {

    public static void main(String[] args) {
        //Client
        //How to run:
        /*

         Run DriverServer.
         Write server IP in the client.run(ip) method.
         Run Driver.

         */

        BigInteger pk_mine = BigInteger.valueOf(3);
        BigInteger pk_other = BigInteger.valueOf(7);
        BigInteger p = new BigInteger("7620046599588662839088988721699316128379386201615986136646983477178094169858900860766086410346851315352398744286870986214557059590891002974460122094313582636104894801755304245985273544067588914290933851934660352003340093461298808205612145767244995098905052230695214413473707561979465149596491370907157");
        BigInteger q = new BigInteger("9004496944309282978163483361693890413696351249782395227038629284046281079851565195291149923581649880534789546512093771199085109195689325927578815940600089094289132080132630166593219211980881424312366403188049883466681707354748303414469546886382396876230879194090056368927520035941930241561961484172919");

        RSA rsa = new RSA(p,q,pk_mine);

        QueueHandler qh = new QueueHandler(pk_mine,pk_other,rsa);

        DistributedClient client = new DistributedClient(40123, qh);
        new Thread(new Runnable() {
            public void run() {
                client.run("10.192.3.32");
            }
        }).start();

        qh.sendCertificate();

        new Thread(new Runnable() {
            @Override
            public void run() {
               while(true) {
                   if (!qh.checkResponses()) {
                       System.out.println("REJECT");
                       break;
                   }
               }
            }
        }).start();


/*
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
        long startTime = System.nanoTime();
        rsa.sign(message2);
        long endTime = System.nanoTime();
        double duration = ((endTime - startTime)/1000000000.0);
        System.out.println("Took " + duration + " seconds to sign message using hash function and RSA.");

        startTime = System.nanoTime();
        rsa.decrypt(message2);
        endTime = System.nanoTime();
        duration = ((endTime - startTime)/1000000000.0);
        System.out.println("Took " + duration + " seconds to sign message using RSA");
*/
    }

}
