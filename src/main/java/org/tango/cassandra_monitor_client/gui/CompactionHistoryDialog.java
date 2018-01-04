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
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


//===============================================================
/**
 *	JDialog Class to display compaction history
 *
 *	@author  Pascal Verdier
 */
//===============================================================


public class CompactionHistoryDialog extends JDialog {
    private List<Record> recordList = new ArrayList<>();
    private List<Record> allRecordList = new ArrayList<>();
    private DataTableModel dataTableModel;

    private static final String[] TABLE_COLUMNS = {
           "Date" , "Keyspace", "Column Family", "Ratio",
    };
    private static final int[] COLUMN_WIDTHS = {
            100, 80, 150, 60,
    };
    //===============================================================
	/**
	 *	Creates new form CompactionHistoryDialog
	 */
	//===============================================================
	public CompactionHistoryDialog(JFrame parent, CassandraNode cassandraNode) throws DevFailed {
		super(parent, true);
		initComponents();
		String[] history =  cassandraNode.displayCompactionHistory();
		for (String line : history) {
			allRecordList.add(new Record(line));
		}
        Collections.sort(allRecordList, new RecordComparator());
		setRecordList();
        buildHistoryTable();

		titleLabel.setText("Compaction History on " + cassandraNode.getName());
		pack();
 		ATKGraphicsUtils.centerDialog(this);
	}
    //===============================================================
    //===============================================================
    private void setRecordList() {
	    boolean showAll = showAllButton.isSelected();
	    recordList.clear();
	    for (Record record : allRecordList) {
	        if (record.keyspace.equals("hdb") || showAll) {
	            recordList.add(record);
            }
        }
    }
    //===============================================================
    //===============================================================
    public void buildHistoryTable() {
        // Create the table
        dataTableModel = new DataTableModel();
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
        TableColumn tableColumn;
        while (columnEnum.hasMoreElements()) {
            tableColumn = (TableColumn) columnEnum.nextElement();
            tableColumn.setPreferredWidth(COLUMN_WIDTHS[i++]);
        }

        //	Put it in scrolled pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    //===============================================================
    /**
     * Format with time before date, and no millis
     * @param ms date to format
     * @return the formatted date
     */
    //===============================================================
    public static String formatDate(long ms) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm:ss");
        return simpleDateFormat.format(new Date(ms));
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
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        showAllButton = new javax.swing.JRadioButton();
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

        jLabel1.setText("         ");
        topPanel.add(jLabel1);

        showAllButton.setText("Show All");
        showAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllButtonActionPerformed(evt);
            }
        });
        topPanel.add(showAllButton);

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
    @SuppressWarnings("UnusedParameters")
    private void showAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllButtonActionPerformed
        // TODO add your handling code here:
        setRecordList();
        dataTableModel.fireTableDataChanged();
    }//GEN-LAST:event_showAllButtonActionPerformed
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
    private javax.swing.JRadioButton showAllButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
	//===============================================================




	//===============================================================
	//===============================================================
    private class Record {
        private long time;
        private String date;
        private String keyspace;
        private String columnName;
        private String ratioStr;
        //===========================================================
        private Record(String line) {
            StringTokenizer stk = new StringTokenizer(line, ",");
            setDate(stk.nextToken().trim());
            keyspace = stk.nextToken().trim();
            columnName = stk.nextToken().trim();
            String in = stk.nextToken().trim();
            String out = stk.nextToken().trim();
            setRatio(in, out);
        }
        //===========================================================
        private void setDate(String str) {
            time = Long.parseLong(str);
            date = formatDate(time);
        }
        //===========================================================
        private void setRatio(String in, String out) {
            try {
                double ratio = (double) Integer.parseInt(out) / Integer.parseInt(in);
                ratioStr = String.format("%.2f", ratio);
            }
            catch (NumberFormatException e) {
                System.err.println(e.toString());
                ratioStr = "? ?";
            }
        }
        //===========================================================
        public String toString() {
            return date + ":\t" + keyspace + "\t" + "\t" + columnName + "\t" + ratioStr;
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
    private class RecordComparator implements Comparator<Record> {
        @Override
        public int compare(Record record1, Record record2) {
            return Long.compare(record2.time, record1.time);
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
            return TABLE_COLUMNS.length;
        }
        //==========================================================
        public int getRowCount() {
            return recordList.size();
        }
        //==========================================================
        public String getColumnName(int columnIndex) {
            String title;
            if (columnIndex >= getColumnCount())
                title = TABLE_COLUMNS[getColumnCount()-1];
            else
                title = TABLE_COLUMNS[columnIndex];
            return title;
        }
        //==========================================================
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0: return recordList.get(row).date;
                case 1: return recordList.get(row).keyspace;
                case 2: return recordList.get(row).columnName;
                case 3: return recordList.get(row).ratioStr;
            }
            return "";
        }
        //==========================================================
    }
    //==============================================================
    //==============================================================
}
