package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import org.junit.Assert;

public class JaxRsWebAppWSTest extends JaxRsWebAppBaseTest {
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
            wsclient.reset(1);
            wsclient.sendMessage(("GET /endpoint/get/10").getBytes());
            wsclient.await(3);
            Response resp = new Response(wsclient.getReceivedBytes().get(0));
            Assert.assertEquals("10", new String(resp.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void invokePOST() {
        try {
            wsclient.reset(1);
            wsclient.sendMessage("POST /endpoint/post/20\r\nContent-Type: text/plain\r\n\r\n22".getBytes());
            wsclient.await(3);
            Response resp = new Response(wsclient.getReceivedBytes().get(0));
            Assert.assertEquals("20", new String(resp.getEntity()));
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
    }

    @Override
    protected void afterGETTest() {
    }

    @Override
    protected void beforePOSTTest() {
    }

    @Override
    protected void afterPOSTTest() {
    }

}
