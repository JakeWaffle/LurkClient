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

        Pattern pattern = Pattern.compile("(Name:|Description:|Health:|Gold:|Attack:|Defense:|Regen:|Status:)(.*?)(\n|$)");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String description  = "<description>";
        String health       = "<health>";
        String attack       = "<attack>";
        String defense      = "<defense>";
        String regen        = "<regen>";

        while (matcher.find()) {
            String type = matcher.group(1);
            if (type.equals("Name:"))
                name = matcher.group(2);
            else if (type.equals("Description:"))
                description = matcher.group(2);
            else if (type.equals("Health:"))
                health = matcher.group(2);
            else if (type.equals("Attack:"))
                attack = matcher.group(2);
            else if (type.equals("Defense:"))
                defense = matcher.group(2);
            else if (type.equals("Regen:"))
                regen = matcher.group(2);
            else
                _logger.warn("Invalid Regex group for MonsterInfo: " + type);
        }

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