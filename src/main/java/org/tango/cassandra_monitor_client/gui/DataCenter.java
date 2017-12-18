//+======================================================================
// :  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// : pascal_verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// :  $
//
//-======================================================================

package org.tango.cassandra_monitor_client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.tango.cassandra_monitor_client.tools.IConstants.BACKGROUND;


/**
 * This class is able to model a data center containing several nodes
 *
 * @author verdier
 */

public class DataCenter extends ArrayList<CassandraNode> {
    private String name;
    private JPanel panel;
    private static final String[] columnHeaders = {
            "Rack", "Node",  "Cluster", "Vers.",
            "Tokens", "Owns", "State", " Data  Load ", "Comp.",
    };
    //===============================================================
    //===============================================================
    public DataCenter(String name) {
        this.name = name;
    }
    //===============================================================
    //===============================================================
    private void buildPanel() {
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        //  Label for data center name
        JLabel label = new JLabel(name);
        label.setFont(new Font("Dialog", Font.BOLD, 18));
        gbc.gridwidth = 5;
        panel.add(label, gbc);
        gbc.gridwidth = 1;

        //  Build column headers
        gbc.gridx = 0;
        gbc.gridy++;
        for (String header : columnHeaders) {
            label = new JLabel(header);
            label.setFont(new Font("Dialog", Font.BOLD, 12));
            panel.add(label, gbc);
            gbc.gridx++;
        }

        //  Build line for each node
        gbc.gridy++;
        Font font = new Font("Dialog", Font.PLAIN, 12);
        for (CassandraNode node : this) {
            gbc.gridx = 0;
            label = new JLabel(node.getRackName());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            label = new JLabel(node.getName());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            label = new JLabel(node.getCluster());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            label = new JLabel(node.getVersion());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            label = new JLabel(node.getTokens());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            label = new JLabel(node.getOwns());
            label.setFont(font);
            panel.add(label, gbc);
            gbc.gridx++;
            panel.add(node.getStateViewer(), gbc);
            gbc.gridx++;
            panel.add(node.getDataLoadViewer(), gbc);
            gbc.gridx++;
            panel.add(node.getCompactionButton(), gbc);
            gbc.gridx++;
            panel.add(node.getTestButton(), gbc);
            gbc.gridy++;
        }
    }
    //===============================================================
    //===============================================================
    public String getName() { return name; }
    //===============================================================
    //===============================================================
    public JPanel getPanel() {
        if (panel==null)
            buildPanel();
        return panel; }
    //===============================================================
    //===============================================================
    public void sort() {
        Collections.sort(this, new NodeComparator());
    }
    //===============================================================
    //===============================================================




    //===============================================================
    //===============================================================
    private class NodeComparator implements Comparator<CassandraNode> {
        @Override
        public int compare(CassandraNode node1, CassandraNode node2) {
            return node1.getName().compareTo(node2.getName());
        }
    }
    //===============================================================
    //===============================================================
}
