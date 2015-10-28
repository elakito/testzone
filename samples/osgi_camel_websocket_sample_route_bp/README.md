Apache Camel Atmosphere-Websocket Echo sample (Blueprint)
=================================================

This bundle uses websocket servlet service published by
osgi_camel_websocket_service_bp
and implements a scenario having two routes using Bluepring: 
the first one greets to the caller and the second one greets to 
everyone.

        <route>
            <from uri="atmosphere-websocket:///hola"/>
            <process ref="websocketProcessor"/>
            <to uri="atmosphere-websocket:///hola"/>
        </route>
        <route>
            <from uri="atmosphere-websocket:///hola2"/>
            <process ref="websocketProcessor"/>
            <to uri="atmosphere-websocket:///hola2?sendToAll=true"/>
        </route>

The first route is hosted at
  http://localhost:8181/camel/websockets/hola 
and the second route at 
  http://localhost:8181/camel/websockets/hola2

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
