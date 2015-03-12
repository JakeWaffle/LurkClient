package com.lcsc.cs.lurkclient.game;

import java.awt.*;

/**
 * Created by Student on 3/12/2015.
 */
public class LogicLinker {
    private EntityContainer entities;
    private EventBox        eventBox;
    private InputBox        inputBox;

    public LogicLinker(EntityContainer entities, EventBox eventBox, InputBox inputBox) {
        this.entities = entities;
        this.eventBox = eventBox;
        this.inputBox = inputBox;
    }
}
