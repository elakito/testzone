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

package me.temp.tests.camel.xmltokenize.test;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.NodeList;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.TokenXMLExpressionIterator;
import org.apache.camel.support.XMLTokenExpressionIterator;

import org.junit.Assert;
import org.junit.Test;

public class XMLTokenizeComparisonTest extends Assert {
    // xmltokenize
    @Test
    public void testXMLTokenizeRSS() throws Exception {
        performXMLTokenize("rss", "<item>", "utf-8", 10000);
        performXMLTokenize("rss", "<item>", "utf-8", 100000);
        performXMLTokenize("rss", "<item>", "utf-8", 1000000);
    }

    @Test
    public void testXMLTokenizeParts() throws Exception {
        performXMLTokenize("parts", "<Part>", "utf-8", 10000);
        performXMLTokenize("parts", "<Part>", "utf-8", 100000);
    }

    
    // xtokenize
    @Test
    public void testXTokenizeRSS() throws Exception {
        performXTokenize("rss", "//item", "utf-8", 10000);
        performXTokenize("rss", "//item", "utf-8", 100000);
        performXTokenize("rss", "//item", "utf-8", 1000000);
    }

    @Test
    public void testXTokenizeParts() throws Exception {
        performXTokenize("parts", "//*:Part", "utf-8", 10000);
        performXTokenize("parts", "//*:Part", "utf-8", 100000);
    }

    
    // xpath
    @Test
    public void testXPathRSS() throws Exception {
        XPathBuilder exp = XPathBuilder.xpath("//item");
        performXPath("rss", exp, "utf-8", 10000);
        // skip the meaningless tests that lead to OOM
//        performXPath("rss", exp, "utf-8", 100000);
//        performXPath("rss", exp, "utf-8", 1000000);
    }

    @Test
    public void testXPathParts() throws Exception {
        XPathBuilder exp = XPathBuilder.xpath("//x:Part");
        Map<String, String> nsmap = new HashMap<String, String>();
        nsmap.put("x", "http://www.mic-cust.com/test/parts/v1.1");
        exp.setNamespaces(nsmap);
        performXPath("parts", exp, "utf-8", 10000);
        // skip the meaningless tests that lead to OOM
//        performXPath("parts", exp, "utf-8", 100000);
    }
    
    private void performXMLTokenize(String name, String param, String charset, int repeat) throws Exception {
        InputStream in = createInputStream(name, charset, repeat);
        TestTokenXMLExpressionIterator xtei = new TestTokenXMLExpressionIterator(param, "<*>");
        verifyCount("xmlTokenize", name, xtei.createIterator(in, charset), repeat);
    }
    
    private void performXTokenize(String name, String param, String charset, int repeat) throws Exception {
        InputStream in = createInputStream(name, charset, repeat);
        TestXMLTokenExpressionIterator xtei = new TestXMLTokenExpressionIterator(param, 'i');
        verifyCount("xtokenize", name, xtei.createIterator(in, charset), repeat);
    }

    private void performXPath(String name, XPathBuilder param, String charset, int repeat) throws Exception {
        InputStream in = createInputStream(name, charset, repeat);
        TestXPathTokenIterator xtei = new TestXPathTokenIterator(param);
        verifyCount("xpath", name, xtei.createIterator(in, charset), repeat);
    }

    private InputStream createInputStream(String name, String charset, int repeat) throws IOException {
        String part = readString(name + "-part.txt", charset);
        String head = readString(name + "-head.txt", charset);
        String tail = readString(name + "-tail.txt", charset);
        
        return TestDataGenerator.createTokenDataInputStream(part, repeat,  head, tail, null, charset);
    }

    private void verifyCount(String lang, String name, Iterator<?> it, int repeat) throws IOException {
        long stime = System.currentTimeMillis();        
        System.out.println("started " + lang + " on " + name + " containing " + repeat + " tokens");

        int n = 0;
        while (it.hasNext()) {
            Object t = it.next();
            n++;
        }
        assertEquals(repeat, n);
        if (it instanceof Closeable) {
            ((Closeable)it).close();
        }

        long etime = System.currentTimeMillis();
        long dtime = etime - stime;
        System.out.println("time taken: " + dtime + "ms; " + (repeat / dtime) + " items/ms");
    }

    private static String readString(String res, String charset) throws IOException {
        InputStream in = null;
        try {
            in = XMLTokenizeComparisonTest.class.getClassLoader().getResourceAsStream(res);
            byte[] buf = new byte[4096];
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            for (;;) {
                int n = in.read(buf, 0, buf.length);
                if (n < 0) {
                    break;
                }
                bais.write(buf, 0, n);
            }
            return bais.toString(charset);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    
    // the regex based xml tokenzier
    private static class TestTokenXMLExpressionIterator extends TokenXMLExpressionIterator {

        public TestTokenXMLExpressionIterator(String tagToken, String inheritNamespaceToken) {
            super(tagToken, inheritNamespaceToken);
        }

        @Override
        protected Iterator<?> createIterator(InputStream in, String charset) {
            return super.createIterator(in, charset);
        }
    }
    
    // the stax basd xml tokenizer
    private static class TestXMLTokenExpressionIterator extends XMLTokenExpressionIterator {

        public TestXMLTokenExpressionIterator(String path, char mode) {
            super(path, mode);
        }

        @Override
        protected Iterator<?> createIterator(InputStream in, String charset) throws XMLStreamException,
            UnsupportedEncodingException {
            return super.createIterator(in, charset);
        }
        
    }

    // an equaivalent iterator for using camel's xpath builder
    private static class TestXPathTokenIterator {
        private XPathBuilder exp;
        private Exchange exchange;

        public TestXPathTokenIterator(XPathBuilder exp) {
            this.exp = exp;
            this.exchange = ExchangeBuilder.anExchange(new DefaultCamelContext()).build();
        }
        
        protected Iterator<?> createIterator(InputStream in, String charset) {
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
