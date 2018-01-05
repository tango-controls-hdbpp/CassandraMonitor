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

package org.tango.cassandramonitor;

import fr.esrf.Tango.DevFailed;
import org.tango.cassandradistribution.HdbTable;
import org.tango.utils.DevFailedUtils;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;

import static org.tango.cassandramonitor.IConstants.COLUMN_FAMILIES;
import static org.tango.cassandramonitor.IConstants.JMX_SERVICES;


/**
 * This class is able to
 *
 * @author verdier
 */

public class JmxUtilities {
    private JMXConnector connector;
    private MBeanServerConnection connection;
    private String connectionError = null;
    private String node;
    private short port;
    private String user;
    private String password;
    private int timeout;
    private ConnectionThread connectionThread = null;
    private List<ObjectName> objectNameList = new ArrayList<>();
    private List<HdbTable> hdbTableList = new ArrayList<>();
    //===============================================================
    //===============================================================
    public JmxUtilities(String node, short port, String user, String password, int timeout) throws DevFailed {
        this.node = node;
        this.port = port;
        this.user = user;
        this.password = password;
        this.timeout = timeout;
        connect();
        initObjectNameList();
    }
    //===============================================================
    //===============================================================
    private void initObjectNameList() throws DevFailed {
        for (String jmxService : JMX_SERVICES) {
            try {
                objectNameList.add(new ObjectName(jmxService));
            } catch (MalformedObjectNameException e) {
                throw DevFailedUtils.newDevFailed("MalformedObjectNameException", e.toString());
            }
        }
    }
    //===============================================================
    //===============================================================
    public boolean connectionFailed() {
        return connectionError!=null;
    }
    //===============================================================
    //===============================================================
    public String getConnectionError() {
        return connectionError;
    }
    //===============================================================
    //===============================================================
    public void close() {
        if (connectionError==null) {
            try {
                connector.close();
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }
        }
    }
    //===============================================================
    //===============================================================
    private void connect() throws DevFailed {
        //  Check if a ConnectionThread already running
        if (connectionThread!=null) {
            if (connectionThread.running) {
                connectionError = "JMX connection timeout";
                throw DevFailedUtils.newDevFailed("ConnectionError", connectionError);
            }
            if (connectionThread.exception!=null) {
                //  Get exception and reset it for next loop
                connectionError = connectionThread.exception.getMessage();
                connectionThread.exception = null;
                throw DevFailedUtils.newDevFailed("ConnectionError", connectionError);
            }
        }
        //  Start a new connection thread
        connectionThread = new ConnectionThread();
        connectionThread.start();
        try {
            //  And wait with timeout
            connectionThread.join(timeout*1000);
        }
        catch (InterruptedException e){
            connectionError = e.toString();
            throw DevFailedUtils.newDevFailed("ConnectionError", e.toString());
        }
        //  Check if thread has connected
        if (connectionThread.running) {
            connectionError = "JMX connection timeout";
            throw DevFailedUtils.newDevFailed("ConnectionError", connectionError);
        }
        if (connectionThread.exception!=null) {
            connectionError = connectionThread.exception.getMessage();
            System.err.println(connectionError);
            throw DevFailedUtils.newDevFailed("ConnectionError", connectionError);
        }
    }
    //===============================================================
    //===============================================================
    private void initHdbTableList() throws DevFailed {
        if (connectionError!=null) {
            connect();
        }
        try {
            Set<ObjectName> objectNames = connection.queryNames(objectNameList.get(COLUMN_FAMILIES), null);
            for (ObjectName objectName : objectNames) {
                String name = objectName.toString();
                int idx = name.lastIndexOf("=");
                if (idx>0)
                    hdbTableList.add(new HdbTable(name.substring(++idx)));
            }
        } catch (IOException e) {
            connectionError = e.toString();
            throw DevFailedUtils.newDevFailed(e.toString(), e.getMessage());
        }
    }
    //===============================================================
    //===============================================================
    public Object getAttribute(int objectIndex, String jmxAttributeName) throws DevFailed {
        return getAttribute(objectNameList.get(objectIndex), jmxAttributeName);
    }
    //===============================================================
    //===============================================================
    public Object getAttribute(ObjectName objectName, String jmxAttributeName) throws DevFailed {
        if (connectionError!=null) {
            connect();
        }
        try {
            return connection.getAttribute(objectName, jmxAttributeName);
        } catch (AttributeNotFoundException | InstanceNotFoundException |
                ReflectionException | MBeanException | IOException e) {
            connectionError = e.toString();
            throw DevFailedUtils.newDevFailed(e.toString(), e.getMessage());
        }
    }
    //===============================================================
    //===============================================================
    public List<HdbTable> getTableSizes()throws DevFailed {
        if (hdbTableList.isEmpty())
            initHdbTableList();

        for (HdbTable hdbTable : hdbTableList) {
            long size = (long)getAttribute(hdbTable.getReadSizeObjectName(), "Count");
            hdbTable.setSize((double) size/1.e6); // to Mb
        }
        return hdbTableList;
    }
    //===============================================================
    //===============================================================



    //===============================================================
    /*
     * A thread to manage timeout on JMX connection
     */
    //===============================================================
    private class ConnectionThread extends Thread {
        IOException exception = null;
        boolean running = true;
        public void run() {
            String jmxUrl = "service:jmx:rmi:///jndi/rmi://" + node + ":" + port + "/jmxrmi";
            Map<String, String[]> map = new HashMap<>();
            map.put(JMXConnector.CREDENTIALS, new String[]{ user, password });

            try {
                connector = JMXConnectorFactory.connect(new JMXServiceURL(jmxUrl), map);
                connection = connector.getMBeanServerConnection();
                connectionError = null;
            } catch (IOException e) {
                connectionError = e.toString();
                exception = e;
            }
            running = false;
        }
    }
    //===============================================================
    //===============================================================
}
