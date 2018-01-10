//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for main swing class.
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015
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

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DbClass;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoApi.DbServer;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.Except;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import org.tango.cassandra_monitor_client.tools.ReleaseNote;
import org.tango.cassandra_monitor_client.tools.IconUtils;
import org.tango.cassandra_monitor_client.tools.PopupHtml;
import org.tango.cassandra_monitor_client.tools.SplashUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

//=======================================================
/**
 *	JFrame Class to display info
 *
 * @author  Pascal Verdier
 */
//=======================================================
public class CassandraMonitoring extends JFrame {
    private CompactionChartDialog compactionChartDialog;
    private List<CassandraNode> cassandraNodeList = new ArrayList<>();
    private List<DataCenter> dataCenterList = new ArrayList<>();
    private RequestTrendDialog requestTrendDialog;
    private HdbTableInformationDialog hdbTableSizeDialog = null;
    private HdbTableInformationDialog hdbSsTableDialog = null;
	//=======================================================
    /**
	 *	Creates new form CassandraMonitoring
	 */
	//=======================================================
    public CassandraMonitoring() throws DevFailed {
        checkDistributionDevice();
        SplashUtils.getInstance().startSplash();
        SplashUtils.getInstance().setSplashProgress(10, "Building GUI");
        initComponents();
        SplashUtils.getInstance().setSplashProgress(20, "Building GUI");
        buildDeviceList();
        SplashUtils.getInstance().setSplashProgress(30, "Building GUI");
        buildDataCenterPanels();
        requestTrendDialog = new RequestTrendDialog(this, dataCenterList);

        setTitle("CassandraMonitoring - " + SplashUtils.getRevisionNumber());
        ImageIcon icon = IconUtils.getInstance().getIcon("cassandra.jpeg", 0.10);
        setIconImage(icon.getImage());
        pack();
        setLocation(new Point(50, 30));
        SplashUtils.getInstance().stopSplash();
	}
	//=======================================================
	//=======================================================
    private void buildDataCenterPanels() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 20, 10, 20);
        for(DataCenter dataCenter : dataCenterList) {
            dataCenter.sort();
            panel.add(dataCenter.getPanel(), gbc);
            gbc.gridx++;
        }
        getContentPane().add(panel, BorderLayout.CENTER);
    }
	//=======================================================
	//=======================================================
    private void buildDeviceList() throws DevFailed {
        String className = System.getenv("ClassName");
        if (className==null)
            Except.throw_exception("PropertyNotDefined", "ClassName not defined");
        String instanceName = System.getenv("InstanceName");
        if (instanceName==null)
            Except.throw_exception("PropertyNotDefined", "InstanceName not defined");

        String serverName = className+'/'+instanceName;
        String[] deviceNames = new DbServer(serverName).get_device_name(className);
        if (deviceNames.length==0)
            Except.throw_exception("NoDeviceFound", "No device found for server " + serverName);
        for (String deviceName : deviceNames) {
            System.out.println(deviceName);
            cassandraNodeList.add(new CassandraNode(deviceName));
        }
        buildDistribution();
    }
	//=======================================================
	//=======================================================
    private void buildDistribution() {
        Map<String, DataCenter> dataCenterMap = new HashMap<>();
        for (CassandraNode node : cassandraNodeList) {
            DataCenter dataCenter = dataCenterMap.get(node.getDataCenter());
            if (dataCenter==null) {
                dataCenter = new DataCenter(this, node.getDataCenter());
                dataCenterMap.put(node.getDataCenter(), dataCenter);
            }
            dataCenter.add(node);
        }
        Set<String> keys = dataCenterMap.keySet();
        for (String key : keys)
            dataCenterList.add(dataCenterMap.get(key));
    }
	//=======================================================
	//=======================================================
    private void checkDistributionDevice() throws DevFailed {
        DbClass clazz = new DbClass("CassandraMonitor");
        DbDatum datum = clazz.get_property("DistributionDeviceName");
        if (datum.is_empty())
            Except.throw_exception("PropertyNotDefined",
                    "CassandraMonitor.DistributionDeviceName class property not defined !");
        try {
            new DeviceProxy(datum.extractString()).ping();
        } catch (DevFailed e) {
            String message = "Device   "+datum.extractString()+"   is not alive";
            ErrorPane.showErrorMessage(this, null, new Exception(message));
        }
    }
	//=======================================================
	//=======================================================

	//=======================================================
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	//=======================================================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel topPanel = new javax.swing.JPanel();
        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
        javax.swing.JMenu viewMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem overviewItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem compactionsItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem requestTrendItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem tableSizeItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem ssTableNumberItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem releaseMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 18));
        titleLabel.setText("Cassandra Monitoring Client");
        topPanel.add(titleLabel);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitItem.setMnemonic('E');
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        viewMenu.setMnemonic('V');
        viewMenu.setText("View");

        overviewItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        overviewItem.setMnemonic('O');
        overviewItem.setText("Overview");
        overviewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewItemActionPerformed(evt);
            }
        });
        viewMenu.add(overviewItem);

        compactionsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        compactionsItem.setMnemonic('C');
        compactionsItem.setText("Compactions");
        compactionsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compactionsItemActionPerformed(evt);
            }
        });
        viewMenu.add(compactionsItem);

        requestTrendItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        requestTrendItem.setMnemonic('R');
        requestTrendItem.setText("Client Requests");
        requestTrendItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestTrendItemActionPerformed(evt);
            }
        });
        viewMenu.add(requestTrendItem);

        tableSizeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        tableSizeItem.setMnemonic('T');
        tableSizeItem.setText("Table sizes");
        tableSizeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableSizeItemActionPerformed(evt);
            }
        });
        viewMenu.add(tableSizeItem);

        ssTableNumberItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        ssTableNumberItem.setMnemonic('S');
        ssTableNumberItem.setText("SS Table number");
        ssTableNumberItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ssTableNumberItemActionPerformed(evt);
            }
        });
        viewMenu.add(ssTableNumberItem);

        menuBar.add(viewMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("help");

        releaseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        releaseMenuItem.setMnemonic('R');
        releaseMenuItem.setText("Release Notes");
        releaseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(releaseMenuItem);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	//=======================================================
	//=======================================================
    @SuppressWarnings("UnusedParameters")
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        doClose();
    }//GEN-LAST:event_exitItemActionPerformed

	//=======================================================
	//=======================================================
    @SuppressWarnings("UnusedParameters")
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        doClose();
    }//GEN-LAST:event_exitForm

    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        String message = "This application is able monitor Cassandra data centers\n" +
                "Release: " + SplashUtils.getRevisionNumber() +
                "\n\nPascal Verdier - ESRF - Accelerator Control Unit";
        try {
            JOptionPane.showMessageDialog(this,
                    message, "Help Window", JOptionPane.INFORMATION_MESSAGE,
                    IconUtils.getInstance().getIcon("cassandra.jpeg", 0.33));
        }
        catch (DevFailed e) {
            System.err.println(e.errors[0].desc);
            //  Display without icon
            JOptionPane.showMessageDialog(this, message, "Help Window", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void compactionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compactionsItemActionPerformed
        displayCompactionChartDialog(null);
    }//GEN-LAST:event_compactionsItemActionPerformed
    //=======================================================
    //=======================================================
    void displayCompactionChartDialog(CassandraNode cassandraNode) {
        if (compactionChartDialog == null)
            compactionChartDialog = new CompactionChartDialog(this, dataCenterList);
        if (cassandraNode!=null)
            CompactionChartDialog.setSelection(cassandraNode);
        compactionChartDialog.setVisible(true);
    }
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void releaseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseMenuItemActionPerformed
        new PopupHtml(this).show(ReleaseNote.str);
    }//GEN-LAST:event_releaseMenuItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void requestTrendItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestTrendItemActionPerformed
        requestTrendDialog.setVisible(true);
    }//GEN-LAST:event_requestTrendItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void overviewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewItemActionPerformed
        JTabbedPane tabbedPane = new JTabbedPane();
        int i=0;
        for (DataCenter dataCenter : dataCenterList) {
            tabbedPane.add(dataCenter.getTableScrollPane());
            tabbedPane.setTitleAt(i, dataCenter.getName());
            i++;
        }
        JDialog dialog = buildDialog(this, true, null, tabbedPane);
        ATKGraphicsUtils.centerDialog(dialog);
        dialog.setVisible(true);
    }//GEN-LAST:event_overviewItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void tableSizeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableSizeItemActionPerformed
        try {
            if (hdbTableSizeDialog!=null) {
                hdbTableSizeDialog.doClose();
            }
            //  display dialog
            hdbTableSizeDialog = new HdbTableInformationDialog(this,
                    dataCenterList, HdbTableInformationDialog.HDB_TABLE_SIZE);
            hdbTableSizeDialog.setVisible(true);
        }
        catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_tableSizeItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void ssTableNumberItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ssTableNumberItemActionPerformed
        // TODO add your handling code here:
        try {
            if (hdbSsTableDialog!=null) {
                hdbSsTableDialog.doClose();
            }
            //  display dialog
            hdbSsTableDialog = new HdbTableInformationDialog(this,
                    dataCenterList, HdbTableInformationDialog.HDB_SS_TABLE_NUMBER);
            hdbSsTableDialog.setVisible(true);
        }
        catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_ssTableNumberItemActionPerformed
	//=======================================================
	//=======================================================
    private void doClose() {
        System.exit(0);
    }
    //=======================================================
    //=======================================================



    //=======================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //=======================================================


    //=======================================================
    /**
     * @param args the command line arguments
     */
	//=======================================================
    public static void main(String args[]) {
		try {
      		new CassandraMonitoring().setVisible(true);
		}
		catch(DevFailed e) {
            SplashUtils.getInstance().stopSplash();
            ErrorPane.showErrorMessage(new Frame(), null, e);
			System.exit(0);
		}
    }
    //===============================================================
    /**
     *  Build a simple dialog with component at center position
     * @param parent dialog parent
     * @param modal  true if dialog must be modal
     * @param title  dialog title
     * @param component component to be added at center
     * @return the dialog
     */
    //===============================================================
    public static JDialog buildDialog(JFrame parent, boolean modal, String title, Component component) {
        final JDialog dialog = new JDialog(parent, modal);
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(component, BorderLayout.CENTER);
        JButton button = new JButton("Dismiss");
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        JPanel panel = new JPanel();
        panel.add(button);
        dialogPanel.add(panel, BorderLayout.SOUTH);
        if (title!=null) {
            panel = new JPanel();
            panel.add(new JLabel(title));
            dialogPanel.add(panel, BorderLayout.NORTH);
        }
        dialog.setContentPane(dialogPanel);
        dialog.pack();
        return dialog;
    }

    //===============================================================
    //===============================================================
}
