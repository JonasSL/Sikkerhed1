package com.company;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jonaslarsen on 09/05/2016.
 */
public class QueueHandler {
    LinkedBlockingQueue<Object> inputQueue;
    LinkedBlockingQueue<Object> outputQueue;

    ArrayList<Object> history;
    RSA rsa;

    final BigInteger p = BigInteger.valueOf(100003);
    final BigInteger g = BigInteger.valueOf(100000);

    BigInteger x_mine;
    BigInteger key;
    BigInteger pk_mine;
    BigInteger pk_other;

    boolean hasSentCert = false;
    boolean hasSentSig = false;

    public QueueHandler(BigInteger pka, BigInteger pkb, RSA rsa) {

        inputQueue = new LinkedBlockingQueue<>();
        history = new ArrayList();
        outputQueue = new LinkedBlockingQueue<>();

        int temp = (int) (Math.random()*100001);
        x_mine = BigInteger.valueOf(temp);
        this.pk_mine = pka;
        this.pk_other = pkb;
        this.rsa = rsa;
    }

    public boolean checkResponses() {
        Object input = null;
        try {
            input = inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (input != null && input instanceof Certificate) {
            System.out.println("is certificate");
            Certificate cert = (Certificate) input;
            history.add(cert);
            key = cert.number.modPow(x_mine,p);
            System.out.println(key);
            if (!hasSentCert) {
                sendCertificate();
            }
        } else  {
            ArrayList<Object> list = (ArrayList<Object>) input;

            for (int i = 0; i<list.size();i++) {

                if (list.get(i) instanceof Certificate) {
                    Certificate certMine = (Certificate) history.get(i);
                    Certificate certOther = (Certificate) list.get(i);

                    if (!(rsa.verify(certMine.pk,certOther.pk))) {
                        return false;
                    }
                    if (!(rsa.verify(certMine.number,certOther.number))) {
                        return false;
                    }
                }
            }
            System.out.println(key);
            if (!hasSentSig) {
                sendSignedMessages();
            }
        }
        return true;

    }

    public void sendSignedMessages() {
        ArrayList<Object> list = new ArrayList();
        for (Object obj: history) {
            if (obj instanceof Certificate) {
                list.add(signCerficate((Certificate) obj));
            } else {
                list.add(rsa.sign((BigInteger) obj));
            }
        }

        try {
            outputQueue.put(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hasSentSig = true;

    }

    public Certificate signCerficate(Certificate cert) {
        return new Certificate(rsa.sign(cert.pk),rsa.sign(cert.number));
    }



    public void sendCertificate() {
        BigInteger number = g.modPow(x_mine,p);
        Certificate cert = new Certificate(pk_mine, number);
        try {
            outputQueue.put(cert);
            history.add(cert);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hasSentCert = true;
    }

    public BigInteger getKey() {
        return key;
    }
}
