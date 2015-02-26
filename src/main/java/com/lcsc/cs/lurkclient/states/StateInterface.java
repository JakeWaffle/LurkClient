package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;

/**
 * Created by Student on 2/24/2015.
 */
public interface StateInterface {
    //This must create the JPanel for the state.
    public JPanel createState();
    //THis must be a blocking method that runs for the life of the state. Then it returns
    //the name of the next state class to run or 'Quit'.
    public String run();
}