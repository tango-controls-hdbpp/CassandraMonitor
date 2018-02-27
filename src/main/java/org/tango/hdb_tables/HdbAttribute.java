//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jhdbextract/src/org/tango/jhdbextract/commons/ArchiveAttribute.java,v $
//
// Project:   Tango
//
// Description:  java source code for Tango tool..
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,
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
// $Revision: 1.6 $
//
//-======================================================================

package org.tango.hdb_tables;

/**
 *  Defines an attribute stored in HDB++.
 *  It is a att_conf table row.
 */
public class HdbAttribute {
    private String name;
    private String tableName;
    private int ttl;
    //===============================================================
    //===============================================================
    public HdbAttribute(String csName, String attName, String tableName, int ttl) {
        this.name   = buildFullAttributeName(csName, attName);
        this.tableName = tableName;
        this.ttl = ttl;
    }
    //===============================================================
    //===============================================================
    public static String buildFullAttributeName(String controlSystemName, String attributeName) {
        return "tango://" + controlSystemName + "/" + attributeName;
    }
    //===============================================================
    //===============================================================
    public int getTtl() {
        return ttl;
    }
    //===============================================================
    //===============================================================
    String getName() {
        return name;
    }
    //===============================================================
    //===============================================================
    String getTableName() {
        return tableName;
    }
    //===============================================================
    //===============================================================
    public String toString() {
        return  ", name= "   + name +
                ", tableName= "  + tableName + ", TTL= " + ttl;
    }
    //===============================================================
    //===============================================================
}
