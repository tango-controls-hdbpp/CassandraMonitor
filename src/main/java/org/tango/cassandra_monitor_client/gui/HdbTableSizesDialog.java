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
//-======================================================================

package org.tango.cassandra_monitor_client.gui;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.TangoDs.Except;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 *	JDialog Class to display HDB table sizes
 *
 *	@author  Pascal Verdier
 */

public class HdbTableSizesDialog extends JDialog {
    private DisplayedData displayedData;
    private List<CassandraNode> cassandraNodesList = new ArrayList<>();
    private String[] rowHeaders;
    private JTable jTable;
    private int selectedRow = -1;
    private int mode;

    public static final int HDB_TABLE_SIZE = 0;
    public static final int HDB_SS_TABLE_NUMBER = 1;

    private static final Color firstColumnBackground = new Color(0xe0e0e0);
    private static final Color selectionBackground = new Color(0xe0e0ff);
    //===============================================================
	/**
	 *	Creates new form CompactionHistoryDialog
	 */
	//===============================================================
	public HdbTableSizesDialog(JFrame parent, List<DataCenter> dataCenterList, int mode) throws DevFailed {
		super(parent, true);
		this.mode = mode;
		initComponents();

        //  Read the table size from cassandra nodes and build JTable object
        List<HdbTableList> hdbTableLists = buildHdbTablesList(dataCenterList);
        buildDisplayedData(hdbTableLists);
        buildSizeTable();

        switch (mode) {
            case HDB_TABLE_SIZE:
                titleLabel.setText("HDB table sizes");
                break;
            case HDB_SS_TABLE_NUMBER:
                titleLabel.setText("SS Tables");
        }
		pack();
 		ATKGraphicsUtils.centerDialog(this);
	}
    //===============================================================
    //===============================================================
    private List<HdbTableList> buildHdbTablesList(List<DataCenter> dataCenterList) throws DevFailed {
	    List<HdbTableList> hdbTableLists = new ArrayList<>();
	    //  For each Cassandra node, start a thread to rad the table sizes
        for (DataCenter dataCenter : dataCenterList) {
            for (CassandraNode cassandraNode : dataCenter) {
                cassandraNodesList.add(cassandraNode);
                //noinspection MismatchedQueryAndUpdateOfCollection
                HdbTableList hdbTableList = new HdbTableList(cassandraNode);
                hdbTableLists.add(hdbTableList);
            }
        }

        //  Wait for the end of each thread
        StringBuilder errorMessage = new StringBuilder();
        for (HdbTableList hdbTableList : hdbTableLists) {
            try {
                hdbTableList.thread.join();
            } catch (InterruptedException e) { /* */ }
            if (hdbTableList.thread.error != null) {
                errorMessage.append(hdbTableList.thread.error).append('\n');
            }
        }
        if (errorMessage.length()>0) {
            Except.throw_exception("", errorMessage.toString());
        }
        return hdbTableLists;
    }
    //===============================================================
    //===============================================================
    private void buildDisplayedData(List<HdbTableList> hdbTableLists) {
	    //  Build row headers with first cassandra node
        int i=0;
        rowHeaders = new String[hdbTableLists.get(0).size()];
        for (HdbTable hdbTable : hdbTableLists.get(0))
            rowHeaders[i++] = hdbTable.name;

        //  Build the final displayed data
        displayedData = new DisplayedData(rowHeaders, cassandraNodesList.size());
        int index = 0;
        for (HdbTableList hdbTableList : hdbTableLists) {
            displayedData.addText(index++, hdbTableList);
        }
    }
    //===============================================================
    //===============================================================
    public void buildSizeTable() {
        // Create the table
        DataTableModel dataTableModel = new DataTableModel();
        jTable = new JTable(dataTableModel);
        jTable.setRowSelectionAllowed(true);
        jTable.setColumnSelectionAllowed(true);
        jTable.setDragEnabled(false);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        jTable.setDefaultRenderer(String.class, new LabelCellRenderer());
        jTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tableActionPerformed(evt);
            }
        });

        //  Set column width
        final Enumeration columnEnum = jTable.getColumnModel().getColumns();
        int i = 0;
        int tableWidth = 0;
        TableColumn tableColumn;
        while (columnEnum.hasMoreElements()) {
            tableColumn = (TableColumn) columnEnum.nextElement();
            int columnWidth =  i++==0 ? 200 : 80;
            tableColumn.setPreferredWidth(columnWidth);
            tableWidth += columnWidth;
        }

        //	Put it in scrolled pane
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(tableWidth, 640));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    //=======================================================
    //=======================================================
    private void tableActionPerformed(MouseEvent event) {
        Point clickedPoint = new Point(event.getX(), event.getY());
        selectedRow = jTable.rowAtPoint(clickedPoint);
        jTable.repaint();
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
        titleLabel = new javax.swing.JLabel();
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 18));
        titleLabel.setText("Dialog Title");
        topPanel.add(titleLabel);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        cancelBtn.setText("Cancel");
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
	//===============================================================
	private void doClose() {
		setVisible(false);
		dispose();
	}
	//===============================================================
	//===============================================================


	//===============================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
	//===============================================================




	//===============================================================
    /*
     *  A JTable line
     */
	//===============================================================
    private class DisplayedLine {
        private String tableName;
        private String[] text;
        //===========================================================
        private DisplayedLine(String tableName, int arraySize) {
            this.tableName = tableName;
            text = new String[arraySize];
        }
        //===========================================================
        private void setTexAt(int index, String text) {
            this.text[index] = text;
        }
        //===========================================================
    }
	//===============================================================
    /*
     * A list of JTable line (Displayed table data)
     */
	//===============================================================
    private class DisplayedData extends ArrayList<DisplayedLine> {
        //===========================================================
        private DisplayedData(String[] tableNames, int nb) {
            for (String tableName : tableNames) {
                add(new DisplayedLine(tableName, nb));
            }
        }
        //===========================================================
        private void addText(int index, HdbTableList hdbTables) {
            for (HdbTable hdbTable : hdbTables) {
                switch (mode) {
                    case HDB_TABLE_SIZE:
                        addText(hdbTable.name, index, hdbTable.sizeStr);
                        break;
                    case HDB_SS_TABLE_NUMBER:
                        addText(hdbTable.name, index, Integer.toString(hdbTable.ssTableNumber));
                }
            }
        }
        //===========================================================
        private void addText(String tableName, int index, String text) {
            for (DisplayedLine line : this) {
                if (line.tableName.equals(tableName))
                    line.setTexAt(index, text);
            }
        }
        //===========================================================
    }
	//===============================================================
	//===============================================================






    //==============================================================
    /**
     * The Data Table Model
     */
    //==============================================================
    public class DataTableModel extends AbstractTableModel {
        //==========================================================
        public int getColumnCount() {
            return cassandraNodesList.size()+1;
        }
        //==========================================================
        public int getRowCount() {
            return rowHeaders.length;
        }
        //==========================================================
        public String getColumnName(int column) {
            String title;
            if (column == 0)
                title = "HDB table name";
            else
                title = cassandraNodesList.get(column-1).getName();
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
            if (isVisible())
                return getValueAt(0, column).getClass();
            else
                return null;
        }
        //==========================================================
    }
    //==============================================================
    //==============================================================


    //=========================================================================
    /**
     * Renderer to set cell color
     */
    //=========================================================================
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
            switch (column) {
                case 0:
                    if (row==selectedRow)
                        setBackground(selectionBackground);
                    else
                        setBackground(firstColumnBackground);
                    setText(rowHeaders[row]);
                    break;
                default:
                    if (row==selectedRow)
                        setBackground(selectionBackground);
                    else
                        setBackground(Color.white);
                    setText(displayedData.get(row).text[column-1]);
                    break;
            }
            return this;
        }
    }
    //=========================================================================



    //==============================================================
    //==============================================================
    private class ReadDataThread extends Thread {
        private HdbTableList hdbTableList;
        private String error = null;
        private ReadDataThread(HdbTableList hdbTableList) {
            this.hdbTableList = hdbTableList;
        }
        public void run() {
            try {
                switch (mode) {
                    case HDB_TABLE_SIZE:
                        DevVarDoubleStringArray dsa = hdbTableList.cassandraNode.readHdbTableSizes();
                        //  Build HdbTable objects
                        for (int i = 0 ; i<dsa.dvalue.length ; i++) {
                            hdbTableList.add(new HdbTable(dsa.svalue[i], dsa.dvalue[i]));
                        }
                        break;
                    case HDB_SS_TABLE_NUMBER:
                        String[] array = hdbTableList.cassandraNode.readSsTableNumbers();
                        for (String line : array) {
                            StringTokenizer stk = new StringTokenizer(line, ":");
                            String name = stk.nextToken().trim();
                            String nbStr = stk.nextToken().trim();
                            int nb = Integer.parseInt(nbStr);
                            hdbTableList.add(new HdbTable(name, nb));
                        }
                        break;
                }
                Collections.sort(hdbTableList, new TableComparator());
            }
            catch (DevFailed e) {
                error = e.errors[0].desc;
            }
        }
    }
    //==============================================================
    //==============================================================






    //=========== HDB table size management ==================


    //===============================================================
    /*
     * A class defining a HDB table
     */
    //===============================================================
    private class HdbTable {
        private String name;
        private double size;
        private String sizeStr;
        private int ssTableNumber=0;
        //===========================================================
        private HdbTable(String name, int ssTableNumber) {
            this.name = name;
            this.ssTableNumber = ssTableNumber;
        }
        //===========================================================
        private HdbTable(String name, double size) {
            this.name = name;
            this.size = size;
            if (size<1.0)
                sizeStr =  String.format("%.1f", size*1000.0) + " Kb";
            else
            if (size<1000)
                sizeStr = String.format("%.1f", size) + " Mb";
            else
                sizeStr = String.format("%.1f", size/1000.0) + " Gb";
        }
        //===========================================================
    }
    //===============================================================
    //===============================================================



    //===============================================================
    /*
     * A list of HDB table in a cassandra node
     */
    //===============================================================
    private class HdbTableList extends ArrayList<HdbTable> {
        private CassandraNode cassandraNode;
        private ReadDataThread thread;
        //===========================================================
        private HdbTableList(CassandraNode cassandraNode) {
            this.cassandraNode = cassandraNode;
            thread = new ReadDataThread(this);
            thread.start();
        }
        //===========================================================
    }
    //===============================================================
    //===============================================================



    //==============================================================
    /**
     * To sort record by size order
     */
    //==============================================================
    private class TableComparator implements Comparator<HdbTable> {
        @Override
        public int compare(HdbTable hdbTable1, HdbTable hdbTable2) {
            switch (mode) {
                case HDB_TABLE_SIZE:
                    if (hdbTable1.size == hdbTable2.size)
                        return hdbTable2.name.compareTo(hdbTable1.name);
                    else
                        return Double.compare(hdbTable2.size, hdbTable1.size);

                case  HDB_SS_TABLE_NUMBER:
                    if (hdbTable1.ssTableNumber == hdbTable2.ssTableNumber)
                        return hdbTable2.name.compareTo(hdbTable1.name);
                    else
                        return Double.compare(hdbTable2.ssTableNumber, hdbTable1.ssTableNumber);
            }
            return 0;
        }
    }
    //==============================================================
    //==============================================================
}
