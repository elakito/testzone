package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import java.util.List;

import org.junit.Assert;

public class JaxRsWebAppWSAsyncTest extends JaxRsWebAppBaseTest {
    private WsTestClient wsclient;
    
    @Override
    protected String getWebResourcePath() {
        return "/jaxrs_ws_webapp";
    }

    @Override
    protected void setUpClient() {
        try {
            wsclient = new WsTestClient("ws://localhost:8181/endpoint", 1);
            wsclient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void invokeGET() {
        try {
            wsclient.sendMessage(("GET /endpoint/get/10").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void invokePOST() {
        try {
            wsclient.sendMessage("POST /endpoint/post/20\r\nContent-Type: text/plain\r\n\r\n22".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void cleanUpClient() {
        wsclient.close();
    }

    @Override
    protected void beforeGETTest() {
        wsclient.reset(CALL_COUNT);
    }

    @Override
    protected void afterGETTest() {
        try {
            wsclient.await(300);
            List<byte[]> rawresponses = wsclient.getReceivedBytes();
            Assert.assertEquals(CALL_COUNT, rawresponses.size());
            for (int i = 0; i < CALL_COUNT; i++) {
                Response resp = new Response(rawresponses.get(i));                
                Assert.assertEquals("10", new String(resp.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforePOSTTest() {
        wsclient.reset(CALL_COUNT);
    }

    @Override
    protected void afterPOSTTest() {
        try {
            wsclient.await(300);
            List<byte[]> rawresponses = wsclient.getReceivedBytes();
            Assert.assertEquals(CALL_COUNT, rawresponses.size());
            for (int i = 0; i < CALL_COUNT; i++) {
                Response resp = new Response(rawresponses.get(i));                
                Assert.assertEquals("20", new String(resp.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
