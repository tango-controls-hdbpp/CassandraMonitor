//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  Basic Dialog Class to display info
//
// $Author: pascal_verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009,2010,2011,2012,2013,2014,2015
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
// $Revision:  $
//
// $Log:  $
//
//-======================================================================

package org.tango.cassandra_monitor_client.gui;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.util.List;

/**
 *	JDialog Class to display info
 *
 *	@author  Pascal Verdier
 */

public class CompactionDialog extends JDialog {
	private JFrame	parent;
    private List<DataCenter> dataCenterList;
    private static final int LINE_SIZE_MAX = 6;
    static final Color VALIDATION_COLOR = new Color(0x66ff66);
    static final Color CLEANUP_COLOR    = new Color(0x0fccff);
    static final Color ERROR_COLOR = new Color(0xff8080);
	//===============================================================
	/**
	 *	Creates new form CompactionChartDialog
	 */
	//===============================================================
	public CompactionDialog(JFrame parent, List<DataCenter> dataCenterList) {
		super(parent, false);
		this.parent = parent;
		this.dataCenterList = dataCenterList;
		initComponents();
        buildChartPanel();

        //  Build the compaction table
        CompactionTable compactionTable = new CompactionTable(dataCenterList);
        tabbedPane.add(compactionTable);
        tableScrollPane.setViewportView(compactionTable);

        buildLegendPanel();
		pack();

        Point point = parent.getLocationOnScreen();
        point.y += parent.getHeight();
 		setLocation(point);
	}
	//===============================================================
	//===============================================================
    void setChartDisplayed() {
	    tabbedPane.setSelectedIndex(1);
    }
	//===============================================================
	//===============================================================
    private void buildChartPanel() {
        // Build the chart panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        for (DataCenter dataCenter : dataCenterList) {
            JLabel label = new JLabel(dataCenter.getName());
            label.setFont(new Font("Dialog", Font.BOLD, 18));
            centerTopPanel.add(label, gbc);
            gbc.gridy++;

            //	Create and add chart for each node
            for (CassandraNode node : dataCenter) {
                centerTopPanel.add(node.getCompactionChart(), gbc);
                if (++gbc.gridx==LINE_SIZE_MAX) {
                    gbc.gridx = 0;
                    gbc.gridy++;
                }
            }

            //  Add a DataCenter separator
            gbc.gridx = 0;
            gbc.gridy++;
            centerTopPanel.add(new JLabel("   "), gbc);
            gbc.gridy++;
        }
    }
	//===============================================================
    private GridBagConstraints legendGbc = new GridBagConstraints();
	private static final Dimension legendDimension = new Dimension(20, 10);
	//===============================================================
    private void buildLegendPanel(String text, Color background) {
        if (text!=null) {
            if (background!=null) {
                JButton button = new JButton("  ");
                button.setBackground(background);
                button.setBorder(new SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                button.setEnabled(false);
                button.setPreferredSize(legendDimension);
                legendPanel.add(button, legendGbc);
                legendGbc.gridx++;
            }
            JLabel label = new JLabel(text + "  ");
            label.setFont(new Font("Dialog", Font.BOLD, 11));
            legendPanel.add(label, legendGbc);
            legendGbc.gridx++;
        }
        else {
            //  Add a label as separator
            legendPanel.add(new JLabel("           "), legendGbc);
            legendGbc.gridx++;
        }
    }
	//===============================================================
	//===============================================================
    private void buildLegendPanel() {
        legendGbc.insets = new Insets(5,0,0,5);
        legendGbc.gridx = 0;
        legendGbc.gridy = 0;
        buildLegendPanel("Bars:", null);
        buildLegendPanel("Compactions", Color.red);
        buildLegendPanel("Validations", VALIDATION_COLOR);
        buildLegendPanel("Cleanups", CLEANUP_COLOR);
        buildLegendPanel(null, null);
        buildLegendPanel("Chart:", null);
        buildLegendPanel("Error or compaction pipe", ERROR_COLOR);
    }
	//===============================================================
	//===============================================================

	//===============================================================
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	//===============================================================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel topPanel = new javax.swing.JPanel();
        javax.swing.JPanel titlePanel = new javax.swing.JPanel();
        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        tableScrollPane = new javax.swing.JScrollPane();
        javax.swing.JPanel centerPanel = new javax.swing.JPanel();
        legendPanel = new javax.swing.JPanel();
        centerTopPanel = new javax.swing.JPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout());

        topPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 18));
        titleLabel.setText("Compaction Activity");
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, java.awt.BorderLayout.NORTH);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        cancelBtn.setText("Dismiss");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelBtn);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        tabbedPane.addTab("Table", tableScrollPane);

        centerPanel.setLayout(new java.awt.BorderLayout());

        legendPanel.setLayout(new java.awt.GridBagLayout());
        centerPanel.add(legendPanel, java.awt.BorderLayout.NORTH);

        centerTopPanel.setLayout(new java.awt.GridBagLayout());
        centerPanel.add(centerTopPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Chart", centerPanel);

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	//===============================================================
	//===============================================================
	@SuppressWarnings("UnusedParameters")
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
		doClose();
	}//GEN-LAST:event_cancelBtnActionPerformed

	//===============================================================
	//===============================================================
    @SuppressWarnings("UnusedParameters")
	private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
		doClose();
	}//GEN-LAST:event_closeDialog

	//===============================================================
	/**
	 *	Closes the dialog
	 */
	//===============================================================
	private void doClose() {
	
		if (parent==null)
			System.exit(0);
		else {
			setVisible(false);
			dispose();
		}
	}
	//===============================================================
	//===============================================================

	//===============================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerTopPanel;
    private javax.swing.JPanel legendPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
	//===============================================================
}
