This bundle uses websocket servlet service published by
osgi_camel_websocket_service_bp
and implements a scenario that queries the stock quote of the specified ticker symbol 
and broadcasts its result in the json format to every listener.

        <route>
            <from uri="wsservlet:///watchstockquotes"/>
            <process ref="stockQuoteProcessor"/>
	    <marshal ref="xmljson"/>
            <to uri="wsservlet:///watchstockquotes?sendToAll=true"/>
        </route>

This route is hosted at
  http://localhost:8181/camel/websockets/watchstockquotes











