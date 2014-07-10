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
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import org.apache.camel.support.TokenXMLExpressionIterator;
import org.apache.camel.support.XMLTokenExpressionIterator;

import org.junit.Assert;
import org.junit.Test;

public class XMLTokenizeComparisonTest extends Assert {
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

    @Test
    public void testxXTokenizeRSS() throws Exception {
        performXTokenize("rss", "//item", "utf-8", 10000);
        performXTokenize("rss", "//item", "utf-8", 100000);
        performXTokenize("rss", "//item", "utf-8", 1000000);
    }

    @Test
    public void testxXTokenizeParts() throws Exception {
        performXTokenize("parts", "//*:Part", "utf-8", 10000);
        performXTokenize("parts", "//*:Part", "utf-8", 100000);
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
        ((Closeable)it).close();

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
        public Iterator<?> createIterator(InputStream in, String charset) {
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
}
