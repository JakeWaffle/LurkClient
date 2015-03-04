package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;
import java.util.Map;

/**
 * Created by Student on 2/24/2015.
 */
public interface StateInterface {
    //This initializes the state.
    //@param params This map should have been passed to this state from the previous state.
    //              getNextStateParams() is the method that returns the parameters for the next state.
    public void init(Map<String,String> params);

    //This will return the JPanel for the current state so the window can be updated.
    //@return A JPanel object filled with all of the things that will be displayed in this state.
    public JPanel createState();

    //This will run the main loop of the current state. THis must be a blocking method
    //that runs for the life of the state.
    //@return A boolean that says whether we're quiting the program or not.
    public boolean run();

    //This is called after run finishes and specifies what the next state is going to be.
    //@return A string that should be a class name (without the package) of a class that implements
    //          this interface.
    public State getNextState();

    //This returns parameters that will then be passed into the next state's init() method.
    //@return A map that represents the next state's parameters.
    public Map<String,String> getNextStateParams();

    //This is used to stop the state in a nice manner.
    public void cleanUp();
}