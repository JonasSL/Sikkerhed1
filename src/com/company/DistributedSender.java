package com.company;

import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 201303833 on 22-04-2016.
 * This file handles sending of data to the other part:
 * Checks it's register in EventReplayer and takes it if it finds something.
 */
public class DistributedSender implements Runnable {
    ObjectOutputStream objectOutput;
    Socket socket;
    QueueHandler qh;

    DistributedSender(QueueHandler qh, ObjectOutputStream objectOutput, Socket socket) {
        this.qh = qh;
        this.objectOutput = objectOutput;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while(socket != null){
                if ((qh.outputQueue.peek()) != null) {
                    objectOutput.writeObject(qh.outputQueue.take());
                    System.out.println("sent from sender");
            }}
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
