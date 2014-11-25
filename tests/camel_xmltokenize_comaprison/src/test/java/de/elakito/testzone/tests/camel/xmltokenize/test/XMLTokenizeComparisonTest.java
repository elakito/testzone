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
import org.apache.camel.support.TokenXMLPairExpressionIterator;
import org.apache.camel.support.XMLTokenExpressionIterator;

import org.junit.Assert;
import org.junit.Test;

public class XMLTokenizeComparisonTest extends Assert {
    // xmltokenize
    @Test
    public void testXMLTokenizeRSS() throws Exception {
        performXMLTokenize("rss", "<item>", "<rss>", "utf-8", 10000);
        performXMLTokenize("rss", "<item>", "<rss>", "utf-8", 100000);
        performXMLTokenize("rss", "<item>", "<rss>", "utf-8", 1000000);
    }

    @Test
    public void testXMLTokenizeParts() throws Exception {
        performXMLTokenize("parts", "<Part>", "<PartMessage>", "utf-8", 10000);
        performXMLTokenize("parts", "<Part>", "<PartMessage>", "utf-8", 100000);
        performXMLTokenize("parts", "<Part>", "<PartMessage>", "utf-8", 1000000);
    }

    @Test
    public void testXMLTokenizeRSSWrapped() throws Exception {
        performXMLTokenize("rss", "<item>", "<*>", "utf-8", 10000);
        performXMLTokenize("rss", "<item>", "<*>", "utf-8", 100000);
        performXMLTokenize("rss", "<item>", "<*>", "utf-8", 1000000);
    }

    @Test
    public void testXMLTokenizePartsWrapped() throws Exception {
        performXMLTokenize("parts", "<Part>", "<*>", "utf-8", 10000);
        performXMLTokenize("parts", "<Part>", "<*>", "utf-8", 100000);
        performXMLTokenize("parts", "<Part>", "<*>", "utf-8", 1000000);
    }

    
    // xtokenize
    @Test
    public void testXTokenizeRSS() throws Exception {
        performXTokenize("rss", "//item", 'i', "utf-8", 10000);
        performXTokenize("rss", "//item", 'i', "utf-8", 100000);
        performXTokenize("rss", "//item", 'i', "utf-8", 1000000);
    }

    @Test
    public void testXTokenizeParts() throws Exception {
        performXTokenize("parts", "//*:Part", 'i', "utf-8", 10000);
        performXTokenize("parts", "//*:Part", 'i', "utf-8", 100000);
        performXTokenize("parts", "//*:Part", 'i', "utf-8", 1000000);
    }

    @Test
    public void testXTokenizeRSSWrapped() throws Exception {
        performXTokenize("rss", "//item", 'w', "utf-8", 10000);
        performXTokenize("rss", "//item", 'w', "utf-8", 100000);
        performXTokenize("rss", "//item", 'w', "utf-8", 1000000);
    }

    @Test
    public void testXTokenizePartsWrapped() throws Exception {
        performXTokenize("parts", "//*:Part", 'w', "utf-8", 10000);
        performXTokenize("parts", "//*:Part", 'w', "utf-8", 100000);
        performXTokenize("parts", "//*:Part", 'w', "utf-8", 1000000);
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
//        performXPath("parts", exp, "utf-8", 1000000);
    }

    // old xmltokenize
    @Test
    public void testXMLPairTokenizeRSS() throws Exception {
        performXMLPairTokenize("rss", "<item>", "</item>", "<rss>", "utf-8", 10000);
        performXMLPairTokenize("rss", "<item>", "</item>", "<rss>", "utf-8", 100000);
        performXMLPairTokenize("rss", "<item>", "</item>", "<rss>", "utf-8", 1000000);
    }

    @Test
    public void testXMLPairTokenizeParts() throws Exception {
        performXMLPairTokenize("parts", "<Part>", "</Part>", "<PartMessage>", "utf-8", 10000);
        performXMLPairTokenize("parts", "<Part>", "</Part>", "<PartMessage>", "utf-8", 100000);
        performXMLPairTokenize("parts", "<Part>", "</Part>", "<PartMessage>", "utf-8", 1000000);
    }
    
    private void performXMLTokenize(String name, String param, String nsparam, String charset, int repeat) throws Exception {
        System.out.println("starting xmlTokenize-" + ("<*>".equals(nsparam) ? "w" : "i") + " on " + name + " containing " + repeat + " tokens");
        long stime = System.currentTimeMillis();
        InputStream in = createInputStream(name, charset, repeat);
        TestTokenXMLExpressionIterator xtei = new TestTokenXMLExpressionIterator(param, nsparam);
        verifyCount("xmlTokenize-" + ("<*>".equals(nsparam) ? "w" : "i"), name, xtei.createIterator(in, charset), repeat, stime);
    }
    
    private void performXTokenize(String name, String param, char mode, String charset, int repeat) throws Exception {
        System.out.println("starting xtokenize-" + mode + " on " + name + " containing " + repeat + " tokens");
        long stime = System.currentTimeMillis();
        InputStream in = createInputStream(name, charset, repeat);
        TestXMLTokenExpressionIterator xtei = new TestXMLTokenExpressionIterator(param, mode);
        verifyCount("xtokenize-" + mode, name, xtei.createIterator(in, charset), repeat, stime);
    }

    private void performXPath(String name, XPathBuilder param, String charset, int repeat) throws Exception {
        System.out.println("starting xpath-i on " + name + " containing " + repeat + " tokens");
        long stime = System.currentTimeMillis();
        InputStream in = createInputStream(name, charset, repeat);
        TestXPathTokenIterator xtei = new TestXPathTokenIterator(param);
        verifyCount("xpath-i", name, xtei.createIterator(in, charset), repeat, stime);
    }

    private void performXMLPairTokenize(String name, String sparam, String eparam, String nsparam, String charset, int repeat) throws Exception {
        System.out.println("starting xmlPairtokenize-i on " + name + " containing " + repeat + " tokens");
        long stime = System.currentTimeMillis();
        InputStream in = createInputStream(name, charset, repeat);
        TestTokenXMLPairExpressionIterator xtei = new TestTokenXMLPairExpressionIterator(sparam, eparam, nsparam);
        verifyCount("xmlPairTokenize-i", name, xtei.createIterator(in, charset), repeat, stime);
    }
    
    private InputStream createInputStream(String name, String charset, int repeat) throws IOException {
        String part = readString(name + "-part.txt", charset);
        String head = readString(name + "-head.txt", charset);
        String tail = readString(name + "-tail.txt", charset);
        
        return TestDataGenerator.createTokenDataInputStream(part, repeat,  head, tail, null, charset);
    }

    private void verifyCount(String lang, String name, Iterator<?> it, int repeat, long stime) throws IOException {

        int n = 0;
        while (it.hasNext()) {
            Object t = it.next();
            // we compare the performance for generating string tokens
            assertTrue(t instanceof String);
            n++;
        }
        assertEquals(repeat, n);
        if (it instanceof Closeable) {
            ((Closeable)it).close();
        }

        long etime = System.currentTimeMillis();
        long dtime = etime - stime;
        long rate = repeat / dtime;
        System.out.println("time taken: " + dtime + "ms; " + rate + " items/ms");
        // output the line for generating a comparison table for the markdown format
        System.out.println("json:[\"" 
            + lang.substring(0, lang.length() - 2) + "\", \"" + (lang.endsWith("w") ? 'o' : 'x') + "\", \"" 
            + name + "\", " + repeat + ", " + dtime + "]");
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
    
    // the regex based xml tokenzier of camel 2.13.2 that uses a complex regex that works for the normal start
    // end elements as well as self-closed elements
    private static class TestTokenXMLExpressionIterator extends TokenXMLExpressionIterator {

        public TestTokenXMLExpressionIterator(String tagToken, String inheritNamespaceToken) {
            super(tagToken, inheritNamespaceToken);
        }

        @Override
        protected Iterator<?> createIterator(InputStream in, String charset) {
            return super.createIterator(in, charset);
        }
    }
    
    // the stax basd xml tokenizer of camel 2.14 that works for any xml structure
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

    // the old regex based xml tokenizer of camel 2.9 that used a simplar regex that only works for the normal start end elements
    @SuppressWarnings("deprecation")
    private static class TestTokenXMLPairExpressionIterator extends TokenXMLPairExpressionIterator {

        public TestTokenXMLPairExpressionIterator(String startToken, String endToken, String inheritNamespaceToken) {
            super(startToken, endToken, inheritNamespaceToken);
        }

        @Override
        protected Iterator<?> createIterator(InputStream in, String charset) {
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
