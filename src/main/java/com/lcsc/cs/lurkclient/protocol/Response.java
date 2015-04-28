package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/3/2015.
 */
public class Response {
    private static final Logger     _logger = LoggerFactory.getLogger(Response.class);
    public  final ResponseType      type;
    public  final String            message;

    public static List<Response> getResponses(String message) {
        List<Response> responses = new ArrayList<Response>();

        Pattern pattern = Pattern.compile(ResponseType.getResponseTypePattern());
        Matcher matcher = pattern.matcher(message);

        //We need to match the first item to get an idea of where we're starting and which response type we're
        // starting with.
        if (matcher.find()) {
            ResponseType type   = ResponseType.fromString(matcher.group());
            //The message starts after this header and ends before the next header.
            int start           = matcher.end();
            int end             = -1;
            while (matcher.find()) {
                end             = matcher.start();

                Response newResp = new Response(type, message.substring(start+1, end));
                responses.add(newResp);

                type            = ResponseType.fromString(matcher.group());
                start           = matcher.end();
            }

            Response newResp = new Response(type, message.substring(start+1));
            responses.add(newResp);
        }
        else {
            _logger.error("Message doesn't have any valid headers for some reason: "+message);
        }

        return responses;
    }

    public Response(ResponseType type, String message) {
        if (type == ResponseType.INFORM){
            //The unneeded message length after the header needs to be replaced since this is
            //an INFOM message.
            message = message.replaceFirst("[0-9]+", "");

            if (message.contains("GameDescription:"))
                this.type = ResponseType.QUERY_INFORM;
            else if (message.contains("Location:"))
                this.type = ResponseType.PLAYER_INFORM;
            else if (message.contains("Connection:") || message.contains("Monster:"))
                this.type = ResponseType.ROOM_INFORM;
            else if (message.contains("Name:"))
                this.type = ResponseType.MONSTER_INFORM;
            else
                this.type = ResponseType.INVALID;
        }
        else
            this.type       = type;

        this.message = message.trim();

        _logger.info("Received Message:\n"+toString());
    }

    public String getType() {
        return type.toString();
    }

    public String getResponse() {
        return message;
    }

    public String toString(){
        String str = "";
        try {
            str = String.format(String.format("Response Type: " + type.toString() + "\nResponse: " + message));
        } catch(Exception e) {
            _logger.error("Kyle probably was being mean again...", e);
        }
        return str;
    }
}