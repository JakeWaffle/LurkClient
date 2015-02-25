package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;

/**
 * Created by Student on 2/24/2015.
 */
public interface StateInterface {
    public JPanel createState();
    public String run();
}