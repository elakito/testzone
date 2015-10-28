Apache CXF JAXRS websocket client sample using Apache Camel
=================================================

This demo illustrates how to develope a simple jaxrs based
websocket client. It also servers as a test case to verify if 
CXF's websocket client feature is enabled in your OSGi container.

This demo uses Apache Camel to trigger the service invocation
for its simplicity.

Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

```bash
$  mvn clean install
```

Running the demo in OSGi
------------------------
After building the sample, install the bundle to your karaf
container. The CXF websocket feature must be installed on your
karaf container. 

To install the CXF websocket feature, run the following karaf console
commands.

```bash
karaf@root()> feature:repo-add cxf 3.0.1
karaf@root()> feature:install cxf-jaxrs cxf-transports-websocket-client
```

To install this sample bundle, run the karaf console command.

```bash
karaf@root()> install -s mvn:de.elakito.testzone.samples/osgi-cxf-websocket-sample-jaxrs-client/0.0.2
```

See
[instruction_osgi_cxf_websocket_sample_route](https://github.com/elakito/testzone/blob/master/samples/instruction_osgi_cxf_websocket_sample_route.txt)
