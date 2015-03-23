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
 * This handles the box that informs the user of all the things that are going on. For the most part
 * this will just be displaying notifications from the server. But there will be messages for things within
 * the client.
 */
public class EventBox {
    private static final Logger     logger      = LoggerFactory.getLogger(LoginForm.class);
    private static final int        MAX_LINES   = 150;

    public  final        JTextArea  eventText;


    public EventBox(int x, int y, JPanel panel) {
        eventText = new JTextArea(30, 1);
        eventText.setMinimumSize(eventText.getPreferredSize());

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(1024*1024));
        eventText.setDocument(doc);
        eventText.setLineWrap(true);
        eventText.setEditable(false);

        //This should make it so that the event box automatically scrolls down when text is appended to the end.
        DefaultCaret caret = (DefaultCaret)eventText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Font oldFont    = eventText.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        eventText.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets = new Insets(0, 0, 5, 0);
        c.fill          = GridBagConstraints.BOTH;
        c.gridx         = x;
        c.gridy         = y;
        panel.add(new JScrollPane(eventText), c);

        eventText.append("###################\n");
        eventText.append("Welcome to the Game!\n");
        eventText.append("###################");
    }

    public void appendText(String text) {
        eventText.append("\n" + text);

        //This will delete old event text when the line count is above some limit!
        if (eventText.getLineCount() > MAX_LINES) {
            int linesToDelete = eventText.getLineCount() - MAX_LINES;

            try {
                int end = eventText.getLineEndOffset(linesToDelete - 1);
                eventText.replaceRange("", 0, end);
            } catch (BadLocationException e) {
                logger.error("Line End Offset is Invalid!", e);
            }
        }

        eventText.setCaretPosition(eventText.getDocument().getLength());
    }
}
