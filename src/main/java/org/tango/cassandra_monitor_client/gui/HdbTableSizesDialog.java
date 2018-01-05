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
    private List<HdbTable> hdbTableList = new ArrayList<>();
    private List<HdbTable> allHdbTableList = new ArrayList<>();
    private DataTableModel dataTableModel;
    private double totalSize;

    private static final String[] TABLE_COLUMNS = {
           "HDB table name" , "size", "Ratio (%)"
    };
    private static final int[] COLUMN_WIDTHS = {
            150, 80, 80,
    };
    //===============================================================
	/**
	 *	Creates new form CompactionHistoryDialog
	 */
	//===============================================================
	public HdbTableSizesDialog(JFrame parent, List<DataCenter> dataCenterList) throws DevFailed {
		super(parent, true);
		initComponents();
        //  showAll not used anymore
        showAllButton.setSelected(true);
        showAllButton.setVisible(false);

        //  Get sizes from first node
        CassandraNode cassandraNode = dataCenterList.get(0).get(0);
        DevVarDoubleStringArray dsa = cassandraNode.readHdbTableSizes();

        //  Build HdbTable objects
        totalSize = 0;
		for (int i=0 ; i<dsa.dvalue.length ; i++) {
		    HdbTable hdbTable = new HdbTable(dsa.svalue[i], dsa.dvalue[i]);
            totalSize += hdbTable.size;
            allHdbTableList.add(hdbTable);
		}
        Collections.sort(allHdbTableList, new TableComparator());
		setHdbTableList();
        buildSizeTable();

		titleLabel.setText("HDB table sizes");
		pack();
 		ATKGraphicsUtils.centerDialog(this);
	}
    //===============================================================
    //===============================================================
    private void setHdbTableList() {
	    boolean showAll = showAllButton.isSelected();
	    hdbTableList.clear();
	    for (HdbTable hdbTable : allHdbTableList) {
	        if (hdbTable.size>0 || showAll) {
	            hdbTableList.add(hdbTable);
            }
        }
    }
    //===============================================================
    //===============================================================
    public void buildSizeTable() {
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

        JPanel topPanel = new JPanel();
        titleLabel = new JLabel();
        JLabel jLabel1 = new JLabel();
        showAllButton = new JRadioButton();
        JPanel bottomPanel = new JPanel();
        JButton cancelBtn = new JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
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

        getContentPane().add(topPanel, BorderLayout.NORTH);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelBtn);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

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
        setHdbTableList();
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
    private JRadioButton showAllButton;
    private JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
	//===============================================================




	//===============================================================
	//===============================================================
    private class HdbTable {
        private String name;
        private double size;
        private String sizeStr;
        private String ratio = null;
        //===========================================================
        private HdbTable(String name, double size) {
            this.name = name;
            this.size = size;
            if (size<1.0)
                sizeStr = Double.toString(size) + " Mb";
            else
            if (size<1000)
                sizeStr = String.format("%.1f", size) + " Mb";
            else
                sizeStr = String.format("%.1f", size/1000.0) + " Gb";
         }
        //===========================================================
        private String getRatio() {
            if (ratio==null)    //  Do it only first time
                ratio = String.format("%.3f", 100.0*size/totalSize);
            return ratio;
        }
        //===========================================================
        public String toString() {
            return name + ":\t" + size;
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
            return TABLE_COLUMNS.length;
        }
        //==========================================================
        public int getRowCount() {
            return hdbTableList.size();
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
                case 0: return hdbTableList.get(row).name;
                case 1: return hdbTableList.get(row).sizeStr;
                case 2: return hdbTableList.get(row).getRatio();
            }
            return "";
        }
        //==========================================================
    }
    //==============================================================
    //==============================================================
}
