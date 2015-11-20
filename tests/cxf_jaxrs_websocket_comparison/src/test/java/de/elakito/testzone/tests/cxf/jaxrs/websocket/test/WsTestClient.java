package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketByteListener;
import com.ning.http.client.ws.WebSocketTextListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WsTestClient {
    private static final boolean DEBUG = false;
    private List<Object> received;
    private CountDownLatch latch;
    private AsyncHttpClient client;
    private WebSocket websocket;
    private String url;
    private boolean connected;
    
    public WsTestClient(String url, int count) {
        this.received = new ArrayList<Object>();
        this.latch = new CountDownLatch(count);
        this.client = new AsyncHttpClient();
        this.url = url;
    }
    
    public void connect() throws InterruptedException, ExecutionException, IOException {
        websocket = client.prepareGet(url).execute(
            new WebSocketUpgradeHandler.Builder()
            .addWebSocketListener(new WsSocketListener()).build()).get();
        connected = true;
    }

    public void sendTextMessage(String message) {
        websocket.sendMessage(message);
    }

    public void sendMessage(byte[] message) {
        websocket.sendMessage(message);
    }
    
    public boolean await(int secs) throws InterruptedException {
        return latch.await(secs, TimeUnit.SECONDS);
    }
    
    public void reset(int count) {
        assert count >= 0;
        latch = new CountDownLatch(count);
        received.clear();
    }

    public List<Object> getReceived() {
        return received;
    }

    public void close() {
        connected = false;
        websocket.close();
        client.close();
    }

    class WsSocketListener implements WebSocketTextListener, WebSocketByteListener {

        public void onOpen(WebSocket websocket) {
            if (DEBUG) {
                System.out.println("[ws] opened");            
            }
        }

        public void onClose(WebSocket websocket) {
            if (DEBUG) {
                System.out.println("[ws] closed");            
            }
            if (connected) {
                try {
                    connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onError(Throwable t) {
            if (DEBUG) {
                System.out.println("[ws] error: " + t);                        
            }
        }

        public void onMessage(byte[] message) {
            received.add(message);
            if (DEBUG) {
                System.out.println("[ws] received bytes --> " + new String(message));
            }
            latch.countDown();
        }

        public void onFragment(byte[] fragment, boolean last) {
            // TODO Auto-generated method stub
            if (DEBUG) {
                System.out.println("[ws] received fragment bytes (last?" + last + ")--> " + new String(fragment));
            }
        }

        public void onMessage(String message) {
            received.add(message);
            if (DEBUG) {
                System.out.println("[ws] received --> " + message);
            }
            latch.countDown();
        }

        public void onFragment(String fragment, boolean last) {
            // TODO Auto-generated method stub
            if (DEBUG) {
                System.out.println("[ws] received fragment bytes (last?" + last + ")--> " + fragment);
            }
        }
        
    }
}
