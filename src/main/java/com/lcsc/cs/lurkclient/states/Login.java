package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Student on 2/24/2015.
 */
public class Login implements StateInterface {
    private boolean finished;
    private String  nextState;

    public Login() {this.finished = false;}

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params) {}

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("Hello World");
        panel.add(label);

        return panel;
    }

    public boolean run() {
        System.out.println("OIASdoaosdnfoiasndofiahnsd");

        while (!this.finished) {
        }
        return true;
    }

    public String getNextState() {
        return this.nextState;
    }

    public Map<String,String> getNextStateParams() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public void cleanUp() {}
}
