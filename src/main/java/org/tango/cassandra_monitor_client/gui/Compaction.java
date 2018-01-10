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

import fr.esrf.TangoApi.PipeBlob;
import fr.esrf.TangoApi.PipeDataElement;

import static org.tango.cassandra_monitor_client.gui.CassandraNode.*;


/**
 * This class defines a Compaction action
 *
 * @author verdier
 */

public class Compaction {
    //===============================================================
    //===============================================================
    private String tableName;
    private String taskName;
    private int    taskType = COMPACTION;
    private long   total = -1;
    private long   completed = -1;
    private double ratio = 0.0;
    private String totalStr;
    private String ratioStr;
    //=================================================================
    //=================================================================
    public Compaction(String errorDescription) {
        taskType = PIPE_ERROR;
        tableName = errorDescription;
        taskName = "Error";
    }
    //=================================================================
    //=================================================================
    public Compaction(PipeDataElement pipeDataElement) {
        tableName = pipeDataElement.getName();

        //  Decode info from pipe
        PipeBlob pipeBlob = pipeDataElement.extractPipeBlob();
        taskName = pipeBlob.getName();
        if (taskName.equalsIgnoreCase("validation"))
            taskType = VALIDATION;
        else
        if (taskName.equalsIgnoreCase("cleanup"))
            taskType = CLEANUP;

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
    //=================================================================
    public String getTableName() {
        return tableName;
    }
    //=================================================================
    //=================================================================
    public String getTaskName() {
        return taskName;
    }
    //=================================================================
    //=================================================================
    public int getTaskType() {
        return taskType;
    }
    //=================================================================
    //=================================================================
    public String getTotalStr() {
        return totalStr;
    }
    //=================================================================
    //=================================================================
    public double getRatio() {
        return ratio;
    }
    //=================================================================
    //=================================================================
    public String getRatioStr() {
        return ratioStr;
    }
    //=================================================================
    //=================================================================
    public String toString() {
        return tableName + "(" + taskName + "): " +
                completed + "/" + total + " bytes\t"+ ratioStr;
    }
    //=================================================================
    //=================================================================
}
