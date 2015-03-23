package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Student on 2/24/2015.
 * Here you will enter information pertaining to a player's login.
 */
public class LoginForm implements StateInterface {
    private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    private boolean     endProgram;
    private boolean     finished;
    private State       nextState;
    private Map<String, String> nextStateParams;

    private MailMan     mailMan;

    public LoginForm() {
        this.endProgram = false;
        this.finished   = false;
        this.nextStateParams = new HashMap<String, String>();
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, MailMan mailMan) {
        this.mailMan = mailMan;

        this.mailMan.registerListener(new ResponseListener() {
            @Override
            public void notify(List<Response> responses) {
                for (Response response : responses)
                    if (response.type == ResponseType.ACCEPTED || response.type == ResponseType.REJECTED) {
                        LoginForm.this.handleLogin(response);
                    }
                    else if (response.type == ResponseType.QUERY_INFORM) {
                        LoginForm.this.handleQueryResults(response);
                    }

            }
        });
    }

    public JPanel createState() {
        JPanel panel            = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title    = new JLabel("Login");

        Font oldFont    = title.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.gridy         = 0;
        panel.add(title, c);

        JLabel name     = new JLabel("Player Name");

        oldFont         = name.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        name.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridy         = 1;
        panel.add(name, c);

        final JTextField nameTextField    = new JTextField(25);

        oldFont         = nameTextField.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        nameTextField.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridy         = 2;
        panel.add(nameTextField, c);

        JButton connectBtn  = new JButton("Login as Player");

        oldFont         = connectBtn.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        connectBtn.setFont(newFont);

        //This handles the connecting to the server using the entered host and port.
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String name = nameTextField.getText();
                Command cmd = new Command(CommandType.CONNECT, name);
                LoginForm.this.mailMan.sendMessage(cmd);
            }
        });

        c               = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 5;
        panel.add(connectBtn, c);

        return panel;
    }

    private synchronized void handleLogin(Response response) {
        if (response.type == ResponseType.ACCEPTED || response.type == ResponseType.REJECTED) {
            switch (response.message.toLowerCase()) {
                case "name already taken":
                case "dead without health":
                    //I don't think anything should be done in this case?
                    JOptionPane.showMessageDialog(null, response.getResponse(), "Login Response", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "new player":
                    //Switch to the screen where the user enters in his player's data.
                    this.nextState = State.PLAYER_INFO_FORM;
                    this.finished = true;
                    break;
                case "reprising player":
                    this.mailMan.sendMessage(new Command(CommandType.QUERY));
                    this.nextState = State.GAME;
                    break;
                default:
                    logger.error("Invalid Response for LoginForm:\n" + response.toString());
                    break;
            }
        }
        else {
            logger.debug("Unneeded Response:\n"+response.toString());
        }
    }

    private synchronized void handleQueryResults(Response response) {
        this.nextStateParams.put("QUERY", response.message);
        this.finished = true;
    }

    public boolean run() {
        logger.debug("Running the Login state!");

        while (!this.finished) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        mailMan.clearListeners();

        return this.endProgram;
    }

    public State getNextState() {
        return this.nextState;
    }

    public Map<String,String> getNextStateParams() {
        return this.nextStateParams;
    }

    public void cleanUp() {
        this.endProgram = true;
        this.finished = true;
    }
}
