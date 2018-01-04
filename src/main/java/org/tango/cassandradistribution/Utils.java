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
 * This class is a set of static methods
 *
 * @author verdier
 */

public class Utils {
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
                //	An error occurs try to read it
                InputStream errorStream = process.getErrorStream();
                br = new BufferedReader(new InputStreamReader(errorStream));
                while ((str = br.readLine()) != null) {
                    //System.out.println(str);
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
    public static List<String> splitLines(String code) {
        StringTokenizer stk = new StringTokenizer(code, "\n");
        List<String> lines = new ArrayList<>();
        while (stk.hasMoreTokens()) {
            lines.add(stk.nextToken());
        }
        return lines;
    }
    //===============================================================
    //===============================================================
    public static List<String> readCompactionHistory(String hostName) throws DevFailed {
        String result = Utils.executeShellCmd("nodetool --host " + hostName + " compactionhistory");
        List<String> lines = splitLines(result);
        List<String> list = new ArrayList<>();
        //  For each line after two first ones
        int i=0;
        for (String line : lines) {
            if (i++>1) {
                //  remove id and put time at first column
                StringTokenizer stk = new StringTokenizer(line);
                if (stk.countTokens() > 6) {
                    stk.nextToken(); // ID not used
                    String keyspace = stk.nextToken();
                    String column = stk.nextToken();
                    String time = stk.nextToken();
                    String bytesIn = stk.nextToken();
                    String bytesOut = stk.nextToken();
                    list.add(time + ", " + keyspace + ", " + column + ", " + bytesIn + ", " + bytesOut);
                }
            }
        }
        return list;

    }
    //===============================================================
    //===============================================================
}
