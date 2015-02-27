package com.lcsc.cs.lurkclient.protocol;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;

/**
 * Created by Jake on 2/26/2015.
 */
public class Messenger {
    static Logger logger = Logger.getLogger(Messenger.class);
    private static Socket sock = null;
    private static OutputStream out = null;
    private static Reader in = null;

    //This will attempt to connect to the server
    //@param host The host we're connecting to.
    //@param port The port the server is listening on.
    public static boolean connect(String host, int port) {
        boolean success = true;

        try {
            sock    = new Socket(host, port);
            out     = new DataOutputStream(sock.getOutputStream());
            in      = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (UnknownHostException e) {
            success = false;
            logger.error(String.format("Unknown Server\nHost: %s\nPort: %d", host, port), e);
        } catch (IOException e) {
            success = false;
            logger.error("OutputStream/InputStream failed to initialize", e);
        }
        return success;
    }

    public static void disconnect() {
        if (sock != null && out != null && in != null) {
            try {
                sock.close();
                out.close();
                in.close();
            } catch (IOException e) {
                logger.error("Couldn't close socket to server!", e);
            }
        }
    }

    //This will send a message to the server
    public static void sendMessage(Message message) {
        try {
            out.write(message.toBytes());
        } catch(IOException e) {
            logger.error("Couldn't write to server", e);
        }
    }
}
