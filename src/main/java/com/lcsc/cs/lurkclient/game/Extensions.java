package com.lcsc.cs.lurkclient.game;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jake on 3/22/2015.
 * This class holds an EntityContainer for the Extensions to be selected and also contains information
 * about each extension.
 */
public class Extensions {
    private EntityContainer _extensions;

    //This maps an extension's nice name to its extension info.
    private Map<String, ExtensionInfo> _extensionInfo;

    public Extensions(JPanel panel, int x, int y, List<ExtensionInfo> extensionInfo) {
        _extensions     = new EntityContainer("Extensions", x, y, ListSelectionModel.SINGLE_SELECTION, panel);
        _extensionInfo  = new HashMap<String, ExtensionInfo>();

        for (ExtensionInfo info : extensionInfo) {
            _extensions.add(info.niceName);
            _extensionInfo.put(info.niceName, info);
        }
    }

    /**
     * This allows us to know which extension is selected so that we can use it in the LogicLinker when the
     * 'Use Extension' button is pressed.
     * @return The selected element's nice name. If nothing is selected null will be returned.
     */
    public String getSelectedExtension() {
        return _extensions.getSelectedElement();
    }

    public ExtensionInfo getExtensionInfo(String niceName) {
        return _extensionInfo.get(niceName);
    }
}
