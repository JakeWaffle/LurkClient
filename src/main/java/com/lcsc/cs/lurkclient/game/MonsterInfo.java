package com.lcsc.cs.lurkclient.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/18/2015.
 */
public class MonsterInfo {
    private static final Logger _logger = LoggerFactory.getLogger(MonsterInfo.class);

    public final String     info;
    public final String     name;
    public final String     description;
    public final String     health;
    public final String     attack;
    public final String     defense;
    public final String     regen;

    public MonsterInfo(String info) {
        this.info      = info;

        Pattern pattern = Pattern.compile("Name: |Description: |Health: |Gold: |Attack: |Defense: |Regen: |Status: ");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String description  = "<description>";
        String health       = "<health>";
        String attack       = "<attack>";
        String defense      = "<defense>";
        String regen        = "<regen>";

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
                else if (type.equals("Attack: "))
                    attack      = info.substring(start+1, end);
                else if (type.equals("Defense: "))
                    defense     = info.substring(start+1, end);
                else if (type.equals("Regen: "))
                    regen       = info.substring(start+1, end);
                else
                    _logger.warn("Invalid Regex group for MonsterInfo: " + type);

                type            = matcher.group();
                start           = matcher.end();
            }

            if (type.equals("Name: "))
                name        = info.substring(start+1);
            else if (type.equals("Description: "))
                description = info.substring(start+1);
            else if (type.equals("Health: "))
                health      = info.substring(start+1);
            else if (type.equals("Attack: "))
                attack      = info.substring(start+1);
            else if (type.equals("Defense: "))
                defense     = info.substring(start+1);
            else if (type.equals("Regen: "))
                regen       = info.substring(start+1);
            else
                _logger.warn("Invalid Regex group for RoomInfo: "+type);
        }
        else
            _logger.warn("The given PlayerInfo string is invalid: "+info);

        this.name           = name.trim();
        this.description    = description.trim();
        this.health         = health.trim();
        this.attack         = attack.trim();
        this.defense        = defense.trim();
        this.regen          = regen.trim();

        if (this.name.equals("<name>") || this.description.equals("<description>")
                || this.health.equals("<health>") || this.attack.equals("<attack>")
                || this.defense.equals("<defense>") || this.regen.equals("<regen>"))
            _logger.warn("The given MonsterInfo string has an invalid parameter: "+info);
    }
}