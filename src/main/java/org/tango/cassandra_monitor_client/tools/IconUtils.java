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
//-======================================================================

package org.tango.cassandra_monitor_client.tools;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


/**
 * This class is a set of static utility methods
 *
 * @author verdier
 */

public class IconUtils {
    private static final IconUtils instance = new IconUtils();
    private static final String ImagePath = "/cassandra_monitor_client/images/";
    private static ImageIcon progressIcon = null;
    private static ImageIcon redBall = null;
    //===============================================================
    //===============================================================
    public static IconUtils getInstance() {
        return instance;
    }
    //===============================================================
    //===============================================================

    //===============================================================
    //===============================================================
    public ImageIcon getIcon(String filename) throws DevFailed {
        URL url = getClass().getResource(ImagePath + filename);
        if (url == null) {
            url = getClass().getResource(ImagePath + "components/" + filename);
            if (url == null) {
                Except.throw_exception("FILE_NOT_FOUND",
                        "Icon file  " + filename + "  not found");
            }
        }

        //noinspection ConstantConditions
        return new ImageIcon(url);
    }

    //===============================================================
    //===============================================================
    public ImageIcon getIcon(String filename, double ratio) throws DevFailed {
        ImageIcon icon = getIcon(filename);
        return getIcon(icon, ratio);
    }

    //===============================================================
    //===============================================================
    public ImageIcon getIcon(ImageIcon icon, double ratio) {
        if (icon != null) {
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();

            width = (int) (ratio * width);
            height = (int) (ratio * height);

            icon = new ImageIcon(
                    icon.getImage().getScaledInstance(
                            width, height, Image.SCALE_SMOOTH));
        }
        return icon;
    }
    //===============================================================
    //===============================================================
    public static ImageIcon getProgressIcon() {
        if (progressIcon == null)
            try {
                progressIcon = getInstance().getIcon("progress.gif");
            } catch (DevFailed e) {
                System.err.println(e.errors[0].desc);
            }
        return progressIcon;
    }
    //===============================================================
    //===============================================================
    public static ImageIcon getRedBall() {
        if (redBall == null)
            try {
                redBall = getInstance().getIcon("redBall.gif");
            } catch (DevFailed e) {
                System.err.println(e.errors[0].desc);
            }
        return redBall;
    }
    //===============================================================
    //===============================================================
}
