package com.lcsc.cs.lurkclient.game;

import com.lcsc.cs.lurkclient.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
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
    private final Extensions      _extensions;
    private final ActionButtons   _actionBtns;

    public LogicLinker(MailMan mailMan,
                       PlayerStats playerStats,
                       Room curRoom,
                       EventBox eventBox,
                       InputBox inputBox,
                       ActionButtons actionBtns,
                       Extensions extensions) {
        _mailMan        = mailMan;
        _playerStats    = playerStats;
        _curRoom        = curRoom;
        _eventBox       = eventBox;
        _inputBox       = inputBox;
        _actionBtns     = actionBtns;
        _extensions     = extensions;
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
                        LogicLinker.this.processQueryInfo(response);
                    }
                    else if (response.type == ResponseType.NOTIFY || response.type == ResponseType.RESULT) {
                        LogicLinker.this._eventBox.appendText("\n" + response.message);
                    }
                    else if (response.type == ResponseType.REJECTED) {
                        LogicLinker.this._eventBox.appendText("\nRejected: " + response.message);
                    }
                    else if (response.type == ResponseType.MESSAGE) {
                        LogicLinker.this._eventBox.appendText(response.message);
                    }
                    else if (response.type == ResponseType.ROOM_INFORM) {
                        LogicLinker.this.processRoomInfo(response);
                    }
                    else if (response.type == ResponseType.MONSTER_INFORM) {
                        LogicLinker.this.processMonsterInfo(response);
                    }
                    else if (response.type == ResponseType.PLAYER_INFORM) {
                        LogicLinker.this.processPlayerInfo(response);
                    }
                }
            }
        });
    }

    private synchronized void processQueryInfo(Response response) {
        Pattern pattern = Pattern.compile(".*(?<!NiceName:).*(Name:)(.*?)(Description:)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.name.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Gold:)(.*?)(Attack:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.gold.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Attack:)(.*?)(Defense:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.atk.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Defense:)(.*?)(Regen:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.def.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Regen:)(.*?)(Status)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.reg.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Status:)(.*?)(Location:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.status.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Location:)(.*?)(Health:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.location.setText(matcher.group(2).trim());

        pattern = Pattern.compile("(Health:)(.*?)(Started:)", Pattern.DOTALL);
        matcher = pattern.matcher(response.message);
        if (matcher.find())
            _playerStats.health.setText(matcher.group(2).trim());

        pattern = Pattern.compile("Player:(.*?)(\n|$)");
        matcher = pattern.matcher(response.message);
        List<String> playerNames = new ArrayList<String>();
        //We need to match the first "Player: " to get an idea of where the player's name starts.
        while (matcher.find()) {
            playerNames.add(matcher.group(1).trim());
        }
        _curRoom.updateGlobalPlayers(playerNames);
    }

    private synchronized void processRoomInfo(Response response) {
        LogicLinker._logger.debug("Room Info: " + response.message);
        RoomInfo roomInfo = new RoomInfo(response.message);

        if (!_playerStats.location.getText().equals(roomInfo.name)) {
            LogicLinker.this._eventBox.appendText("\nEntering " + roomInfo.name + ":\n" + roomInfo.description);
            LogicLinker.this._curRoom.updateRoom(roomInfo);
        }
    }

    private synchronized void processMonsterInfo(Response response) {
        LogicLinker._logger.debug("Monster Info: " + response.message);
        LogicLinker.this._curRoom.addMonster(new MonsterInfo(response.message));
    }

    private synchronized void processPlayerInfo(Response response) {
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

                    LogicLinker.this._eventBox.appendText(String.format("(Public)>%s",
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
                        LogicLinker.this._eventBox.appendText("\n"+monsterInfo.info);
                    }
                    nothingSelected = false;
                }

                List<String> playerNames = LogicLinker.this._curRoom.getSelectedLocalPlayers();
                if (playerNames.size() > 0) {
                    for (String playerName : playerNames) {
                        PlayerInfo playerInfo = LogicLinker.this._curRoom.getLocalPlayerInfo(playerName);
                        LogicLinker.this._eventBox.appendText("\n"+playerInfo.info);
                    }
                    nothingSelected = false;
                }

                String extensionNiceName = _extensions.getSelectedExtension();
                if (extensionNiceName != null) {
                    ExtensionInfo info = _extensions.getExtensionInfo(extensionNiceName);
                    LogicLinker.this._eventBox.appendText("\n"+info.info);
                    nothingSelected = false;
                }

                if (nothingSelected)
                    JOptionPane.showMessageDialog(null, "Select a local player or a monster!", "Invalid State", JOptionPane.WARNING_MESSAGE);
            }
        });

        _actionBtns.extBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String extensionNiceName = _extensions.getSelectedExtension();
                if (extensionNiceName != null) {
                    ExtensionInfo info = _extensions.getExtensionInfo(extensionNiceName);

                    //These are all unique parameter names that mean that the parameter can be selected from
                    //one of the EntityContainers!
                    if (info.parameter.equals("Being")) {
                        String beingName                = null;
                        List<String> localPlayerNames   = LogicLinker.this._curRoom.getSelectedLocalPlayers();
                        List<String> globalPlayerNames  = _curRoom.getSelectedGlobalPlayers();
                        List<String> monsterNames       = LogicLinker.this._curRoom.getSelectedMonsters();

                        if (localPlayerNames.size() == 1 && globalPlayerNames.size() == 0 && monsterNames.size() == 0) {
                            beingName = localPlayerNames.get(0);
                        }
                        else if (globalPlayerNames.size() == 1 && localPlayerNames.size() == 0 && monsterNames.size() == 0) {
                            beingName = globalPlayerNames.get(0);
                        }
                        else if (monsterNames.size() == 1 && localPlayerNames.size() == 0 && globalPlayerNames.size() == 0) {
                            beingName = monsterNames.get(0);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Select one monster, local player or active player!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (beingName != null) {
                            _eventBox.appendText("\nExtension "+info.niceName+" has been used!");
                            _mailMan.sendMessage(new Command(info.name, beingName));
                        }
                    }
                    else if (info.parameter.equals("Player Name")) {
                        String playerName               = null;
                        List<String> localPlayerNames   = LogicLinker.this._curRoom.getSelectedLocalPlayers();
                        List<String> globalPlayerNames  = _curRoom.getSelectedGlobalPlayers();

                        if (localPlayerNames.size() == 1 && globalPlayerNames.size() == 0) {
                            playerName = localPlayerNames.get(0);
                        }
                        else if (globalPlayerNames.size() == 1&& localPlayerNames.size() == 0) {
                            playerName = globalPlayerNames.get(0);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Select one local or active player!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (playerName != null) {
                            _eventBox.appendText("\nExtension "+info.niceName+" has been used!");
                            _mailMan.sendMessage(new Command(info.name, playerName));
                        }
                    }
                    else if (info.parameter.equals("Monster Name")) {
                        List<String> monsterNames = LogicLinker.this._curRoom.getSelectedMonsters();
                        if (monsterNames.size() == 1) {
                            _eventBox.appendText("\nExtension "+info.niceName+" has been used!");
                            _mailMan.sendMessage(new Command(info.name, monsterNames.get(0)));
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Select a monster!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                    }
                    else if (info.parameter.equals("Room Name")) {
                        String selectedRoom = _curRoom.getSelectedRoom();
                        if (selectedRoom != null) {
                            _eventBox.appendText("\nExtension "+info.niceName+" has been used!");
                            _mailMan.sendMessage(new Command(info.name, selectedRoom));
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Select a room!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                    }
                    //If a parameter can't be selected from an EntityContainer it must be entered into the input box.
                    else {
                        String inputParam = LogicLinker.this._inputBox.getInput();

                        //This will make the input parameter excluded from the command.
                        if (inputParam.length() == 0 && !info.parameter.equals("<parameter>"))
                            JOptionPane.showMessageDialog(null, "Enter a valid "+info.parameter+" into the input box!", "Invalid State", JOptionPane.WARNING_MESSAGE);
                        else if (inputParam.length() == 0 && info.parameter.equals("<parameter>"))
                            inputParam = null;

                        _eventBox.appendText("\nExtension "+info.niceName+" has been used!");
                        _mailMan.sendMessage(new Command(info.name, inputParam));
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "Select an extension!", "Invalid State", JOptionPane.WARNING_MESSAGE);
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
