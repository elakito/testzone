This bundle invokes a websocket server consumer 
endpoint published by osgi_camel_websocket_sample_route_bp
using a camel route using websocket client endpoints.
More precisely, a file consumer endpoint reads a file and 
forwards it to a websocket client producer which invokes
the websocket hola service. Any data sent back from the
websocket hola service is saved in a file producer endpoint.

        <route>
            <from uri="file:///tmp/camel-file/ws/hola/in"/>
            <process ref="websocketProcessor"/>
            <to uri="ws://localhost:8181/camel/websockets/hola"/>
        </route>
        <route>
            <from uri="ws://localhost:8181/camel/websockets/hola"/>
            <process ref="websocketProcessor"/>
            <to uri="file:///tmp/camel-file/ws/hola/out"/>
        </route>
