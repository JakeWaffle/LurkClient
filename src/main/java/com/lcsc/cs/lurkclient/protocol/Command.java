package com.lcsc.cs.lurkclient.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jake on 2/26/2015.
 */
public class Command {
    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private final   CommandType type;
    private         String      body = null;

    public Command(CommandType type, String body) {
        this.type       = type;
        this.body       = body;
    }

    public Command(CommandType type) {
        this.type = type;
    }

    public CommandType getCommandType() {
        return this.type;
    }

    private String buildMessage() {
        String message = this.type.getCommandHeader();

        if (body != null) {
            message += " "+this.body;
        }

        return message;
    }

    public byte[] toBytes() {
        return this.buildMessage().getBytes();
    }
    public String toString() {
        return this.buildMessage();
    }
}