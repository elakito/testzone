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

package de.elakito.testzone.tests.camel.xmltokenize.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.w3c.dom.NodeList;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.impl.DefaultCamelContext;

public class XPathTokenizeComparisonTest extends AbstractXMLTokenizeComparisonTest {
    @Override
    protected TestInstance createTokenIterator(String type, char mode) {
        TestInstance ti = null;

        if ("rss".equals(type) && mode == 'i') {
            ti = new TestTokenIterator(XPathBuilder.xpath("//item"));
        } else if ("parts".equals(type) && mode == 'i') {
            Map<String, String> nsmap = new HashMap<String, String>();
            nsmap.put("x", "http://www.mic-cust.com/test/parts/v1.1");
            XPathBuilder exp = XPathBuilder.xpath("//x:Part");
            exp.setNamespaces(nsmap);
            ti = new TestTokenIterator(exp);
        }
            
        return ti;
    }

    @Override
    protected String getTokenizerName() {
        return "xpath";
    }

    @Override
    protected int getMaxTestSize() {
        // xpath is memory bounded
        return 10000;
    }

    // an equaivalent iterator for using camel's xpath builder
    private static class TestTokenIterator implements TestInstance {
        private XPathBuilder exp;
        private Exchange exchange;

        public TestTokenIterator(XPathBuilder exp) {
            this.exp = exp;
            this.exchange = ExchangeBuilder.anExchange(new DefaultCamelContext()).build();
        }
        
        public Iterator<?> createIterator(InputStream in, String charset) {
            exchange.getIn().setBody(in);
            exchange.getIn().setHeader(Exchange.CHARSET_NAME, charset);

            final XmlConverter converter = new XmlConverter();
            final NodeList list = exp.evaluate(exchange, NodeList.class);
            return new Iterator<Object> () {
                private int index;
                @Override
                public boolean hasNext() {
                    return index < list.getLength();
                }

                @Override
                public Object next() {
                    Object obj = null;
                    try {
                        obj = converter.toString(list.item(index++), exchange);
                    } catch (TransformerException e) {
                        // ignore
                    }
                    return obj;
                }

                @Override
                public void remove() {
                    // noop
                }
            };
        }
    }
}
