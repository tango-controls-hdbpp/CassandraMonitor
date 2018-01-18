//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jblvac/src/jblvac/vacuum_panel/AtkMoniTrend.java,v $
//
// Project:   Tango
//
// Description:  Manage a ATK trend
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009
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
//
//-======================================================================

package org.tango.cassandra_monitor_client.gui;

import fr.esrf.tangoatk.widget.attribute.Trend;
import org.tango.cassandra_monitor_client.tools.SplashUtils;

import java.awt.*;
import java.util.List;


/**
 * Manage a ATK trend
 *
 * @author Pascal Verdier
 */
public class AtkMoniTrend extends Trend {
    private String title;
    private int colorStep;
    private static final Dimension trendSize = new Dimension(1024, 640);
    //===============================================================
    /**
     * Creates new form AtkMoniTrend trend
     *
     * @param title trend title
     * @param attributeNames list of attributes to be monitored
     */
    //===============================================================
    public AtkMoniTrend(String title, List<String> attributeNames) {
        super();
        this.title = title;
        colorStep = 255/attributeNames.size()*2;  //    *2 because Read and Write
        String config = buildAtkMoniConfig(attributeNames);
        setSetting(config);
        //System.out.println(config);
        setPreferredSize(trendSize);
        getModel().startRefresher();
    }
    //===============================================================
    //===============================================================
    private String buildAtkMoniConfig(String attributeName, int curveNumber) {
        String code = atkMoniLinearAttributeConfig;
        if (attributeName.toLowerCase().contains("write")) //  Put read on Y1, write on Y2
            code = replace(code, "dv0_selected:2\n", "dv0_selected:3\n");
        code = replace(code, "dv0", "dv" + curveNumber);
        code = replace(code, "ATTRIBUTE", attributeName);
        String newColor = getNewColorString(attributeName);
        code = replace(code, "COLOR", newColor);
        return code;
    }
    //===============================================================
    //===============================================================
    private String buildAtkMoniConfig(List<String> attributeNames) {
        StringBuilder code = new StringBuilder(
                replace(atkMoniConfigHeader, "TITLE", (title==null? "null" : title)));

        //  Add each attribute
        int curveNumber = 0;
        for (String attributeName : attributeNames) {
            SplashUtils.getInstance().increaseSplashProgress(2, "Create configuration");
            code.append(buildAtkMoniConfig(attributeName, curveNumber++));
        }
        return replace(code.toString(), "NB_CURVES", Integer.toString(curveNumber));
    }
    //===============================================================
    //===============================================================
    public static String replace(String code, String oldSrc, String newSrc) {
        int start;
        int end = 0;
        while ((start=code.indexOf(oldSrc, end))>=0) {
            end = start+oldSrc.length();
            code = code.substring(0, start) + newSrc + code.substring(end);
        }
        return code;
    }
    //===============================================================
    //===============================================================
    public void stopRefresher() {
        clearModel();
    }
    //===============================================================
    //===============================================================






    //===============================================================
    /**
     *  Color management
     *  Set red -> yellow for READ     and    blue -> cyan for WRITE
     */
    //===============================================================
    private int readGreen  = 0x0;
    private int writeGreen = 0x0;
    private String getNewColorString(String attributeName) {
        int green;
        if (attributeName.toLowerCase().contains("read")) {
            green = readGreen;
            readGreen += colorStep;
            return "" + 255 + "," + green + "," + 0;
        }
        else {
            green = writeGreen;
            writeGreen += colorStep;
            return "" + 0 + "," + green + "," + 255;
        }
    }
    //===============================================================
    //===============================================================



    //===============================================================
    //===============================================================
    private static final String atkMoniConfigHeader =
            "graph_title:'TITLE'\n" +
            "label_visible:true\n" +
            "label_placement:4\n" +
            "label_font:Dialog,0,12\n" +
            "graph_background:204,204,204\n" +
            "chart_background:255,255,255\n" +
            "title_font:Dialog,1,24\n" +
            "display_duration:28800000\n" + // 8h in ms
            "precision:0\n" +
            "toolbar_visible:true\n" +
            "tree_visible:true\n" +
            "date_visible:false\n" +
            "window_pos:1087,527\n" +
            "window_size:1024,640\n" +
            "show_device_name:0\n" +
            "refresh_time:10000\n" +
            "source:2\n" +
            "min_refresh_time:0\n" +
            "xvisible:true\n" +
            "xgrid:true\n" +
            "xsubgrid:false\n" +
            "xtimeannosubtick:0\n" +
            "xgrid_style:1\n" +
            "xmin:0.0\n" +
            "xmax:100.0\n" +
            "xautoscale:true\n" +
            "xscale:0\n" +
            "xformat:0\n" +
            "xtitle:'null'\n" +
            "xcolor:0,0,0\n" +
            "xlabel_font:Dialog,1,11\n" +
            "xfit_display_duration:false\n" +
            "xpercentscrollback:10.0\n" +

            "y1visible:true\n" +
            "y1grid:true\n" +
            "y1subgrid:false\n" +
            "y1timeannosubtick:0\n" +
            "y1grid_style:1\n" +
            "y1min:0.0\n" +
            "y1max:100.0\n" +
            "y1autoscale:true\n" +
            "y1scale:0\n" +
            "y1format:0\n" +
            "y1title:'Read'\n" +
            "y1color:0,0,0\n" +
            "y1label_font:Dialog,0,11\n" +
            "y1fit_display_duration:true\n" +
            "y1percentscrollback:0.0\n" +

            "y2visible:true\n" +
            "y2grid:false\n" +
            "y2subgrid:false\n" +
            "y2timeannosubtick:0\n" +
            "y2grid_style:1\n" +
            "y2min:0.0\n" +
            "y2max:100.0\n" +
            "y2autoscale:true\n" +
            "y2scale:0\n" +
            "y2format:0\n" +
            "y2title:'Write'\n" +
            "y2color:0,0,0\n" +
            "y2label_font:Dialog,0,11\n" +
            "y2fit_display_duration:true\n" +
            "y2percentscrollback:0.0\n" +
            "dv_number:NB_CURVES\n";

    private static final String atkMoniLinearAttributeConfig =
            "dv0_name:'ATTRIBUTE'\n" +
            "dv0_selected:2\n" +
            "dv0_showminalarm:false\n" +
            "dv0_showmaxalarm:false\n" +
            "dv0_linecolor:COLOR\n" +
            "dv0_linewidth:1\n" +
            "dv0_linestyle:0\n" +
            "dv0_fillcolor:COLOR\n" +
            "dv0_fillmethod:2\n" +
            "dv0_fillstyle:0\n" +
            "dv0_viewtype:0\n" +
            "dv0_barwidth:10\n" +
            "dv0_markercolor:255,0,0\n" +
            "dv0_markersize:6\n" +
            "dv0_markerstyle:0\n" +
            "dv0_A0:0.0\n" +
            "dv0_A1:1.0\n" +
            "dv0_A2:0.0\n" +
            "dv0_labelvisible:true\n" +
            "dv0_clickable:true\n" +
            "dv0_labelColor:0,0,0\n";
}
