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

package de.elakito.testzone.tests.stax.offset.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Assert;
import org.junit.Test;

public class StaxOffsetModuloIntComparisonTest extends Assert {
    private static final XMLInputFactory factory = XMLInputFactory.newFactory();
    private static String DATA = createTestData("/data2.xml");
    private static char[] HEAD = "<wrapper>".toCharArray();
    private static char[] BUFFER;
    private static int REPEAT;
    private static int MAXCOUNT;

    static {
        int rp = 4096 / DATA.length();
        BUFFER = new char[rp * DATA.length()];
        for (int i = 0; i < rp; i++) {
            DATA.getChars(0, DATA.length(), BUFFER, i * DATA.length());
        }
        REPEAT = Integer.MAX_VALUE / BUFFER.length * 2;
        // one less than the last message message
        MAXCOUNT = rp * REPEAT - 1;
    }

    @Test
    public void testOffsetModuloInt() throws Exception {
        Reader rd = null;
        try {
            rd = createTestReader();
            verifyOffsetModuloInt(rd);
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    private Reader createTestReader() {
        return new Reader() {
            private int count;
            private int offset;
            private boolean started;
            
            @Override
            public void close() throws IOException {
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                int total = 0;
                if (!started) {
                    System.arraycopy(HEAD, 0, cbuf, off, HEAD.length);
                    off += HEAD.length;
                    len -= HEAD.length;
                    total = HEAD.length;
                    started = true;
                }

                while (count < REPEAT) {
                    if (len > BUFFER.length - offset) {
                        int clen = BUFFER.length - offset;
                        System.arraycopy(BUFFER, offset, cbuf, off, clen);
                        off += clen;
                        offset = 0;
                        total += clen;

                        len -= clen;
                        count++;
                    } else {
                        System.arraycopy(BUFFER, offset, cbuf, off, len);
                        off += len;
                        offset += len;
                        total += len;
                        break;
                    }
                }
                return total > 0 ? total : -1;
            }
        };
    }

    private static void verifyOffsetModuloInt(Reader rd) throws Exception {
        XMLStreamReader reader = factory.createXMLStreamReader(rd);
        try {
            System.out.println("### StAX Factory: " + factory.getClass());
            verifyLocation(reader);
            System.out.println();
        } finally {
            reader.close();
        }
    }

    private static void verifyLocation(XMLStreamReader reader) throws XMLStreamException {
        QName qn;
        long longoffset = 0;
        int oldpos = 0;
        int count = 0;
        int olddiff = 0;
        boolean overflown = false;
        int n = 0;
        StringBuilder errors = new StringBuilder();
        while (n != XMLStreamConstants.END_DOCUMENT) {
            n = reader.next();
            switch (n) {
            case XMLStreamConstants.START_ELEMENT:
                qn = reader.getName();
                if ("message".equals(qn.getLocalPart())) {
                    count++;
                    if (count % 1000000 == 0) {
                        System.out.print(".");
                    }
                    int pos = reader.getLocation().getCharacterOffset();
                    if (pos < 0 && !overflown) {
                        System.out.print("^");
                        overflown = true;
                    } else if (pos > 0 && overflown) {
                        System.out.print("v");
                        overflown = false;
                    }
                    
                    int diff = pos - oldpos;
                    if (count == 2) {
                        // get the offset difference between the 1st and the 2nd messages
                        System.out.println("### Offset Difference: " + diff);
                    } else if (count > 2){
                        // verify the offset difference between the subsequent two messages
                        if (olddiff != diff) {
                            errors.append("Offset value diverted at " + count + "-th message; offset " + longoffset + "\n");
                            System.out.println("### Offset Changed to: " + diff);
                            olddiff = diff;
                        }
                    }
                    longoffset += pos - oldpos;
                    oldpos = pos;
                    olddiff = diff;
                }
                if (count > MAXCOUNT) {
                    System.out.println();
                    System.out.println("### Final Offset Value: " + longoffset);
                    if (errors.length() > 0) {
                        fail(errors.toString());
                    }
                    return;
                }
                
                break;
            default:
            }
        }
    }

    private static String createTestData(String path) {
        InputStream in = null;
        String data = null;
        try {
            in = StaxOffsetModuloIntComparisonTest.class.getResourceAsStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1028];
            for (;;) {
                int n = in.read(buf, 0, buf.length);
                if (n < 0) {
                    break;
                }
                baos.write(buf, 0, n);
            }
            data = baos.toString("utf-8");
        } catch (Exception e) {
            // 
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return data;
    }
}
