package com.lcsc.cs.lurkclient.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 3/15/2015.
 */
public class Room {
    private       String            _name;
    private final EntityContainer   _monsters;
    private final EntityContainer   _connections;

    public Room(EntityContainer monsters, EntityContainer rooms) {
        _name           = "<name>";
        _monsters       = monsters;
        _connections    = rooms;
    }

    public void addConnection(String connection) {
        _connections.add(connection);
    }

    public void addMonster(String monster) {
        _monsters.add(monster);
    }

    public void newRoom(RoomInfo newRoom) {
        _name = newRoom.name;
        _monsters.update(newRoom.monsters);
        _connections.update(newRoom.connections);
    }
}
