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

package de.elakito.testzone.samples.osgi.cxf.jaxws.greeter.provider;

import java.util.logging.Logger;

import javax.jws.WebService;

import org.apache.cxf.hello_world_soap_http.Greeter;

/**
 * 
 */
@WebService(targetNamespace = "http://cxf.apache.org/hello_world_soap_http", name = "Greeter", serviceName="GreeterService")
public class GreeterENImpl implements Greeter {
    private static final Logger LOG = 
        Logger.getLogger(GreeterENImpl.class.getPackage().getName());
    
    /* (non-Javadoc)
     * @see org.apache.cxf.hello_world_soap_http.Greeter#greetMe(java.lang.String)
     */
    public String greetMe(String me) {
        LOG.info("Executing operation greetMe with " + me);
        System.out.println("*** => Hello " + me);
        return "Hello " + me;
    }
    
    /* (non-Javadoc)
     * @see org.apache.cxf.hello_world_soap_http.Greeter#greetMeOneWay(java.lang.String)
     */
    public void greetMeOneWay(String me) {
        LOG.info("Executing operation greetMeOneWay with " + me);
        System.out.println("*** => Hello " + me);
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.hello_world_soap_http.Greeter#sayHi()
     */
    public String sayHi() {
        LOG.info("Executing operation sayHi");
        return "Good day";
    }
}
