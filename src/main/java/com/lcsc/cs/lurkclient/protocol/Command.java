package com.lcsc.cs.lurkclient.protocol;

/**
 * Created by Jake on 2/26/2015.
 */
public class Command {
    private final String message;

    public Command(CommandType commandType, String body) {
        this.message = String.format("%s %s", commandType.getCommandName(), body);
    }

    public Command(String message) {
        this.message = message;
    }

    public CommandType getCommandType() {
        String cmdText = this.message.substring(0,5);
        CommandType cmd = CommandType.fromString(cmdText);

        return cmd;
    }

    public byte[] toBytes() {
        return message.getBytes();
    }
}