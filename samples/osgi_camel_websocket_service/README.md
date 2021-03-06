Apache Camel Atmosphere-Websocket servlet service sample (Spring)
=================================================

This bundle publishes CamelWebSocketServlet as an OSGi service using
SpringDM.

When this bundle is deployed on karaf, the service will be hosted at

  http://localhost:8181/camel/websockets/

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

See
[instruction_osgi_camel_websocket_sample_route](https://github.com/elakito/testzone/blob/master/samples/instruction_osgi_camel_websocket_sample_route.txt)
