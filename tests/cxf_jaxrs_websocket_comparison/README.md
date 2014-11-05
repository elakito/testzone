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

|        | HTTPCond. sync | HTTPCond. async | WSCond. sync | WSCond. async | WSAHC sync |  WSAHC async |
|--------|----------:|----------:|--------:|--------:|-------:|----------:|
| GET    |  49 ms    | 29 ms     | 43 ms   |  --     | 19 ms  | 6 ms      |
|        |  2040 /s  | 3448 /s   | 2345 /s |  --     | 5263 /s| 16666 /s  |
| POST   |  46 ms    | 157 ms    | 39 ms   |  --     | 20 ms  | 4 ms      |
|        |  2173 /s  | 634 /s    | 2564 /s |  --     | 5000 /s| 25000 /s  |
| GET    |  48 ms    | 41 ms     | --      |  --     | --     | --        |
| http-hc|  2083 /s  | 2439 /s   | --      |  --     | --     | --        |
| POST   |  49 ms    | 26 ms     | --      |  --     | --     | --        |
| http-hc|  2040 /s  | 3846 /s   | --      |  --     | --     | --        |

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





