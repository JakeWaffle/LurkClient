package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Jake on 2/26/2015.
 */
public class MailMan extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MailMan.class);

    private         boolean                 done;
    private         boolean                 connected   = false;
    private         Socket                  sock        = null;
    private         OutputStream            out         = null;
    private         BufferedReader          in          = null;
    private         List<ResponseListener>  listeners   = new ArrayList<ResponseListener>();
    private         BlockingQueue<List<Response>> responseQueue;
    private MailBox framer;

    public MailMan() {
        this.done = false;
        this.responseQueue = new ArrayBlockingQueue<List<Response>>(50);
    }

    //This method will be waiting for input from the server essentially.
    //Then it will send those messages back to all of the listeners that are registered.
    public void run() {
        while (!done) {
            while (this.responseQueue.size() > 0) {
                try {
                    List<Response> responses = this.responseQueue.take();
                    for (ResponseListener listener : this.listeners) {
                        listener.notify(responses);
                    }
                } catch(InterruptedException e) {
                    logger.error("Interrupted while removing from the response queue.", e);
                }
            }

            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                logger.error("Interrupted while sleeping! I'm really mad!", e);
            }
        }
    }

    public synchronized boolean isConnected() {
        return this.connected;
    }

    //This will attempt to connect to the server
    //@param host The host we're connecting to.
    //@param port The port the server is listening on.
    public synchronized boolean connect(String host, int port) {
        boolean success = true;

        try {
            this.sock       = new Socket(host, port);
            this.out        = new DataOutputStream(this.sock.getOutputStream());
            this.in         = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.framer     = new MailBox(this.responseQueue, this.in);
            this.connected  = true;
            this.framer.start();
        } catch (UnknownHostException e) {
            success = false;
            logger.error(String.format("Unknown Server\nHost: %s\nPort: %d", host, port), e);
        } catch (IOException e) {
            success = false;
            logger.error("OutputStream/InputStream failed to initialize", e);
        }

        return success;
    }

    public synchronized void disconnect() {
        this.done = true;
        if (this.sock != null && this.out != null && this.in != null) {
            try {
                this.framer.stopReceiving();
                logger.debug("Intentionally closing the socket so the MailBox isn't blocking on a read anymore!");
                this.sock.close();
                this.out.close();
                this.in.close();
                //The framer won't join until it's done with its read operation (which is why the socket and reader
                // are closed!)
                this.framer.join();
                this.connected = false;
                logger.debug("Joined MailBox thread!");
            } catch (IOException e) {
                logger.error("Couldn't close socket to server!", e);
            } catch(InterruptedException e) {
                logger.error("Couldn't join the MailBox thread.", e);
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
            Thread.sleep(100);
        } catch(IOException e) {
            logger.error("Couldn't write to server", e);
        } catch(InterruptedException e) {
            logger.error("Interrupted while sleeping! I'm really mad!", e);
        }
    }
}