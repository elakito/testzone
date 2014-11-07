Apache CXF Jaxrs service invocation over HTTP or WebSocket performance comparisons
=================================================

This test shows how the HTTP and WebSocket transports affect the performance of a simple jaxrs GET and POST service.


Building
--------
From the base directory of this sample, the pom.xml file
is used to build and run the standalone unit test.

  mvn clean install
  
Results
------------------------
See results/ for more details

|         | HTTPCond. sync | HTTPCond. async | WSCond. sync | WSCond. async | WSAHC sync |  WSAHC async |
|---------|---------------:|----------:|--------:|--------:|-------:|----------:|
| GET     | 35 ms; 2857 /s | 27 ms; 5883 /s | 34 ms; 2941 /s |  --  | 18 ms; 5555 /s | 5 ms; 20000 /s |
| POST    | 49 ms; 2040 /s | 152 ms; 657 /s | 37 ms; 2702 /s |  --  | 20 ms; 5000 /s | 4 ms; 25000 /s |
| GET(hc) | 36 ms; 2777 /s | 24 ms; 4146 /s | --      |  --     | --     | --        |
| POST(hc)| 50 ms; 2000 /s | 28 ms; 3571 /s | --      |  --     | --     | --        |

Each test invoking 100 GET and POST CXF jaxrs services over HTTP or WebSocket, 
repeated 100 times, and the best time taken. 
http-hc (nio) was enabled for the last two rows

There seems to be some issues with the POST invocation for the asynchronous case when using the
normal cxf's http transport component. In contrast, using cxf's http-hc transport does not exhibit
this issue. However, increasing the repeat and count values when using the http-hc component will
lead to other issues that lead to test failures, whereas the same values do not lead to test
failures when using the normal http component.

Test options
The following maven options are available to change the setting of the tests.

|  maven option | effect                                                     |
|---------------|------------------------------------------------------------|
|-Dtest.count   | the number of times to invoke a service in a test (default 100) |
|-Dtest.repeat  | the maximum number of times to repeat each test (default 1000)  |
|-Dtest.stop    | the number of times to run each test after there is no update to the best time (default 50)  |
|-Dtest.delay   | the delay in millisecond between each test run (default 500) |
|-Dtest.disable | a list of test variants to disable e.g. get (default null) |
|-Pasync        | enabling cxf-transports-http-hc                            |





