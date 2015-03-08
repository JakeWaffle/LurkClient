package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;
import com.lcsc.cs.lurkclient.tools.DocumentSizeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 3/8/2015.
 */
public class Game implements StateInterface{
    private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean     endProgram;
    private boolean     finished;
    private State       nextState;

    private Messenger messenger;

    public Game() {
        this.endProgram = false;
        this.finished   = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, Messenger messenger) {
        this.messenger = messenger;
    }

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title = new JLabel("The Game!");

        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridy = 0;
        panel.add(title, c);

        return panel;
    }

    public boolean run() {
        logger.debug("Running the Game state!");

        while (!this.finished) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        messenger.clearListeners();

        return this.endProgram;
    }

    public State getNextState() {
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
