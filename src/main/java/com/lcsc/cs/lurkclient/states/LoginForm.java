package com.lcsc.cs.lurkclient.states;

import org.apache.log4j.Logger;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Student on 2/24/2015.
 */
public class LoginForm implements StateInterface {
    static Logger logger = Logger.getLogger(LoginForm.class);

    private boolean endProgram;
    private boolean finished;
    private String  nextState;

    public LoginForm() {
        this.endProgram = false;
        this.finished   = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params) {}

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("Hello World");
        panel.add(label);

        return panel;
    }

    public boolean run() {
        logger.debug("Running the Login state!");

        while (!this.finished) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        return this.endProgram;
    }

    public String getNextState() {
        return this.nextState;
    }

    public Map<String,String> getNextStateParams() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public void cleanUp() {
        this.endProgram = true;
        this.finished = true;
    }
}
