package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Jake on 3/3/2015.
 */
public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    public static List<Response> getResponses(String message) {
        List<Response> responses = new ArrayList<Response>();

        logger.info("Received Message:\n"+message);

        //TODO Parse the message that was received from the server and instantiate Response objects!

        return responses;
    }

    public Response(ResponseType type, String message) {

    }
}
