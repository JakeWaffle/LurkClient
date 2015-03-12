package com.lcsc.cs.lurkclient.game;

import com.lcsc.cs.lurkclient.states.LoginForm;
import com.lcsc.cs.lurkclient.tools.DocumentSizeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;

/**
 * Created by Jake on 3/11/2015.
 */
public class EventBox {
    private static final Logger logger      = LoggerFactory.getLogger(LoginForm.class);
    private static final int    MAX_LINES   = 100;

    private JTextArea   eventText;


    public EventBox() {}

    public void addEventBox(int x, int y, JPanel panel) {
        this.eventText = new JTextArea(30, 1);

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        this.eventText.setDocument(doc);
        this.eventText.setLineWrap(true);
        this.eventText.setEditable(false);

        //This should make it so that the event box automatically scrolls down when text is appended to the end.
        DefaultCaret caret = (DefaultCaret)this.eventText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Font oldFont    = this.eventText.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        this.eventText.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.fill          = GridBagConstraints.BOTH;
        c.gridx         = x;
        c.gridy         = y;
        panel.add(new JScrollPane(this.eventText), c);
    }

    public void appendText(String text) {
        this.eventText.append(text);

        //This will delete old event text when the line count is above some limit!
        if (this.eventText.getLineCount() > MAX_LINES) {
            int linesToDelete = this.eventText.getLineCount() - MAX_LINES;

            try {
                int end = this.eventText.getLineEndOffset(linesToDelete-1);
                this.eventText.replaceRange("", 0, end);
            } catch(BadLocationException e) {
                logger.error("Line End Offset is Invalid!", e);
            }
        }
    }
}
