package com.lcsc.cs.lurkclient;

import com.lcsc.cs.lurkclient.states.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.event.WindowEvent;


import java.awt.Container;
import java.lang.Override;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Student
 */
public class Main extends JFrame{
    private Container                   contentPane;

    private String                      currentStateName    = "NULL";
    private StateInterface              currentState;

    public Main() {
        super();
        this.contentPane = this.getContentPane();
        this.setSize(1024,768);
        this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        //We will handle the closing of the window manually so that states can be cleaned up nicely
        //before the program quits randomly.
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //This intercepts the window closing event (when the 'x' is pressed.)
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(Main.this,
                        "Are you sure you want to do that?", "You're leaving me?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    Main.this.closeWindow();
                }
            }
        });

        this.setVisible(true);
    }

    //This is called when the 'x' in the window is pressed. It is designed to manually close the window/program
    //and also inform the current state that it needs to clean up its stuff.
    private void closeWindow() {
        this.currentState.cleanUp();
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

    //This handles loading a new state by updating the currentState variable and fetching a new
    //JPanel for the window.
    //@param nextState          This should be the EXACT class name of one of the classes in the
    //                          com.lcsc.cs.lurkclient.states package. This class name is used to dynamically load
    //                          the corresponding class for the next state.
    //@param nextStateParams    This contains all of the parameters that are to be passed to the next state. These
    //                          parameters were received from the previous state.
    private void changeState(String nextState, Map<String,String> nextStateParams) {
        this.currentStateName   = nextState;

        Class<?> clazz = null;
        try {
            //The nextState class is assumed to exist within this package!
            clazz = Class.forName("com.lcsc.cs.lurkclient.states."+nextState);
            this.currentState       = (StateInterface)clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.currentState.init(nextStateParams);

        this.contentPane.removeAll();
        JPanel newScene             = this.currentState.createState();
        this.contentPane.add(newScene);

        this.validate();
        this.repaint();
        this.setVisible(true);
    }

    //This is the main loop for the gui state machine! It handles changing states and determining when the end
    //of the program has been reached.
    public void mainLoop() {
        //The game will start out in the Login state.
        this.changeState("Login", null);

        boolean done = false;
        while (!done) {
            done = this.currentState.run();

            if (done == true) {
                break;
            }

            String nextState                    = this.currentState.getNextState();
            Map<String,String> nextStateParams  = this.currentState.getNextStateParams();

            this.changeState(nextState, nextStateParams);
        }

        //Clean up stuff and close application here...
        System.exit(0);
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.mainLoop();
    }
}