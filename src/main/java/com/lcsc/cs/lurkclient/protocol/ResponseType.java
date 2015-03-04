package com.lcsc.cs.lurkclient.protocol;

/**
 * Created by Jake on 3/3/2015.
 */
public enum ResponseType {
    ACCEPTED("ACEPT"),
    REJECTED("REJEC"),
    RESULT("RESLT"),
    INFORM("INFOM"),
    MESSAGE("MSSG"),
    NOTIFY("NOTIF");

    private final String type;

    private ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static ResponseType fromString(String type) {
        if (type != null) {
            for (ResponseType r : ResponseType.values()) {
                if (type.equalsIgnoreCase(r.getType())) {
                    return r;
                }
            }
        }
        return null;
    }
};