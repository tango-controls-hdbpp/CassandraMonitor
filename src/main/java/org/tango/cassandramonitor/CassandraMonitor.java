/*----- PROTECTED REGION ID(CassandraMonitor.java) ENABLED START -----*/
//=============================================================================
//
// file :        CassandraMonitor.java
//
// description : Java source for the CassandraMonitor class and its commands.
//               The class is derived from Device. It represents the
//               CORBA servant object which will be accessed from the
//               network. All commands which can be executed on the
//               CassandraMonitor are implemented in this file.
//
// project :     Cassandra monitor
//
// This file is part of Tango device class.
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
// $Author:  $
// Copyright (C): 2016
//                European Synchrotron Radiation Facility
//                BP 220, Grenoble 38043
//                France
//
// $Revision:  $
// $Date:  $
//
// $HeadURL:  $
//
//=============================================================================
//                This file is generated by POGO
//        (Program Obviously used to Generate tango Object)
//=============================================================================

/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.java

package org.tango.cassandramonitor;

/*----- PROTECTED REGION ID(CassandraMonitor.imports) ENABLED START -----*/

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.Except;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.tango.server.InvocationContext;
import org.tango.server.ServerManager;
import org.tango.server.annotation.*;
import org.tango.server.device.DeviceManager;
import org.tango.server.dynamic.DynamicManager;
import org.tango.server.pipe.PipeValue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.StringTokenizer;

import static org.tango.cassandramonitor.IConstants.*;

@SuppressWarnings({"FieldCanBeLocal", "unused","RedundantThrows","DefaultAnnotationParam"})
/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.imports

/**
 *  CassandraMonitor class description:
 *    This class is used to monitor a Cassandra cluster
 */

@Device
public class CassandraMonitor {

	protected static final Logger logger = LoggerFactory.getLogger(CassandraMonitor.class);
	protected static final XLogger xlogger = XLoggerFactory.getXLogger(CassandraMonitor.class);
	//========================================================
	//	Programmer's data members
	//========================================================
    /*----- PROTECTED REGION ID(CassandraMonitor.variables) ENABLED START -----*/

    //	Put static variables here
    private static final long READ_JMX_PERIOD = 10; // seconds

    /*----- PROTECTED REGION END -----*/	//	CassandraMonitor.variables
	/*----- PROTECTED REGION ID(CassandraMonitor.private) ENABLED START -----*/

    //	Put private variables here
    private CompactionsThread compactionsThread;
	private JmxUtilities jmxUtilities;

	/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.private

	//========================================================
	//	Property data members and related methods
	//========================================================
	/**
	 * Class Property DistributionDeviceName
	 * Device name for CassandraDistribution class.
	 */
	@ClassProperty(name="DistributionDeviceName", description="Device name for CassandraDistribution class." )
	private String distributionDeviceName;
	/**
	 * set property DistributionDeviceName
	 * @param  distributionDeviceName  see description above.
	 */
	public void setDistributionDeviceName(String distributionDeviceName) {
		this.distributionDeviceName = distributionDeviceName;
		/*----- PROTECTED REGION ID(CassandraMonitor.setDistributionDeviceName) ENABLED START -----*/
		

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setDistributionDeviceName
	}
	
	/**
	 * Device Property JMXPort
	 * JMX port
	 */
	@DeviceProperty(name="JMXPort", description="JMX port" ,
	        defaultValue={ "7199" } )
	private short jMXPort;
	/**
	 * set property JMXPort
	 * @param  jMXPort  see description above.
	 */
	public void setJMXPort(short jMXPort) {
		this.jMXPort = jMXPort;
		/*----- PROTECTED REGION ID(CassandraMonitor.setJMXPort) ENABLED START -----*/

        //	Check property value here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setJMXPort
	}
	
	/**
	 * Device Property Node
	 * Name or IP address of the Cassandra node we want to communicate with via JMX.
	 */
	@DeviceProperty(name="Node", description="Name or IP address of the Cassandra node we want to communicate with via JMX."  , isMandatory=true)
	private String node;
	/**
	 * set property Node
	 * @param  node  see description above.
	 */
	public void setNode(String node) {
		this.node = node;
		/*----- PROTECTED REGION ID(CassandraMonitor.setNode) ENABLED START -----*/

        //	Check property value here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setNode
	}
	
	/**
	 * Device Property JMXUser
	 * JMX user name if authentication is enabled on JMX service
	 */
	@DeviceProperty(name="JMXUser", description="JMX user name if authentication is enabled on JMX service"  )
	private String jMXUser;
	/**
	 * set property JMXUser
	 * @param  jMXUser  see description above.
	 */
	public void setJMXUser(String jMXUser) {
		this.jMXUser = jMXUser;
		/*----- PROTECTED REGION ID(CassandraMonitor.setJMXUser) ENABLED START -----*/

        //	Check property value here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setJMXUser
	}
	
	/**
	 * Device Property JMXPassword
	 * JMX password if authentication is enabled on JMX service
	 */
	@DeviceProperty(name="JMXPassword", description="JMX password if authentication is enabled on JMX service"  )
	private String jMXPassword;
	/**
	 * set property JMXPassword
	 * @param  jMXPassword  see description above.
	 */
	public void setJMXPassword(String jMXPassword) {
		this.jMXPassword = jMXPassword;
		/*----- PROTECTED REGION ID(CassandraMonitor.setJMXPassword) ENABLED START -----*/

        //	Check property value here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setJMXPassword
	}
	
	/**
	 * Device Property JMXConnectionTimeout
	 * Timeout on JMX connection (seconds).
	 */
	@DeviceProperty(name="JMXConnectionTimeout", description="Timeout on JMX connection (seconds)." ,
	        defaultValue={ "3" } )
	private int jMXConnectionTimeout;
	/**
	 * set property JMXConnectionTimeout
	 * @param  jMXConnectionTimeout  see description above.
	 */
	public void setJMXConnectionTimeout(int jMXConnectionTimeout) {
		this.jMXConnectionTimeout = jMXConnectionTimeout;
		/*----- PROTECTED REGION ID(CassandraMonitor.setJMXConnectionTimeout) ENABLED START -----*/
		
		//	Check property value here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setJMXConnectionTimeout
	}
	


	//========================================================
	//	Miscellaneous methods
	//========================================================
	/**
	 * Initialize the device.
	 * 
	 * @throws DevFailed if something fails during the device initialization.
	 */
	@Init(lazyLoading = false)
	public void initDevice() throws DevFailed {
		xlogger.entry();
		logger.debug("init device " + deviceManager.getName());
		/*----- PROTECTED REGION ID(CassandraMonitor.initDevice) ENABLED START -----*/
        // ToDo initialize Device

		//  Check if class property is OK
        if (distributionDeviceName==null || distributionDeviceName.isEmpty()) {
            System.err.println("*** Class property DistributionDeviceName is not defined ! ***");
            System.exit(0);
        }
        //  Property has been set -> read it to initialize
		//	Now done at DataCenter read
        // initializeFromDistribution();


        //  Initialize the JMX utility
		jmxUtilities = new JmxUtilities(node, jMXPort, jMXUser, jMXPassword, jMXConnectionTimeout);

        //System.out.println(jmxUtilities.getAttribute(SS_TABLE_COUNT, ATTR_VALUE));

        compactionsThread = new CompactionsThread(deviceManager, jmxUtilities, node);
        compactionsThread.start();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.initDevice
		xlogger.exit();
	}

	/**
	 * all resources may be closed here. Collections may be also cleared.
	 * 
	 * @throws DevFailed if something fails during the device object deletion.
	 */
	@Delete
	public void deleteDevice() throws DevFailed {
		xlogger.entry();
		/*----- PROTECTED REGION ID(CassandraMonitor.deleteDevice) ENABLED START -----*/

		jmxUtilities.close();
		compactionsThread.stopThread();

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.deleteDevice
		xlogger.exit();
	}

	/**
	 * Method called before and after command and attribute calls.
	 * @param ctx the invocation context
	 * @throws DevFailed if something fails during this method execution.
	 */
	@AroundInvoke
	public void aroundInvoke(final InvocationContext ctx) throws DevFailed {
		xlogger.entry();
			/*----- PROTECTED REGION ID(CassandraMonitor.aroundInvoke) ENABLED START -----*/

        //	Put aroundInvoke code here
			
			/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.aroundInvoke
		xlogger.exit();
	}

	
	/**
	 * dynamic command and attribute management. Will be injected by the framework.
	 */
	@DynamicManagement
	protected DynamicManager dynamicManager;
	/**
	 * @param dynamicManager the DynamicManager instance 
	 * @throws DevFailed if something fails during this method execution.
	 */
	public void setDynamicManager(final DynamicManager dynamicManager) throws DevFailed {
		this.dynamicManager = dynamicManager;
		/*----- PROTECTED REGION ID(CassandraMonitor.setDynamicManager) ENABLED START -----*/

        //	Put your code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.setDynamicManager
	}
	
	/**
	 * Device management. Will be injected by the framework.
	 */
	@DeviceManagement
	DeviceManager deviceManager;
	public void setDeviceManager(DeviceManager deviceManager){
		this.deviceManager= deviceManager ;
	}


	//========================================================
	//	Attribute data members and related methods
	//========================================================
	/**
	 * Attribute DataCenter, String, Scalar, READ
	 * description:
	 *     Data center where node is located
	 */
	@Attribute(name="DataCenter", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Data center where node is located", label="Data center")
	private String dataCenter = "";
	/**
	 * Read attribute DataCenter
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getDataCenter() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getDataCenter) ENABLED START -----*/
		//	If empty (not initialized) try again
		if (dataCenter.isEmpty())
			initializeFromDistribution();
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getDataCenter
		attributeValue.setValue(dataCenter);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute Rack, String, Scalar, READ
	 * description:
	 *     Rack where node is located
	 */
	@Attribute(name="Rack", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Rack where node is located", label="Rack")
	private String rack = "";
	/**
	 * Read attribute Rack
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getRack() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getRack) ENABLED START -----*/
		
		//	Put read attribute code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getRack
		attributeValue.setValue(rack);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute ClusterName, String, Scalar, READ
	 * description:
	 *     Cassandra cluster name
	 */
	@Attribute(name="ClusterName", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Cassandra cluster name", label="Cluster name")
	private String clusterName = "";
	/**
	 * Read attribute ClusterName
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getClusterName() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getClusterName) ENABLED START -----*/

        //	Put read attribute code here
        clusterName = jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_CLUSTER).toString();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getClusterName
		attributeValue.setValue(clusterName);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute DataLoadStr, String, Scalar, READ
	 * description:
	 *     Data Load on disk of the Cassandra node (node from the node property).
	 */
	@Attribute(name="DataLoadStr", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Data Load on disk of the Cassandra node (node from the node property).",
	                     label="DataLoadStr")
	private String dataLoadStr = "";
	/**
	 * Read attribute DataLoadStr
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getDataLoadStr() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getDataLoadStr) ENABLED START -----*/

        //	Put read attribute code here
        dataLoadStr = jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_LOAD).toString();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getDataLoadStr
		attributeValue.setValue(dataLoadStr);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute OperationMode, String, Scalar, READ
	 * description:
	 *     Cassandra node current operation mode (NORMAL,JOINING,LEAVING...)
	 */
	@Attribute(name="OperationMode", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Cassandra node current operation mode (NORMAL,JOINING,LEAVING...)",
	                     label="Operation Mode", archiveEventPeriod="3600000")
	private String operationMode = "";
	/**
	 * Read attribute OperationMode
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getOperationMode() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getOperationMode) ENABLED START -----*/

        //	Put read attribute code here
        operationMode = jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_OPERATION_MODE).toString();
        switch (operationMode) {
            case "STARTING":
                setState(DevState.INIT);
                break;
            case "NORMAL":
                setState(DevState.ON);
                break;
            case "JOINING":
                setState(DevState.STANDBY);
                break;
            case "LEAVING":
            case "MOVING":
            case "DRAINING":
                setState(DevState.MOVING);
                break;
            case "DRAINED":
                setState(DevState.OFF);
                break;
            case "DECOMMISSIONED":
                setState(DevState.DISABLE);
                break;
            default:
                setState(DevState.UNKNOWN);
        }
        setStatus("Node " + node + " is " + operationMode);
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getOperationMode
		attributeValue.setValue(operationMode);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute DataLoad, long, Scalar, READ
	 * description:
	 *     Data Load on disk of the Cassandra node (node from the node property).
	 */
	@Attribute(name="DataLoad", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Data Load on disk of the Cassandra node (node from the node property).",
	                     label="Data Load", unit="Kb", standardUnit="1", displayUnit="0.0009765625",
	                     changeEventAbsolute="1", archiveEventPeriod="3600000", archiveEventAbsolute="1")
	private long dataLoad;
	/**
	 * Read attribute DataLoad
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getDataLoad() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getDataLoad) ENABLED START -----*/

        //	Put read attribute code here
        dataLoad = (Long) jmxUtilities.getAttribute(STORAGE_LOAD, ATTR_COUNT);
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getDataLoad
		attributeValue.setValue(dataLoad);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute CassandraVersion, String, Scalar, READ
	 * description:
	 *     Cassandra release version
	 */
	@Attribute(name="CassandraVersion", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Cassandra release version", label="Cassandra Version")
	private String cassandraVersion = "";
	/**
	 * Read attribute CassandraVersion
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getCassandraVersion() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getCassandraVersion) ENABLED START -----*/

        //	Put read attribute code here
        cassandraVersion = jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_RELEASE).toString();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getCassandraVersion
		attributeValue.setValue(cassandraVersion);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute Owns, String, Scalar, READ
	 * description:
	 *     Node`s owns
	 */
	@Attribute(name="Owns", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Node`s owns", label="Owns")
	private String owns = "";
	/**
	 * Read attribute Owns
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getOwns() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getOwns) ENABLED START -----*/
		
		//	Put read attribute code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getOwns
		attributeValue.setValue(owns);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute Tokens, String, Scalar, READ
	 * description:
	 *     Node`s tokens
	 */
	@Attribute(name="Tokens", isPolled=true, pollingPeriod=60000)
	@AttributeProperties(description="Node`s tokens", label="Tokens")
	private String tokens = "";
	/**
	 * Read attribute Tokens
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getTokens() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getTokens) ENABLED START -----*/
		
		//	Put read attribute code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getTokens
		attributeValue.setValue(tokens);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute WriteRequests, double, Scalar, READ
	 * description:
	 *     Write request (events/second)
	 */
	@Attribute(name="WriteRequests", isPolled=true, pollingPeriod=10000)
	@AttributeProperties(description="Write request (events/second)", label="Write requests",
	                     unit="ev/s", format="%.1f", changeEventAbsolute="0.1", archiveEventRelative="0.1")
	private double writeRequests;
	/**
	 * Read attribute WriteRequests
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getWriteRequests() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getWriteRequests) ENABLED START -----*/
		
		//	ToDo Put read attribute code here
        writeRequests = (double)jmxUtilities.getAttribute(WRITE_REQUESTS, ATTR_RATE);

        /*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getWriteRequests
		attributeValue.setValue(writeRequests);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute ReadRequests, double, Scalar, READ
	 * description:
	 *     Read request (events/second)
	 */
	@Attribute(name="ReadRequests", isPolled=true, pollingPeriod=10000)
	@AttributeProperties(description="Read request (events/second)", label="Read requests",
	                     unit="ev/s", format="%.1f", changeEventAbsolute="0.1", archiveEventRelative="0.1")
	private double readRequests;
	/**
	 * Read attribute ReadRequests
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getReadRequests() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getReadRequests) ENABLED START -----*/

        readRequests = (double)jmxUtilities.getAttribute(READ_REQUESTS, ATTR_RATE);
		
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getReadRequests
		attributeValue.setValue(readRequests);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute PendingCompactionTasks, int, Scalar, READ
	 * description:
	 *     Pending Compaction tasks
	 */
	@Attribute(name="PendingCompactionTasks", isPolled=true, pollingPeriod=10000)
	@AttributeProperties(description="Pending Compaction tasks", label="Pending Compaction Tasks",
	                     changeEventAbsolute="0.1")
	private int pendingCompactionTasks;
	/**
	 * Read attribute PendingCompactionTasks
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getPendingCompactionTasks() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getPendingCompactionTasks) ENABLED START -----*/

        pendingCompactionTasks = (int) jmxUtilities.getAttribute(PENDING_TASKS, "Value");

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getPendingCompactionTasks
		attributeValue.setValue(pendingCompactionTasks);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute SsTableNumber, int, Scalar, READ
	 * description:
	 *     SS Table number
	 */
	@Attribute(name="SsTableNumber", isPolled=true, pollingPeriod=10000)
	@AttributeProperties(description="SS Table number", label="SS Table number",
	                     changeEventAbsolute="0.1")
	private int ssTableNumber;
	/**
	 * Read attribute SsTableNumber
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getSsTableNumber() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getSsTableNumber) ENABLED START -----*/
		

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getSsTableNumber
		attributeValue.setValue(ssTableNumber);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute UnreachableNodes, String, Spectrum, READ
	 * description:
	 *     List of Cassandra nodes from the cluster which are currently unreachable.
	 */
	@Attribute(name="UnreachableNodes", isPolled=true, pollingPeriod=3000)
	@AttributeProperties(description="List of Cassandra nodes from the cluster which are currently unreachable.",
	                     label="Unreachable Nodes")
	private String[] unreachableNodes = new String[2000];
	/**
	 * Read attribute UnreachableNodes
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getUnreachableNodes() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getUnreachableNodes) ENABLED START -----*/

        //	Put read attribute code here
        java.util.List list = (List) jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_UNREACHABLE);
        unreachableNodes = new String[list.size()];
        int i = 0;
        for (Object n : list) {
            try {
                InetAddress inetAddress = InetAddress.getByName(n.toString());
                unreachableNodes[i] = inetAddress.getHostName();
            } catch (UnknownHostException ex) {
                unreachableNodes[i] = n.toString();
            }
            i++;
        }
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getUnreachableNodes
		attributeValue.setValue(unreachableNodes);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute LiveNodes, String, Spectrum, READ
	 * description:
	 *     List of Cassandra nodes from the cluster which are currently alive
	 */
	@Attribute(name="LiveNodes", isPolled=true, pollingPeriod=3000)
	@AttributeProperties(description="List of Cassandra nodes from the cluster which are currently alive",
	                     label="Live nodes")
	private String[] liveNodes = new String[2000];
	/**
	 * Read attribute LiveNodes
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getLiveNodes() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getLiveNodes) ENABLED START -----*/

        //	Put read attribute code here
        List list = (List) jmxUtilities.getAttribute(STORAGE_SERVICE, ATTR_LIVE_MODE);
        liveNodes = new String[list.size()];
        int i = 0;
        for (Object n : list) {
            try {
                InetAddress inetAddress = InetAddress.getByName(n.toString());
                liveNodes[i] = inetAddress.getHostName();
            } catch (UnknownHostException ex) {
                liveNodes[i] = n.toString();
            }
            i++;
        }
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getLiveNodes
		attributeValue.setValue(liveNodes);
		xlogger.exit();
		return attributeValue;
	}
	
	/**
	 * Attribute SsTableNumberList, String, Spectrum, READ
	 * description:
	 *     SS Table number per HDB table
	 */
	@Attribute(name="SsTableNumberList", isPolled=true, pollingPeriod=10000)
	@AttributeProperties(description="SS Table number per HDB table", label="SS Table number list",
	                     changeEventAbsolute="0.1")
	private String[] ssTableNumberList = new String[1024];
	/**
	 * Read attribute SsTableNumberList
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getSsTableNumberList() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraMonitor.getSsTableNumberList) ENABLED START -----*/

		jmxUtilities.readSsTables();
		List<HdbTable> hdbTableList = jmxUtilities.getHdbTableList();
		ssTableNumber = 0;
		ssTableNumberList = new String[hdbTableList.size()];
		int i=0;
		for (HdbTable hdbTable : hdbTableList) {
			ssTableNumberList[i++] = hdbTable.getName()+":\t" + hdbTable.getSsTableCount();
			ssTableNumber += hdbTable.getSsTableCount();
		}

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getSsTableNumberList
		attributeValue.setValue(ssTableNumberList);
		xlogger.exit();
		return attributeValue;
	}
	

	//========================================================
	//	Pipe data members and related methods
	//========================================================
	/**
	 * Pipe Compactions
	 * description:
	 *     Give the compactions status
	 */
	@Pipe(displayLevel=DispLevel._OPERATOR, label="Compactions")
	private PipeValue compactions;
	/**
	 * Read Pipe Compactions
	 * 
	 * @return attribute value
	 * @throws DevFailed if read pipe failed.
	 */
	public PipeValue getCompactions() throws DevFailed {
		xlogger.entry();
		/*----- PROTECTED REGION ID(CassandraMonitor.getCompactions) ENABLED START -----*/
        /*
         *  Done by thread
         */
        compactions = compactionsThread.getPipeCompactions();

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getCompactions
		xlogger.exit();
		return compactions;
	}

	//========================================================
	//	Command data members and related methods
	//========================================================
	/**
	 * The state of the device
	*/
	@State (isPolled=true, pollingPeriod=1000)
	private DevState state = DevState.UNKNOWN;
	/**
	 * Execute command "State".
	 * description: This command gets the device state (stored in its 'state' data member) and returns it to the caller.
	 * @return Device state
	 * @throws DevFailed if command execution failed.
	 */
	public final DevState getState() throws DevFailed {
		/*----- PROTECTED REGION ID(CassandraMonitor.getState) ENABLED START -----*/

        //	If connection failed set as UNKNOWN
		if (jmxUtilities.connectionFailed())
			state = DevState.UNKNOWN;
		else
			compactionsThread.pushPipeEventIfNeeded();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getState
		return state;
	}
	/**
	 * Set the device state
	 * @param state the new device state
	 */
	public void setState(final DevState state) {
		this.state = state;
	}
	
	/**
	 * The status of the device
	 */
	@Status (isPolled=true, pollingPeriod=1000)
	private String status = "Server is starting. The device state is unknown";
	/**
	 * Execute command "Status".
	 * description: This command gets the device status (stored in its 'status' data member) and returns it to the caller.
	 * @return Device status
	 * @throws DevFailed if command execution failed.
	 */
	public final String getStatus() throws DevFailed {
		/*----- PROTECTED REGION ID(CassandraMonitor.getStatus) ENABLED START -----*/

		//	If connection failed set with error
		if (jmxUtilities.connectionFailed())
			status = jmxUtilities.getConnectionError();

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.getStatus
		return status;
	}
	/**
	 * Set the device status
	 * @param status the new device status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}
	
	/**
	 * Execute command "ReadCompactionHistory".
	 * description: Read the compaction history for this host.
	 * @return compation history as:
	 *         compacted_at, keyspace_name, columnfamily_name, bytes_in, bytes_out
	 * @throws DevFailed if command execution failed.
	 */
	@Command(name="ReadCompactionHistory", inTypeDesc="", outTypeDesc="compation history as:\ncompacted_at, keyspace_name, columnfamily_name, bytes_in, bytes_out")
	public String[] ReadCompactionHistory() throws DevFailed {
		xlogger.entry();
		String[] readCompactionHistoryOut;
		/*----- PROTECTED REGION ID(CassandraMonitor.readCompactionHistory) ENABLED START -----*/

		if (distributionProxy==null)
			distributionProxy = new DeviceProxy(distributionDeviceName);
		DeviceData argIn = new DeviceData();
		argIn.insert(node);
		DeviceData argOut = distributionProxy.command_inout("ReadCompactionHistory", argIn);
		readCompactionHistoryOut = argOut.extractStringArray();
		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.readCompactionHistory
		xlogger.exit();
		return readCompactionHistoryOut;
	}
	
	/**
	 * Execute command "ReadHdbTableSizes".
	 * description: Returns the size of each hdb table in bytes.
	 * @return Hdb table sizes in Mb
	 * @throws DevFailed if command execution failed.
	 */
	@Command(name="ReadHdbTableSizes", inTypeDesc="", outTypeDesc="Hdb table sizes in Mb")
	public DevVarDoubleStringArray ReadHdbTableSizes() throws DevFailed {
		xlogger.entry();
		DevVarDoubleStringArray readHdbTableSizesOut;
		/*----- PROTECTED REGION ID(CassandraMonitor.readHdbTableSizes) ENABLED START -----*/

		jmxUtilities.readTableSizes();
		List<HdbTable> hdbTableList = jmxUtilities.getHdbTableList();
		readHdbTableSizesOut = new DevVarDoubleStringArray();
		readHdbTableSizesOut.svalue = new String[hdbTableList.size()];
		readHdbTableSizesOut.dvalue = new double[hdbTableList.size()];
		int i=0;
		for (HdbTable hdbTable : hdbTableList) {
		    readHdbTableSizesOut.svalue[i] = hdbTable.getName();
		    readHdbTableSizesOut.dvalue[i] = hdbTable.getSize();
		    i++;
        }

		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.readHdbTableSizes
		xlogger.exit();
		return readHdbTableSizesOut;
	}
	

	//========================================================
	//	Programmer's methods
	//========================================================
	/*----- PROTECTED REGION ID(CassandraMonitor.methods) ENABLED START -----*/

    //========================================================
	private static DeviceProxy distributionProxy = null;
    //========================================================
    private void initializeFromDistribution() {
        try {
        	if (distributionProxy==null)
        		distributionProxy = new DeviceProxy(distributionDeviceName);
            DeviceAttribute attribute = distributionProxy.read_attribute("NodeDistribution");
            String[] lines = attribute.extractStringArray();
            for (String line : lines) {
            	//	Get line for specified node
                if (line.startsWith(node)) {
                	int index = line.indexOf(':');
                	if (index<0)
						Except.throw_exception("SyntaxError",
								"Syntax error in DistributionDevice attribute");

					// split after removing node name
                    StringTokenizer stk = new StringTokenizer(line.substring(++index), ",");
                    if (stk.countTokens()!=4)
                        Except.throw_exception("SyntaxError",
                                "Syntax error in DistributionDevice attribute");
                    dataCenter = stk.nextToken().trim();
                    rack = stk.nextToken().trim();
                    owns = stk.nextToken().trim();
                    tokens = stk.nextToken().trim();
                }
            }
        }
        catch (DevFailed e) {
            //System.err.println(e.errors[0].desc + "  (" + deviceManager.getName() + ")");
        }
    }
    //========================================================
    //========================================================


    /*----- PROTECTED REGION END -----*/	//	CassandraMonitor.methods


	
	
	
	
	/**
	 * Starts the server.
	 * @param args program arguments (instance_name [-v[trace level]]  [-nodb [-dlist <device name list>] [-file=fileName]])
	 */
	public static void main(final String[] args) {
		/*----- PROTECTED REGION ID(CassandraMonitor.main) ENABLED START -----*/
// /**
// 	 * Execute command "ReadCompactionHistory".
// 	 * description: Read the compaction history for specified host.
// 	 *              Each record containns:keyspace_name, columnfamily_name, compacted_at, bytes_in, bytes_out
// 	 * @param readCompactionHistoryIn Host name
// 	 * @return 
// 	 * @throws DevFailed if command execution failed.
// 	 */
// 	@Command(name="ReadCompactionHistory", inTypeDesc="Host name", outTypeDesc="")
// 	public String[] ReadCompactionHistory(String readCompactionHistoryIn) throws DevFailed {
// 		xlogger.entry();
// 		String[] readCompactionHistoryOut;
// 		
// 		//	Put command code here
// 		
// 		xlogger.exit();
// 		return readCompactionHistoryOut;
// 	}

// /**
// 	 * Read attribute DiskUsed
// 	 * 
// 	 * @return attribute value
// 	 * @throws DevFailed if read attribute failed.
// 	 */
// 	public org.tango.server.attribute.AttributeValue getDiskUsed() throws DevFailed {
// 		xlogger.entry();
// 		org.tango.server.attribute.AttributeValue
// 			attributeValue = new org.tango.server.attribute.AttributeValue();
//         long used = (long) jmxUtilities.getAttribute(DISK_USED, ATTR_VALUE);
//         diskUsed = ((double)used/1073741824);
// 		attributeValue.setValue(diskUsed);
// 		xlogger.exit();
// 		return attributeValue;
// 	}

// /**
// 	 * Read attribute HdbTableSizes
// 	 * 
// 	 * @return attribute value
// 	 * @throws DevFailed if read attribute failed.
// 	 */
// 	public org.tango.server.attribute.AttributeValue getHdbTableSizes() throws DevFailed {
// 		xlogger.entry();
// 		org.tango.server.attribute.AttributeValue
// 			attributeValue = new org.tango.server.attribute.AttributeValue();
// 		
// 		hdbTableSizes = jmxUtilities.getTableSizes();
// 		
// 		attributeValue.setValue(hdbTableSizes);
// 		xlogger.exit();
// 		return attributeValue;
// 	}


		/*----- PROTECTED REGION END -----*/	//	CassandraMonitor.main
		ServerManager.getInstance().start(args, CassandraMonitor.class);
		System.out.println("------- Started -------------");
	}
}
