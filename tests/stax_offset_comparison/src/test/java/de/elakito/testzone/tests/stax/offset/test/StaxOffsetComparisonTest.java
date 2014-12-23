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
import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Assert;
import org.junit.Test;

public class StaxOffsetComparisonTest extends Assert {
    private static final XMLInputFactory factory = XMLInputFactory.newFactory();
    private static String DATA1 = createTestData("/data1.xml");
    private static String DATA2 = createTestData("/data2.xml");

    @Test
    public void testOffsetPositions() throws Exception {
        verifyOffsetPositions(DATA1);
        verifyOffsetPositions(DATA2);
    }

    
    private static void verifyOffsetPositions(String data) throws Exception {
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(data));
        try {
            System.out.println("### Document of length: " + data.length() + " with content\n" + data);
            System.out.println("### StAX Factory: " + factory.getClass());
            verifyLocation(reader, data);
            System.out.println();
        } finally {
            reader.close();
        }
    }

    private static void verifyLocation(XMLStreamReader reader, String data) throws XMLStreamException {
        QName qn;
        int n = 0;
        inspectOffset("START_DOCUMENT", reader, data);

        while (n != XMLStreamConstants.END_DOCUMENT) {
            n = reader.next();
            switch (n) {
            case XMLStreamConstants.START_ELEMENT:
                qn = reader.getName();
                inspectOffset("START_ELEMENT:" + qn, reader, data);
                break;
            case XMLStreamConstants.END_ELEMENT:
                qn = reader.getName();
                inspectOffset("END_ELEMENT:" + qn, reader, data);
                break;
            default:
            }
        }
        inspectOffset("END_DOCUMENT", reader, data);
    }

    private static void inspectOffset(String desc, XMLStreamReader reader, String data) {
        int pos = reader.getLocation().getCharacterOffset();
        System.out.println("### " + desc + "; loc=" + pos + "; char='" + getOffsetChar(pos, data) + "'");
    }
    
    private static char getOffsetChar(int pos, String data) {
        return 0 <= pos && pos < data.length() ? data.charAt(pos) : 0;
    }
    
    private static String createTestData(String path) {
        InputStream in = null;
        String data = null;
        try {
            in = StaxOffsetComparisonTest.class.getResourceAsStream(path);
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
