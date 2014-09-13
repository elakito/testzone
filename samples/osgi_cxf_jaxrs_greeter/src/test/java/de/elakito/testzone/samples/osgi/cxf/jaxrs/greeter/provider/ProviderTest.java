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

package de.elakito.testzone.samples.osgi.cxf.jaxrs.greeter.provider;

import javax.ws.rs.core.HttpHeaders;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.testutil.common.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.elakito.testzone.samples.osgi.cxf.jaxrs.greeter.common.Greeter;
import de.elakito.testzone.samples.osgi.cxf.jaxrs.greeter.provider.GreeterImpl;

public class ProviderTest extends Assert {
    private static final String PORT = TestUtil.getPortNumber(ProviderTest.class);
    private static final String ADDRESS = "http://localhost:" + PORT + "/cxf";

    private Server server;
    
    @Before
    public void setup() {
        GreeterImpl impl = new GreeterImpl();
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBean(impl);
        sf.setAddress(ADDRESS);

        server = sf.create();
        
        System.out.println("Starting endpoint at " + ADDRESS);
        server.start();
    }
    
    @After
    public void cleanup() {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }
    
    @Test
    public void testServices() throws Exception {
        Greeter greeter = JAXRSClientFactory.create(ADDRESS, Greeter.class);
        Client client = WebClient.client(greeter);
        client.header(HttpHeaders.USER_AGENT, Greeter.class.getName());

        System.out.println("say hi ...");
        String hi = greeter.sayHi();
        System.out.println("said: " + hi);
        assertEquals("Bonjour", hi);
        
        System.out.println("greet me ...");
        String me = greeter.greetMe("Ranna");
        System.out.println("said: " + me);
        assertEquals("Hello Ranna", me);
        
    }
}
