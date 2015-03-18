package com.lcsc.cs.lurkclient.game;

import com.lcsc.cs.lurkclient.protocol.MailMan;
import com.lcsc.cs.lurkclient.protocol.Response;
import com.lcsc.cs.lurkclient.protocol.ResponseListener;
import com.lcsc.cs.lurkclient.protocol.ResponseType;
import com.lcsc.cs.lurkclient.states.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Student on 3/12/2015.
 * This class has access to information about the various elements of the gui and the
 * information about the game. Using this information this class links up data with the gui and
 * also server responses to the data/gui.
 */
public class LogicLinker {
    private static final Logger   _logger      = LoggerFactory.getLogger(LogicLinker.class);

    private final MailMan         _mailMan;
    private final PlayerStats     _playerStats;
    private final Room            _curRoom;
    private final EventBox        _eventBox;
    private final InputBox        _inputBox;
    private final ActionButtons   _actionBtns;

    public LogicLinker(MailMan mailMan,
                       PlayerStats playerStats,
                       Room curRoom,
                       EventBox eventBox,
                       InputBox inputBox,
                       ActionButtons actionBtns) {
        _mailMan        = mailMan;
        _playerStats    = playerStats;
        _curRoom        = curRoom;
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
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.QUERY_INFORM) {
                        Pattern pattern = Pattern.compile(".*(?<!NiceName:).*(Name:)(.*)(Description:)", Pattern.DOTALL);
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
                    }
                }
            }
        });

        //This will update the information about the room connections.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.ROOM_INFORM) {
                        LogicLinker._logger.debug("Room Info: " + response.message);
                        LogicLinker.this._curRoom.newRoom(new RoomInfo(response.message));
                    }
                }
            }
        });

        //This will update the information about the monsters in the room.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.MONSTER_INFORM) {
                        LogicLinker._logger.debug("Monster Info: " + response.message);
                        //LogicLinker.this._curRoom.addMonster(asdfasdf);
                    }
                }
            }
        });

        //This will update the information about the players in the room.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.PLAYER_INFORM) {
                        LogicLinker._logger.debug("Player Info: " + response.message);
                        LogicLinker.this._curRoom.addPlayer(new PlayerInfo(response.message));
                    }
                }
            }
        });

        //This will just update the EventBox with notify messages from the server.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.NOTIFY)
                        LogicLinker.this._eventBox.appendText(response.message);
                }
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
