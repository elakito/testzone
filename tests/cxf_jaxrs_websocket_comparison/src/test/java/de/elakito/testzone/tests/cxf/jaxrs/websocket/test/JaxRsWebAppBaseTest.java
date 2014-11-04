package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

public abstract class JaxRsWebAppBaseTest {
    protected static int RUN_REPEAT;
    protected static int CALL_COUNT;
    protected static boolean GET_DISABLED;
    protected static boolean POST_DISABLED;

    private org.eclipse.jetty.server.Server server;
    
    protected abstract String getWebResourcePath();
    
    protected abstract void setUpClient();
    
    protected abstract void invokeGET();
    protected abstract void beforeGETTest();
    protected abstract void afterGETTest();
    
    protected abstract void invokePOST();
    protected abstract void beforePOSTTest();
    protected abstract void afterPOSTTest();
    
    protected abstract void cleanUpClient();

    @BeforeClass
    public static void readProperties() {
        RUN_REPEAT = Integer.getInteger("test.repeat", 100);
        CALL_COUNT = Integer.getInteger("test.count", 1000);
        String prop = System.getProperty("test.disable");
        if (prop != null) {
                prop = prop.toLowerCase();
                GET_DISABLED = prop.indexOf("get") >= 0;
                POST_DISABLED = prop.indexOf("post") >= 0;
        }
    }

    protected void setUpServer() {
        server = new org.eclipse.jetty.server.Server();
        String resourcePath = getWebResourcePath();
        
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8181);
        server.setConnectors(new Connector[] {connector});
        
        WebAppContext webappcontext = new WebAppContext();
        String contextPath = null;
        try {
            contextPath = getClass().getResource(resourcePath).toURI().getPath();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        webappcontext.setContextPath("/");

        webappcontext.setWar(contextPath);
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {webappcontext, new DefaultHandler()});
        server.setHandler(handlers);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }
    
    protected void cleanUpServer() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static boolean waitForResults(Future<?>[] results, long maxtimetowait) {
        boolean waiting = true;
        while (waiting && maxtimetowait > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //ignore
            }
            maxtimetowait -= 10;
            waiting = false;
            for (int i = 0; i < results.length; i++) {
                if (!results[i].isDone()) {
                    waiting = true;
                    break;
                }
            }
        }
        return waiting;
    }

    @Test
    public void testCalls() throws Exception {
        setUpServer();

        try {
            setUpClient();

            long getbesttime = Long.MAX_VALUE;
            if (!GET_DISABLED) {
                for (int j = 1; j <= RUN_REPEAT; j++) {
                    long begintime = System.currentTimeMillis();
                    beforeGETTest();
                    for (long i = 0; i < CALL_COUNT; i++) {
                        invokeGET();
                    }
                    afterGETTest();
                    long endtime = System.currentTimeMillis();
                    long difftime = endtime - begintime;
                    System.out.println("Run " + j + ": Time taken for " + CALL_COUNT + " GET calls: " + difftime + "ms; " + (CALL_COUNT * 1000 / difftime) + " calls/s");
                    if (getbesttime > difftime) {
                        getbesttime = difftime;
                    }
                }
            }

            long postbesttime = Long.MAX_VALUE;
            if (!POST_DISABLED) {
                for (int j = 1; j <= RUN_REPEAT; j++) {
                    long begintime = System.currentTimeMillis();
                    beforePOSTTest();
                    for (long i = 0; i < CALL_COUNT; i++) {
                        invokePOST();
                    }
                    afterPOSTTest();
                    long endtime = System.currentTimeMillis();
                    long difftime = endtime - begintime;
                    System.out.println("Run " + j + ": Time taken for " + CALL_COUNT + " POST calls: " + difftime + "ms; " + (CALL_COUNT * 1000 / difftime) + " calls/s");
                    if (postbesttime > difftime) {
                        postbesttime = difftime;
                    }
                }
            }
            
            System.out.println("Result for " + this.getClass().getSimpleName());
            if (GET_DISABLED) {
                System.out.println("GET Tests disabled");
            } else {
                System.out.println("Best Time taken for " + CALL_COUNT + " GET calls: " + getbesttime + "ms; " + (CALL_COUNT * 1000 / getbesttime) + " calls/s");
            }
            if (POST_DISABLED) {
                System.out.println("POST Tests disabled");
            } else {
                System.out.println("Best Time taken for " + CALL_COUNT + " POST calls: " + postbesttime + "ms; " + (CALL_COUNT * 1000 / postbesttime) + " calls/s");
            }
            
            cleanUpClient();
            cleanUpServer();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }


    //from systests/jaxrs
    protected static class Response {
        private byte[] data;
        private int pos; 
        private int statusCode;
        private String contentType;
        private byte[] entity;
        
        public Response(byte[] data) {
            this.data = data;
            String line = readLine();
            if (line != null) {
                statusCode = Integer.parseInt(line);
                while ((line = readLine()) != null) {
                    if (line.length() > 0) {
                        int del = line.indexOf(':');
                        String h = line.substring(0, del).trim();
                        String v = line.substring(del + 1).trim();
                        if ("Content-Type".equalsIgnoreCase(h)) {
                            contentType = v;
                        }
                    }
                }
            }
            entity = new byte[data.length - pos];
            System.arraycopy(data, pos, entity, 0, entity.length);
        }
                
            
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getContentType() {
            return contentType;
        }
        
        public byte[] getEntity() {
            return entity;
        }
        
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("Status: ").append(statusCode).append("\r\n");
            sb.append("Type: ").append(contentType).append("\r\n");
            sb.append("Entity: ").append(new String(entity)).append("\r\n");
            return sb.toString();
        }
        
        private String readLine() {
            StringBuilder sb = new StringBuilder();
            while (pos < data.length) {
                int c = 0xff & data[pos++];
                if (c == '\n') {
                    break;
                } else if (c == '\r') {
                    continue;
                } else {
                    sb.append((char)c);
                }
            }
            if (sb.length() == 0) {
                return null;
            }
            return sb.toString();
        }
    }
}
