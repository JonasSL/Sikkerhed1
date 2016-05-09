package com.company;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by 201303833 on 22-04-2016.
 * This file handles recieving data from the other part, that is: Reads from a given stream and gives it to ER.
 */
public class DistributedReceiver implements Runnable{
    ObjectInputStream objectInput;
    QueueHandler qh;
    Socket socket;
    public DistributedReceiver(ObjectInputStream objectInput, QueueHandler qh, Socket socket)
    {
        this.objectInput = objectInput;
        this.qh = qh;
        this.socket = socket;
    }

    @Override
    public void run() {
        Object o;
        try {

            while(socket != null){
                if((o = objectInput.readObject()) != null) {
                    qh.inputQueue.put(o);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
