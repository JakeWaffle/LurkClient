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

        // Create some items to add to the list
        DefaultListModel monsters = new DefaultListModel();
        monsters.addElement("Monster1");
        monsters.addElement("Monster2");
        JList monsterList = new JList(monsters);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        listPanel.add(monsterList, c);

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

        // Create some items to add to the list
        DefaultListModel rooms = new DefaultListModel();
        rooms.addElement("Room1");
        rooms.addElement("Room2");
        JList roomList = new JList(rooms);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        listPanel.add(roomList, c);

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

        // Create some items to add to the list
        DefaultListModel players = new DefaultListModel();
        players.addElement("Player1");
        players.addElement("Player2");
        JList playerList = new JList(players);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 5;
        listPanel.add(playerList, c);

        c               = new GridBagConstraints();
        c.gridheight    = 3;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(listPanel, c);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JTextArea descriptionText = new JTextArea(5, 1);

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        descriptionText.setDocument(doc);
        descriptionText.setLineWrap(true);

        oldFont         = descriptionText.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        descriptionText.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill          = GridBagConstraints.BOTH;
        c.gridx         = 0;
        c.gridy         = 0;
        mainPanel.add(new JScrollPane(descriptionText), c);

        JTextField inputBox    = new JTextField(32);

        oldFont         = inputBox.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        inputBox.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill          = GridBagConstraints.HORIZONTAL;
        c.gridy         = 1;
        mainPanel.add(inputBox, c);

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
