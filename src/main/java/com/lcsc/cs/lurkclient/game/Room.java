package com.lcsc.cs.lurkclient.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jake on 3/15/2015.
 */
public class Room {
    private       RoomInfo          _roomInfo = null;
    private final EntityContainer   _monsters;
    private final EntityContainer   _connections;
    private final EntityContainer   _localPlayers;
    private final EntityContainer   _globalPlayers;


    //These map the names to the being's info.
    //Names can be found in the EntityContainers.
    private final Map<String, MonsterInfo> _monstersInfo;
    private final Map<String, PlayerInfo>  _localPlayersInfo;


    public Room(EntityContainer monsters, EntityContainer rooms, EntityContainer localPlayers, EntityContainer globalPlayers) {
        _monsters       = monsters;
        _connections    = rooms;
        _localPlayers   = localPlayers;
        _globalPlayers  = globalPlayers;

        _monstersInfo       = new HashMap<String, MonsterInfo>();
        _localPlayersInfo   = new HashMap<String, PlayerInfo>();
    }

    /**
     * This will get the room connection that is selected currently.
     * @return A string representing a room connection's name. If no room is selected this will be null.
     */
    public String getSelectedRoom() {
        return _connections.getSelectedElement();
    }

    /**
     * This will get a local monster that is that is selected currently.
     * @return A string representing a player's name. If no player is selected this will be null.
     */
    public List<String> getSelectedMonsters() {
        return _monsters.getSelectedElements();
    }

    /**
     * This will get a local monster's MonsterInfo.
     * @param monsterName The name of the monster we want info for.
     * @return A MonsterInfo object for the given monsterName. If no monster is selected this will be null.
     */
    public MonsterInfo getMonsterInfo(String monsterName) {
        return _monstersInfo.get(monsterName);
    }

    /**
     * This will get a local player that is that is selected currently.
     * @return A string representing a player's name. If no player is selected this will be null.
     *         If no player is selected this will be null.
     */
    public List<String> getSelectedLocalPlayers() {
        return _localPlayers.getSelectedElements();
    }

    /**
     * This will get a local player's PlayerInfo.
     * @param playerName The name of the player we want info for.
     * @return A PlayerInfo object for the given playerName.
     */
    public PlayerInfo getLocalPlayerInfo(String playerName) {
        return _localPlayersInfo.get(playerName);
    }

    /**
     * This will get an active player that is that is selected currently.
     * @return A string representing a player's name. If no player is selected this will be null.
     */
    public List<String> getSelectedGlobalPlayers() {
        return _globalPlayers.getSelectedElements();
    }


     public List<String> getGlobalPlayers() {
        return _globalPlayers.getAllElements();
     }

     /**
     * This method is meant to clear the room's monsters and players in case one of them has left.
     * After this is called the server will send back a message that displays which players/monsters are still
     * in the room.
     */
    public void clearLocalBeings() {
        _monsters.clear();
        _monstersInfo.clear();
        _localPlayers.clear();
        _localPlayersInfo.clear();
    }

    public void addMonster(MonsterInfo monster) {
        _monsters.add(monster.name);
        _monstersInfo.put(monster.name, monster);
    }

    public void addLocalPlayer(PlayerInfo player) {
        _localPlayers.add(player.name);
        _localPlayersInfo.put(player.name, player);
    }

    public void updateGlobalPlayers(List<String> playerNames) {
        _globalPlayers.update(playerNames);
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

