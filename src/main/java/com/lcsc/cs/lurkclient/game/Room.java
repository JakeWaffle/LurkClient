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
    private final EntityContainer   _players;

    public Room(EntityContainer monsters, EntityContainer rooms, EntityContainer players) {
        _name           = "<name>";
        _monsters       = monsters;
        _connections    = rooms;
        _players        = players;
    }

    public void addConnection(String connection) {
        _connections.add(connection);
    }

    public void addMonster(String monster) {
        _monsters.add(monster);
    }

    /**
     * This method is meant to clear the room's monsters and players in case one of them has left.
     * After this is called the server will send back a message that displays which players/monsters are still
     * in the room.
     * TODO Later this class should put the players and monsters into a list saying they may or may not still exist.
     * Then they'll be taken care of accordingly after the next batch of player informs has been received.
     */
    public void clear() {
        _monsters.clear();
        _players.clear();
    }

    public void addPlayer(PlayerInfo player) {
        _players.add(player.name);
    }

    public void newRoom(RoomInfo newRoom) {
        _name = newRoom.name;
        _monsters.update(newRoom.monsters);
        _connections.update(newRoom.connections);
    }
}
