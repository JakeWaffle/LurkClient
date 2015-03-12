package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Jake on 3/7/2015.
 */
public class MailBox extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MailBox.class);

    private         boolean                 done;
    private         BlockingQueue<Response> responseQueue;
    private         BufferedReader          serverReader;

    public MailBox(BlockingQueue<Response> responseQueue, BufferedReader reader) {
        this.done           = false;
        this.responseQueue  = responseQueue;
        this.serverReader   = reader;
    }

    public void run() {
        while (!done) {
            char[] msg = new char[4096];
            try {
               this.serverReader.read(msg);
            } catch (IOException e) {
                logger.error("MailBox was interrupted probably so it could join its thread.");
            }

            List<Response> responses = Response.getResponses(new String(msg));
            for (Response response : responses) {
                this.responseQueue.add(response);
            }
        }
    }

    public synchronized void stopFramer() {
        this.done = true;
    }
}
