This bundle uses websocket servlet service published by
osgi_camel_websocket_service_bp and implements a scenario that queries 
the stock quote of the specified ticker symbol 
and broadcasts its result in the json format to every listener.
The stock quotes are retrieved from www.webservicex.net.

        <route>
            <from uri="atmosphere-websocket:///watchstockquotes"/>
            <setHeader headerName="CamelHttpMethod"><constant>GET</constant></setHeader>
            <setHeader headerName="CamelHttpQuery"><simple>symbol=${in.body}</simple></setHeader>

            <to uri="http://www.webservicex.net/stockquote.asmx/GetQuote"/>

            <process ref="dataExtractor"/>
            <marshal ref="xmljson"/>

            <!-- for testing this demo using websocket.org's echo demo, we must convert bytes to string -->
            <convertBodyTo type="java.lang.String"/>

            <to uri="atmosphere-websocket:///watchstockquotes?sendToAll=true"/>
        </route>

This route is hosted at
  http://localhost:8181/camel/websockets/watchstockquotes











