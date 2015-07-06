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
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractXMLTokenizeComparisonTest extends Assert {
    private static final int DEFAULT_MIN_TEST_SIZE = 10000;
    private static final int DEFAULT_MAX_TEST_SIZE = 1000000;
    
    @Test
    public void testTokenize() throws Exception {
        testTokenize("rss", 'i');
        testTokenize("rss", 'w');
        testTokenize("parts", 'i');
        testTokenize("parts", 'w');
    }

    private void testTokenize(String type, char mode) throws Exception {
        for (int n = getMinTestSize(); n <= getMaxTestSize(); n *= 10) {
            performTokenize(type, mode, n);
        }
    }
    
    private void performTokenize(String type, char mode, int n) throws IOException {
        System.out.println("starting " + getTokenizerName() + "-" + mode + " on " + type + " containing " + n + " tokens");
        InputStream in = createInputStream(type, "utf-8", n);
        TestInstance ti = createTokenIterator(type, mode);

        long stime = System.currentTimeMillis();
        if (ti == null) {
            System.out.println("skipping unsupported " + getTokenizerName() + "-" + mode);
        } else {
            verifyCount(type,mode, ti.createIterator(in, "utf-8"), n, stime);
        }
    }

    private InputStream createInputStream(String type, String charset, int repeat) throws IOException {
        String part = readString(type + "-part.txt", charset);
        String head = readString(type + "-head.txt", charset);
        String tail = readString(type + "-tail.txt", charset);
        
        return TestDataGenerator.createTokenDataInputStream(part, repeat,  head, tail, null, charset);
    }

    private void verifyCount(String type, char mode, Iterator<?> it, int repeat, long stime) throws IOException {

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
        System.out.println("json:[\"" + getTokenizerName() + "\", \"" + mode  + "\", \"" 
            + type + "\", " + repeat + ", " + dtime + "]");
    }

    /**
     * Returns an instance of the corresponding tokenizing iterator to be tested.
     * 
     * @return
     */
    protected abstract TestInstance createTokenIterator(String type, char mode);

    /**
     * Returns the name of the tokenzier.
     */
    protected abstract String getTokenizerName();

    protected int getMinTestSize() {
        return DEFAULT_MIN_TEST_SIZE;
    }

    protected int getMaxTestSize() {
        return DEFAULT_MAX_TEST_SIZE;
    }

    private static String readString(String res, String charset) throws IOException {
        InputStream in = null;
        try {
            in = AbstractXMLTokenizeComparisonTest.class.getClassLoader().getResourceAsStream(res);
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
}
