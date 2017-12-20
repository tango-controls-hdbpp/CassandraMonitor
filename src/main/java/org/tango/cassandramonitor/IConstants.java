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


/**
 * This class is able to
 *
 * @author verdier
 */

public interface IConstants {

    int READ = 0;
    int WRITE = 1;

    //  JMX services
    int STORAGE_SERVICE = 0;
    int STORAGE_LOAD = 1;
    int WRITE_REQUESTS = 2;
    int READ_REQUESTS = 3;
    String[] JMX_SERVICES = {
            "org.apache.cassandra.db:type=StorageService",
            "org.apache.cassandra.metrics:type=Storage,name=Load",
            "org.apache.cassandra.metrics:type=ClientRequest,scope=Write,name=Latency",
            "org.apache.cassandra.metrics:type=ClientRequest,scope=Read,name=Latency",
    };



    //  JMX attribute names (org.apache.cassandra.db:type=StorageService)
    String ATTR_LOAD = "LoadString";
    String ATTR_OPERATION_MODE = "OperationMode";
    String ATTR_RELEASE = "ReleaseVersion";
    String ATTR_CLUSTER = "ClusterName";
    String ATTR_UNREACHABLE = "UnreachableNodes";
    String ATTR_LIVE_MODE = "LiveNodes";
    String ATTR_COMPACTIONS = "Compactions";


    //  JMX attribute names (org.apache.cassandra.metrics)
    String ATTR_COUNT = "Count";
    String ATTR_RATE = "OneMinuteRate";
}
