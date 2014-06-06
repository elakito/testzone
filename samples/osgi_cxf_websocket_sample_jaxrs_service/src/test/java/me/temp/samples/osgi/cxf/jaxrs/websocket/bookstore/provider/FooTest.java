package me.temp.samples.osgi.cxf.jaxrs.websocket.bookstore.provider;


import org.junit.Test;

public class FooTest {
    static final String ADDRESS = "ws://localhost:8181/cxf/RestContext/jaxrs_websocket_bookstore_bp/web/bookstore";

    @Test
    public void testFoo() throws Exception {
        WebSocketTestClient client = new WebSocketTestClient(ADDRESS);
        client.connect();
        client.close();

    }
}
