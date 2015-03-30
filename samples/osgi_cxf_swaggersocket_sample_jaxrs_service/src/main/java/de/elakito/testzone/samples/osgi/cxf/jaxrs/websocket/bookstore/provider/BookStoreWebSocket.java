/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// copied from cxf org.apache.cxf.systest.jaxrs.websocket
package de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.provider;


import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.cxf.jaxrs.ext.StreamingResponse;

import de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.common.Book;
import de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.common.BookStore;

public class BookStoreWebSocket implements BookStore {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public byte[] getBookName() {
        return "CXF in Action".getBytes();
    }

    public void getBookNameStream(HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();
        response.setContentType("text/plain");
        os.write("CXF in Action".getBytes());
        os.flush();
    }

    public Book getBook(long id) {
        return new Book("CXF in Action", id);
    }

    public Long echoBookId(long theBookId) {
        return new Long(theBookId);
    }

    public StreamingOutput getBookBought() {
        return new StreamingOutput() {
            public void write(final OutputStream out) throws IOException, WebApplicationException {
                out.write(("Today: " + new java.util.Date()).getBytes());
                // just for testing, using a thread
                executor.execute(new Runnable() {
                    public void run() {
                        try {
                            for (int r = 2, i = 1; i <= 5; r *= 2, i++) {
                                Thread.sleep(500);
                                out.write(Integer.toString(r).getBytes());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
    }

    public StreamingResponse<Book> getBookStream() {
        return new StreamingResponse<Book>() {
            public void writeTo(final StreamingResponse.Writer<Book> out) throws IOException {
                out.write(new Book("WebSocket1", 1));
                executor.execute(new Runnable() {
                    public void run() {
                        try {
                            for (int i = 2; i <= 5; i++) {
                                Thread.sleep(500);
                                out.write(new Book("WebSocket" + i, i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
    }
    
    
}


