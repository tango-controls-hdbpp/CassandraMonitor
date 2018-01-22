//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  Basic Dialog Class to display info
//
// $Author: pascal_verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009,2010,2011,2012,2013,2014,2015
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
// $Revision:  $
//
// $Log:  $
//
//-======================================================================

package org.tango.cassandra_monitor_client.gui;

import fr.esrf.tangoatk.widget.util.chart.*;

import java.awt.*;
import java.util.List;

import static org.tango.cassandra_monitor_client.gui.CassandraNode.CLEANUP;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.COMPACTION;
import static org.tango.cassandra_monitor_client.gui.CassandraNode.VALIDATION;
import static org.tango.cassandra_monitor_client.gui.CompactionDialog.CLEANUP_COLOR;
import static org.tango.cassandra_monitor_client.gui.CompactionDialog.VALIDATION_COLOR;
import static org.tango.cassandramonitor.IConstants.refreshMonitor;

/**
 *	Chart to display compactions/validations/cleanups/....
 *
 *	@author  Pascal Verdier
 */

public class CompactionChart extends JLChart implements IJLChartListener {
	private CassandraNode cassandraNode;
    private JLDataView compactionDataView;
    private JLDataView validationDataView;
    private JLDataView cleanupDataView;
    private static final Dimension chartDimension = new Dimension(200, 200);
	//===============================================================
	//===============================================================
	public CompactionChart(CassandraNode cassandraNode) {
		super();
		this.cassandraNode = cassandraNode;
		setHeaderFont(new Font("Dialog", Font.BOLD, 12));
		setHeader(cassandraNode.getName());
		setLabelVisible(false);
		setLabelFont(new Font("Dialog", Font.BOLD, 12));
		setJLChartListener(this);

		//  Set Y axis.
		JLAxis y1Axis = getY1Axis();
		y1Axis.setName("Compaction (%)");
		y1Axis.setAutoScale(false);
        y1Axis.setMaximum(100);
		y1Axis.setGridVisible(true);

		//  Set X axis.
		JLAxis  xAxis = getXAxis();
		xAxis.setName("table in compaction");
		xAxis.setAnnotation(JLAxis.VALUE_ANNO);
		xAxis.setGridVisible(true);
		xAxis.setSubGridVisible(true);
        xAxis.setMinimum(0);
        xAxis.setAutoScale(false);

        compactionDataView = buildDataView("Compactions", Color.red, getY1Axis());
        validationDataView = buildDataView("Validations", VALIDATION_COLOR, getY1Axis());
        cleanupDataView    = buildDataView("Cleanup",     CLEANUP_COLOR, getY1Axis());
        setPreferredSize(chartDimension);
	}
	//===============================================================
	//===============================================================
    private JLDataView buildDataView(String name, Color color, JLAxis axis) {
        JLDataView dataView = new JLDataView();
        dataView.setName(name);
        dataView.setColor(color);
        dataView.setFill(true);
        dataView.setFillColor(color);
        dataView.setViewType(JLDataView.TYPE_BAR);
        dataView.setBarWidth(2);

        axis.addDataView(dataView);
        return dataView;
    }
	//===============================================================
	//===============================================================
    public void updateCurves() {
        compactionDataView.reset();
        validationDataView.reset();
        cleanupDataView.reset();
        synchronized (refreshMonitor) {
            List<Compaction> compactionList = cassandraNode.getCompactionList();
            if (compactionList.isEmpty()) {
                setHeader(cassandraNode.getName() + " (No Compaction)");
            } else {
                setHeader(cassandraNode.getName());
                int x = 1;
                for (Compaction compaction : compactionList) {
                    switch (compaction.getTaskType()) {
                        case COMPACTION:
                            compactionDataView.add(x++, 100.0 * compaction.getRatio());
                            break;
                        case CLEANUP:
                            cleanupDataView.add(x++, 100.0 * compaction.getRatio());
                            break;
                        default:
                            validationDataView.add(x++, 100.0 * compaction.getRatio());
                    }
                }
                getXAxis().setMaximum(compactionList.size() + 1);
            }
        }
        repaint();
    }
    //===============================================================
    //===============================================================
    @Override
    public String[] clickOnChart(JLChartEvent chartEvent) {
        JLDataView dataView = chartEvent.getDataView();
        int index = chartEvent.getDataViewIndex();
        List<Compaction> compactionList = cassandraNode.getCompactionList();

        int i=0;
        for (Compaction compaction : compactionList) {
            //  Search index for specified data view
            if (isDataView(dataView, COMPACTION) ||
                    isDataView(dataView, VALIDATION) || isDataView(dataView, CLEANUP)) {

                //  If found, return compaction/validation parameters
                if (i == index) {
                    return new String[]  { compaction.getTableName() };
                }
                i++;
            }
        }

        return new String[] { "index " + index + " not found in " + dataView.getName()};
    }
    //===============================================================
    //===============================================================
    private boolean isDataView(JLDataView dataView, int taskType) {
	    switch (taskType) {
            case COMPACTION:
                return dataView==compactionDataView;
            case VALIDATION:
                return dataView==validationDataView;
            case CLEANUP:
                return dataView==cleanupDataView;
        }
        return false;
    }
    //===============================================================
	//===============================================================
}
