package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.game.*;
import com.lcsc.cs.lurkclient.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 3/8/2015.
 * This is the main part of the client. This is where the player will be actually playing the game.
 */
public class Game implements StateInterface{
    private static final Logger _logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean         _endProgram;
    private boolean         _finished;
    private State           nextState;

    private MailMan         _mailMan;

    private Room            _curRoom;
    private EntityContainer _rooms;
    private EntityContainer _monsters;
    private EntityContainer _localPlayers;
    private EntityContainer _globalPlayers;
    private PlayerStats     _stats;

    private EventBox        _eventBox;
    private InputBox        _inputBox;
    private ActionButtons   _actionBtns;

    private LogicLinker     _linker;

    public Game() {
        this._endProgram = false;
        this._finished = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, MailMan mailMan) {
        this._mailMan = mailMan;
    }

    public JPanel createState() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title = new JLabel("The Game!");

        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(title, c);

        JPanel leftPanel = new JPanel(new GridBagLayout());

        _stats = new PlayerStats(leftPanel, 0, 0);

        JPanel listPanel = new JPanel(new GridBagLayout());

        _rooms          = new EntityContainer("Rooms", 0, 0, listPanel);
        _monsters       = new EntityContainer("Monsters", 0, 2, listPanel);
        _localPlayers   = new EntityContainer("Local Players", 0, 4, listPanel);
        _globalPlayers  = new EntityContainer("Active Players", 0, 6, listPanel);

        _curRoom    = new Room(_monsters, _rooms, _localPlayers, _globalPlayers);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets = new Insets(0, 0, 10, 0);
        c.fill  = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        leftPanel.add(listPanel, c);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridheight    = 3;
        c.insets = new Insets(0, 10, 10, 5);
        c.fill  = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(leftPanel, c);


        JPanel mainPanel    = new JPanel(new GridBagLayout());
        _eventBox           = new EventBox(0, 0, mainPanel);
        _inputBox           = new InputBox(0, 1, 50, mainPanel);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets = new Insets(10, 5, 10, 5);
        c.fill  = GridBagConstraints.BOTH;
        c.gridheight    = 3;
        c.gridx         = 1;
        c.gridy         = 1;

        panel.add(mainPanel, c);

        _actionBtns = new ActionButtons(panel, 2, 1, 3);

        _linker = new LogicLinker(_mailMan, _stats, _curRoom, _eventBox, _inputBox, _actionBtns);
        _linker.setActionListeners();

        return panel;
    }

    public void updateGame() {
        _mailMan.sendMessage(new Command(CommandType.QUERY));
    }

    public boolean run() {
        _logger.debug("Running the Game state!");

        _mailMan.sendMessage(new Command(CommandType.START));

        while (!_finished) {
            updateGame();
            try {
                Thread.sleep(5000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        _mailMan.clearListeners();

        return _endProgram;
    }

    public State getNextState() {
        return nextState;
    }

    public Map<String,String> getNextStateParams() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public void cleanUp() {
        _endProgram = true;
        _finished = true;
    }
}
