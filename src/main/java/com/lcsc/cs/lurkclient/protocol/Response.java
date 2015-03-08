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
    private ResponseType    responseType;
    private String          message;

    public static List<Response> getResponses(String message) {
        List<Response> responses = new ArrayList<Response>();

        logger.info("Received Message:\n"+message);

        String[] words = message.split("\\s+");

        ResponseType    respType    = null;
        int             start       = 0;
        for (String word : words) {
            ResponseType newRespType = ResponseType.fromString(word);
            if (newRespType != null) {
                int end = message.indexOf(word, start);
                //We've found an additional response in the message.
                if (respType != null) {
                    //This message doesn't include the response type!
                    String respMsg  = message.substring(start, end);
                    Response resp   = new Response(respType, respMsg);
                    responses.add(resp);
                }
                respType = newRespType;
                //This makes the start of the next response message start after the response type.
                start = end + word.length();
            }
        }
        if (respType != null) {
            //This message doesn't include the response type!
            String respMsg  = message.substring(start, message.length());
            Response resp   = new Response(respType, respMsg);
            responses.add(resp);
        }

        return responses;
    }

    public Response(ResponseType type, String message) {
        this.responseType   = type;
        this.message        = message.trim();;
        logger.debug(this.toString());
    }

    public String getType() {
        return this.responseType.toString();
    }

    public String getResponse() {
        return this.message;
    }

    public String toString(){
        return String.format(String.format("Response Type: %s\nResponse:%s", this.responseType.toString(), this.message));
    }
}
