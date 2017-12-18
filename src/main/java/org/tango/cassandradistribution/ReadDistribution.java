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
import fr.esrf.TangoDs.Except;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * This class is able to
 *
 * @author verdier
 */

public class ReadDistribution {
    private List<Host> hostList = new ArrayList<>();
    //===============================================================
    //===============================================================
    public ReadDistribution() throws DevFailed {
        String result = executeShellCmd("nodetool status -r hdb");
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
        StringTokenizer stk = new StringTokenizer(code, "\n");
        String dataCenter = "";
        while (stk.hasMoreTokens()) {
            String line = stk.nextToken();
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
    /**
     *	Execute a shell command and throw exception if command failed.
     *
     *	@param command	shell command to be executed.
     */
    //===============================================================
    public static String executeShellCmd(String command) throws DevFailed {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);

            // get command's output stream and
            // put a buffered reader input stream on it.
            InputStream inputStream = process.getInputStream();
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(inputStream));

            // read output lines from command
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }

            // wait for end of command
            process.waitFor();

            // check its exit value
            int retVal;
            if ((retVal = process.exitValue()) != 0) {
                //	An error occured try to read it
                InputStream errorStream = process.getErrorStream();
                br = new BufferedReader(new InputStreamReader(errorStream));
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                    sb.append(str).append("\n");
                }
                Except.throw_exception("ShellCommandFailed",
                        "the shell command\n" + command + "\nreturns : " + retVal + " !\n\n" + sb);
            }
        }
        catch (InterruptedException|IOException e) {
            Except.throw_exception("ShellCommandFailed", e.toString());
        }
        return sb.toString();
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
