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

package org.tango.cassandradistribution;

import fr.esrf.Tango.DevFailed;
import org.tango.utils.DevFailedUtils;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


/**
 * This class defines a HDB table
 *
 * @author verdier
 */

public class HdbTable {
    private String name;
    private ObjectName readSizeObjectName;
    private ObjectName ssTableObjectName;
    private double size;
    private int ssTableCount;

    private static final String metricsHeader = "org.apache.cassandra.metrics:type=ColumnFamily,keyspace=hdb,scope=";
    private static final String sizeEnd = ",name=LiveDiskSpaceUsed";
    private static final String ssTableEnd = ",name=LiveSSTableCount";
    //===============================================================
    //===============================================================
    public HdbTable(String tableName) throws DevFailed {
        this.name = tableName;
        try {
            readSizeObjectName = new ObjectName(metricsHeader + name + sizeEnd);
            ssTableObjectName = new ObjectName(metricsHeader + name + ssTableEnd);
        }catch (MalformedObjectNameException e) {
            throw DevFailedUtils.newDevFailed(e.toString(), e.getMessage());
        }
    }
    //===============================================================
    //===============================================================
    public String getName() {
        return name;
    }
    //===============================================================
    //===============================================================
    public ObjectName getSsTableObjectName() {
        return ssTableObjectName;
    }
    //===============================================================
    //===============================================================
    public ObjectName getReadSizeObjectName() {
        return readSizeObjectName;
    }
    //===============================================================
    //===============================================================
    public double getSize() {
        return size;
    }
    //===============================================================
    //===============================================================
    public void setSize(double size) {
        this.size = size;
    }
    //===============================================================
    //===============================================================
    public int getSsTableCount() {
        return ssTableCount;
    }
    //===============================================================
    //===============================================================
    public void setSsTableCount(int ssTableCount) {
        this.ssTableCount = ssTableCount;
    }
    //===============================================================
    //===============================================================
}
