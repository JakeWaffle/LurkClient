package com.lcsc.cs.lurkclient.protocol;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Jake on 3/7/2015.
 */
public class MessageFramer extends Thread {
    private static  Logger                  logger = Logger.getLogger(MessageFramer.class);
    private         BlockingQueue<Response> responseQueue;
    private         BufferedReader          serverReader;

    public MessageFramer(BlockingQueue<Response> responseQueue, BufferedReader reader) {
        this.responseQueue  = responseQueue;
        this.serverReader   = reader;
    }

    public void run() {
        while (true) {
            char[] msg = new char[4096];
            try {
               this.serverReader.read(msg);
            } catch (IOException e) {
                logger.error("Couldn't read input from the server.", e);
            }

            List<Response> responses = Response.getResponses(new String(msg));
            for (Response response : responses) {
                this.responseQueue.add(response);
            }
        }
    }
}
