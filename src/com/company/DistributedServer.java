package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jonaslarsen on 20/04/2016.
 */
public class DistributedServer implements Peer {

    protected int portNumber;
    protected ServerSocket serverSocket;
    private QueueHandler qh;
    Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
    private Boolean wantToDC = false;

    public DistributedServer(int portNumber, QueueHandler qh) {
        this.portNumber = portNumber;
        this.qh = qh;
    }


    public void run() {
        System.out.println("Server started");

        registerOnPort();

        while (!wantToDC) {

            socket = waitForConnectionFromClient();
            System.out.println(getLocalHostAddress());
            if (socket != null) {
                System.out.println("Connection from " + socket);

                try {
                    objectOutput = new ObjectOutputStream(socket.getOutputStream());
                    //objectOutput.flush();
                    objectInput = new ObjectInputStream(socket.getInputStream());

                    // Read and print what the client is sending
                    DistributedSender distributedSender = new DistributedSender(qh, objectOutput, socket);
                    new Thread(new Runnable() {
                        public void run() {distributedSender.run();}}).start();

                    DistributedReceiver distributedReceiver = new DistributedReceiver(objectInput, qh, socket);
                    distributedReceiver.run();

                } catch (Exception e) {

                    // We report but otherwise ignore IO Exceptions
                    System.err.println(e);

                }
                System.out.println("Connection closed by client.");
            }

        }
        deregisterOnPort();
        System.out.println("Goodbye world!");
    }

    /**
     *
     * Will print out the IP address of the local host and the port on which this
     * server is accepting connections.
     */
    protected String getLocalHostAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println(e);
            System.exit(-1);
            return "Cannot resolve the Internet address of the local host.";
        }
    }

    /**
     *
     * Will register this server on the port number portNumber. Will not start waiting
     * for connections. For this you should call waitForConnectionFromClient().
     */
    protected void registerOnPort() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            serverSocket = null;
            System.err.println("Cannot open server socket on port number" + portNumber);
            System.err.println(e);
            System.exit(-1);
        }
    }

    @Override
    public void deregisterOnPort() {
        if (serverSocket != null) {
            try {
                //Check for null before closing the stream/socket
                if (objectInput !=null){
                    objectInput.close();
                }

                if (objectOutput!= null) {
                    objectOutput.close();
                }

                if (socket != null) {
                    socket.close();
                    socket = null;
                }

                serverSocket.close();
                serverSocket = null;
                System.out.println("socket is closed!");
                wantToDC = true;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     *
     * Waits for the next client to connect on port number portNumber or takes the
     * next one in line in case a client is already trying to connect. Returns the
     * socket of the connection, null if there were any failures.
     */
    protected Socket waitForConnectionFromClient() {
        Socket res = null;
        try {
            res = serverSocket.accept();
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }


}
