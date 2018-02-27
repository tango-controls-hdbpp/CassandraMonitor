//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jhdbextract/src/org/tango/jhdbextract/commons/HdbTable.java,v $
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
// $Revision: 1.4 $
//
//-======================================================================


package org.tango.hdb_tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This hdb table is a map of the concrete one.
 * It is a list of attributes.
 */

public class HdbTable extends ArrayList<String> {
    private String name;
    //===============================================================
    //===============================================================
    HdbTable(String name) {
        this.name = name;
    }
    //===============================================================``
    //===============================================================
    public String getName() {
        return name;
    }
    //===============================================================
    //===============================================================
    public void sort() {
        Collections.sort(this, new StringComparator());
    }
    //===============================================================
    //===============================================================
    public String toString() {
        return name + ":  " + size() + " attributes";

    }
    //===============================================================
    //===============================================================


    //======================================================
    /**
     * Comparators class to sort collection
     */
    //======================================================
    class StringComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            if (s1 == null)
                return 1;
            else if (s2 == null)
                return -1;
            else
                return s1.compareTo(s2);
        }
    }
}
