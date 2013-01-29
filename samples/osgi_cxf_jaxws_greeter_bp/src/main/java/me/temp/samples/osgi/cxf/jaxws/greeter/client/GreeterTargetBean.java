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

package me.temp.samples.osgi.cxf.jaxws.greeter.client;

import javax.xml.ws.BindingProvider;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.hello_world_soap_http.Greeter;
import org.apache.cxf.hello_world_soap_http.GreeterService;

/**
 * 
 */
public class GreeterTargetBean {
    private Client client;
    private Greeter greeter;
    private Bus bus;
    private String address;
    
    public void greetMe(Exchange exchange) {
        Bus oldbus = BusFactory.getThreadDefaultBus();
        BusFactory.setThreadDefaultBus(bus);
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
            BusFactory.setThreadDefaultBus(oldbus);
        }
    }

    public void init() {
        Bus oldbus = BusFactory.getThreadDefaultBus();
        BusFactory.setThreadDefaultBus(bus);
        try {
            GreeterService service = new GreeterService(GreeterTargetBean.class.getResource("/wsdl/hello_world.wsdl"));
            greeter = service.getGreeterPort();
            if (address != null) {
                ((BindingProvider)greeter).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
            }
            client = ClientProxy.getClient(greeter);
            System.out.println("Greeter endpoint: " + client.getEndpoint().getEndpointInfo());
            
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } finally {
            BusFactory.setThreadDefaultBus(oldbus);
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
