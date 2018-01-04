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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * This class is able to read node distribution (data centers, racks, ..) using nodetool
 *
 * @author verdier
 */

public class ReadDistribution {
    private List<Host> hostList = new ArrayList<>();
    //===============================================================
    //===============================================================
    public ReadDistribution() throws DevFailed {
        String result = Utils.executeShellCmd("nodetool status -r hdb");
        System.out.println(result);
        splitLines(result);
        System.out.println("================================================");
        System.out.println(this);
    }
    //===============================================================
    //===============================================================
    public String[] getHostInformation() {
        String[] information = new String[hostList.size()];
        int i=0;
        for (Host host : hostList)
            information[i++] = host.toString();
        return information;
    }
    //===============================================================
    //===============================================================
    private void splitLines(String code) {
        List<String> lines = Utils.splitLines(code);
        String dataCenter = "";
        for (String line : lines) {
            if (line.startsWith("Datacenter")) {
                dataCenter = line.substring(line.indexOf(":")+1).trim();
            }
            else
            if (line.startsWith("UN")) {
                hostList.add(new Host(line, dataCenter));
            }
        }
    }
    //===============================================================
    //===============================================================
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Host host : hostList)
            sb.append(host).append("\n");
        return sb.toString().trim();
    }
    //===============================================================
    //===============================================================


    //===============================================================
    //===============================================================
    private class Host {
        private String name;
        private String rack;
        private String dataCenter;
        private String owns;
        private String tokens;
        //===========================================================
        private Host(String line, String dataCenter) {
            this.dataCenter = dataCenter;
            parseLine(line);
            //  Check to remove fqdn
            if (name.indexOf('.')>0)
                name = name.substring(0, name.indexOf('.'));
        }
        //===========================================================
        private void parseLine(String line) {
            StringTokenizer stk = new StringTokenizer(line);
            stk.nextToken();    //  unused
            name = stk.nextToken();
            stk.nextToken();    //  unused
            stk.nextToken();    //  unused
            tokens = stk.nextToken();
            owns = stk.nextToken();
            stk.nextToken();    //  unused
            rack = stk.nextToken();
        }
        //===========================================================
        public String toString() {
            return name + ":\t" + dataCenter + ", " + rack + ", " + owns + ", " + tokens;
        }
        //===========================================================
    }
    //===============================================================
    //===============================================================
}
