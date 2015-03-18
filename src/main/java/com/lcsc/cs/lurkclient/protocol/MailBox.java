package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/7/2015.
 */
public class MailBox extends Thread {
    private static final Logger _logger = LoggerFactory.getLogger(MailBox.class);

    private         boolean                 _done;
    private         BlockingQueue<List<Response>> _responseQueue;
    private         BufferedReader          _serverReader;

    public MailBox(BlockingQueue<List<Response>> responseQueue, BufferedReader reader) {
        _done = false;
        _responseQueue = responseQueue;
        _serverReader = reader;
    }

    public void run() {
        while (!_done) {
            char[] msg = new char[4096];
            try {
               _serverReader.read(msg);
            } catch (IOException e) {
                _logger.error("MailBox was interrupted probably so it could join its thread.");
            }

            String message = new String(msg).replaceAll("\0", "");

            if (message.startsWith("INFOM")) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(message);

                int msgLength   = 0;
                if (matcher.find())
                    msgLength       = Integer.parseInt( matcher.group(0));

                //This adds to the length of the message, since 'INFOM ' is technically apart of the message.
                msgLength       += "INFOM ".length();

                //If we don't already have the entire message.
                while (message.length() < msgLength) {
                    Arrays.fill(msg, 0, msg.length, '\0');
                    try {
                        _serverReader.read(msg);
                    } catch (IOException e) {
                        _logger.error("MailBox was interrupted probably so it could join its thread.");
                    }
                    message += new String(msg).replaceAll("\0", "");
                }
            }

            //TODO Bundle responses with the same ResponseType into a list and then send them to each response listener!
            List<Response> responses = Response.getResponses(message);
            _responseQueue.add(responses);
        }
    }

    public synchronized void stopFramer() {
        _done = true;
    }
}
