package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class TestServer {
    private String address;
    private Server server;
    private Object app = new MyEndpointImpl();
    
    @Path("endpoint")
    public interface MyEndpoint {
        @GET
        @Produces({"text/plain"})
        @Path("get/{key}")
        String get(@PathParam("key") String key);

        @POST
        @Produces({"text/plain"})
        @Consumes({"text/plain"})
        @Path("post/{key}")
        String post(@PathParam("key") String key, String value);
    }

    public static final class MyEndpointImpl implements MyEndpoint {
        private final Map<String,String> map = new HashMap<String, String>();
        @Context
        private HttpHeaders headers;
        @Context 
        private HttpServletResponse response;
        
        public String get(String key) {
            if (key.startsWith("10")) {
                return key;
            }
            String value = map.get(key);
            if (value == null)
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            return value;
        }

        public String post(String key, String value) {
            if (key.startsWith("20")) {
                return key;
            }
            map.put(key, value);
            return value;
        }
        
        public Map<String,String> getMap() {
            return map;
        }
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public Object getApp() {
        if (app == null) {
            
        }
        return app;
    }

    public void setApp(Object app) {
        this.app = app;
    }

    public static void main(String[] args) throws Exception {
        TestServer testserver = new TestServer();
        String address = args.length == 1 ? args[0] : "http://localhost:8181/"; 
        testserver.setAddress(address);

        testserver.start();
        System.out.println("#### stared server at : " + address + "; waiting for debugging...");
        Thread.sleep(600000);

        testserver.stop();
        testserver.destroy();
    }
    
    public void start() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBean(app);
        sf.setAddress(address);
        server = sf.create();
        server.start();
    }
    
    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
    
    public void destroy() throws Exception {
        if (server != null) {
            server.destroy();
        }
    }

}
