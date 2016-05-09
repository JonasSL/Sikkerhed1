package com.company;

import java.math.BigInteger;

/**
 * Created by jonaslarsen on 09/05/2016.
 */
public class DriverServer {
    public static void main(String[] args) {


        BigInteger pk_mine = BigInteger.valueOf(7);
        BigInteger pk_other = BigInteger.valueOf(3);
        BigInteger p = new BigInteger("7620046599588662839088988721699316128379386201615986136646983477178094169858900860766086410346851315352398744286870986214557059590891002974460122094313582636104894801755304245985273544067588914290933851934660352003340093461298808205612145767244995098905052230695214413473707561979465149596491370907157");
        BigInteger q = new BigInteger("9004496944309282978163483361693890413696351249782395227038629284046281079851565195291149923581649880534789546512093771199085109195689325927578815940600089094289132080132630166593219211980881424312366403188049883466681707354748303414469546886382396876230879194090056368927520035941930241561961484172919");

        RSA rsa = new RSA(p,q,pk_mine);


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
                        break;
                    }
                }
            }
        }).start();




    }
}
