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
        Pattern pattern = Pattern.compile("Name:|Description:|Connection:|Monster:");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String description  = "<description>";

        if (matcher.find()) {
            //The message starts after this header and ends before the next header.
            int start           = matcher.end();
            int end             = -1;
            String type         = matcher.group();
            while (matcher.find()) {
                end             = matcher.start();

                if (type.equals("Name:"))
                    name        = info.substring(start, end);
                else if (type.equals("Description:"))
                    description = info.substring(start, end);
                else if (type.equals("Connection:"))
                    connections.add(info.substring(start, end).trim());
                else if (type.equals("Monster:"))
                    monsters.add(info.substring(start, end).trim());
                else
                    _logger.warn("Invalid Regex group for RoomInfo: " + type);

                type            = matcher.group();
                start           = matcher.end();
            }

            if (type.equals("Name: "))
                name        = info.substring(start);
            else if (type.equals("Description:"))
                description = info.substring(start);
            else if (type.equals("Connection:"))
                connections.add(info.substring(start).trim());
            else if (type.equals("Monster:"))
                monsters.add(info.substring(start).trim());
            else
                _logger.warn("Invalid Regex group for RoomInfo: "+type);
        }
        else
            _logger.warn("The given RoomInfo string is invalid: "+info);

        this.name           = name.trim();
        this.description    = description.trim();

        if (this.name.equals("<name>") || this.description.equals("<description>"))
            _logger.warn("The given RoomInfo string has no name or description: "+info);
        else if (connections.size() == 0)
            _logger.warn("The given RoomInfo string has no room connections: "+info);
    }
}
