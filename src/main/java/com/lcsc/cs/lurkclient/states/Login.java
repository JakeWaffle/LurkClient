package com.lcsc.cs.lurkclient.states;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;

/**
 * Created by Student on 2/24/2015.
 */
public class Login implements StateInterface {
    private boolean finished;

    public Login() {this.finished = false;}

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("Hello World");
        panel.add(label);

        return panel;
    }

    public String run() {
        System.out.println("OIASdoaosdnfoiasndofiahnsd");
        while (!this.finished) {}
        return "Quit";
    }
}