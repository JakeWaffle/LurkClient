package com.lcsc.cs.lurkclient.protocol;

/**
 * Created by Jake on 3/3/2015.
 */
public enum CommandType {
    CONNECT("CNNCT"),
    SET_ATTACK_STAT("ATTCK"),
    SET_DEFENSE_STATE("DEFNS"),
    SET_REGEN_STAT("REGEN"),
    SET_PLAYER_DESC("DESCR"),
    LEAVE("LEAVE"),
    QUERY("QUERY"),
    START("START"),
    ACTION("ACTON");

    private final String commandName;

    private CommandType(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public static CommandType fromString(String commandName) {
        if (commandName != null) {
            for (CommandType c : CommandType.values()) {
                if (commandName.equalsIgnoreCase(c.getCommandName())) {
                    return c;
                }
            }
        }
        return null;
    }
};