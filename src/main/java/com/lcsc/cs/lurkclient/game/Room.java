package com.lcsc.cs.lurkclient.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 3/15/2015.
 */
public class Room {
    private       RoomInfo          _roomInfo = null;
    private final EntityContainer   _monsters;
    private final EntityContainer   _connections;
    private final EntityContainer   _players;

    public Room(EntityContainer monsters, EntityContainer rooms, EntityContainer players) {
        _monsters       = monsters;
        _connections    = rooms;
        _players        = players;
    }

    public String getSelectedRoom() {
        return _connections.getSelectedElement();
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

    public void addMonster(MonsterInfo monster) {
        _monsters.add(monster.name);
    }

    public void addPlayer(PlayerInfo player) {
        _players.add(player.name);
    }

    /**
     * Note that this method is called whenever a room info message is sent from the server. It might mean
     * that we have gone to another room or it could mean that a fight just occurred and stuff is being updated.
     * @param room
     */
    public void updateRoom(RoomInfo room) {
        _roomInfo = room;
        //_monsters.update(newRoom.monsters);
        _connections.update(room.connections);
    }
}
