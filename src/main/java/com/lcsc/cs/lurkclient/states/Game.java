package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.game.EntityContainer;
import com.lcsc.cs.lurkclient.game.EventBox;
import com.lcsc.cs.lurkclient.game.InputBox;
import com.lcsc.cs.lurkclient.protocol.*;
import com.lcsc.cs.lurkclient.tools.DocumentSizeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 3/8/2015.
 */
public class Game implements StateInterface{
    private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean         endProgram;
    private boolean         finished;
    private State           nextState;

    private MailMan         mailMan;
    private EntityContainer entities;
    private EventBox        eventBox;
    private InputBox        inputBox;

    public Game() {
        this.endProgram = false;
        this.finished   = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, MailMan mailMan) {
        this.mailMan    = mailMan;
        this.entities   = new EntityContainer();
        this.eventBox   = new EventBox();
        this.inputBox   = new InputBox();
    }

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title = new JLabel("The Game!");

        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c = new GridBagConstraints();
        c.weightx = c.weighty = 0.05;
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(title, c);

        JPanel listPanel = new JPanel(new GridBagLayout());

        JLabel monsterLabel = new JLabel("Monsters");

        oldFont = monsterLabel.getFont();
        newFont = new Font(oldFont.getFontName(), Font.PLAIN, 25);
        monsterLabel.setFont(newFont);

        c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        listPanel.add(monsterLabel, c);
        this.entities.addMonsterList(0, 1, listPanel);

        JLabel roomrLabel = new JLabel("Rooms");

        oldFont = roomrLabel.getFont();
        newFont = new Font(oldFont.getFontName(), Font.PLAIN, 25);
        roomrLabel.setFont(newFont);

        c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 2;
        listPanel.add(roomrLabel, c);
        this.entities.addRoomList(0, 3, listPanel);

        JLabel playersLabel = new JLabel("Players");

        oldFont = playersLabel.getFont();
        newFont = new Font(oldFont.getFontName(), Font.PLAIN, 25);
        playersLabel.setFont(newFont);

        c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 4;
        listPanel.add(playersLabel, c);
        this.entities.addPlayerList(0, 5, listPanel);

        c               = new GridBagConstraints();
        c.gridheight    = 3;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(listPanel, c);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        this.eventBox.addEventBox(0, 0, mainPanel);
        this.inputBox.addInputBox(0, 1, 50, mainPanel);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridheight    = 2;
        c.gridx         = 1;
        c.gridy         = 1;

        panel.add(mainPanel, c);

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
        mailMan.clearListeners();

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
