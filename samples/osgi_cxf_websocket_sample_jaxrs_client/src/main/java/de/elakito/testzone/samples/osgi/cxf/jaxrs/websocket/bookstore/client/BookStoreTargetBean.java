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

package de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.client;

import javax.ws.rs.core.HttpHeaders;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.common.Book;
import de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.common.BookStore;


/**
 *
 */
public class BookStoreTargetBean {
    private Client client;
    private BookStore bookstore;
    private Bus bus;
    private String address;

    public void getBook(Exchange exchange) {
        Bus oldbus = BusFactory.getDefaultBus();
        BusFactory.setDefaultBus(bus);
        try {
            synchronized (bookstore) {
                Message message = exchange.getIn();
                String idstr = message.getBody(String.class);
                System.out.println("Getting book with id " + idstr + " at address " + address);
                long id = idstr == null ? 1L : Long.parseLong(idstr);
                Book result = bookstore.getBook(id);
                String r = toString(result);
                System.out.println("BookStore response: " + r);
                exchange.getOut().setBody(r);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } finally {
            BusFactory.setDefaultBus(oldbus);
        }
    }

    public void init() {
        Bus oldbus = BusFactory.getDefaultBus();
        BusFactory.setDefaultBus(bus);
        try {
            bookstore = JAXRSClientFactory.create(address, BookStore.class);
            client = WebClient.client(bookstore);
            client.header(HttpHeaders.USER_AGENT, BookStore.class.getName());

            System.out.println("BookStore endpoint: " + client.getBaseURI());
            
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Error e) {
            e.printStackTrace();
            throw e;
        } finally {
            BusFactory.setDefaultBus(oldbus);
        }
    }
    
    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private static String toString(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append("Book(id = '").append(book.getId()).append("', name = '")
        .append(book.getName()).append("')");

        return sb.toString();
        
    }
}
