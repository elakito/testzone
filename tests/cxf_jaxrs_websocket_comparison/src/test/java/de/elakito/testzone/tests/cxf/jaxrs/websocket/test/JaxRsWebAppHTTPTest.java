package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import javax.ws.rs.core.HttpHeaders;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Assert;

import de.elakito.testzone.tests.cxf.jaxrs.websocket.test.TestServer.MyEndpoint;

public class JaxRsWebAppHTTPTest extends JaxRsWebAppBaseTest {
    private TestServer.MyEndpoint resource;
    private Client client;
    
    @Override
    protected String getWebResourcePath() {
        return "/jaxrs_http_webapp";
    }

    @Override
    protected void setUpClient() {
        resource = JAXRSClientFactory.create("http://localhost:8181/", MyEndpoint.class);
        client = WebClient.client(resource);
        client.header(HttpHeaders.USER_AGENT, JaxRsWebAppHTTPTest.class.getName());
    }

    @Override
    protected void cleanUpClient() {
        client.close();
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
                Assert.assertEquals("10", resource.get("10"));
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
                Assert.assertEquals("20", resource.post("20", "22"));
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
