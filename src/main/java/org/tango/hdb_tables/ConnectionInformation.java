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

package org.tango.hdb_tables;

/**
 * This class contains HDB connection info.
 * (data center, rack, ...)
 *
 * @author verdier
 */

public class ConnectionInformation {
    private String hostName;
    private String dataCenter;
    private String rack;
    private String nbTokens;
    //===============================================================
    //===============================================================
    public ConnectionInformation(String hostName, String dataCenter, String rack, String nbTokens)  {
        this.hostName = hostName;
        this.dataCenter = dataCenter;
        this.rack = rack;
        this.nbTokens = nbTokens;
    }
    //===============================================================
    //===============================================================
    @SuppressWarnings("unused")
    public String getHostName() {
        return hostName;
    }
    //===============================================================
    //===============================================================
    public String getDataCenter() {
        return dataCenter;
    }
    //===============================================================
    //===============================================================
    public String getRack() {
        return rack;
    }
    //===============================================================
    //===============================================================
    public String getNbTokens() {
        return nbTokens;
    }
    //===============================================================
    //===============================================================
}
