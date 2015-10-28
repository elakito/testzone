Apache CXF JAXRS SwaggerSocket sample service
=================================================

This demo illustrates how to develop a simple jaxrs based
service that can be invoked using SwaggerSocket. SwaggerSocket is
a protocol that allows any existing REST services to be invoked
over WebSocket. See further information on SwaggerSocket at its
project page at
[https://github.com/swagger-api/swagger-socket](https://github.com/swagger-api/swagger-socket)

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

```bash
  mvn clean install
```

Running the demo in OSGi
------------------------
After building the sample, install the bundle to your karaf
container. The SwaggerSocket feature and the CXF websocket feature must be installed on your
karaf container.

To install the SwaggerSocket feature, run the following karaf console commands.

```bash
feature:repo-add mvn:com.wordnik/swaggersocket-karaf-features/2.0.1-SNAPSHOT/xml/features
feature:install swaggersocket-server
```


To install the CXF websocket feature, run the following karaf console
commands. (Note we need CXF 3.0.5 or newer to get the integrated atmosphere support)

```bash
  feature:repo-add cxf 3.0.5-SNAPSHOT
  feature:install cxf-jaxrs cxf-transports-websocket-server
```

To install this sample bundle, run the karaf console command.

```bash
  install -s mvn:de.elakito.testzone.samples/osgi-cxf-swaggersocket-sample-jaxrs-service/0.0.3-SNAPSHOT
```

You can invoke the provided services using your websocket client or
use the unit tests included in this package (the unit tests are deactivated
by default). 


See
[instruction_osgi_cxf_swaggersocket_sample_route](https://github.com/elakito/testzone/blob/master/samples/instruction_osgi_cxf_swaggersocket_sample_route.txt)
