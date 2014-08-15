Apache CXF Jaxrs client sample using Apache Camel
=================================================

This demo illustrates how to develope a simple jaxrs based
service. It also servers as a test case to verify if CXF's
Jaxrs is correctly enabled in your OSGi container.

This demo uses Apache Camel to trigger the service invocation
for its simplicity.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Running the demo in OSGi
------------------------
After building the sample, copy the jar file at the target
directory into the hot deployment directory of the OSGi
container (e.g., deploy in Apache Karaf).


If the CXF servlet is running at other than http://localhost:8181/cxf,
set the correct endpoint URL in the configuration file 
me.temp.samples.osgi_cxf_greeter.cfg and place it in the configuration 
directory of the OSGi container. (e.g., etc in Apache Karaf). A sample
configuration file is located at src/test/resources/etc.







