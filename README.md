Testzone
========

This is a repository for some test and sample applications.

List of and sample applications and tests
------------------------------------
Samples

* samples/osgi_cxf_jaxws_greeter[_bp]

a scenario bundle using Apache CXF's jaxws with Apache Camel to trigger each call.
see samples/instruction_osgi_cxf_jaxws_greeter.txt for the instruction.

* samples/osgi_cxf_jaxrs_greeter[_bp]

a scenario bundle using Apache CXF's jaxrs with Apache Camel to trigger each call.
see samples/instruction_osgi_cxf_jaxrs_greeter.txt for the instruction.

* samples/osgi_camel_cxf_jaxws_greeter_bp

a scenario bundle using Apache CXF's jaxws with Apache Camel to route jaxws 
service calls based on the content to one of the specified target services.
see samples/instruction_osgi_camel_cxf_jaxws_greeter.txt for the instruction.

* samples/osgi_camel_websocket_sample_route[_bp]

a scenario bundle using Apache Camel's server-side websocket component atmosphere-websocket
see samples/instruction_osgi_camel_websocket_sample_route.txt for the instruction.

* samples/osgi_camel_websocket_service[_bp]

a servlet registration service to run websocket service-side scenarios

* samples/osgi_camel_ws_sample_route[_bp]

a scenario bundle using Apache Camel's client-side websocket component ahc-ws to call a camel's server-side websocket service hosted by osgi_camel_websocket_sample_route[_bp]

* samples/osgi_camel_ws_sample_cxf_jaxrs

a scenario using Apache Camel's client-side websocket component ahc-ws to call a cxf's jaxrs service over websocket.

* samples/osgi_camel_websocket_sample_logfeeder

a a log-feeder application using Camel's stream and atmosphere-websocket components to push the logging events to the browser over the websocket.

* samples/osgi_cxf_websocket_sample_jaxrs_client

a scenario bundle using Apache CXF's jaxrs client using the websocket transport with Apache Camel to trigger each call.

* samples/osgi_cxf_websocket_sample_jaxrs_service

a scenario bundle using Apache CXF's jaxrs service using the websocket transport
see samples/instruction_osgi_cxf_websocket_sample_route.txt for the instruction.

Tests

* cxf_cos_comparison

a performance test for CXF's CachedOutputStream's various encryption algorithms.


* camel_xmltokenize_comparison

a performance test for Camel's xmltokenizers.
