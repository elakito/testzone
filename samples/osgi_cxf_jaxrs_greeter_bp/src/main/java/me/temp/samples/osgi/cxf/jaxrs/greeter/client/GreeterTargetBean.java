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

package me.temp.samples.osgi.cxf.jaxrs.greeter.client;

import javax.ws.rs.core.HttpHeaders;

import me.temp.samples.osgi.cxf.jaxrs.greeter.common.Greeter;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;


/**
 *
 */
public class GreeterTargetBean {
    private Client client;
    private Greeter greeter;
    private Bus bus;
    private String address;

    public void greetMe(Exchange exchange) {
        Bus oldbus = BusFactory.getDefaultBus();
        BusFactory.setDefaultBus(bus);
        try {
            synchronized (greeter) {
                Message message = exchange.getIn();
                String name = message.getBody(String.class);
                System.out.println("Greeting " + name + " at address " + address);

                String result = greeter.greetMe(name);
                System.out.println("Greeter response: " + result);
                exchange.getOut().setBody(result);
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
            greeter = JAXRSClientFactory.create(address, Greeter.class);
            client = WebClient.client(greeter);
            client.header(HttpHeaders.USER_AGENT, Greeter.class.getName());
            
            System.out.println("Greeter endpoint: " + client.getCurrentURI());
            
        } catch (RuntimeException e) {
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
}
