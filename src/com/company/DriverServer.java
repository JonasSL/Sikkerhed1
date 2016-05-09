package com.company;

import java.math.BigInteger;

/**
 * Created by jonaslarsen on 09/05/2016.
 */
public class DriverServer {
    public static void main(String[] args) {
        BigInteger pk_mine = BigInteger.valueOf(5);
        BigInteger pk_other = BigInteger.valueOf(3);
        RSA rsa = new RSA(pk_mine);

        QueueHandler qh = new QueueHandler(pk_mine,pk_other,rsa);

        DistributedServer server = new DistributedServer(40123, qh);
        //Run server in seperate thread.
        new Thread(new Runnable() {
            public void run() {
                server.run();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (!qh.checkResponses()) {
                        System.out.println("REJECT");
                    }
                }
            }
        }).start();




    }
}
