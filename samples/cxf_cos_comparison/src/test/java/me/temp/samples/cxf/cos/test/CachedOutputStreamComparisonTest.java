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

package me.temp.samples.cxf.cos.test;


import java.io.InputStream;

import org.apache.cxf.io.CachedOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class CachedOutputStreamComparisonTest extends Assert {
    static final int TEST_THRESHOLD = 8;
    static final int TEST_INITIAL_SIZE = 1024;
    static final int TEST_UNIT_SIZE = 4096;
    static final byte[] TEST_UNIT_DATA;

    static {
        TEST_UNIT_DATA = new byte[TEST_UNIT_SIZE];
        for (int i = 0; i < TEST_UNIT_SIZE; i++) {
            TEST_UNIT_DATA[i] = (byte)(0xff & (0x20 + (i % 0x5e)));
        }
    }
    
    private long invokeWriteRead(int factor, String enc) throws Exception {
        CachedOutputStream cos = new CachedOutputStream(TEST_THRESHOLD);
        if (enc != null) {
            cos.setCipherTransformation(enc);
        }
        byte[] buf = new byte[TEST_UNIT_SIZE];
        long starttime = System.currentTimeMillis();
        int size = TEST_INITIAL_SIZE * factor;
        for (int i = 0; i < size; i++) {
            cos.write(TEST_UNIT_DATA);
        }
        cos.flush();
        InputStream in = cos.getInputStream();
        int toread = size * TEST_UNIT_SIZE;
        int offset = 0;
        while (toread > 0) {
            int n = in.read(buf, 0, buf.length);
            if (n < 0) {
                break;
            }
            // verify the decoded data agains the original data
            int nvfd = 0;
            while (nvfd < n) {
                if (buf[nvfd] != TEST_UNIT_DATA[(nvfd + offset) % TEST_UNIT_SIZE]) {
                    fail("corrupted at " + nvfd + "-th byte in the buffer with reference at " 
                        + (nvfd + offset) % TEST_UNIT_SIZE);
                }
                nvfd++;
            }
            offset = (offset + n) % TEST_UNIT_SIZE; 
            toread -= n;
        }
        long endtime = System.currentTimeMillis();
        cos.close();
        long total = (size * TEST_UNIT_SIZE) / 1024;
        System.out.println("enc: " + (enc == null ? "none" : enc ) + "; roundtrip: " + total + "KB");
        System.out.println("  time=" + (endtime - starttime) + "ms; " + total / (endtime - starttime) + "MB/s");
        return endtime - starttime;
    }

    private void repeatInvokeWriteRead(String enc) throws Exception {
        for (int f = 1; invokeWriteRead(f, enc) < 5000 && f < 16; f <<= 2);
    }
    
    @Test
    public void testWriteReadRoundtripWithCipher() throws Exception {
        repeatInvokeWriteRead(null);
        repeatInvokeWriteRead("DES/CFB8/NoPadding");
        repeatInvokeWriteRead("BLOWFISH");
        repeatInvokeWriteRead("ARCFOUR");
        repeatInvokeWriteRead("RC4");
    }

}
