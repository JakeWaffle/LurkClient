package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jake on 3/14/2015.
 */
public class ActionButtons {
    public final JButton fightBtn;
    public final JButton changeRoomBtn;
    public final JButton messageBtn;

    public ActionButtons(JPanel mainPanel, int x, int y, int btnPanelHeight) {
        JPanel buttonPanel  = new JPanel(new GridBagLayout());

        fightBtn            = new JButton("Fight");

        Font oldFont        = fightBtn.getFont();
        Font newFont        = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        fightBtn.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets        = new Insets(0, 0, 10, 0);
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

        messageBtn          = new JButton("Send Message");

        oldFont             = messageBtn.getFont();
        newFont             = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        messageBtn.setFont(newFont);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill              = GridBagConstraints.HORIZONTAL;
        c.insets            = new Insets(0, 0, 10, 0);
        c.gridy             = 2;
        buttonPanel.add(messageBtn, c);

        c                   = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridheight        = btnPanelHeight;
        c.insets            = new Insets(0, 5, 0, 10);
        c.gridx             = x;
        c.gridy             = y;
        mainPanel.add(buttonPanel, c);
    }
}
