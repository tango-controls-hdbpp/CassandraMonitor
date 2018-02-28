# Cassandra Monitor Tango Device Server and Client
[![Download](https://api.bintray.com/packages/tango-controls/maven/CassandraMonitor/images/download.svg)](https://bintray.com/tango-controls/maven/CassandraMonitor/_latestVersion)
[![TangoControls](https://img.shields.io/badge/-Tango--Controls-7ABB45.svg?style=flat&logo=%20data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAACAAAAAkCAYAAADo6zjiAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEwAACxMBAJqcGAAAAsFJREFUWIXtl01IFVEYht9zU%2FvTqOxShLowlOgHykWUGEjUKqiocB1FQURB0KJaRdGiaFM7gzZRLWpTq2olhNQyCtpYCP1gNyIoUTFNnxZzRs8dzvw4Q6564XLnfOf73vedc2a%2BmZEKALgHrC3CUUR8CxZFeEoFalsdM4uLmMgFoIlZLJp3A9ZE4S2oKehhlaR1BTnyg2ocnW%2FxsxEDhbYij4EPVncaeASMAavnS%2FwA8NMaqACNQCew3f4as3KZOYh2SuqTVJeQNiFpn6QGSRVjTH9W%2FiThvcCn6H6n4BvQDvQWFT%2BSIDIFDAKfE3KOAQeBfB0XGPeQvgE67P8ZoB44DvTHmFgJdOQRv%2BUjc%2BavA9siNTWemgfA3TwGquCZ3w8szFIL1ALngIZorndvgJOR0GlP2gtJkzH%2Bd0fGFxW07NqY%2FCrx5QRXcYjbCbmxF1dkBSbi8kpACah3Yi2Sys74cVyxMWY6bk5BTwgRe%2BYlSzLmxNpU3aBeJogk4XWWpJKUeiap3RJYCpQj4QWZDQCuyIAk19Auj%2BAFYGZZjTGjksaBESB8P9iaxUBIaJzjZcCQcwHdj%2BS2Al0xPOeBYYKHk4vfmQ3Y8YkIwRUb7wQGU7j2ePrA1URx93ayd8UpD8klyPbSQfCOMIO05MbI%2BDvwBbjsMdGTwlX21AAMZzEerkaI9zFkP4AeYCPBg6gNuEb6I%2FthFgN1KSQupqzoRELOSed4DGiJala1UmOMr2U%2Bl%2FTWEy9Japa%2Fy41IWi%2FJ3d4%2FkkaAw0Bz3AocArqApwTvet3O3GbgV8qqjAM7bf4N4KMztwTodcYVyelywKSCD5V3xphNXoezuTskNSl4bgxJ6jPGVJJqbN0aSV%2Bd0M0aO7FCs19Jo2lExphXaTkxdRVgQFK7DZVDZ8%2BcpdmQh3wuILh7ut3AEyt%2B51%2BL%2F0cUfwFOX0t0StltmQAAAABJRU5ErkJggg%3D%3D)](http://www.tango-controls.org) 
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

The CassandraMonitor device server is a Tango device server
    (see [tango-controls website](http://www.tango-controls.org)),
     able to monitor what's happening on a given Apache Cassandra node.

The goal is to benefit from the Tango Controls features to be able to identify the current state of
    Cassandra nodes and to allow interested Tango clients to react according to the current state
    (triggering alarms, notifying Tango clients) for instance.
    
Another goal is to use the Tango archiving capabilities (like HDB++) to monitor interesting Cassandra metrics.
The device server is written in JAVA and is using JMX internally to access Cassandra metrics.

The CassandraMonitor client is a JAVA GUI dedicated to display information provided by the device.

This project was originally developed as part of the
  [Tango HDB++ project](http://tango-controls.readthedocs.io/en/latest/tools-and-extensions/archiving/HDB++.html)
  (Cassandra version) but could be used independently to monitor any Cassandra node from a TANGO Control System.

### Prerequisites

A configured Tango Control System is required to be able to run this device server.
Java must be installed on the machine this device server will be running on.
The Cassandra nodes this device server will monitor must be configured to allow JMX access from the machine
    where CassandraMonitor device server will be running.
    Please refer to Apache Cassandra documentation to know how to configure JMX.
    This is configured in cassandra-env.sh configuration file.

For the moment, CassandraMonitor has been developed and tested with Cassandra version 2.2.11.
It might work with other versions but there is no warranty since there might be incompatible changes
    in the Cassandra JMX interface.

To compile this project you need [Maven](https://maven.apache.org/) and then:
```
git clone git@github.com:tango-controls-hdbpp/CassandraMonitor.git
cd CassandraMonitor
mvn package
```

### Installing

You can download the jar file from Bintray:
[![Download](https://api.bintray.com/packages/tango-controls/maven/CassandraMonitor/images/download.svg)](https://bintray.com/tango-controls/maven/CassandraMonitor/_latestVersion)

To run a test instance of the device server (named test), you can execute the following instructions:
An instance of the CassandraMonitor device server must be defined in the Tango database of your choice,
if you are using a database.

You can use **Jive** Server Wizard (from the **tools** menu) or use the **Create server** entry from jive's
**Edit** menu to define the instance and declare new devices.

The device server can be started without Tango database too.
Please refer to the Tango-Controls documentation for these steps.

To run the device serer you will need the following jar files:
* JTango.jar: [![JTango.jar](https://api.bintray.com/packages/tango-controls/jtango/JTango/images/download.svg)](https://bintray.com/tango-controls/jtango/JTango)
* libhdbpp-java.jar: [![libhdbpp-java.jar](https://api.bintray.com/packages/tango-controls/maven/libhdbpp-java/images/download.svg)](https://bintray.com/tango-controls/maven/libhdbpp-java)

To run the client you will need the same jar files and:
* ATKCore.jar: [![ATKCore.jar](https://api.bintray.com/packages/tango-controls/maven/ATKCore/images/download.svg)](https://bintray.com/tango-controls/maven/ATKCore)
* ATKWidget.jar: [![ATKWidget.jar](https://api.bintray.com/packages/tango-controls/maven/ATKWidget/images/download.svg)](https://bintray.com/tango-controls/maven/ATKWidget)
* Jive.jar: [![Jive.jar](https://api.bintray.com/packages/tango-controls/maven/Jive/images/download.svg)](https://bintray.com/tango-controls/maven/Jive)

In run the device server or the client you must put all these jar files in the CLASSPATH environment.
You need to set the HDB_TYPE to cassandra and set the HDB_CONTACT_POINTS to your cassandra cluster entry points.

Your start up script must look like (for server or client):

```
JARS=<path/to/jar/files>
CLASSPATH=$JARS/libhdbpp-java.jar:$JARS/JTango.jar>:$JARS/ATKCore.jar:$JARS/ATKWidget.jar
CLASSPATH=$CLASSPATH:$JARS/Jive.jar:$JARS/CassandraMonitor.jar
export CLASSPATH

export HDB_TYPE=cassandra
export HDB_CONTACT_POINTS=hdb1,hdb2

#  Start the device server (instance name is test)
java org.tango.cassandramonitor.CassandraMonitor  test

#  Start the client 
export ClassName=CassandraMonitor
export InstanceName=test
java org.tango.cassandramonitor_client.gui.CassandraMonitoring
```

Please refer to the Tango-Controls documentation to learn in details how to declare a new device server.

Node property must be created for your devices. You can use jive to create this property.
This Node property defines the hostname of the Cassandra node the CassandraMonitor device will talk to.
If you have configured JMX with authentication, you will have to define JMXUser and JMXPassword device properties.
If a Cassandra node is configured to listen on a JMX port different than the default one (7199),
JMXPort device property must be defined.

TANGO_HOST environment variable must be defined to be able to communicate with the Tango Database server
 (if you are using a Tango database).

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct,
    and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available,
    see the [tags on this repository](https://github.com/tango-controls-hdbpp/CassandraMonitor/tags). 

## Authors

* [Reynald Bourtembourg] (https://github.com/bourtemb)
* [Pascal Verdier] (https://github.com/Pascal-Verdier)

See also the list of [contributors](https://github.com/tango-controls/CassandraMonitor/contributors) 
    who participated in this project.

## License

This project is licensed under the GPL V3 License - see the [LICENSE.md](LICENSE.md) file for details.

## Related links

* [Tango-Controls](http://www.tango-controls.org)
* [Tango HDB++ project](http://tango-controls.readthedocs.io/en/latest/tools-and-extensions/archiving/HDB++.html)

## Acknowledgments

* Billie Thompson * - [PurpleBooth](https://github.com/PurpleBooth) for the [README](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2) and [CONTRIBUTING](https://gist.github.com/PurpleBooth/b24679402957c63ec426) templates.


