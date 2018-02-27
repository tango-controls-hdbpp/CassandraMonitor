//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jhdbextract/src/org/tango/jhdbextract/commons/MySqlConnection.java,v $
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
// $Revision: 1.1.1.1 $
//
//-======================================================================

package org.tango.hdb_tables;


import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.DriverException;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;
import org.tango.utils.DevFailedUtils;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * This class is a singleton defining a Cassandra database connection
 * Created: VERDIER
 * Date: 10/10/14
 */
public class CassandraConnection {

    private Cluster cluster;
    private Session session;
    private HdbTables hdbTables;
    private Hashtable<String, HdbAttribute> attributeMap = new Hashtable<>();

    private static String[] contactPoints = {"cassandra2"};
    private static CassandraConnection instance = null;
    private static final Object monitor = new Object();
    //===============================================================
    //===============================================================
    private CassandraConnection() {

        //  Get contact points from environment variable
        String str = System.getenv("HDB_CONTACT_POINTS");
        if (str!=null && !str.isEmpty()) {
            StringTokenizer stk = new StringTokenizer(str, ",");
            contactPoints = new String[stk.countTokens()];
            int i=0;
            while (stk.hasMoreTokens())
                contactPoints[i++] = stk.nextToken();
        }

        //  Build cluster with contact points
        Cluster.Builder builder = Cluster.builder();
        for (String contactPoint : contactPoints) {
            builder.addContactPoint(contactPoint);
        }
        cluster = builder.build();

        //  Set protocol
        cluster.getConfiguration()
                .getProtocolOptions()
                .setCompression(ProtocolOptions.Compression.LZ4);
        //  Build session on database
        String hdbName = "hdb";
        str = System.getenv("HDB_NAME");
        if (str!=null && !str.isEmpty())
            hdbName = str;
        session = cluster.connect(hdbName);
    }
    //===============================================================
    //===============================================================
    public static CassandraConnection getInstance() {
        synchronized (monitor) {
            if (instance==null) {
                instance = new CassandraConnection();
            }
            return instance;
        }
    }
    //===============================================================
    //===============================================================
    Session getSession() {
        return session;
    }
    //===============================================================
    //===============================================================
    public HdbTables getHdbTables() throws DevFailed {
        if (hdbTables==null)
            updateArchiveAttributes();
        return hdbTables;
    }
    //===============================================================
    //===============================================================
    void updateArchiveAttributes() throws DevFailed {
        String query = "SELECT * FROM att_conf";
        ResultSet resultSet;
        attributeMap.clear();
        try {
            resultSet = getSession().execute(query);
            for (Row row : resultSet) {
                String csName = row.getString("cs_name");
                String attName = row.getString("att_name");
                //UUID uuid = row.getUUID("att_conf_id");
                String tableNme = row.getString("data_type");
                int ttl = row.getInt("ttl");

                HdbAttribute attribute = new HdbAttribute(csName, attName, tableNme, ttl);
                attributeMap.put(attribute.getName(), attribute);
            }
        } catch (DriverException e) {
            Except.throw_exception("ReadDataFailed", e.getMessage());
        }
        hdbTables = new HdbTables(attributeMap);
        //System.out.println(hdbTables);
    }
    //===============================================================
    //===============================================================
    public ConnectionInformation getConnectionInformation(String hostName) throws DevFailed {
        Metadata metadata = cluster.getMetadata();
        for (Host host : metadata.getAllHosts()) {
            String name = host.getAddress().getCanonicalHostName();
            int idx = name.indexOf('.');
            if (idx>0) name = name.substring(0, idx);
            if (name.equals(hostName)) {
                return new ConnectionInformation(
                        name, host.getDatacenter(), host.getRack(),
                        Integer.toString(host.getTokens().size()));
            }
        }
        throw DevFailedUtils.newDevFailed("Host " + hostName + "  not found on Cassandra connection");
    }
    //===============================================================
    //===============================================================
    public String getConnectionInformation() {
        Metadata metadata = cluster.getMetadata();
        StringBuilder sb = new StringBuilder();
        sb.append("Cassandra Database cluster: ").append(metadata.getClusterName()).append('\n');
        for ( Host host : metadata.getAllHosts() ) {
            sb.append("  - ").append(host.getDatacenter())
                    .append(": ").append(host.getAddress().getCanonicalHostName())
                    .append("  in  ").append(host.getRack())
                    .append("  is ").append((host.isUp())? "Up" : "Down")
                    .append('\n');
        }
        return sb.toString().trim();
    }
    //===============================================================
    //===============================================================
    public String toString() {
        return contactPoints[0];
    }
    //===============================================================
    //===============================================================
}
