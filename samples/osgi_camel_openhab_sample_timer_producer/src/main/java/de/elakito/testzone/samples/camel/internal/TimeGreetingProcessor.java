/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.elakito.testzone.samples.camel.internal;

import java.util.Date;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeGreetingProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TimeGreetingProcessor.class);
    private String greeting;

    public void process(Exchange exchange) throws Exception {
        String request = exchange.getIn().getBody(String.class);
        LOG.info("*** get the request is " + request);
	System.out.println("*** get the request is " + request);
        exchange.getIn().setBody((request == null ? greeting : request) + " " + new Date());
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }
}
