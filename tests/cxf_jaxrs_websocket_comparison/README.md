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
| GET     | 49 ms; 2040 /s | 29 ms; 3448 /s | 43 ms; 2345 /s |  --  | 19 ms; 5263 /s | 6 ms; 16666 /s |
| POST    | 46 ms; 2173 /s | 157 ms; 634 /s | 39 ms; 2564 /s |  --  | 20 ms; 5000 /s | 4 ms; 25000 /s |
| GET(hc) | 48 ms; 2083 /s | 41 ms; 2439 /s | --      |  --     | --     | --        |
| POST(hc)| 49 ms; 2040 /s | 26 ms; 3846 /s | --      |  --     | --     | --        |

Each test invoking 100 GET and POST CXF jaxrs services over HTTP or WebSocket, 
repeated 100 times, and the best time taken. 
http-hc (nio) was enabled for the last two rows

Test options
The following maven options are available to change the setting of the tests.

|  maven option | effect                                                     |
|---------------|------------------------------------------------------------|
|-Dtest.repeat  | the number of times to repeat each test (default 100)      |
|-Dtest.count   | the number of times to invoke a service in a test (default 100) |
|-Dtest.disable | a list of test variants to disable e.g. get (default none) |
|-Pasync        | enabling cxf-transports-http-hc                            |





