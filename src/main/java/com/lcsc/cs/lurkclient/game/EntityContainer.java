package com.lcsc.cs.lurkclient.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jake on 3/11/2015.
 * This just handles the elements within some JList.
 */
public class EntityContainer {
    private static final Logger     _logger      = LoggerFactory.getLogger(LogicLinker.class);
    private final DefaultListModel  _entities;
    private final JList             _entityList;

    public EntityContainer(String title, int x, int y, JPanel panel) {
        JLabel titleLbl = new JLabel(title);

        Font oldFont = titleLbl.getFont();
        Font newFont = new Font(oldFont.getFontName(), Font.PLAIN, 25);
        titleLbl.setFont(newFont);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor    = GridBagConstraints.WEST;
        c.gridx     = x;
        c.gridy     = y;
        panel.add(titleLbl, c);

        _entities = new DefaultListModel();

        _entityList = new JList(_entities);
        _entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollList = new JScrollPane(_entityList);
        scrollList.setPreferredSize(new Dimension(100, 100));
        scrollList.setMinimumSize(_entityList.getPreferredSize());

        c           = new GridBagConstraints();
        c.weightx   = c.weighty = 1.0;
        c.fill      = GridBagConstraints.BOTH;
        c.gridx     = x;
        c.gridy     = y+1;

        panel.add(scrollList, c);
    }

    //This makes sure that every added element is unique!
    public void add(String element) {
        if (!_entities.contains(element)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    _entities.addElement(element);
                    _logger.debug(String.format("Element added to EntityContainer: %s", element));
                }
            });
        }
        else
            _logger.debug(String.format("Element already exists in EntityContainer: %s", element));
    }

    /**
     * This will add a list of the current elements in the EntityContainer.
     * With this list we will remove/add elements as needed so that the elements
     * list and the entity container have the same elements.
     * @param elements The current list of elements.
     */
    public void update(List<String> elements) {
        clear();
        for (String element : elements)
            add(element);
    }

    public void remove(String element) {
        _entities.removeElement(element);
    }

    public String getSelectedElement() {
        return (String)_entityList.getSelectedValue();
    }

    public void clear() {
        _entities.clear();
    }
}
