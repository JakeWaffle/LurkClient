package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jake on 3/14/2015.
 */
public class PlayerStats {
    public final JLabel name;
    public final JLabel health;
    public final JLabel gold;
    public final JLabel atk;
    public final JLabel def;
    public final JLabel reg;
    public final JLabel status;
    public final JLabel location;

    public PlayerStats(JPanel panel, int x, int y) {
        JPanel statPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        addTitle(statPanel, "Player Stats", 0, 0, 2);

        addLabel(statPanel, "Name: ", 0, 1);
        name        = addLabel(statPanel, "<Name>", 1, 1);
        addLabel(statPanel, "Health: ", 0, 2);
        health      = addLabel(statPanel, "<Health>", 1, 2);
        addLabel(statPanel, "Gold: ", 0, 3);
        gold        = addLabel(statPanel, "<Gold>", 1, 3);
        addLabel(statPanel, "Attack: ", 0, 4);
        atk         = addLabel(statPanel, "<Attack>", 1, 4);
        addLabel(statPanel, "Defense: ", 0, 5);
        def         = addLabel(statPanel, "<Defense>", 1, 5);
        addLabel(statPanel, "Regen: ", 0, 6);
        reg         = addLabel(statPanel, "<Regen>", 1, 6);
        addLabel(statPanel, "Status: ", 0, 7);
        status      = addLabel(statPanel, "<Status>", 1, 7);
        addLabel(statPanel, "Location: ", 0, 8);
        location    = addLabel(statPanel, "<Location>", 1, 8);

        c = new GridBagConstraints();
        c.anchor    = GridBagConstraints.WEST;
        c.gridx     = x;
        c.gridy     = y;
        panel.add(statPanel, c);
    }

    private void addTitle(JPanel panel, String title, int x, int y, int gridWidth) {
        JLabel label = new JLabel(title);

        Font oldFont = label.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.BOLD, 25);
        label.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor    = GridBagConstraints.WEST;
        c.gridwidth = gridWidth;
        c.gridx     = x;
        c.gridy     = y;
        panel.add(label, c);
    }

    private JLabel addLabel(JPanel panel, String title, int x, int y) {
        JLabel label = new JLabel(title);

        Font oldFont = label.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.PLAIN, 15);
        label.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor    = GridBagConstraints.WEST;
        c.gridx     = x;
        c.gridy     = y;
        panel.add(label, c);

        return label;
    }
}
