package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jake on 3/11/2015.
 */
public class EntityContainer {
    private DefaultListModel    rooms;
    private DefaultListModel    players;
    private DefaultListModel    monsters;

    private JList               roomList;
    private JList               playerList;
    private JList               monsterList;

    public EntityContainer() {
        this.rooms      = new DefaultListModel();
        this.rooms.addElement("asdfasdf");
        this.players    = new DefaultListModel();
        this.monsters   = new DefaultListModel();
    }

    public void addRoomList(int x, int y, JPanel panel) {
        this.roomList = new JList(this.rooms);
        this.roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints c    = new GridBagConstraints();
        c.fill                  = GridBagConstraints.BOTH;
        c.gridx = x;
        c.gridy = y;

        panel.add(new JScrollPane(this.roomList), c);
    }

    public void addPlayerList(int x, int y, JPanel panel) {
        this.playerList = new JList(this.players);
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints c    = new GridBagConstraints();
        c.fill                  = GridBagConstraints.BOTH;
        c.gridx = x;
        c.gridy = y;

        panel.add(new JScrollPane(this.playerList), c);
    }

    public void addMonsterList(int x, int y, JPanel panel) {
        this.monsterList = new JList(this.monsters);
        this.monsterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints c    = new GridBagConstraints();
        c.fill                  = GridBagConstraints.BOTH;
        c.gridx = x;
        c.gridy = y;

        panel.add(new JScrollPane(this.monsterList), c);
    }
}
