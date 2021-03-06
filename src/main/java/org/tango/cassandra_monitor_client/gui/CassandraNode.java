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
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoApi.events.ITangoPipeListener;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoApi.events.TangoPipeEvent;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.attribute.SimpleScalarViewer;
import fr.esrf.tangoatk.widget.attribute.StateViewer;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorHistory;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import org.tango.cassandra_monitor_client.tools.IconUtils;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.tango.cassandra_monitor_client.gui.CompactionDialog.ERROR_COLOR;
import static org.tango.cassandra_monitor_client.gui.DataCenter.BACKGROUND;
import static org.tango.cassandramonitor.IConstants.READ;
import static org.tango.cassandramonitor.IConstants.WRITE;
import static org.tango.cassandramonitor.IConstants.refreshMonitor;


/**
 * This class is able to model a cassandra node
 *
 * @author verdier
 */

public class CassandraNode extends DeviceProxy {
    private String name;
    private String deviceName;
    private String dataCenter;
    private String rackName;
    private String tokens;
    private String cluster;
    private String version;
    private String ipAddress;
    private List<Compaction> compactionList = new ArrayList<>();
    private StateViewer stateViewer;
    private SimpleScalarViewer dataLoadViewer;
    private SimpleScalarViewer[] requestViewers;
    private SimpleScalarViewer pendingViewer;
    private SimpleScalarViewer ssTableViewer;
    private JLabel compactionLabel;
    private CompactionChart compactionChart;

    private static final String pipeName = "Compactions";
    public static final int PIPE_ERROR = 0;
    public static final int COMPACTION = 1;
    public static final int VALIDATION = 2;
    public static final int CLEANUP = 3;
    private static AttributeList attributeList = null;
    private static final ErrorHistory errorHistory = new ErrorHistory();
    //===============================================================
    //===============================================================
    public CassandraNode(String deviceName) throws DevFailed {
        super(deviceName);
        this.deviceName = deviceName;
        DbDatum datum = get_property("node");
        if (datum.is_empty())
            name = "? ?";
        else
            name = datum.extractString();

        //  Read static info from node
        initializeFromDevice();

        //  Initialize attribute list and error history
        if (attributeList==null) {
            attributeList = new AttributeList();
            attributeList.addErrorListener(errorHistory);
            attributeList.addSetErrorListener(errorHistory);
        }

        //  Build viewers
        buildStateViewer(deviceName);
        buildDataLoadViewer(deviceName);
        buildScalarViewers(deviceName);
        initialize();
        compactionChart = new CompactionChart(this);

        compactionLabel = new JLabel("");
        compactionLabel.setBackground(BACKGROUND);
        compactionLabel.setToolTipText("Compactions/validations/cleanup/...");
        compactionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            InetAddress inetAddress = InetAddress.getByName(name);
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        //  Subscribe to pipe event
        TangoEventsAdapter adapter = new TangoEventsAdapter(this);
        PipeEventListener pipeListener = new PipeEventListener();
        adapter.addTangoPipeListener(pipeListener, pipeName, TangoConst.NOT_STATELESS);
    }
    //===============================================================
    //===============================================================
    private void initializeFromDevice() throws DevFailed {
        DeviceAttribute[] attributes = read_attribute(new String[] {
                "DataCenter", "Rack", "Tokens" });
        int i=0;
        dataCenter = attributes[i++].extractString();
        rackName   = attributes[i++].extractString();
        tokens     = attributes[i].extractString();
    }
    //===============================================================
    //===============================================================
    public void testDevice() {
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
    public String[] displayCompactionHistory() throws DevFailed {
        return this.command_inout("ReadCompactionHistory").extractStringArray();
    }
    //===============================================================
    //===============================================================
    public String getDataCenter() {
        return dataCenter;
    }
    //===============================================================
    //===============================================================
    public String getDeviceName() {
        return deviceName;
    }
    //===============================================================
    //===============================================================
    public String getName() {
        return name;
    }
    //===============================================================
    //===============================================================
    public JLabel getCompactionLabel() {
        return compactionLabel;
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
    public SimpleScalarViewer getPendingViewer() {
        return pendingViewer;
    }
    //===============================================================
    //===============================================================
    public SimpleScalarViewer getSsTableViewer() {
        return ssTableViewer;
    }
    //===============================================================
    //===============================================================
    public SimpleScalarViewer[] getRequestViewers() {
        return requestViewers;
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
    public String getTpAddress() {
        return ipAddress;
    }
    //===============================================================
    //===============================================================
    public String getTokens() {
        return tokens;
    }
    //===============================================================
    //===============================================================
    public static ErrorHistory getErrorHistory() {
        return errorHistory;
    }
    //===============================================================
    //===============================================================
    private void buildStateViewer(String deviceName) throws DevFailed {
        try {
            //  Add a state viewer
            stateViewer = new StateViewer();
            stateViewer.setLabel("");
            stateViewer.setBackground(BACKGROUND);
            IDevStateScalar stateScalar =
                    (IDevStateScalar) attributeList.add(deviceName + "/state");
            stateViewer.setModel(stateScalar);
            errorHistory.add(stateViewer);
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
            dataLoadViewer.setBackgroundColor(Color.white);
            dataLoadViewer.setBackground(Color.white);
            dataLoadViewer.setToolTipText(" Data  Load ");
            dataLoadViewer.setModel((IStringScalar) attributeList.add(deviceName + "/DataLoadStr"));
            errorHistory.add(dataLoadViewer);
        }
        catch (ConnectionException e) {
            Except.throw_exception("ConnectionFailed", e.getDescription());
        }
    }
    //===========================================================
    //===========================================================
    public void buildScalarViewers(String deviceName) throws DevFailed {
        try {
            requestViewers = new SimpleScalarViewer[2];
            requestViewers[READ] = new SimpleScalarViewer();
            requestViewers[READ].setBackgroundColor(Color.white);
            requestViewers[READ].setBackground(Color.white);
            requestViewers[READ].setToolTipText(" Read Client Requests ");
            requestViewers[READ].setModel((INumberScalar) attributeList.add(deviceName + "/ReadRequests"));

            requestViewers[WRITE] = new SimpleScalarViewer();
            requestViewers[WRITE].setBackgroundColor(Color.white);
            requestViewers[WRITE].setBackground(Color.white);
            requestViewers[WRITE].setToolTipText(" Write Client Requests ");
            requestViewers[WRITE].setModel((INumberScalar) attributeList.add(deviceName + "/WriteRequests"));

            pendingViewer = new SimpleScalarViewer();
            pendingViewer.setBackgroundColor(Color.white);
            pendingViewer.setBackground(Color.white);
            pendingViewer.setToolTipText(" Pending Compaction Tasks ");
            pendingViewer.setUnitVisible(false);
            pendingViewer.setModel((INumberScalar) attributeList.add(deviceName + "/PendingCompactionTasks"));

            ssTableViewer = new SimpleScalarViewer();
            ssTableViewer.setBackgroundColor(Color.white);
            ssTableViewer.setBackground(Color.white);
            ssTableViewer.setToolTipText(" SS Table Number ");
            ssTableViewer.setUnitVisible(false);
            ssTableViewer.setModel((INumberScalar) attributeList.add(deviceName + "/SsTableNumber"));

            errorHistory.add(requestViewers[READ]);
            errorHistory.add(requestViewers[WRITE]);
            errorHistory.add(pendingViewer);
            errorHistory.add(ssTableViewer);
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
        ImageIcon icon = null;
        compactionLabel.setToolTipText(null);
        String type = pipeBlob.getName();
        synchronized (refreshMonitor) {
            if (type.equals("Compactions")) {
                icon = IconUtils.getProgressIcon();
                for (PipeDataElement dataElement : pipeBlob) {
                    compactionList.add(new Compaction(dataElement));
                }
            } else if (type.equals("ERROR")) {
                icon = IconUtils.getRedBall();
                compactionChart.setBackground(ERROR_COLOR);
                PipeDataElement dataElement = pipeBlob.get(0);
                String errorDescription = dataElement.extractStringArray()[0];
                compactionLabel.setToolTipText(errorDescription);
                compactionList.add(new Compaction(errorDescription));
            }
        }
        compactionLabel.setIcon(icon);

        //  Update chart
        if (compactionChart!=null)
            compactionChart.updateCurves();
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
    public DevVarDoubleStringArray readHdbTableSizes() throws DevFailed {
        return command_inout("ReadHdbTableSizes").extractDoubleStringArray();
    }
    //===============================================================
    //===============================================================
    public String[] readSsTableNumbers() throws DevFailed {
        return read_attribute("ssTableNumberList").extractStringArray();
    }
    //===============================================================
    //===============================================================




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
