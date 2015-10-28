Apache Camel AHC-Websocket client route sample (Blueprint)
=================================================

This bundle invokes a websocket server consumer 
endpoint published by osgi_camel_websocket_sample_route_bp
using a camel route using websocket client endpoints.
More precisely, a file consumer endpoint reads a file and 
forwards it to a websocket client producer which invokes
the websocket hola service. Any data sent back from the
websocket hola service is saved in a file producer endpoint.

        <route>
            <from uri="file:///tmp/camel-file/ws/hola/in"/>
            <process ref="websocketProcessor"/>
            <to uri="ahc-ws://localhost:8181/camel/websockets/hola"/>
        </route>
        <route>
            <from uri="ahc-ws://localhost:8181/camel/websockets/hola"/>
            <process ref="websocketProcessor"/>
            <to uri="file:///tmp/camel-file/ws/hola/out"/>
        </route>

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
