package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import org.apache.cxf.transport.websocket.WebSocketConstants;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

public abstract class JaxRsWebAppBaseTest {
    private static final String RESULT_RUN_TEMPLATE = "Run %d: Time taken for %d %s calls: %dms; %d calls/s";
    private static final String BEST_TIME_TEMPLATE = "Best Time taken for %d %s calls: %dms; %d calls/s";
    protected static int RUN_REPEAT;
    protected static int STOP_REPEAT;
    protected static int CALL_COUNT;
    protected static long DELAY;
    protected static boolean GET_DISABLED;
    protected static boolean POST_DISABLED;
    protected static boolean USING_HTTP_HC;

    private org.eclipse.jetty.server.Server server;
    
    protected abstract String getWebResourcePath();

    protected abstract void setUpClient();
    protected abstract void cleanUpClient();

    protected abstract Tester createGETTester();
    protected abstract Tester createPOSTTester();
    
    @BeforeClass
    public static void readProperties() {
        RUN_REPEAT = Integer.getInteger("test.repeat", 1000);
        STOP_REPEAT = Integer.getInteger("test.stop", 50);
        CALL_COUNT = Integer.getInteger("test.count", 100);
        DELAY = Long.getLong("test.delay", 500);
        String prop = System.getProperty("test.disable");
        if (prop != null) {
            prop = prop.toLowerCase();
            GET_DISABLED = prop.indexOf("get") >= 0;
            POST_DISABLED = prop.indexOf("post") >= 0;
        }
        try {
            Class.forName("org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit");
            USING_HTTP_HC = true;
        } catch (Throwable t) {
            USING_HTTP_HC = false;
        }

    }

    protected void setUpServer() {
        server = new org.eclipse.jetty.server.Server(8181);
        String resourcePath = getWebResourcePath();
        
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
                getbesttime = runTest(createGETTester());
            }
            long postbesttime = Long.MAX_VALUE;
            if (!POST_DISABLED) {
                postbesttime = runTest(createPOSTTester());
            }
            
            System.out.print("Result for " + this.getClass().getSimpleName());
            System.out.println(USING_HTTP_HC ? " with http-hc" : " without http-hc");
            if (GET_DISABLED) {
                System.out.println("GET Tests disabled");
            } else {
                System.out.println(
                    String.format(BEST_TIME_TEMPLATE,
                        new Object[]{CALL_COUNT, "GET", getbesttime, CALL_COUNT * 1000 / getbesttime}));
            }
            if (POST_DISABLED) {
                System.out.println("POST Tests disabled");
            } else {
                System.out.println(
                    String.format(BEST_TIME_TEMPLATE,
                        new Object[]{CALL_COUNT, "POST", postbesttime, CALL_COUNT * 1000 / postbesttime}));
            }
            
            cleanUpClient();
            cleanUpServer();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }


    private long runTest(Tester tester) throws Exception {
        long besttime = Long.MAX_VALUE;
        int unchanged = 0;
        for (int j = 1; j <= RUN_REPEAT; j++) {
            if (DELAY > 0) {
                Thread.sleep(DELAY);
            }
            long begintime = System.currentTimeMillis();
            tester.beforeMethodTest();
            for (long i = 0; i < CALL_COUNT; i++) {
                tester.invokeMethod();
            }
            tester.afterMethodTest();
            long endtime = System.currentTimeMillis();
            long difftime = endtime - begintime;
            System.out.println(
                String.format(RESULT_RUN_TEMPLATE, 
                    new Object[]{j, CALL_COUNT, tester.getMethod(), difftime, CALL_COUNT * 1000 / difftime}));
            if (besttime > difftime) {
                besttime = difftime;
                unchanged = 0;
            } else {
                unchanged++;
            }
            if (unchanged > STOP_REPEAT) {
                break;
            }
        }
        return besttime;
    }

    //from systests/jaxrs
    protected static class Response {
        private Object data;
        private int pos; 
        private int statusCode;
        private String contentType;
        private String id;
        private Object entity;
        
        Response(Object data) {
            this.data = data;
            String line;
            boolean first = true;
            while ((line = readLine()) != null) {
                if (first && isStatusCode(line)) {
                    statusCode = Integer.parseInt(line);
                    continue;
                } else {
                    first = false;
                }

                int del = line.indexOf(':');
                String h = line.substring(0, del).trim();
                String v = line.substring(del + 1).trim();
                if ("Content-Type".equalsIgnoreCase(h)) {
                    contentType = v;
                } else if (WebSocketConstants.DEFAULT_RESPONSE_ID_KEY.equals(h)) {
                    id = v;
                }
            }
            if (data instanceof String) {
                entity = ((String)data).substring(pos);
            } else if (data instanceof byte[]) {
                entity = new byte[((byte[])data).length - pos];
                System.arraycopy((byte[])data, pos, (byte[])entity, 0, ((byte[])entity).length);
            }
        }
            
        private static boolean isStatusCode(String line) {
            char c = line.charAt(0);
            return '0' <= c && c <= '9';
        }

        public int getStatusCode() {
            return statusCode;
        }
        
        public String getContentType() {
            return contentType;
        }
        
        public Object getEntity() {
            return entity;
        }
        
        public String getTextEntity() {
            return gettext(entity);
        }

        public String getId() {
            return id;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Status: ").append(statusCode).append("\r\n");
            sb.append("Type: ").append(contentType).append("\r\n");
            sb.append("Entity: ").append(gettext(entity)).append("\r\n");
            return sb.toString();
        }
        
        private String readLine() {
            StringBuilder sb = new StringBuilder();
            while (pos < length(data)) {
                int c = getchar(data, pos++);
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

        private int length(Object o) {
            if (o instanceof String) {
                return ((String)o).length();
            } else if (o instanceof char[]) {
                return ((char[])o).length;
            } else if (o instanceof byte[]) {
                return ((byte[])o).length;
            } else {
                return 0;
            }
        }

        private int getchar(Object o, int p) {
            return 0xff & (o instanceof String ? ((String)o).charAt(p) : (o instanceof byte[] ? ((byte[])o)[p] : -1));
        }

        private String gettext(Object o) {
            return o instanceof String ? (String)o : (o instanceof byte[] ? new String((byte[])o) : null);
        }
    }
    
    protected static interface Tester {
        String getMethod();
        

        
        void invokeMethod();
        void beforeMethodTest();
        void afterMethodTest();
        
    }
}
