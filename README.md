# LurkClient
A gui client written in Java that uses the Lurk protocol! This is a class project with the goal of making a game client
for a server that uses the Lurk protocol. The idea is that this protocol is supposed to be very generic so that this
client can be used for multiple servers.

#Lurk Protocol

All messages begin with a 5-letter header, all in caps, followed by a space. The contents after that point vary depending on the type of message.
Client Messages

##Client Messages

```
CNNCT name(string)
    Connects as a particular player name. The server responds with either "REJEC Name Already Taken", "REJEC Dead Without Health", "ACEPT New Player", or "ACEPT Reprising Player". If accepted as a new player, the server will require attack, defense, regen, and description to be specified. If reprising an existing player, the player may or may not have been started previously. The server will send an INFOM describing the character's current state. Other players will be notified in the case of a reprisal.
ATTCK attack_stat(int)
    Used to specify the attack stat for a new player. This cannot be specified after a player has started playing, but can be re-specified until the player has started the game. This will result in "REJEC Stats Too High", "REJEC Incorrect State", or "ACEPT Fine". The server limit on stats can be determined by sending QUERY
DEFNS defense_stat(int)
    Used to specify the defense stat for a new player. This cannot be specified after a player has started playing, but can be re-specified until the player has started the game. This will result in "REJEC Stats Too High", "REJEC Incorrect State", or "ACEPT Fine". The server limit on stats can be determined by sending QUERY
REGEN regen_stat(int)
    Used to specify the regen stat for a new player. This cannot be specified after a player has started playing, but can be re-specified until the player has started the game. This will result in "REJEC Stats Too High", "REJEC Incorrect State", or "ACEPT Fine". The server limit on stats can be determined by sending QUERY. The usefulness of this stat depends on how the server implements regen, and some servers may ignore it entirely.
DESCR description(string)
    Used to specify the description of a player. This will result in "ACEPT Fine", provided it does not exceed the maximum message length (1024*1024) and a CNNCT has been previously sent and accepted. If no player has been specified, "REJEC Incorrect State" will be sent. The server may truncate long descriptions.
LEAVE
    Used to leave the server. Your character will be marked inactive, and remain started if started. Will result in "ACEPT Fine" if no CNNCT has been issued, "ACEPT Character Not Started" if your character has not been started, and "ACEPT Your lifeless corpse hits the floor" if your character has started.
QUERY
    Will result in an INFOM from the server containing your current status, a description of the game, a list of extensions supported by the server, and a list of active players. Extensions should be supported in the client, but the game should be playable without using them.
ACTON action(string) action_parameters(string)
    Perform actions, each of which has a 5-letter code. For example, to change rooms, send ACTON CHROM room_name. Actions can only be performed after starting (server will replay "REJEC Incorrect State" otherwise). Each action has a variety of possible results.
```

##Actions

```
Actions are specified using ACTON from the list above.
CHROM room_name(string)
    Change rooms. This may result in "REJEC No Connection", "RESLT Collected (int) Gold", "RESLT Enter No Gold". Other players in the room will be notified of your arrival. Either RESLT will be followed by a series of INFOM messages describing the room, monsters in the room, and other players in the room. If a monster initiates a fight, it may result in immediate battle. Alternatively, a monster may choose to initiate a fight later. Any players who have join_battle set will join you in any fight that initiates in the room they are in.
FIGHT
    Initiates a fight in the room you are in. Players with join_battle set will automatically join you. No RESLT will be sent. NOTIF messages will be sent as usual for battles. If "NOTIF Death" is sent, your player will become inactive.
MESSG player_name(string) message(string)
    Sends messages to other players. The specified player must be active to receive messages. Results in "REJEC Cannot Message Player" or "ACEPT Fine".
```

##Server Messages

```
The server may send messages in response to a request from the client (ACEPT, REJEC, RESLT), or at other times (MESSG, NOTIF). The client must always be ready for a MESSG or NOTIF, regardless of if anything has been sent.
ACEPT condition(string)
    Used to acknowledge acceptance of messages. If only an acknowledgment is required, the condition will be "Fine". Otherwise, contents may vary. This message is always sent in response to a message from the client.
REJEC reason(string)
    Indicates rejection of a request from the client. The reason will vary according to the circumstances. This message is always sent in response to a message from the client.
RESLT result(string)
    Indicates the result of an action by the client. This message is always sent in response to a message from the client.
INFOM size(int)
    Indicates information is about to be sent. Immediately after this message, the client should expect "size" bytes of un-headered information. This may include room descriptions, player descriptions, monster descriptions, or information specific to a request the client has made (i.e. in response to a QUERY).
MESSG message(string)
    Used to send messages to the player. This indicates information about other players joining the game, or messages directly from other players.
NOTIF notification(string)
    Used for notifications which the client may not be expecting. This can include being involved in a fight, observing a fight without being involved (if join_battle is 0), etc. In some cases, this may be followed by an INFOM.
```

##Player State

```
The player's state is held as a number of values.
Name (string)
    Name of the player, limited to 40 characters
Description (string)
    Description of the player.
Attack (int)
    Attack stat
Defense (int)
    Defense stat
Regen (int)
    Regen stat
Gold (int)
    Gold collected so far
Active (0/1)
    Whether the player is active or not. If inactive and started, the player will show up as "dead" on the map
Join Battle (0/1)
    Indicates whether the player will join in battles automatically if they occur in his/her room. Setting this to 0 does not prevent any fighting, because players or monsters can initiate a fight against a specific player
Location (room)
    Location of the player. Will be set to 0 if the player is not yet started.
Health (int)
    Current health status of the player. Can be negative if a fight was badly lost. Values lower than 1 indicate the player is dead.
Started
    Indicates whether the player has started (is in the map).
Ready
    Indicates whether the player is ready to start or not. If not ready, START will result in REJEC Incorrect State.
```