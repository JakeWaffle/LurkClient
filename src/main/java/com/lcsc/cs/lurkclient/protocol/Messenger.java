package com.lcsc.cs.lurkclient.protocol;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Jake on 2/26/2015.
 */
public class Messenger extends Thread {
    private static  Logger                  logger      = Logger.getLogger(Messenger.class);
    private         Socket                  sock        = null;
    private         OutputStream            out         = null;
    private         BufferedReader          in          = null;
    private         List<ResponseListener>  listeners   = new ArrayList<ResponseListener>();
    private         BlockingQueue<Response> responseQueue;
    private         MessageFramer           framer;

    public Messenger() {}

    //This method will be waiting for input from the server essentially.
    //Then it will send those messages back to all of the listeners that are registered.
    public synchronized void run() {
        while (true) {
            for (Response response : responseQueue) {
                for (ResponseListener listener : this.listeners) {
                    listener.notify(response);
                }
            }

            try {
                Thread.sleep(200);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //This will attempt to connect to the server
    //@param host The host we're connecting to.
    //@param port The port the server is listening on.
    public synchronized boolean connect(String host, int port) {
        boolean success = true;

        try {
            this.sock    = new Socket(host, port);
            this.out     = new DataOutputStream(this.sock.getOutputStream());
            this.in      = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (UnknownHostException e) {
            success = false;
            logger.error(String.format("Unknown Server\nHost: %s\nPort: %d", host, port), e);
        } catch (IOException e) {
            success = false;
            logger.error("OutputStream/InputStream failed to initialize", e);
        }

        this.framer = new MessageFramer(this.responseQueue, this.in);
        this.framer.start();

        return success;
    }

    public synchronized void disconnect() {
        if (this.sock != null && this.out != null && this.in != null) {
            try {
                this.framer.join();
                this.sock.close();
                this.out.close();
                this.in.close();
            } catch (IOException e) {
                logger.error("Couldn't close socket to server!", e);
            } catch(InterruptedException e) {
                logger.error("Couldn't join the MessageFramer thread.", e);
            }
        }
    }

    public synchronized void registerListener(ResponseListener listener) {
        this.listeners.add(listener);
    }

    public synchronized void clearListeners() {
        this.listeners.clear();
    }

    //This will send a message to the server
    public synchronized void sendMessage(Command command) {
        try {
            logger.info("Sending Message to Server:\n"+command.toString());
            this.out.write(command.toBytes());
        } catch(IOException e) {
            logger.error("Couldn't write to server", e);
        }
    }
}