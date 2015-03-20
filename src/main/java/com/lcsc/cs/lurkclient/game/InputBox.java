package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Created by Jake on 3/11/2015.
 */
public class InputBox {
    private final JTextField _inputBox;

    public InputBox(int x, int y, int width, JPanel panel) {
        _inputBox = new JTextField(width);
        _inputBox.setMinimumSize(_inputBox.getPreferredSize());

        Font oldFont         = this._inputBox.getFont();
        Font newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        this._inputBox.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        //c.weightx = c.weighty = 1.0;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.gridx   = x;
        c.gridy   = y;
        panel.add(this._inputBox, c);
    }

    public String getInput() {
        String input = _inputBox.getText();
        _inputBox.setText("");
        return input;
    }

    public void addKeyListener(KeyListener listener) {
        _inputBox.addKeyListener(listener);
    }
}
