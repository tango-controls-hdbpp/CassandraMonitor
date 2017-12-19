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

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoApi.events.ITangoPipeListener;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoApi.events.TangoPipeEvent;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.core.AttributeList;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.IDevStateScalar;
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.widget.attribute.SimpleScalarViewer;
import fr.esrf.tangoatk.widget.attribute.StateViewer;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static org.tango.cassandra_monitor_client.tools.IConstants.BACKGROUND;


/**
 * This class is able to model a cassandra node
 *
 * @author verdier
 */

public class CassandraNode extends DeviceProxy {
    private String name;
    private String dataCenter;
    private String rackName;
    private String tokens;
    private String owns;
    private String cluster;
    private String version;
    private List<Compaction> compactionList = new ArrayList<>();
    private StateViewer stateViewer;
    private SimpleScalarViewer dataLoadViewer;
    private AttributeList attributeList = new AttributeList();
    private JRadioButton compactionButton;
    private JButton testButton;
    private CompactionChart compactionChart;
    private boolean selected;

    private static final String pipeName = "Compactions";
    public static final int COMPACTION = 0;
    public static final int VALIDATION = 1;
    //===============================================================
    //===============================================================
    public CassandraNode(String deviceName) throws DevFailed {
        super(deviceName);
        DbDatum datum = get_property("node");
        if (datum.is_empty())
            name = "? ?";
        else
            name = datum.extractString();
        initializeFromDevice();
        buildStateViewer(deviceName);
        buildDataLoadViewer(deviceName);
        initialize();
        compactionChart = new CompactionChart(this);
        compactionButton = new JRadioButton("");
        compactionButton.setEnabled(false);
        compactionButton.setBackground(BACKGROUND);
        compactionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                compactionActionPerformed(evt);
            }
        });
        testButton = new JButton("...");
        testButton.setToolTipText("Test device");
        testButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                testActionPerformed();
            }
        });

        //  Subscribe to pipe event
        TangoEventsAdapter adapter = new TangoEventsAdapter(this);
        PipeEventListener pipeListener = new PipeEventListener();
        adapter.addTangoPipeListener(pipeListener, pipeName, TangoConst.NOT_STATELESS);

    }
    //===============================================================
    //===============================================================
    private void initializeFromDevice() throws DevFailed {
        DeviceAttribute[] attributes = read_attribute(new String[] {
                "DataCenter", "Rack", "Owns", "Tokens" });
        int i=0;
        dataCenter = attributes[i++].extractString();
        rackName   = attributes[i++].extractString();
        owns       = attributes[i++].extractString();
        tokens     = attributes[i].extractString();
    }
    //===============================================================
    //===============================================================
    public boolean isSelected() {
        return selected;
    }
    //===============================================================
    //===============================================================
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    //===============================================================
    //===============================================================
    private void testActionPerformed() {
        try {
            //	Start Test Device panel  on selected device
            JDialog d = new JDialog(new JFrame(), false);
            d.setTitle(devname + " Device Panel");
            d.setContentPane(new jive.ExecDev(devname));
            ATKGraphicsUtils.centerDialog(d);
            d.setVisible(true);
        }
        catch(DevFailed e) {
            ErrorPane.showErrorMessage(new JFrame(), devname, e);
        }
    }
    //===============================================================
    //===============================================================
    private void compactionActionPerformed(ActionEvent event) {
        //  Cancel action
        JRadioButton btn = (JRadioButton) event.getSource();
        btn.setSelected(!btn.isSelected());
    }
    //===============================================================
    //===============================================================
    public JButton getTestButton() {
        return testButton;
    }
    //===============================================================
    //===============================================================
    public String getDataCenter() {
        return dataCenter;
    }
    //===============================================================
    //===============================================================
    public String getName() {
        return name;
    }
    //===============================================================
    //===============================================================
    public JRadioButton getCompactionButton() {
        return compactionButton;
    }
    //===============================================================
    //===============================================================
    public StateViewer getStateViewer() {
        return stateViewer;
    }
    //===============================================================
    //===============================================================
    public SimpleScalarViewer getDataLoadViewer() {
        return dataLoadViewer;
    }
    //===============================================================
    //===============================================================
    public CompactionChart getCompactionChart() {
        return compactionChart;
    }
    //===============================================================
    //===============================================================
    public List<Compaction> getCompactionList() {
        return compactionList;
    }
    //===============================================================
    //===============================================================
    public String getRackName() {
        return rackName;
    }
    //===============================================================
    //===============================================================
    public String getTokens() {
        return tokens;
    }
    //===============================================================
    //===============================================================
    public String getOwns() {
        return owns;
    }
    //===============================================================
    //===============================================================
    private void buildStateViewer(String deviceName) throws DevFailed {
        try {
            //  Add a state viewer
            stateViewer = new StateViewer();
            stateViewer.setLabel("");
            stateViewer.setBackground(BACKGROUND);
            IDevStateScalar attState =
                    (IDevStateScalar) attributeList.add(deviceName + "/state");
            stateViewer.setModel(attState);
            //attState.addDevStateScalarListener(this);
            attributeList.refresh();
        }
        catch (ConnectionException e) {
            Except.throw_exception("ConnectionFailed", e.getDescription());
        }
    }
    //===========================================================
    //===========================================================
    public void buildDataLoadViewer(String deviceName) throws DevFailed {
        try {
            dataLoadViewer = new SimpleScalarViewer();
            IStringScalar attLoadFile =
                    (IStringScalar) attributeList.add(deviceName + "/DataLoadStr");
            dataLoadViewer.setModel(attLoadFile);
            dataLoadViewer.setBackgroundColor(Color.white);
            dataLoadViewer.setBackground(Color.white);
        }
        catch (ConnectionException e) {
            Except.throw_exception("ConnectionFailed", e.getDescription());
        }
    }
    //===============================================================
    //===============================================================
    private void buildCompactions(PipeBlob pipeBlob) {
        //  Compaction modified -> update display
        compactionList.clear();
        if (pipeBlob.getName().equals("Compactions")) {
            compactionButton.setSelected(true);
            for (PipeDataElement dataElement : pipeBlob) {
                compactionList.add(new Compaction(dataElement));
            }
        }
        else {
            compactionButton.setSelected(false);
        }

        //  Update chart
        if (compactionChart!=null)
            compactionChart.updateCurves();
    }
    //===============================================================
    //===============================================================
    public void startSimulation() throws DevFailed {
        command_inout("StartSimulation");
    }
    //===============================================================
    //===============================================================
    public String getStatus() {
        try {
            return status();
        }
        catch (DevFailed e) {
            return e.errors[0].desc;
        }
    }
    //===============================================================
    //===============================================================
    public String getHtmlStatus() throws DevFailed {
        DeviceAttribute[] deviceAttributes = read_attribute(
                new String[]{"ClusterName", "Status", "CassandraVersion", "DataLoadStr", "UnreachableNodes" });
        String[] unreachableList = deviceAttributes[4].extractStringArray();

        StringBuilder unreachableStr = new StringBuilder();
        for (String unreachable : unreachableList)
            unreachableStr.append(unreachable).append("<br>");

        return "<tr>\n<td><b>" + name + "</b></td><td>" +
                deviceAttributes[0].extractString()  + "</td><td>" +
                deviceAttributes[1].extractString()  + "</td><td>" +
                deviceAttributes[2].extractString()  + "</td><td>" +
                deviceAttributes[3].extractString()  + "</td><td>" +
                unreachableStr  + "</td>\n</tr>\n";
    }
    //===============================================================
    //===============================================================
    public String getCluster() {
        initialize();
        if (cluster==null)
            return "? ?";
        return cluster;
    }
    //===============================================================
    //===============================================================
    public String getVersion() {
        if (version==null)
            return "? ?";
        return version;
    }
    //===============================================================
    //===============================================================
    private void initialize() {
        if (cluster==null || version==null) {
            try {
                DeviceAttribute[] attributes = read_attribute(
                        new String[]{"ClusterName", "CassandraVersion"});
                cluster = attributes[0].extractString();
                version = attributes[1].extractString();
            } catch (DevFailed e) {
                System.err.println(e.errors[0]);
            }
        }
    }
    //===============================================================
    //===============================================================







    //=========================================================================
    /**
     * Compaction class
     */
    //=========================================================================
    class Compaction {
        String tableName;
        String taskName;
        int    taskType = COMPACTION;
        long   total = -1;
        long   completed = -1;
        double ratio = 0.0;
        String totalStr;
        String ratioStr;
        //=================================================================
        private Compaction(PipeDataElement pipeDataElement) {
            tableName = pipeDataElement.getName();

            PipeBlob pipeBlob = pipeDataElement.extractPipeBlob();
            taskName = pipeBlob.getName();
            if (taskName.equalsIgnoreCase("validation"))
                taskType = VALIDATION;

            for (PipeDataElement dataElement : pipeBlob) {
                String str = dataElement.getName();
                switch (str) {
                    case "total":
                        total = dataElement.extractLong64Array()[0];
                        if (total>1.e9)
                            totalStr = String.format("%.3f", 1.0e-9*total) +" Gbytes";
                        else
                            totalStr = String.format("%.3f", 1.0e-6*total) +" Mbytes";
                        break;
                    case "completed":
                        completed = dataElement.extractLong64Array()[0];
                        break;
                    case "ratio":
                        ratio = dataElement.extractDoubleArray()[0];
                        ratioStr = String.format("%.3f", ratio*100) +" %";
                        break;
                }
            }
        }
        //=================================================================
        public String toString() {
            return tableName + "(" + taskName + "): " +
                    completed + "/" + total + " bytes\t"+ ratioStr;
        }
        //=================================================================
    }
    //=========================================================================
    //=========================================================================





    //=========================================================================
    /**
     * Pipe event listener
     */
    //=========================================================================
    public class PipeEventListener implements ITangoPipeListener {
        //=================================================================
        public void pipe(TangoPipeEvent event) {

            try {
                //	Get the attribute value
                DevicePipe devicePipe = event.getValue();
                buildCompactions(devicePipe.getPipeBlob());
            } catch (DevFailed e) {
                Except.print_exception(e);
                //System.err.println(e.errors[0].desc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //=====================================================================
    //=====================================================================
}
