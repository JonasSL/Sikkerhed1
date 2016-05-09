package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.System.out;

/**
 * Created by jonaslarsen on 20/04/2016.
 */
public class DistributedClient implements Peer {

    protected int portNumber;
    Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
    private QueueHandler qh;

    public DistributedClient(int portNumber, QueueHandler qh) {
        this.portNumber = portNumber;
        this.qh = qh;
    }

    /**
     * Connects to the server on IP address serverName and port number portNumber.
     */
    protected Socket connectToServer(String serverName) {
        Socket res = null;
        try {
            res = new Socket(serverName, portNumber);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("client connect to server exception");
            // We return null on IOExceptions
        }
        return res;
    }

    public void run(String serverName) {

        System.out.println("Client started");

        socket = connectToServer(serverName);


            if (socket != null) {
                System.out.println("The client is connected: " + socket.isConnected());
                out.println("Connected to " + socket);

                try {

                    objectOutput = new ObjectOutputStream(socket.getOutputStream());
                    objectInput = new ObjectInputStream(socket.getInputStream());


                    DistributedSender distributedSender = new DistributedSender(qh, objectOutput, socket);
                    new Thread(new Runnable() {
                        public void run() {
                            distributedSender.run();
                        }}).start();
                            DistributedReceiver distributedReceiver = new DistributedReceiver(objectInput, qh, socket);
                            distributedReceiver.run();

                } catch (Exception e) {
                    // We ignore IOExceptions
                    System.out.println(e);

                }
                out.println("Goodbuy world!");
            }

        }

    @Override
    public void deregisterOnPort() {
        if (socket != null) {
            try {
                objectInput.close();
                objectOutput.close();
                socket.close();
                socket = null;

            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

}



