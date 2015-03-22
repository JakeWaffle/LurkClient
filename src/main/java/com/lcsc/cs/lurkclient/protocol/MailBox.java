package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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
        String incompleteMsg = "";
        while (!_done) {
            char[] msg = new char[4096];
            try {
               _serverReader.read(msg);
            } catch (IOException e) {
                _logger.error("MailBox was interrupted probably so it could join its thread.");
                break;
            }

            String message = new String(msg).replaceAll("\0", "");

            //If we have an incomplete message laying around we need to use it!
            if (incompleteMsg.length() > 0) {
                message         = incompleteMsg + message;
                incompleteMsg   = "";
            }

            List<Response> responses = new ArrayList<Response>();

            //This just searched for the different Response headers that are possible.
            Pattern pattern = Pattern.compile(ResponseType.getResponseTypePattern());
            Matcher matcher = pattern.matcher(message);

            //We need to match the first item to get an idea of where we're starting and which response type we're
            // starting with.
            if (matcher.find()) {
                ResponseType type   = ResponseType.fromString(matcher.group());
                int start           = matcher.end();
                int end             = -1;
                if (matcher.find()) {
                    do {
                        end             = matcher.start();

                        Response newResp = new Response(type, message.substring(start+1, end));
                        responses.add(newResp);

                        type            = ResponseType.fromString(matcher.group());
                        start           = matcher.end();
                    } while (matcher.find());
                }
                //This is the place where we must not trust the INFORM message, because we have no idea if it's
                //complete or not until we check that message size and compare accordingly.
                if (type == ResponseType.INFORM) {
                    String suspiciousMsg = message.substring(start + 1);

                    pattern = Pattern.compile("[0-9]+");
                    matcher = pattern.matcher(suspiciousMsg);

                    int msgLength = 0;
                    if (matcher.find()) {
                        String sMsgLength = matcher.group();
                        msgLength = sMsgLength.length();
                        msgLength += Integer.parseInt(sMsgLength);

                        //The message is complete!
                        if (suspiciousMsg.length() == msgLength) {
                            Response newResp = new Response(type, message.substring(start + 1));
                            responses.add(newResp);
                        }
                        //We've got an incomplete message that will be handled in the next iteration of the
                        //while loop.
                        else if (suspiciousMsg.length() < msgLength) {
                            //We can't forget the header, that's really important!
                            //It was cut off when we were using the regex.
                            incompleteMsg = "INFOM "+suspiciousMsg;
                        }
                        //If this ever would happen, it would be because of an invalid header or lack there of.
                        //So the extras will be tossed away.
                        else if (suspiciousMsg.length() > msgLength) {
                            _logger.error("The INFOM message is larger than the message length: INFOM "+suspiciousMsg);
                            _logger.error("Tossing extra part of the INFOM message: "+suspiciousMsg.substring(msgLength));
                            Response newResp = new Response(type, message.substring(start + 1, msgLength));
                            responses.add(newResp);
                        }
                        else {
                            _logger.error("The suspiciousMsg is very suspicious, this shouldn't happen at all!");
                        }
                    }
                    //An INFOM should at least have a msg length after it!
                    else {
                        _logger.error("INFOM message didn't have a message length after it!?");
                    }
                }
                //No INFOM message means no incomplete message, uhuru! :D
                else {
                    Response newResp = new Response(type, message.substring(start + 1));
                    responses.add(newResp);
                }
            }
            else {
                _logger.error("Message doesn't have any valid headers for some reason: "+message);
            }

            if (responses.size() > 0)
                _responseQueue.add(responses);
        }
    }

    public synchronized void stopReceiving() {
        _done = true;
    }
}
