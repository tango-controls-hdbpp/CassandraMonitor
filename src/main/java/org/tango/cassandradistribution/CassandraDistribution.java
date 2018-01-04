/*----- PROTECTED REGION ID(CassandraDistribution.java) ENABLED START -----*/
//=============================================================================
//
// file :        CassandraDistribution.java
//
// description : Java source for the CassandraDistribution class and its commands.
//               The class is derived from Device. It represents the
//               CORBA servant object which will be accessed from the
//               network. All commands which can be executed on the
//               CassandraDistribution are implemented in this file.
//
// project :     Cassandra distribution
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
//
// Copyright (C): 2017
//                European Synchrotron Radiation Facility
//                BP 220, Grenoble 38043
//                France
//
//=============================================================================
//                This file is generated by POGO
//        (Program Obviously used to Generate tango Object)
//=============================================================================

/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.java

package org.tango.cassandradistribution;

/*----- PROTECTED REGION ID(CassandraDistribution.imports) ENABLED START -----*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.tango.DeviceState;
import org.tango.server.InvocationContext;
import org.tango.server.ServerManager;
import org.tango.server.annotation.AroundInvoke;
import org.tango.server.annotation.Attribute;
import org.tango.server.annotation.AttributeProperties;
import org.tango.server.annotation.ClassProperty;
import org.tango.server.annotation.Command;
import org.tango.server.annotation.Delete;
import org.tango.server.annotation.Device;
import org.tango.server.annotation.DeviceProperty;
import org.tango.server.annotation.DynamicManagement;
import org.tango.server.annotation.Init;
import org.tango.server.annotation.State;
import org.tango.server.annotation.StateMachine;
import org.tango.server.annotation.Status;
import org.tango.server.annotation.DeviceManagement;
import org.tango.server.annotation.Pipe;
import org.tango.server.attribute.ForwardedAttribute;import org.tango.server.pipe.PipeValue;
import org.tango.server.dynamic.DynamicManager;
import org.tango.server.device.DeviceManager;
import org.tango.server.dynamic.DynamicManager;
import org.tango.server.events.EventManager;
import org.tango.server.events.EventType;
import org.tango.utils.DevFailedUtils;

//	Import Tango IDL types
import fr.esrf.Tango.*;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoApi.PipeBlob;
import fr.esrf.TangoApi.PipeDataElement;

import java.util.List;

/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.imports

/**
 *  CassandraDistribution class description:
 *    This class is able to read cassandra distribution (Data centers, racks, hosts, ...)
 *    It uses nodetool apache cassandra.
 *    Thant means it must run on a machine where this tool is installed.
 */

@Device
public class CassandraDistribution {

	protected static final Logger logger = LoggerFactory.getLogger(CassandraDistribution.class);
	protected static final XLogger xlogger = XLoggerFactory.getXLogger(CassandraDistribution.class);
	//========================================================
	//	Programmer's data members
	//========================================================
    /*----- PROTECTED REGION ID(CassandraDistribution.variables) ENABLED START -----*/
    
    //	Put static variables here
    
    /*----- PROTECTED REGION END -----*/	//	CassandraDistribution.variables
	/*----- PROTECTED REGION ID(CassandraDistribution.private) ENABLED START -----*/
	
	//	Put private variables here
	
	/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.private

	//========================================================
	//	Property data members and related methods
	//========================================================


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
		/*----- PROTECTED REGION ID(CassandraDistribution.initDevice) ENABLED START -----*/
		
		//	Put your device initialization code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.initDevice
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
		/*----- PROTECTED REGION ID(CassandraDistribution.deleteDevice) ENABLED START -----*/
		
		//	Put your device clearing code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.deleteDevice
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
			/*----- PROTECTED REGION ID(CassandraDistribution.aroundInvoke) ENABLED START -----*/
			
			//	Put aroundInvoke code here
			
			/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.aroundInvoke
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
		/*----- PROTECTED REGION ID(CassandraDistribution.setDynamicManager) ENABLED START -----*/
		nodeDistribution = new String[] {"Not initialized"};
		nodeDistribution = new ReadDistribution().getHostInformation();

		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.setDynamicManager
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
	 * Attribute NodeDistribution, String, Spectrum, READ
	 * description:
	 *     For each node it gives:
	 *     	- node name
	 *     	- rack name
	 *     	- data center
	 */
	@Attribute(name="NodeDistribution", isPolled=true, pollingPeriod=3000)
	@AttributeProperties(description="For each node it gives:\n	- node name\n	- rack name\n	- data center",
	                     label="Node distribution")
	private String[] nodeDistribution = new String[1024];
	/**
	 * Read attribute NodeDistribution
	 * 
	 * @return attribute value
	 * @throws DevFailed if read attribute failed.
	 */
	public org.tango.server.attribute.AttributeValue getNodeDistribution() throws DevFailed {
		xlogger.entry();
		org.tango.server.attribute.AttributeValue
			attributeValue = new org.tango.server.attribute.AttributeValue();
		/*----- PROTECTED REGION ID(CassandraDistribution.getNodeDistribution) ENABLED START -----*/
		
		//	Put read attribute code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.getNodeDistribution
		attributeValue.setValue(nodeDistribution);
		xlogger.exit();
		return attributeValue;
	}
	


	//========================================================
	//	Command data members and related methods
	//========================================================
	/**
	 * The state of the device
	*/
	@State 
	private DevState state = DevState.UNKNOWN;
	/**
	 * Execute command "State".
	 * description: This command gets the device state (stored in its 'state' data member) and returns it to the caller.
	 * @return Device state
	 * @throws DevFailed if command execution failed.
	 */
	public final DevState getState() throws DevFailed {
		/*----- PROTECTED REGION ID(CassandraDistribution.getState) ENABLED START -----*/
		
		//	Put state code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.getState
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
	@Status 
	private String status = "Server is starting. The device state is unknown";
	/**
	 * Execute command "Status".
	 * description: This command gets the device status (stored in its 'status' data member) and returns it to the caller.
	 * @return Device status
	 * @throws DevFailed if command execution failed.
	 */
	public final String getStatus() throws DevFailed {
		/*----- PROTECTED REGION ID(CassandraDistribution.getStatus) ENABLED START -----*/
		
		//	Put status code here
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.getStatus
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
	 * description: Read the compaction history for specified host.
	 * @param readCompactionHistoryIn Host name
	 * @return compation history as:
	 *         compacted_at, keyspace_name, columnfamily_name, bytes_in, bytes_out
	 * @throws DevFailed if command execution failed.
	 */
	@Command(name="ReadCompactionHistory", inTypeDesc="Host name", outTypeDesc="compation history as:\ncompacted_at, keyspace_name, columnfamily_name, bytes_in, bytes_out")
	public String[] ReadCompactionHistory(String readCompactionHistoryIn) throws DevFailed {
		xlogger.entry();
		String[] readCompactionHistoryOut;
		/*----- PROTECTED REGION ID(CassandraDistribution.readCompactionHistory) ENABLED START -----*/

		List<String> history = Utils.readCompactionHistory(readCompactionHistoryIn);
		readCompactionHistoryOut = history.toArray(new String[history.size()]);
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.readCompactionHistory
		xlogger.exit();
		return readCompactionHistoryOut;
	}
	

	//========================================================
	//	Programmer's methods
	//========================================================
	/*----- PROTECTED REGION ID(CassandraDistribution.methods) ENABLED START -----*/
	
	//	Put your own methods here
	
	/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.methods


	
	
	
	
	/**
	 * Starts the server.
	 * @param args program arguments (instance_name [-v[trace level]]  [-nodb [-dlist <device name list>] [-file=fileName]])
	 */
	public static void main(final String[] args) {
		/*----- PROTECTED REGION ID(CassandraDistribution.main) ENABLED START -----*/
		
		/*----- PROTECTED REGION END -----*/	//	CassandraDistribution.main
		ServerManager.getInstance().start(args, CassandraDistribution.class);
		System.out.println("------- Started -------------");
	}
}
