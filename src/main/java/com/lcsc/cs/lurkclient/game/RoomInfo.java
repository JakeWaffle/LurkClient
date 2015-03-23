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
public class RoomInfo {
    private static final Logger _logger = LoggerFactory.getLogger(RoomInfo.class);

    public final String         name;
    public final String         description;
    public final List<String>   connections = new ArrayList<String>();
    public final List<String>   monsters    = new ArrayList<String>();

    public RoomInfo(String info) {
        Pattern pattern = Pattern.compile("(Name:|Description:|Connection:|Monster:)(.*?)(\n|$)");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String description  = "<description>";


        while (matcher.find()) {
            String type = matcher.group(1);
            if (type.equals("Name:"))
                name = matcher.group(2);
            else if (type.equals("Description:"))
                description = matcher.group(2);
            else if (type.equals("Connection:"))
                connections.add(matcher.group(2).trim());
            else if (type.equals("Monster:"))
                monsters.add(matcher.group(2).trim());
            else
                _logger.warn("Invalid Regex group for RoomInfo: " + type);
        }

        this.name           = name.trim();
        this.description    = description.trim();

        if (this.name.equals("<name>") || this.description.equals("<description>"))
            _logger.warn("The given RoomInfo string has no name or description: "+info);
        else if (connections.size() == 0)
            _logger.warn("The given RoomInfo string has no room connections: "+info);
    }
}
