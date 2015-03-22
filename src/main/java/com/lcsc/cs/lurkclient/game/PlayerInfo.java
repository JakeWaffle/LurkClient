package com.lcsc.cs.lurkclient.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/17/2015.
 */
public class PlayerInfo {
    private static final Logger _logger = LoggerFactory.getLogger(PlayerInfo.class);

    public final String     info;
    public final String     name;
    public final String     description;
    public final String     health;
    public final String     gold;
    public final String     attack;
    public final String     defense;
    public final String     regen;
    public final String     status;

    public PlayerInfo(String info) {
        this.info       = info;

        Pattern pattern = Pattern.compile("Name: |Description: |Health: |Gold: |Attack: |Defense: |Regen: |Status: ");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String description  = "<description>";
        String health       = "<health>";
        String gold         = "<gold>";
        String attack       = "<attack>";
        String defense      = "<defense>";
        String regen        = "<regen>";
        String status       = "<status>";

        if (matcher.find()) {
            //The message starts after this header and ends before the next header.
            int start           = matcher.end();
            int end             = -1;
            String type         = matcher.group();
            while (matcher.find()) {
                end             = matcher.start();

                if (type.equals("Name: "))
                    name        = info.substring(start+1, end);
                else if (type.equals("Description: "))
                    description = info.substring(start+1, end);
                else if (type.equals("Health: "))
                    health      = info.substring(start+1, end);
                else if (type.equals("Gold: "))
                    gold      = info.substring(start+1, end);
                else if (type.equals("Attack: "))
                    attack      = info.substring(start+1, end);
                else if (type.equals("Defense: "))
                    defense     = info.substring(start+1, end);
                else if (type.equals("Regen: "))
                    regen       = info.substring(start+1, end);
                else if (type.equals("Status: "))
                    status      = info.substring(start+1, end);
                else
                    _logger.warn("Invalid Regex group for PlayerInfo: " + type);

                type            = matcher.group();
                start           = matcher.end();
            }

            if (type.equals("Name: "))
                name        = info.substring(start+1);
            else if (type.equals("Description: "))
                description = info.substring(start+1);
            else if (type.equals("Health: "))
                health      = info.substring(start+1);
            else if (type.equals("Gold: "))
                gold      = info.substring(start+1);
            else if (type.equals("Attack: "))
                attack      = info.substring(start+1);
            else if (type.equals("Defense: "))
                defense     = info.substring(start+1);
            else if (type.equals("Regen: "))
                regen       = info.substring(start+1);
            else if (type.equals("Status: "))
                status      = info.substring(start+1);
            else
                _logger.warn("Invalid Regex group for RoomInfo: "+type);
        }
        else
            _logger.warn("The given PlayerInfo string is invalid: "+info);

        this.name           = name.trim();
        this.description    = description.trim();
        this.health         = health.trim();
        this.gold           = gold.trim();
        this.attack         = attack.trim();
        this.defense        = defense.trim();
        this.regen          = regen.trim();
        this.status         = status.trim();

        if (this.name.equals("<name>") || this.description.equals("<description>")
                || this.health.equals("<health>") || this.attack.equals("<attack>")
                || this.defense.equals("<defense>") || this.regen.equals("<regen>")
                || this.status.equals("<status>") || this.status.equals("<gold>"))
            _logger.warn("The given PlayerInfo string has an invalid parameter: "+info);
    }
}