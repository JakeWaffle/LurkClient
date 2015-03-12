package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jake on 3/11/2015.
 */
public class InputBox {
    private JTextField inputBox;

    public InputBox() {}

    public void addInputBox(int x, int y, int width, JPanel panel) {
        this.inputBox    = new JTextField(width);

        Font oldFont         = this.inputBox.getFont();
        Font newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        this.inputBox.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.gridx   = x;
        c.gridy   = y;
        panel.add(this.inputBox, c);
    }
}
