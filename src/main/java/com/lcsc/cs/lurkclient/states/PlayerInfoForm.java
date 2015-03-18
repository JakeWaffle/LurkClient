package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;
import com.lcsc.cs.lurkclient.tools.DocumentSizeFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/7/2015.
 * Here the player will be filling out information for his/her character.
 */
public class PlayerInfoForm implements StateInterface {
    private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean     endProgram;
    private boolean     finished;
    private State       nextState;

    private MailMan     mailMan;

    private JTextArea   gameDescrText = null;

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
    public void init(Map<String,String> params, MailMan mailMan) {
        this.mailMan = mailMan;

        this.mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.QUERY_INFORM) {
                        Pattern pattern = Pattern.compile("(GameDescription:)(.*?)((?<!NiceName:)Name:|Extension:)", Pattern.DOTALL);
                        Matcher matcher = pattern.matcher(response.message);

                        if (matcher.find())
                            if (PlayerInfoForm.this.gameDescrText != null)
                                PlayerInfoForm.this.gameDescrText.setText(matcher.group(2).trim());
                            else
                                logger.error("The game description text is null for some reason?");
                        else
                            logger.error("The game description was unable to be found!");
                    }
                }
            }
        });

        this.mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response: responses) {
                    if (response.type == ResponseType.ACCEPTED || response.type == ResponseType.REJECTED) {
                        PlayerInfoForm.this.handleStats(response);
                    }
                }
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

        JPanel gameDescrPanel   = new JPanel(new GridBagLayout());

        JLabel gameDescr = new JLabel("Game Description");

        oldFont         = gameDescr.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        gameDescr.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.gridy         = 0;
        gameDescrPanel.add(gameDescr, c);

        gameDescrText = new JTextArea(5, 70);
        gameDescrText.setMinimumSize(gameDescrText.getPreferredSize());

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        gameDescrText.setDocument(doc);
        gameDescrText.setLineWrap(true);
        gameDescrText.setEditable(false);

        oldFont         = gameDescrText.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        gameDescrText.setFont(newFont);

        c               = new GridBagConstraints();
        c.fill          = GridBagConstraints.BOTH;
        c.weightx = c.weighty = 1.0;
        c.gridy         = 1;
        gameDescrPanel.add(new JScrollPane(gameDescrText), c);


        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill          = GridBagConstraints.BOTH;
        c.gridy         = 1;
        c.insets        = new Insets(0, 10, 5, 10);
        panel.add(gameDescrPanel, c);

        JPanel descrPanel   = new JPanel(new GridBagLayout());

        JLabel description = new JLabel("Description");

        oldFont         = description.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        description.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.gridy         = 0;
        descrPanel.add(description, c);

        final JTextArea descriptionText = new JTextArea(5, 70);
        descriptionText.setMinimumSize(descriptionText.getPreferredSize());

        doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        descriptionText.setDocument(doc);
        descriptionText.setLineWrap(true);

        oldFont         = descriptionText.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        descriptionText.setFont(newFont);

        c               = new GridBagConstraints();
        c.fill          = GridBagConstraints.BOTH;
        c.weightx = c.weighty = 1.0;
        c.gridy         = 1;
        descrPanel.add(new JScrollPane(descriptionText), c);

        c               = new GridBagConstraints();
        c.gridy         = 2;
        c.weightx = c.weighty = 1.0;
        c.insets        = new Insets(0, 10, 0, 10);
        c.fill          = GridBagConstraints.BOTH;
        panel.add(descrPanel, c);


        JLabel statLabel= new JLabel("Player Stat Points");

        oldFont         = statLabel.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.BOLD, 26);
        statLabel.setFont(newFont);

        c               = new GridBagConstraints();
        c.insets        = new Insets(20, 0, 5, 0);
        c.gridy         = 3;
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
        attackStat.setMinimumSize(attackStat.getPreferredSize());

        oldFont         = attackStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        attackStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
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
        defenseStat.setMinimumSize(defenseStat.getPreferredSize());

        oldFont         = defenseStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        defenseStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.weightx = c.weighty = 1.0;
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
        regenStat.setMinimumSize(regenStat.getPreferredSize());

        oldFont         = regenStat.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        regenStat.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridx         = 1;
        c.gridy         = 2;
        statPanel.add(regenStat, c);

        c               = new GridBagConstraints();
        c.gridy         = 4;

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
                PlayerInfoForm.this.mailMan.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_ATTACK_STAT, attack);
                PlayerInfoForm.this.mailMan.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_DEFENSE_STAT, defense);
                PlayerInfoForm.this.mailMan.sendMessage(cmd);

                cmd             = new Command(CommandType.SET_REGEN_STAT, regen);
                PlayerInfoForm.this.mailMan.sendMessage(cmd);
            }
        });

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridx         = 0;
        c.gridy         = 5;
        panel.add(connectBtn, c);

        //This will tell the server to send us the game description!
        this.mailMan.sendMessage(new Command(CommandType.QUERY));

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
