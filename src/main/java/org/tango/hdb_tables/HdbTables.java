//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jhdbextract/src/org/tango/jhdbextract/commons/HdbTables.java,v $
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
// $Revision: 1.5 $
//
//-======================================================================


package org.tango.hdb_tables;

import java.util.*;

public class HdbTables extends ArrayList<HdbTable> {
    private int attributeCount = 0;
    private int sortCriteria = NB_ATTRIBUTES;
    private static final int ALPHABETIC = 0;
    private static final int NB_ATTRIBUTES = 1;
    //===============================================================
    //===============================================================
    HdbTables(Hashtable<String, HdbAttribute> attributeMap) {

        //  For each attribute
        List<HdbTable> hdbTableList = new ArrayList<>();
        Enumeration attributes = attributeMap.keys();
        while (attributes.hasMoreElements()) {
            String attributeName = (String) attributes.nextElement();
            HdbAttribute hdbAttribute = attributeMap.get(attributeName);
            String type = hdbAttribute.getTableName();

            //  Check if already created
            HdbTable hdbTable = null;
            for (HdbTable table : hdbTableList) {
                if (table.getName().equals(type)) {
                    hdbTable = table;
                }
            }
            if (hdbTable==null) {
                hdbTable = new HdbTable(type);
                hdbTableList.add(hdbTable);
            }
            hdbTable.add(attributeName);
            attributeCount++;
        }
        //  Sort this table
        Collections.sort(hdbTableList, new TableComparator());

        //  For each table, sort attributes
        for (HdbTable table : hdbTableList) {
            table.sort();
        }
        addAll(hdbTableList);
    }
    //===============================================================
    //===============================================================
    public void sortTables(int criteria) {
        sortCriteria = criteria;
        Collections.sort(this, new TableComparator());
    }
    //===============================================================
    //===============================================================
    public int getAttributeCount() {
        return attributeCount;
    }
    //===============================================================
    //===============================================================
    public List<String> checkAttributeInMultipleTables() {
        long t0 = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        for (int i=0 ; i<size()-1 ; i++) {
            HdbTable hdbTable = get(i);
            for (String attribute : hdbTable) {
                for (int j=i+1 ; j<size() ; j++) {
                    HdbTable targetHdbTable = get(j);
                    for (String targetAttribute : targetHdbTable) {
                        if (attribute.equals(targetAttribute)) {
                            list.add(attribute + " in " + hdbTable.getName() + " and " + targetHdbTable.getName());
                        }
                    }
                }
            }
        }
        System.out.println((System.currentTimeMillis()-t0) + " ms ");
        return list;
    }
    //===============================================================
    //===============================================================
    public String toString() {
        StringBuilder sb = new StringBuilder("   " + Integer.toString(attributeCount) + " Attributes:\n");
        for(HdbTable hdbTable : this) {
            sb.append(hdbTable).append('\n');
        }
        return sb.toString().trim();
    }
    //===============================================================
    //===============================================================


    //======================================================
    /**
     * Comparators class to sort collection
     */
    //======================================================
    class TableComparator implements Comparator<HdbTable> {
        public int compare(HdbTable table1, HdbTable table2) {
            switch (sortCriteria) {
                case NB_ATTRIBUTES:
                    if (table1.size() == table2.size())
                        return 0;
                    else
                        return (table1.size()>table2.size()) ? -1 : 1;

                case ALPHABETIC:
                    return alphabeticOrder(table1.getName(), table2.getName());
                default:
                    return 0;
            }
        }
        private int alphabeticOrder(String name1, String name2) {
            if (name1 == null)
                return 1;
            else if (name2 == null)
                return -1;
            else
                return name1.compareTo(name2);
        }
    }
}
