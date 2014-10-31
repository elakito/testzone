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
    protected void invokeGET() {
        Assert.assertEquals("10", resource.get("10"));
    }

    @Override
    protected void invokePOST() {
        Assert.assertEquals("20", resource.post("20", "22"));
    }

    @Override
    protected void cleanUpClient() {
        client.close();
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
        // TODO Auto-generated method stub
        
    }

}
