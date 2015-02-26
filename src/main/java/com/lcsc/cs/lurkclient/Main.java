package com.lcsc.cs.lurkclient;

import com.lcsc.cs.lurkclient.states.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Container;
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
    }

    private void changeState(String nextState) {
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

        this.contentPane.removeAll();
        JPanel newScene         = this.currentState.createState();
        this.contentPane.add(newScene);

        this.validate();
        this.setVisible(true);
    }

    public void mainLoop() {
        //The game will start out in the Login state.
        this.changeState("Login");

        boolean done = false;
        while (!done) {
            String nextState = this.currentState.run();

            if (nextState.equals("Quit")) {
                done = true;
            }
            else {
                this.changeState(nextState);
            }
        }

        //Clean up stuff and close application here...
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();

        main.mainLoop();
    }
}