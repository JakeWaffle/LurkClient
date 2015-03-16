package com.lcsc.cs.lurkclient.game;

import com.lcsc.cs.lurkclient.protocol.MailMan;
import com.lcsc.cs.lurkclient.protocol.Response;
import com.lcsc.cs.lurkclient.protocol.ResponseListener;
import com.lcsc.cs.lurkclient.protocol.ResponseType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Student on 3/12/2015.
 */
public class LogicLinker {
    private final MailMan         _mailMan;
    private final PlayerStats     _playerStats;
    private final Room            _curRoom;
    private final EntityContainer _players;
    private final EventBox        _eventBox;
    private final InputBox        _inputBox;
    private final ActionButtons   _actionBtns;

    public LogicLinker(MailMan mailMan,
                       PlayerStats playerStats,
                       Room curRoom,
                       EntityContainer players,
                       EventBox eventBox,
                       InputBox inputBox,
                       ActionButtons actionBtns) {
        _mailMan        = mailMan;
        _playerStats    = playerStats;
        _curRoom        = curRoom;
        _players        = players;
        _eventBox       = eventBox;
        _inputBox       = inputBox;
        _actionBtns     = actionBtns;
    }

    public void setActionListeners() {
        setServerResponseListener();
        setEnterKeyListener();
    }

    private void setServerResponseListener() {
        //Handles Query commands.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(Response response) {
                if (response.type == ResponseType.QUERY_INFORM) {
                    Pattern pattern = Pattern.compile("(Name:)(.*)(Description:)", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.name.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Gold:)(.*)(Attack:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.gold.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Attack:)(.*)(Defense:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.atk.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Defense:)(.*)(Regen:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.def.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Regen:)(.*)(Status)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.reg.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Status:)(.*)(Location:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.status.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Location:)(.*)(Health:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.location.setText(matcher.group(2).trim());

                    pattern = Pattern.compile("(Health:)(.*)(Started:)", Pattern.DOTALL);
                    matcher = pattern.matcher(response.message);
                    if (matcher.find())
                        LogicLinker.this._playerStats.health.setText(matcher.group(2).trim());

                    //This will search for players in the query and add them to the list of players.
                    pattern = Pattern.compile("Player:");
                    matcher = pattern.matcher(response.message);

                    //We need to match the first "Player: " to get an idea of where the player's name starts.
                    if (matcher.find()) {
                        //The message starts after this header and ends before the next header.
                        int start           = matcher.end();
                        int end             = -1;
                        while (matcher.find()) {
                            end             = matcher.start();
                            _players.add(response.message.substring(start+1, end));
                            start           = matcher.end();
                        }
                        _players.add(response.message.substring(start+1));
                    }
                }
            }
        });

        //This will just update the EventBox with notify messages from the server.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(Response response) {
                if (response.type == ResponseType.NOTIFY)
                    LogicLinker.this._eventBox.appendText(response.message);
            }
        });
    }

    private void setButtonListeners() {

    }

    private void setEnterKeyListener() {
        _inputBox.inputBox.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    JTextField textField = (JTextField) e.getSource();
                    String text = textField.getText();
                    _eventBox.appendText(text);
                    textField.setText("");
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
        });
    }
}
