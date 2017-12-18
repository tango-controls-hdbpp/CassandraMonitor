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
import fr.esrf.TangoApi.DbServer;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.Except;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import org.tango.cassandra_monitor_client.tools.PopupHtml;

import javax.swing.*;
import java.awt.*;
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
    private Map<String, DataCenter> dataCenterMap = new HashMap<>();
	//=======================================================
    /**
	 *	Creates new form CassandraMonitoring
	 */
	//=======================================================
    public CassandraMonitoring() throws DevFailed {
        initComponents();
        buildDeviceList();
        buildDataCenterPanels();
        compactionChartDialog = new CompactionChartDialog(this, dataCenterList);
        pack();
        ATKGraphicsUtils.centerFrameOnScreen(this);
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
    private void buildDistribution(String line) throws DevFailed {
        int idx = line.indexOf(':');
        if (idx>0) {
            String nodeName = line.substring(0, idx);
            StringTokenizer stk = new StringTokenizer(line.substring(idx+1), ",");
            if (stk.countTokens()!=4)
                Except.throw_exception("SyntaxError", "Syntax error in DistributionDevice attribute");
            String dataCenterName = stk.nextToken();
            String rackName  = stk.nextToken();
            String owns = stk.nextToken();
            String tokens = stk.nextToken();

            DataCenter dataCenter = dataCenterMap.get(dataCenterName);
            if (dataCenter==null) {
                dataCenter = new DataCenter(dataCenterName);
                dataCenterList.add(dataCenter);
                dataCenterMap.put(dataCenterName, dataCenter);
            }
            CassandraNode cassandraNode = getCassandraNode(nodeName);
            if (cassandraNode!=null) {
                cassandraNode.setDistributionInfo(rackName, owns, tokens);
                dataCenter.add(cassandraNode);
            }
        }
    }
	//=======================================================
	//=======================================================
    private void buildDistribution() throws DevFailed {
        String deviceName = System.getenv("DistributionDevice");
        if (deviceName==null)
            Except.throw_exception("PropertyNotDefined", "DistributionDevice not defined");
        DeviceAttribute attribute = new DeviceProxy(deviceName).read_attribute("NodeDistribution");
        String[] hostDistribution = attribute.extractStringArray();
        for (String line : hostDistribution)
            buildDistribution(line);
    }
	//=======================================================
	//=======================================================
    private CassandraNode getCassandraNode(String name) {
        for (CassandraNode cassandraNode : cassandraNodeList)
            if (cassandraNode.getName().equals(name))
                return cassandraNode;
        return null;
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
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem startSimulationItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
        javax.swing.JMenu viewMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem viewStatusItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem compactionsItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem jMenuItem1 = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 18));
        jLabel1.setText("Cassandra Monitoring Client");
        topPanel.add(jLabel1);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        startSimulationItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        startSimulationItem.setMnemonic('S');
        startSimulationItem.setText("Start Simulation");
        startSimulationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationItemActionPerformed(evt);
            }
        });
        fileMenu.add(startSimulationItem);

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

        viewStatusItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        viewStatusItem.setMnemonic('S');
        viewStatusItem.setText("Status");
        viewStatusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStatusItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewStatusItem);

        compactionsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        compactionsItem.setMnemonic('C');
        compactionsItem.setText("Compactions");
        compactionsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compactionsItemActionPerformed(evt);
            }
        });
        viewMenu.add(compactionsItem);

        menuBar.add(viewMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("help");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setMnemonic('A');
        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem1);

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
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String  message = "This application is able to bla bla\n" +
                "\nPascal Verdier - Accelerator Control Unit";
        JOptionPane.showMessageDialog(this, message, "Help Window", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void compactionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compactionsItemActionPerformed
        compactionChartDialog.setVisible(true);
    }//GEN-LAST:event_compactionsItemActionPerformed
    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void startSimulationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationItemActionPerformed
        try {
            for (CassandraNode cassandraNode : cassandraNodeList) {
                cassandraNode.startSimulation();
            }
        }
        catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, e.getMessage(), e);
        }
    }//GEN-LAST:event_startSimulationItemActionPerformed

    //=======================================================
    //=======================================================
    @SuppressWarnings("UnusedParameters")
    private void viewStatusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStatusItemActionPerformed
        // TODO add your handling code here:
        StringBuilder   sb = new StringBuilder(PopupHtml.htmlPageTitle("Cassandra Node Status"));
        sb.append("<center><table Border=2 CellSpacing=0>\n");
        sb.append(PopupHtml.htmlTableLine(
                new String[] { "Node", "Cluster", "Status", "Version", "Load", "Unreachable" }, true));
        for (CassandraNode node : cassandraNodeList) {
            try {
                sb.append(node.getHtmlStatus());
            }
            catch (DevFailed e) {
                ErrorPane.showErrorMessage(this, node.getName(), e);
            }
        }
        sb.append("</table>");
        new PopupHtml(this).show(sb.toString(), 640, 480);
    }//GEN-LAST:event_viewStatusItemActionPerformed
	//=======================================================
	//=======================================================
    private void doClose() {
        System.exit(0);
    }
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
            ErrorPane.showErrorMessage(new Frame(), null, e);
			System.exit(0);
		}
    }


	//=======================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	//=======================================================

}
