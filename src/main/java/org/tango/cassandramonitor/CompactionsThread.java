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
import fr.esrf.TangoApi.PipeDataElement;
import org.tango.server.device.DeviceManager;
import org.tango.server.pipe.PipeValue;

import java.util.HashMap;
import java.util.List;

import static org.tango.cassandramonitor.IConstants.ATTR_COMPACTIONS;
import static org.tango.cassandramonitor.IConstants.COMPACTION_MANAGER;


/**
 * This class is able to check if compaction is active
 * and fill a PipeValue with compaction info.
 *
 * @author verdier
 */

class CompactionsThread extends Thread {
    private PipeValue pipeCompactions;
    private DeviceManager deviceManager;
    private JmxToCompactions jmxToCompactions;
    private JmxUtilities jmxUtilities;
    private String cassandraNode;
    private boolean runThreads = true;
    //===============================================================
    //===============================================================
    CompactionsThread(DeviceManager deviceManager, JmxUtilities jmxUtilities, String cassandraNode) {
        this.deviceManager = deviceManager;
        this.jmxUtilities = jmxUtilities;
        this.cassandraNode = cassandraNode;
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
    void pushPipeEventIfNeeded() throws DevFailed {
        synchronized (monitor) {
            if (changed) {
                deviceManager.pushPipeEvent("Compactions", pipeCompactions);
                changed = false;
            }
        }
    }
    //===============================================================
    //===============================================================


    private final Object monitor = new Object();
    private boolean changed = false;
    //===============================================================
    //===============================================================
    public void run() {
        while (runThreads) {
            synchronized (monitor) {
                try {
                    //  Build compaction object list
                    Object jmxAtt = jmxUtilities.getAttribute(COMPACTION_MANAGER, ATTR_COMPACTIONS);
                    //noinspection unchecked
                    jmxToCompactions.setList((List<HashMap<String, String>>) jmxAtt);
                    if (jmxToCompactions.hasChanged()) {
                        //  And sent it to pipe and event
                        pipeCompactions = new PipeValue(jmxToCompactions.getPipeBlob());
                        changed = true;
                        //pushPipeEvent();
                        //System.out.println("Compactions  change");
                    } else {
                        //  Check if first call without change
                        if (pipeCompactions == null)
                            pipeCompactions = new PipeValue(jmxToCompactions.getPipeBlob());
                        changed = false;
                        //System.out.println("No change");
                    }
                } catch (Exception e) {
                    String errorDescription;
                    if (e instanceof DevFailed)
                        errorDescription = ((DevFailed) e).errors[0].desc;
                    else
                        errorDescription = e.toString();

                    //  Build pipe with error description and send event
                    PipeBlob pipeBlob = new PipeBlob("ERROR");
                    pipeBlob.add(new PipeDataElement(cassandraNode, errorDescription));
                    pipeCompactions = new PipeValue(pipeBlob);
                    changed = true;
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
