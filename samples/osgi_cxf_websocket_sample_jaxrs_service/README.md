Apache CXF JAXRS websocket sample service
=================================================

This demo illustrates how to develop a simple jaxrs based
service that can be invoked over websocket.

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
container. The CXF websocket feature must be installed on your
karaf container. 

To install the CXF websocket feature, run the following karaf console
commands.

```bash
  feature:repo-add cxf 3.0.1
  feature:install cxf-jaxrs cxf-transports-websocket-server
```

To install this sample bundle, run the karaf console command.

```bash
  install -s mvn:de.elakito.testzone.samples/osgi-cxf-websocket-sample-jaxrs-service/0.0.2
```

You can invoke the provided services using your websocket client or
use the unit tests included in this package (the unit tests are deactivated
by default). For the latter, run the following maven command

mvn -P\!noTest test

See
../instruction_osgi_cxf_websocket_sample_route.txt
