package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import java.util.concurrent.Future;

import javax.ws.rs.client.Entity;

import org.apache.cxf.jaxrs.client.WebClient;

import org.junit.Assert;

public class JaxRsWebAppHTTPAsyncTest extends JaxRsWebAppBaseTest {
    private WebClient client;
    private Future<?>[] asyncResults = new Future<?>[CALL_COUNT];
    private int index;
        
    @Override
    protected String getWebResourcePath() {
        return "/jaxrs_http_webapp";
    }

    @Override
    protected void setUpClient() {
    }

    @Override
    protected void invokeGET() {
        asyncResults[index++] = client.async().get(String.class);
    }

    @Override
    protected void invokePOST() {
        asyncResults[index++] = client.async().post(Entity.text("22"), String.class);
    }

    @Override
    protected void cleanUpClient() {
    }

    @Override
    protected void beforeGETTest() {
        client = WebClient.create("http://localhost:8181/endpoint/get/10");
        index = 0;
    }

    private boolean waitForResults(long maxtime) {
        boolean waiting = true;
        long waittime = 0;
        while (waiting && waittime < maxtime) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
            waittime += 10;
            waiting = false;
            for (int i = 0; i < CALL_COUNT; i++) {
                if (!asyncResults[i].isDone()) {
                    waiting = true;
                    break;
                }
            }
        }
        return waiting;
    }
    
    @Override
    protected void afterGETTest() {
        Assert.assertFalse("Not done", waitForResults(1000));
        
        for (int i = 0; i < CALL_COUNT; i++) {
            try {
                Assert.assertEquals("10", asyncResults[i].get());
            } catch (Exception e) {
                Assert.fail("error in retrieving the result");
            }
        }
        client.close();
    }

    @Override
    protected void beforePOSTTest() {
        client = WebClient.create("http://localhost:8181/endpoint/post/20");
        index = 0;
    }

    @Override
    protected void afterPOSTTest() {
        Assert.assertFalse("Not done", waitForResults(1000));
        
        for (int i = 0; i < CALL_COUNT; i++) {
            try {
                Assert.assertEquals("20", asyncResults[i].get());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("error in retrieving the result");
            }
        }
        client.close();
    }
}
