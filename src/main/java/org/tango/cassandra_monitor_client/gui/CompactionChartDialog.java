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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

import static org.tango.cassandra_monitor_client.gui.CassandraNode.CLEANUP;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.VALIDATION;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.PIPE_ERROR;


//===============================================================
/**
 *	JDialog Class to display info
 *
 *	@author  Pascal Verdier
 */
//===============================================================


@SuppressWarnings("MagicConstant")
public class CompactionChartDialog extends JDialog {

	private JFrame	parent;
    private static CassandraNode selectedNode = null;
    private static List<DataCenter> dataCenterList;
    private static final int LINE_SIZE_MAX = 6;
	//===============================================================
	/**
	 *	Creates new form CompactionChartDialog
	 */
	//===============================================================
	public CompactionChartDialog(JFrame parent, List<DataCenter> dataCenterList) {
		super(parent, false);
		this.parent = parent;
		CompactionChartDialog.dataCenterList = dataCenterList;
		initComponents();
        buildTable();

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
		pack();

        Point point = parent.getLocationOnScreen();
        point.y += parent.getHeight();
 		setLocation(point);
	}
	//===============================================================
	//===============================================================
    private static DataTableModel tableModel;
    private static final String[] columnNames = {
        "Node", "Table", "Task", "Data size", "Ratio",
    };
    private static final int[] columnWidth = { 100, 200, 100, 80, 80 };
    private static final int NODE  = 0;
    private static final int TABLE = 1;
    private static final int TASK  = 2;
    private static final int SIZE  = 3;
    private static final int RATIO = 4;
    static final Color VALIDATION_COLOR = new Color(0x66ff66);
    static final Color CLEANUP_COLOR    = new Color(0x0fccff);
    private static final Color SELECTION_COLOR  = new Color(0xffffdd);
    private static final Color ERROR_COLOR = new Color(0xff8080);
    //===============================================================
    private void buildTable() {
        tableModel = new DataTableModel();

        // Create the table
        JTable table = new JTable(tableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setDragEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        table.setDefaultRenderer(String.class, new LabelCellRenderer());

        //  Set column width
        final Enumeration columnEnum = table.getColumnModel().getColumns();
        int i = 0;
        TableColumn tableColumn;
        while (columnEnum.hasMoreElements()) {
            tableColumn = (TableColumn) columnEnum.nextElement();
            tableColumn.setPreferredWidth(columnWidth[i++]);
        }
        scrollPane.add(table);
        scrollPane.setViewportView(table);
    }
	//===============================================================
	//===============================================================
    public static void setSelection(CassandraNode cassandraNode) {
        CompactionChartDialog.selectedNode = cassandraNode;
        tableModel.fireTableDataChanged();
        //  Set selection in light gray
        for (DataCenter dataCenter : dataCenterList) {
            for (CassandraNode node : dataCenter) {
                if (node.getCompactionChart() == cassandraNode.getCompactionChart()) {
                    node.getCompactionChart().setBackground(SELECTION_COLOR);
                    node.setSelected(true);
                } else {
                    node.getCompactionChart().setBackground(Color.white);
                    node.setSelected(true);
                }
            }
        }
    }
	//===============================================================
	//===============================================================
    public static void fireDataChanged() {
        tableModel.fireTableDataChanged();
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
        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        javax.swing.JPanel centerPanel1 = new javax.swing.JPanel();
        centerTopPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        titleLabel.setText("Compaction Activity");
        topPanel.add(titleLabel);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        centerPanel1.setLayout(new java.awt.BorderLayout());

        centerTopPanel.setLayout(new java.awt.GridBagLayout());
        centerPanel1.add(centerTopPanel, java.awt.BorderLayout.NORTH);

        scrollPane.setPreferredSize(new java.awt.Dimension(500, 80));
        centerPanel1.add(scrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(centerPanel1, java.awt.BorderLayout.CENTER);

        cancelBtn.setText("Dismiss");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelBtn);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

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
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
	//===============================================================





    //==============================================================
    /**
     * The Table tableModel
     */
    //==============================================================
    public class DataTableModel extends AbstractTableModel {
        //==========================================================
        public int getColumnCount() {
            return columnNames.length;
        }
        //==========================================================
        public int getRowCount() {
            if (selectedNode ==null)
                return 0;
            return selectedNode.getCompactionList().size();
        }
        //==========================================================
        public String getColumnName(int columnIndex) {
            String title;
            if (columnIndex >= getColumnCount())
                title = columnNames[getColumnCount()-1];
            else
                title = columnNames[columnIndex];

            // remove tango host if any
            if (title.startsWith("tango://")) {
                int index = title.indexOf('/', "tango://".length());
                title = title.substring(index+1);
            }

            return title;
        }
        //==========================================================
        public Object getValueAt(int row, int column) {
            //  Value to display is returned by
            // LabelCellRenderer.getTableCellRendererComponent()
            return "";
        }
        //==========================================================
        /**
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         *
         * @param  column the specified co;umn number
         * @return the cell class at first row for specified column.
         */
        //==========================================================
        public Class getColumnClass(int column) {
            if (isVisible()) {
                return getValueAt(0, column).getClass();
            }
            else
                return null;
        }
        //==========================================================
        //==========================================================
    }
    //==============================================================
    //==============================================================



    //==============================================================
    /**
     * Renderer to set cell color
     */
    //==============================================================
    public class LabelCellRenderer extends JLabel implements TableCellRenderer {

        //==========================================================
        public LabelCellRenderer() {
            //setFont(new Font("Dialog", Font.BOLD, 11));
            setOpaque(true); //MUST do this for background to show up.
        }
        //==========================================================
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground(getBackground(row));
            switch (column) {
                case NODE:
                    setText(selectedNode.getName());
                    break;
                case TABLE:
                    setText(selectedNode.getCompactionList().get(row).tableName);
                    break;
                case TASK:
                    setText(selectedNode.getCompactionList().get(row).taskName);
                    break;
                case SIZE:
                    setText(selectedNode.getCompactionList().get(row).totalStr);
                    break;
                case RATIO:
                    setText(selectedNode.getCompactionList().get(row).ratioStr);
                    break;
            }
            return this;
        }
        //==========================================================
        private Color getBackground(int row) {
            switch (selectedNode.getCompactionList().get(row).taskType) {
                case VALIDATION:
                    return VALIDATION_COLOR;
                case CLEANUP:
                    return CLEANUP_COLOR;
                case PIPE_ERROR:
                    return ERROR_COLOR;
            }
            return Color.white;
        }
        //==========================================================
    }
    //==============================================================
    //==============================================================

}
