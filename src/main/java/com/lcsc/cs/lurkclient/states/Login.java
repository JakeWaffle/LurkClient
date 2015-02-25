package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Created by Student on 2/24/2015.
 */
public class Login implements StateInterface {
    private boolean finished;

    public Login() {this.finished = false;}

    public JPanel createState() {
        JPanel panel = new JPanel();

        JLabel label = new JLabel("Hello World");
        panel.add(label);

        return panel;
    }

    public String run() {
        System.out.println("OIASdoaosdnfoiasndofiahnsd");
        while (!this.finished) {
            this.finished = true;
        }
        return "Quit";
    }
}
