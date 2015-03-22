package com.lcsc.cs.lurkclient.game;

import com.lcsc.cs.lurkclient.protocol.*;
import com.lcsc.cs.lurkclient.states.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
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
        //setEnterKeyListener();
        setButtonListeners();
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

                        pattern = Pattern.compile("Player:");
                        matcher = pattern.matcher(response.message);
                        List<String> playerNames = new ArrayList<String>();
                        //We need to match the first "Player: " to get an idea of where the player's name starts.
                        if (matcher.find()) {
                            //The message starts after this header and ends before the next header.
                            int start           = matcher.end();
                            int end             = -1;
                            while (matcher.find()) {
                                end             = matcher.start();
                                playerNames.add(response.message.substring(start+1, end).trim());
                                start           = matcher.end();
                            }
                            playerNames.add(response.message.substring(start+1).trim());
                        }
                        LogicLinker.this._curRoom.updateGlobalPlayers(playerNames);
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
                        RoomInfo roomInfo = new RoomInfo(response.message);

                        LogicLinker.this._eventBox.appendText("Entering " + roomInfo.name + ":\n" + roomInfo.description + "\n");
                        LogicLinker.this._curRoom.updateRoom(roomInfo);
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
                        LogicLinker.this._curRoom.addMonster(new MonsterInfo(response.message));
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
                        PlayerInfo player = new PlayerInfo(response.message);
                        //This will prevent the player's character from showing up in the list of players!
                        if (!player.name.equals(LogicLinker.this._playerStats.name.getText())) {
                            LogicLinker._logger.debug("Player Info: " + response.message);
                            LogicLinker.this._curRoom.addLocalPlayer(player);
                        }
                        else {
                            LogicLinker._logger.debug("User's Character Info: " + response.message);
                        }
                    }
                }
            }
        });

        //This will just update the EventBox with responses from the server.
        _mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses) {
                    if (response.type == ResponseType.NOTIFY || response.type == ResponseType.MESSAGE)
                        LogicLinker.this._eventBox.appendText(response.message+"\n");
                    else if (response.type == ResponseType.REJECTED)
                        LogicLinker.this._eventBox.appendText(response.message+"\n");
                    else if (response.type == ResponseType.RESULT)
                        LogicLinker.this._eventBox.appendText(response.message+"\n");
                }
            }
        });
    }

    private void setButtonListeners() {
        _actionBtns.changeRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRoom = _curRoom.getSelectedRoom();
                if (selectedRoom != null) {
                    Command cmd = new Command(CommandType.ACTION, ActionType.CHANGE_ROOM, selectedRoom);
                    _curRoom.clearLocalBeings();
                    LogicLinker.this._mailMan.sendMessage(cmd);
                }
                else
                    JOptionPane.showMessageDialog(null, "Select a room before clicking the 'Change Room' button!", "Invalid State", JOptionPane.WARNING_MESSAGE);
            }
        });

        _actionBtns.fightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogicLinker.this._mailMan.sendMessage(new Command(CommandType.ACTION, ActionType.FIGHT));
            }
        });

        _actionBtns.privateMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedPlayers = _curRoom.getSelectedGlobalPlayers();
                if (selectedPlayers.size() > 0 && !LogicLinker.this._inputBox.isInputEmpty()) {
                    String userInput = LogicLinker.this._inputBox.getInput();
                    for (String selectedPlayer : selectedPlayers) {
                        String message = String.format("%s (Private)From:%s>%s",
                                selectedPlayer,
                                LogicLinker.this._playerStats.name.getText(),
                                userInput);

                        LogicLinker.this._eventBox.appendText(String.format("(Private)To:%s>%s",
                                selectedPlayer,
                                userInput));

                        LogicLinker.this._mailMan.sendMessage(new Command(CommandType.ACTION, ActionType.MESSAGE, message));
                    }
                    LogicLinker.this._eventBox.appendText("\n");
                }
                else if (LogicLinker.this._inputBox.isInputEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter a message into the input box!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(null, "Select a global player before clicking the 'Message' button!", "Invalid State", JOptionPane.WARNING_MESSAGE);
            }
        });

        _actionBtns.publicMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!LogicLinker.this._inputBox.isInputEmpty()) {
                    List<String> playerNames = _curRoom.getGlobalPlayers();
                    String userInput = LogicLinker.this._inputBox.getInput();

                    LogicLinker.this._eventBox.appendText(String.format("(Public)>%s\n",
                            userInput));

                    for (String playerName : playerNames) {
                        String message = String.format("%s (Public)From:%s>%s",
                                playerName,
                                LogicLinker.this._playerStats.name.getText(),
                                userInput);

                        LogicLinker.this._mailMan.sendMessage(new Command(CommandType.ACTION, ActionType.MESSAGE, message));
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Enter a message into the input box!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        _actionBtns.infoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean nothingSelected = true;

                List<String> monsterNames = LogicLinker.this._curRoom.getSelectedMonsters();
                if (monsterNames.size() > 0) {
                    for (String monsterName : monsterNames) {
                        MonsterInfo monsterInfo = LogicLinker.this._curRoom.getMonsterInfo(monsterName);
                        LogicLinker.this._eventBox.appendText(monsterInfo.info+"\n");
                    }
                    nothingSelected = false;
                }

                List<String> playerNames = LogicLinker.this._curRoom.getSelectedLocalPlayers();
                if (playerNames.size() > 0) {
                    for (String playerName : playerNames) {
                        PlayerInfo playerInfo = LogicLinker.this._curRoom.getLocalPlayerInfo(playerName);
                        LogicLinker.this._eventBox.appendText(playerInfo.info+"\n");
                    }
                    nothingSelected = false;
                }

                if (nothingSelected)
                    JOptionPane.showMessageDialog(null, "Select a local player or a monster!", "Invalid State", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void setEnterKeyListener() {
        _inputBox.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JTextField inputBox = (JTextField) e.getSource();
                    String text = inputBox.getText();
                    _eventBox.appendText(text);
                    inputBox.setText("");
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }
}
