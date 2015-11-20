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
                    wsclient.sendMessage(("GET /endpoint/get/10").getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void beforeMethodTest() {
                wsclient.reset(CALL_COUNT);
            }
            @Override
            public void afterMethodTest() {
                try {
                    wsclient.await(5);
                    List<Object> responses = wsclient.getReceived();
                    Assert.assertEquals(CALL_COUNT, responses.size());
                    for (int i = 0; i < CALL_COUNT; i++) {
                        Response resp = new Response(responses.get(i));
                        Assert.assertEquals("10", resp.getTextEntity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    wsclient.sendMessage("POST /endpoint/post/20\r\nContent-Type: text/plain\r\n\r\n22".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void beforeMethodTest() {
                wsclient.reset(CALL_COUNT);
            }
            @Override
            public void afterMethodTest() {
                try {
                    wsclient.await(5);
                    List<Object> responses = wsclient.getReceived();
                    Assert.assertEquals(CALL_COUNT,responses.size());
                    for (int i = 0; i < CALL_COUNT; i++) {
                        Response resp = new Response(responses.get(i));
                        Assert.assertEquals("20", resp.getTextEntity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
