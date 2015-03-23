package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Jake on 3/14/2015.
 */
public class ActionButtons {
    public final JButton fightBtn;
    public final JButton changeRoomBtn;
    public final JButton privateMessageBtn;
    public final JButton publicMessageBtn;
    public final JButton infoBtn;
    public final JButton extBtn;

    public ActionButtons(JPanel mainPanel, int x, int y, int btnPanelHeight) {
        JPanel buttonPanel  = new JPanel(new GridBagLayout());

        fightBtn            = new JButton("Fight");

        Font oldFont        = fightBtn.getFont();
        Font newFont        = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        fightBtn.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets        = new Insets(10, 0, 10, 0);
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.gridy             = 0;
        buttonPanel.add(fightBtn, c);

        changeRoomBtn       = new JButton("Change Room");

        oldFont             = changeRoomBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        changeRoomBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 1;
        buttonPanel.add(changeRoomBtn, c);

        privateMessageBtn   = new JButton("Private Message");

        oldFont             = privateMessageBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        privateMessageBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 2;
        buttonPanel.add(privateMessageBtn, c);

        publicMessageBtn    = new JButton("Public Message");

        oldFont             = publicMessageBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        publicMessageBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 3;
        buttonPanel.add(publicMessageBtn, c);

        infoBtn             = new JButton("Info");

        oldFont             = infoBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        infoBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 4;
        buttonPanel.add(infoBtn, c);

        extBtn      = new JButton("Use Extension");

        oldFont             = extBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        extBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 5;
        buttonPanel.add(extBtn, c);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridheight        = btnPanelHeight;
        c.gridx             = x;
        c.gridy             = y;
        mainPanel.add(buttonPanel, c);
    }
}
