package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.game.*;
import com.lcsc.cs.lurkclient.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/8/2015.
 * This is the main part of the client. This is where the player will be actually playing the game.
 */
public class Game implements StateInterface{
    private static final Logger _logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean             _endProgram;
    private boolean             _finished;
    private Map<String, String> _params;

    private State               _nextState;

    private MailMan             _mailMan;

    private Room                _curRoom;
    private EntityContainer     _rooms;
    private EntityContainer     _monsters;
    private EntityContainer     _localPlayers;
    private EntityContainer     _globalPlayers;
    private PlayerStats         _stats;

    private EventBox            _eventBox;
    private InputBox            _inputBox;
    private ActionButtons       _actionBtns;

    private LogicLinker         _linker;

    public Game() {
        this._endProgram = false;
        this._finished = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, MailMan mailMan) {
        _params = params;
        _mailMan = mailMan;
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

        _rooms          = new EntityContainer("Rooms", 0, 0, ListSelectionModel.SINGLE_SELECTION, listPanel);
        _monsters       = new EntityContainer("Monsters", 0, 2, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, listPanel);
        _localPlayers   = new EntityContainer("Local Players", 0, 4, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, listPanel);
        _globalPlayers  = new EntityContainer("Active Players", 0, 6, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, listPanel);

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


        List<ExtensionInfo> extensionInfo = new ArrayList<ExtensionInfo>();

        if (_params.containsKey("QUERY")) {
            String queryInfo = _params.get("QUERY");

            Pattern pattern = Pattern.compile("(Extension:.*?)(\n\n)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(queryInfo);

            while (matcher.find()) {
                extensionInfo.add(new ExtensionInfo(matcher.group(1).trim()));
            }
        }

        JPanel rightPanel   = new JPanel(new GridBagLayout());

        Extensions extensions = new Extensions(rightPanel, 0, 0, extensionInfo);
        _actionBtns     = new ActionButtons(rightPanel, 0, 2, 1);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridheight        = 3;
        c.insets            = new Insets(0, 5, 0, 10);
        c.gridx             = 2;
        c.gridy             = 1;

        panel.add(rightPanel, c);

        _linker = new LogicLinker(_mailMan, _stats, _curRoom, _eventBox, _inputBox, _actionBtns, extensions);
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
                Thread.sleep(2000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        _mailMan.clearListeners();

        return _endProgram;
    }

    public State getNextState() {
        return _nextState;
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
