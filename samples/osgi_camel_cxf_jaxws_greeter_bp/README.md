Apache Camel route with CXF endpoints sample
=================================================

This demo illustrates how to develope a simple camel-cxf
based jaxws routing scenario. It also servers as a test
case to verify if Camel's CXF endpoints and CXF's 
Jaxws are correctly enabled in your OSGi container.

This demo uses Apache Camel to route Jaxws service calls
to one of the specified target CXF services based on the content.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Running the demo in OSGi
------------------------
After building the sample, start Karaf and install it using
Karaf command:

  install -s mvn:de.elakito.testzone.samples/osgi-cxf-jaxws-greeter-bp

This scenario includes two CXF jaxws Greeter endpoints which are
invoked from the Camel route. Which one is invoked is depends on
the name greeted. If the name ends with a vocal, the spanisch version
of Greeter is invoked. Otherwise, the default english verison of Greeter
is invoked.

If the CXF servlet is running at other than http://localhost:8181/cxf,
set the desired target endpoints in the configuration file 
me.temp.samples.osgi_camel_cxf_greeter.cfg and place it in the configuration 
directory of the OSGi container. (e.g., etc in Apache Karaf).

See
../instruction_osgi_camel_cxf_jaxws_greeter.txt
