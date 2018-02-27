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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.tango.cassandra_monitor_client.gui.CassandraNode.CLEANUP;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.PIPE_ERROR;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.VALIDATION;
import static org.tango.cassandra_monitor_client.gui.CompactionDialog.CLEANUP_COLOR;
import static org.tango.cassandra_monitor_client.gui.CompactionDialog.ERROR_COLOR;
import static org.tango.cassandra_monitor_client.gui.CompactionDialog.VALIDATION_COLOR;
import static org.tango.cassandramonitor.IConstants.refreshMonitor;


/**
 * This class is able to
 *
 * @author verdier
 */

public class CompactionTable extends JTable {
    private List<CassandraNode> cassandraNodeList = new ArrayList<>();
    private List<Record> recordList = new ArrayList<>();

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
    //===============================================================
    /**
     * Constructor for a list of data centers
     * @param dataCenterList data center list
     */
    //===============================================================
    public CompactionTable(List<DataCenter> dataCenterList) {
        buildTable();
        for (DataCenter dataCenter : dataCenterList)
            cassandraNodeList.addAll(dataCenter);

        new RefresherThread().start();
    }
    //===============================================================
    //===============================================================
    private void buildTable() {
        tableModel = new DataTableModel();

        // Create the table
        setModel(tableModel);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(true);
        setDragEnabled(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        setDefaultRenderer(String.class, new LabelCellRenderer());

        //  Set column width
        final Enumeration columnEnum = getColumnModel().getColumns();
        int i = 0;
        TableColumn tableColumn;
        while (columnEnum.hasMoreElements()) {
            tableColumn = (TableColumn) columnEnum.nextElement();
            tableColumn.setPreferredWidth(columnWidth[i++]);
        }
    }
    //===============================================================
    //===============================================================
    public void updateTable() {
        tableModel.fireTableDataChanged();
    }
    //===============================================================
    //===============================================================



    //===============================================================
    //===============================================================
    private class RefresherThread extends Thread {
        public void run() {
            //noinspection InfiniteLoopStatement
            while (true) {
                recordList = new ArrayList<>();
                synchronized (refreshMonitor) {
                    try {
                        for (CassandraNode cassandraNode : cassandraNodeList) {
                            for (Compaction compaction : cassandraNode.getCompactionList()) {
                                if (compaction != null)
                                    recordList.add(new Record(cassandraNode.getName(), compaction));
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                updateTable();
                try { sleep(1000); } catch (InterruptedException e) { /* */ }
            }
        }
    }
    //===============================================================
    //===============================================================



    //===============================================================
    //===============================================================
    private class Record {
        private String nodeName;
        private String tableName;
        private String taskName;
        private String total;
        private String ratio;
        private int taskType;
        private Record(String nodeName, Compaction compaction) {
            this.nodeName = nodeName;
            this.tableName = compaction.getTableName();
            this.taskName = compaction.getTaskName();
            this.total = compaction.getTotalStr();
            this.ratio = compaction.getRatioStr();
            this.taskType = compaction.getTaskType();
        }

    }
    //===============================================================
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
            return recordList.size();
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
            if (row<recordList.size()) {
                Record record = recordList.get(row);
                setBackground(getBackground(record.taskType));
                switch (column) {
                    case NODE:
                        setText(record.nodeName);
                        break;
                    case TABLE:
                        setText(record.tableName);
                        break;
                    case TASK:
                        setText(record.taskName);
                        break;
                    case SIZE:
                        setText(record.total);
                        break;
                    case RATIO:
                        setText(record.ratio);
                        break;
                }
            }
            return this;
        }

        //==========================================================
        private Color getBackground(int taskType) {
            switch (taskType) {
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
    //===============================================================
    //===============================================================
}
