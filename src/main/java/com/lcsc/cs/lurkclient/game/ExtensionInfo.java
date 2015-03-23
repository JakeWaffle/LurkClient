package com.lcsc.cs.lurkclient.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 3/22/2015.
 */
public class ExtensionInfo {
    private static final Logger _logger = LoggerFactory.getLogger(MonsterInfo.class);

    public final String     info;
    public final String     name;
    public final String     niceName;
    public final String     type;
    public final String     description;
    public final String     parameter;

    public ExtensionInfo(String info) {
        this.info      = info;

        Pattern pattern = Pattern.compile("(Extension:|NiceName:|Type:|Description:|Parameter:)(.*?)(\n|$)");
        Matcher matcher = pattern.matcher(info);

        String name         = "<name>";
        String niceName     = "<nice name>";
        String type         = "<type>";
        String description  = "<description>";
        String parameter    = "<parameter>";

        while (matcher.find()) {
            String extType = matcher.group(1);
            if (extType.equals("Extension:"))
                name        = matcher.group(2);
            else if (extType.equals("NiceName:"))
                niceName    = matcher.group(2);
            else if (extType.equals("Type:"))
                type        = matcher.group(2);
            else if (extType.equals("Description:"))
                description = matcher.group(2);
            else if (extType.equals("Parameter:"))
                parameter   = matcher.group(2);
            else
                _logger.warn("Invalid Regex group for ExtensionInfo: " + type);
        }

        this.name           = name.trim();
        this.niceName       = niceName.trim();
        this.type           = type.trim();
        this.description    = description.trim();
        if (parameter.trim().length() == 0 || parameter.trim().equals("<parameter>"))
            this.parameter      = "<parameter>";
        else
            this.parameter      = parameter.trim();

        if (this.name.equals("<name>") || this.description.equals("<description>")
                || this.niceName.equals("<nice name>") || this.type.equals("<type>"))
            _logger.warn("The given ExtensionInfo string has an invalid parameter: "+info);
    }
}
