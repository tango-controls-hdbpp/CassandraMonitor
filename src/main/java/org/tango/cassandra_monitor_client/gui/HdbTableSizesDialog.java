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
import javax.swing.table.TableColumn;
import java.awt.*;
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
    //===============================================================
	/**
	 *	Creates new form CompactionHistoryDialog
	 */
	//===============================================================
	public HdbTableSizesDialog(JFrame parent, List<DataCenter> dataCenterList) throws DevFailed {
		super(parent, true);
		initComponents();

        //  Read the table size from cassandra nodes and build JTable object
        List<HdbTableList> hdbTableLists = buildHdbTablesList(dataCenterList);
        buildFinalData(hdbTableLists);
        buildSizeTable();

		titleLabel.setText("HDB table sizes");
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
    private void buildFinalData(List<HdbTableList> hdbTableLists) {
	    //  Build row headers with first cassandra node
        int i=0;
        rowHeaders = new String[hdbTableLists.get(0).size()];
        for (HdbTable hdbTable : hdbTableLists.get(0))
            rowHeaders[i++] = hdbTable.name;

        //  Build the final displayed data
        displayedData = new DisplayedData(rowHeaders, cassandraNodesList.size());
        int index = 0;
        for (HdbTableList hdbTableList : hdbTableLists) {
            displayedData.addSize(index++, hdbTableList);
        }
    }
    //===============================================================
    //===============================================================
    public void buildSizeTable() {
        // Create the table
        DataTableModel dataTableModel = new DataTableModel();
        JTable table;
        table = new JTable(dataTableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setDragEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));

        //  Set column width
        final Enumeration columnEnum = table.getColumnModel().getColumns();
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
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(tableWidth, 640));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
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
	//===============================================================
    private class DisplayedLine {
        private String tableName;
        private String[] sizeSt;
        //===========================================================
        private DisplayedLine(String tableName, int arraySize) {
            this.tableName = tableName;
            sizeSt = new String[arraySize];
        }
        //===========================================================
        private void addSize(int index, String sizeStr) {
            this.sizeSt[index] = sizeStr;
        }
        //===========================================================
    }
	//===============================================================
	//===============================================================
    private class DisplayedData extends ArrayList<DisplayedLine> {
        //===========================================================
        private DisplayedData(String[] tableNames, int nb) {
            for (String tableName : tableNames) {
                add(new DisplayedLine(tableName, nb));
            }
        }
        //===========================================================
        private void addSize(int index, HdbTableList hdbTables) {
            for (HdbTable hdbTable : hdbTables) {
                addSize(hdbTable.name, index, hdbTable.sizeStr);
            }
        }
        //===========================================================
        private void addSize(String tableName, int index, String sizeSrt) {
            for (DisplayedLine line : this) {
                if (line.tableName.equals(tableName))
                    line.addSize(index, sizeSrt);
            }
        }
        //===========================================================
    }
	//===============================================================
	//===============================================================



	//===============================================================
	//===============================================================
    private class HdbTable {
        private String name;
        private double size;
        private String sizeStr;
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
        public String toString() {
            return name + ":\t" + size;
        }
        //===========================================================
    }
	//===============================================================
	//===============================================================



	//===============================================================
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
        private HdbTable getHdbTable(String tableName) {
            for (HdbTable hdbTable : this)
                if (hdbTable.name.equals(tableName))
                    return hdbTable;
            return null;
        }
        //===========================================================
    }
	//===============================================================
	//===============================================================



    //==============================================================
    /**
     * To sort record by time order
     */
    //==============================================================
    private class TableComparator implements Comparator<HdbTable> {
        @Override
        public int compare(HdbTable hdbTable1, HdbTable hdbTable2) {
            if (hdbTable1.size==hdbTable2.size)
                return hdbTable2.name.compareTo(hdbTable1.name);
            else
                return Double.compare(hdbTable2.size, hdbTable1.size);
        }
    }
    //==============================================================
    //==============================================================





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
            switch (column) {
                case 0: return rowHeaders[row];
                default: return displayedData.get(row).sizeSt[column-1];
            }
        }
        //==========================================================
    }
    //==============================================================
    //==============================================================



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
                DevVarDoubleStringArray dsa = hdbTableList.cassandraNode.readHdbTableSizes();

                //  Build HdbTable objects
                for (int i=0 ; i<dsa.dvalue.length ; i++) {
                    HdbTable hdbTable = new HdbTable(dsa.svalue[i], dsa.dvalue[i]);
                    hdbTableList.add(hdbTable);
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
}
