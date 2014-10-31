package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketByteListener;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WsTestClient {
    private static final boolean DEBUG = false;
    private List<String> received;
    private List<byte[]> receivedBytes;
    private CountDownLatch latch;
    private AsyncHttpClient client;
    private WebSocket websocket;
    private String url;
    private boolean connected;
    
    public WsTestClient(String url, int count) {
        this.received = new ArrayList<String>();
        this.receivedBytes = new ArrayList<byte[]>();
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
        websocket.sendTextMessage(message);
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
        receivedBytes.clear();
    }

    public List<String> getReceived() {
        return received;
    }
    
    public List<byte[]> getReceivedBytes() {
        return receivedBytes;
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
            receivedBytes.add(message);
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
