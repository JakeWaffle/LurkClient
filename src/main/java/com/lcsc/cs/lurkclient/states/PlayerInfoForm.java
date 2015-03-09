package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;
import com.lcsc.cs.lurkclient.tools.DocumentSizeFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 3/7/2015.
 */
public class PlayerInfoForm implements StateInterface {
    private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean     endProgram;
    private boolean     finished;
    private State       nextState;

    private Messenger   messenger;

    //These will allow us to handle the messages sent back from the server.
    //They'll tell us which stat the message was meant for.
    private boolean     statIssue;
    private int         statsSet;

    public PlayerInfoForm() {
        this.endProgram = false;
        this.finished   = false;

        this.statIssue  = false;
        this.statsSet   = 0;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, Messenger messenger) {
        this.messenger = messenger;

        this.messenger.registerListener(new ResponseListener() {
            @Override
            public void notify(Response response) {
                PlayerInfoForm.this.handleStats(response);
            }
        });
    }

    public JPanel createState() {
        JPanel panel            = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title    = new JLabel("Player Information");

        Font oldFont    = title.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridy         = 0;
        panel.add(title, c);

        JPanel descrPanel   = new JPanel(new GridBagLayout());

        JLabel description = new JLabel("Description");

        oldFont         = description.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        description.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.gridy         = 0;
        descrPanel.add(description, c);

        JTextArea descriptionText = new JTextArea(5, 30);

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        descriptionText.setDocument(doc);
        descriptionText.setLineWrap(true);

        oldFont         = descriptionText.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        descriptionText.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridy         = 1;
        descrPanel.add(new JScrollPane(descriptionText), c);


        c               = new GridBagConstraints();
        c.gridy         = 1;

        panel.add(descrPanel, c);


        JLabel statLabel= new JLabel("Player Stat Points");

        oldFont         = statLabel.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.BOLD, 26);
        statLabel.setFont(newFont);

        c               = new GridBagConstraints();
        c.insets        = new Insets(20, 0, 5, 0);
        c.gridy         = 2;
        panel.add(statLabel, c);

        JPanel statPanel = new JPanel(new GridBagLayout());

        JLabel atk      = new JLabel("Attack");

        oldFont         = atk.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 22);
        atk.setFont(newFont);

        c               = new GridBagConstraints();
        c.insets        = new Insets(5, 10, 0, 0);
        c.gridx         = 0;
        c.gridy         = 0;
        statPanel.add(atk, c);

        final JTextField attackStat    = new JTextField(5);

        oldFont         = attackStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        attackStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridx         = 1;
        c.gridy         = 0;
        statPanel.add(attackStat, c);

        JLabel defense  = new JLabel("Defense");

        oldFont         = defense.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 22);
        defense.setFont(newFont);

        c               = new GridBagConstraints();
        c.insets        = new Insets(5, 10, 0, 0);
        c.gridx         = 0;
        c.gridy         = 1;
        statPanel.add(defense, c);

        final JTextField defenseStat    = new JTextField(5);

        oldFont         = defenseStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        defenseStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.gridx         = 1;
        c.gridy         = 1;
        statPanel.add(defenseStat, c);

        JLabel regen    = new JLabel("Regen");

        oldFont         = regen.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 22);
        regen.setFont(newFont);

        c               = new GridBagConstraints();
        c.insets        = new Insets(5, 10, 0, 0);
        c.gridx         = 0;
        c.gridy         = 2;
        statPanel.add(regen, c);

        final JTextField regenStat    = new JTextField(5);

        oldFont         = regenStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        regenStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridx         = 1;
        c.gridy         = 2;
        statPanel.add(regenStat, c);

        c               = new GridBagConstraints();
        c.gridy         = 3;

        panel.add(statPanel, c);

        JButton connectBtn  = new JButton("Submit Player Stats");

        oldFont         = connectBtn.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        connectBtn.setFont(newFont);

        //This handles the connecting to the server using the entered host and port.
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String description = descriptionText.getText();
                String attack   = attackStat.getText();
                try {
                    Integer.parseInt(attack);
                } catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "The attack stat must be a number!", "Invalid Player Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String defense   = defenseStat.getText();
                try {
                    Integer.parseInt(defense);
                } catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "The defense stat must be a number!", "Invalid Player Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String regen   = regenStat.getText();
                try {
                    Integer.parseInt(regen);
                } catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "The regen stat must be a number!", "Invalid Player Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Command cmd     = new Command(CommandType.SET_PLAYER_DESC, description);
                PlayerInfoForm.this.messenger.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_ATTACK_STAT, attack);
                PlayerInfoForm.this.messenger.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_DEFENSE_STAT, defense);
                PlayerInfoForm.this.messenger.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_REGEN_STAT, regen);
                PlayerInfoForm.this.messenger.sendMessage(cmd);
            }
        });

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridx         = 0;
        c.gridy         = 4;
        panel.add(connectBtn, c);

        return panel;
    }

    private synchronized void handleStats(Response response) {
        if (!this.statIssue) {
            switch (response.getResponse()) {
                case "Stats Too High":
                    if (this.statsSet == 1) {
                        JOptionPane.showMessageDialog(null, "Your player's attack stat is too high!", "Stat Response", JOptionPane.WARNING_MESSAGE);
                    } else if (this.statsSet == 2) {
                        JOptionPane.showMessageDialog(null, "Your player's defense stat is too high!", "Stat Response", JOptionPane.WARNING_MESSAGE);
                    } else if (this.statsSet == 3) {
                        JOptionPane.showMessageDialog(null, "Your player's regen stat is too high!", "Stat Response", JOptionPane.WARNING_MESSAGE);
                    }
                    this.statIssue = true;
                    break;
                case "Incorrect State":
                    JOptionPane.showMessageDialog(null, "This is the wrong state for setting stats!", "Stat Response", JOptionPane.WARNING_MESSAGE);
                    this.statIssue = true;
                    break;
                case "Fine":
                    if (this.statsSet == 3) {
                        this.nextState = State.GAME;
                        this.finished = true;
                    }
                    break;
                default:
                    logger.error("Invalid Response for PlayerInfoForm:\n"+response.toString());
                    break;
            }
            this.statsSet += 1;
        }
    }

    public boolean run() {
        logger.debug("Running the PlayerInfo state!");

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
