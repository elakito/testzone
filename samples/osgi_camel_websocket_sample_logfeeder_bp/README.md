Apache Camel Atmosphere-Websocket Stockquote sample (Blueprint)
=================================================

This bundle uses websocket servlet service published by
osgi_camel_websocket_service_bp and implements a scenario that feeds
...


        <route>
            <from uri="atmosphere-websocket:///logfeeder"/>
        </route>

This route is hosted at
  http://localhost:8181/camel/websockets/logfeeder

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
../instruction_osgi_camel_websocket_sample_route.txt
