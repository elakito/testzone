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
    protected void cleanUpClient() {
        wsclient.close();
    }

    @Override
    protected Tester createGETTester() {
        return new Tester() {
            @Override
            public String getMethod() {
                return "GET";
            }
            @Override
            public void invokeMethod() {
                try {
                    wsclient.reset(1);
                    wsclient.sendMessage(("GET /endpoint/get/10").getBytes());
                    wsclient.await(3);
                    Response resp = new Response(wsclient.getReceived().get(0));
                    Assert.assertEquals("10", resp.getTextEntity());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void beforeMethodTest() {
            }
            @Override
            public void afterMethodTest() {
            }
        };
    }

    @Override
    protected Tester createPOSTTester() {
        return new Tester() {
            @Override
            public String getMethod() {
                return "POST";
            }
            @Override
            public void invokeMethod() {
                try {
                    wsclient.reset(1);
                    wsclient.sendMessage("POST /endpoint/post/20\r\nContent-Type: text/plain\r\n\r\n22".getBytes());
                    wsclient.await(3);
                    Response resp = new Response(wsclient.getReceived().get(0));
                    Assert.assertEquals("20", resp.getTextEntity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void beforeMethodTest() {
            }
            @Override
            public void afterMethodTest() {
            }
        };
    }

}
