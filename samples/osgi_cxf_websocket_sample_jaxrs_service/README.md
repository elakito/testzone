Apache CXF Jaxrs websocket sample service
=================================================

This demo illustrates how to develop a simple jaxrs based
service that can be invoked over websocket.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Running the demo in OSGi
------------------------
After building the sample, install the bundle to your karaf
container. The CXF websocket feature must be installed on your
karaf container. 

To install the CXF websocket feature, run the following karaf console
commands.

feature:repo-add cxf 3.0.1
feature:install cxf cxf-transports-websocket-server

To install this sample bundle, run the karaf console command.

install mvn:me.temp.samples/osgi-cxf-websocket-sample-jaxrs-service/0.0.3

You can invoke the provided services using your websocket client or
use the unit tests included in this package (the unit tests are deactivated
by default). For the latter, run the following maven command

mvn -P\!noTest test










