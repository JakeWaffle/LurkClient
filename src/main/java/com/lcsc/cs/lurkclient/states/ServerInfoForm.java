package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 2/26/2015.
 */
public class ServerInfoForm implements StateInterface  {
    private static final Logger logger = LoggerFactory.getLogger(ServerInfoForm.class);

    private boolean     endProgram;
    private boolean     finished;

    private State       nextState;
    private Map<String,String> nextStateParams;


    private Messenger messenger;

    public ServerInfoForm() {
        this.endProgram         = false;
        this.finished           = false;
        //This is just the default in case the server couldn't be connected to or something?
        this.nextState          = State.SERVER_INFO_FORM;
        this.nextStateParams    = new HashMap<String, String>();
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, Messenger messenger) {
        this.messenger = messenger;
    }

    public JPanel createState() {
        JPanel panel            = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title    = new JLabel("Server Connection Info");

        Font oldFont    = title.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.NORTH;
        c.gridy         = 0;
        panel.add(title, c);

        JLabel host     = new JLabel("Hostname");

        oldFont         = host.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        host.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 1;
        panel.add(host, c);

        final JTextField hostTextField    = new JTextField(25);

        oldFont         = hostTextField.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        hostTextField.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridwidth     = GridBagConstraints.REMAINDER;
        c.fill          = GridBagConstraints.HORIZONTAL;
        c.gridy         = 2;
        panel.add(hostTextField, c);

        JLabel port     = new JLabel("Port");

        oldFont         = port.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        port.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 3;
        panel.add(port, c);

        final JTextField portTextField    = new JTextField(25);

        oldFont         = portTextField.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        portTextField.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridwidth     = GridBagConstraints.REMAINDER;
        c.fill          = GridBagConstraints.HORIZONTAL;
        c.gridy         = 4;
        panel.add(portTextField, c);

        JButton connectBtn  = new JButton("Connect to Server");

        oldFont         = connectBtn.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        connectBtn.setFont(newFont);

        //This handles the connecting to the server using the entered host and port.
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String host = hostTextField.getText();
                int port;
                try {
                    port = Integer.parseInt(portTextField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "The port must be a number!", "Invalid Server Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (ServerInfoForm.this.messenger.connect(host, port)) {
                    //The server was connected to successfully!
                    ServerInfoForm.this.nextState   = State.LOGIN_FORM;
                    ServerInfoForm.this.finished    = true;
                }
                else {
                    //The host/port must have been invalid, so we'll let the user know.
                    JOptionPane.showMessageDialog(null, "The entered host or port was invalid!", "Invalid Server Info", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        c               = new GridBagConstraints();
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 5;
        panel.add(connectBtn, c);

        return panel;
    }

    public boolean run() {
        logger.debug("Running the ServerInfo state!");

        while (!this.finished) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        this.messenger.clearListeners();

        return this.endProgram;
    }

    public State getNextState() {
        return this.nextState;
    }

    public Map<String,String> getNextStateParams() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public void cleanUp() {
        this.endProgram = true;
        this.finished = true;
    }
}