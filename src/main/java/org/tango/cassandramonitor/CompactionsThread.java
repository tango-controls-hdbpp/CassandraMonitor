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
import fr.esrf.TangoApi.PipeBlob;
import fr.esrf.TangoDs.Except;
import org.tango.server.device.DeviceManager;
import org.tango.server.events.EventManager;
import org.tango.server.pipe.PipeValue;
import org.tango.utils.DevFailedUtils;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.List;

import static org.tango.cassandramonitor.IConstants.ATTR_COMPACTIONS;


/**
 * This class is able to
 *
 * @author verdier
 */

class CompactionsThread extends Thread {
    private PipeValue pipeCompactions;
    private DeviceManager deviceManager;
    private JmxToCompactions jmxToCompactions;
    private JmxUtilities jmxUtilities;
    private ObjectName compactionObjectName;
    private boolean runThreads = true;
    private static final String COMPACTION_NAME = "org.apache.cassandra.db:type=CompactionManager";
    //===============================================================
    //===============================================================
    CompactionsThread(DeviceManager deviceManager,
                          JmxUtilities jmxUtilities,
                          String cassandraNode) throws DevFailed {
        this.deviceManager = deviceManager;
        this.jmxUtilities = jmxUtilities;
        try {
            compactionObjectName = new ObjectName(COMPACTION_NAME);
        } catch (MalformedObjectNameException ex) {
            throw DevFailedUtils.newDevFailed(ex.toString(), ex.toString());

        }
        jmxToCompactions = new JmxToCompactions(cassandraNode);
    }
    //===============================================================
    //===============================================================
    void stopThread() {
        runThreads = false;
    }
    //===============================================================
    //===============================================================
    PipeValue getPipeCompactions() {
        return pipeCompactions;
    }
    //===============================================================
    //===============================================================
    private void pushPipeEvent() throws DevFailed {
        EventManager.getInstance().pushPipeEvent(
                deviceManager.getName(), "Compactions", pipeCompactions);
            }
    //===============================================================
    //===============================================================
    public void run() {
        while (runThreads) {
            try {
                //  Build compaction object list
                Object jmxAtt = jmxUtilities.getAttribute(compactionObjectName, ATTR_COMPACTIONS);
                //noinspection unchecked
                jmxToCompactions.setList((List<HashMap<String, String>>) jmxAtt);
                if (jmxToCompactions.hasChanged()) {
                    PipeBlob pipeBlob = jmxToCompactions.getPipeBlob();
                    //  And sent it to pipe and event
                    pipeCompactions = new PipeValue(pipeBlob);
                    pushPipeEvent();
                    //System.out.println("Compactions  change");
                } else {
                    //  Check if first call
                    if (pipeCompactions ==null)
                        pipeCompactions = new PipeValue(jmxToCompactions.getPipeBlob());
                    //System.out.println("No change");
                }
            } catch (DevFailed e) {
                //  Build pipe with error description and send event
                pipeCompactions = new PipeValue(new PipeBlob(e.errors[0].desc));
                try {
                    pushPipeEvent();
                } catch (DevFailed devFailed) {
                    Except.print_exception(devFailed);
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }
    }
    //===============================================================
    //===============================================================
}
