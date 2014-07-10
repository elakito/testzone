Testzone
========

This is a repository for some test and sample applications.

List of and sample applications and tests
------------------------------------
Samples

* samples/osgi_cxf_jaxws_greeter[_bp]

a scenario bundle using Apache CXF's jaxws with Apache Camel to trigger each call.

* samples/osgi_cxf_jaxrs_greeter[_bp]

a scenario bundle using Apache CXF's jaxrs with Apache Camel to trigger each call.

* samples/osgi_camel_websocket_sample_route[_bp]

a scenario bundle using Apache Camel's server-side websocket scenario

* samples/osgi_camel_websocket_service[_bp]

a servlet registration service to run websocket service-side scenarios

* samples/osgi_camel_ws_sample_route[_bp]

a scenario bundle using Apache Camel's client-side websocket scenario

* samples/osgi_cxf_websocket_sample_jaxrs_client

a scenario bundle using Apache CXF's jaxrs client using the websocket transport with Apache Camel to trigger each call.

* samples/osgi_cxf_websocket_sample_jaxrs_service

a scenario bundle using Apache CXF's jaxrs service using the websocket transport

Tests

* cxf_cos_comparison

a performance test for CXF's CachedOutputStream's various encryption algorithms.


* camel_xmltokenize_comparison

a performance test for Camel's xmltokenizers.
