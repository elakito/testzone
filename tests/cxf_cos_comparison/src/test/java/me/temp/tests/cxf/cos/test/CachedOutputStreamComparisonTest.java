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

package me.temp.tests.cxf.cos.test;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import org.apache.cxf.io.CachedOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class CachedOutputStreamComparisonTest extends Assert {
    static final int TEST_THRESHOLD = 8;
    static final int TEST_INITIAL_SIZE = 1024;
    static final int TEST_UNIT_SIZE = 4096;
    static final byte[] TEST_UNIT_DATA;
    static final String TEST_CHUNK_STRING = "Hello World! What a nice day."; 
    static final byte[] TEST_CHUNK_BYTES = TEST_CHUNK_STRING.getBytes();
    static final String[] CIPHER_NAMES = {null, "DES/CFB/PKCS5Padding", "DES/ECB/PKCS5Padding", "DES/CFB8/NoPadding",  "DES/CTR/NoPadding",
                                          "BLOWFISH/CFB/NoPadding", "BLOWFISH/ECB/PKCS5Padding", "BLOWFISH/CFB8/NoPadding", "BLOWFISH/CTR/NoPadding",
                                          "AES/CBC/PKCS5Padding", "AES/ECB/PKCS5Padding", "AES/CFB/NoPadding", "AES/CFB8/NoPadding", "AES/CTR/NoPadding",
                                          "RC4"
                                          };

    static {
        TEST_UNIT_DATA = new byte[TEST_UNIT_SIZE];
        for (int i = 0; i < TEST_UNIT_SIZE; i++) {
            TEST_UNIT_DATA[i] = (byte)(0xff & (0x20 + (i % 0x5e)));
        }
    }
    
    private long invokeWriteRead(int factor, String enc, boolean ftc) throws Exception {
        CachedOutputStream cos = new CachedOutputStream(TEST_THRESHOLD);
        try {
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
            if (ftc) {
                cos.close();
            }

            int toread = size * TEST_UNIT_SIZE;
            int offset = 0;
            try {
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
            } finally {
                in.close();
            }
            long endtime = System.currentTimeMillis();
            long total = (size * TEST_UNIT_SIZE) / 1024;
            long dtime = endtime - starttime;
            System.out.println(getCipherName(enc) + ";" + ftc + ";" + total + ";" + dtime + ";" + total / dtime);
            // output the line for generating a comparison table for the markdown format
            System.out.println("json:[\"" 
                + getCipherName(enc) + "\", " + ftc + ", " + total + ", " + dtime + "]");

            return endtime - starttime;
        } finally {
            if (!ftc) {
                cos.close();
            }
        }
    }
    
    private void invokeWriteReadChunk(String enc, boolean ftc) throws Exception {
        CachedOutputStream cos = new CachedOutputStream(TEST_THRESHOLD);
        try {
            if (enc != null) {
                cos.setCipherTransformation(enc);
            }
            // write the original test data
            cos.write(TEST_CHUNK_BYTES);
            cos.flush();
            InputStream in = cos.getInputStream();
            if (ftc) {
                cos.close();
            }

            File tmpfile = cos.getTempFile();
            assertNotNull("no file for cipher " + getCipherName(enc), tmpfile);
            // verify the written raw data is not the same as the original
            if (enc != null) {
                assertFalse("not encodded by " + getCipherName(enc), TEST_CHUNK_STRING.equals(new String(getBytes(new FileInputStream(tmpfile)))));
            }

            // verify the decoded data
            byte[] data = getBytes(in);
            assertArrayEquals("corrupted by cipher " + getCipherName(enc) + " with ftc " + ftc +"; " + new String(TEST_CHUNK_BYTES) + " != " + new String(data),
                              TEST_CHUNK_BYTES, data);
            System.out.println(getCipherName(enc) + ";" + ftc);
            // output the line for generating a comparison table for the markdown format
            System.out.println("json:[\"" + getCipherName(enc) + "\", " + ftc + "]");
        } finally {
            cos.close();
        }
    }

    private static String getCipherName(String enc) {
        return enc == null ? "none" : enc;
    }
    
    private static byte[] getBytes(InputStream in) throws IOException {
        byte[] buf = new byte[512];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            for (;;) {
                int n = in.read(buf, 0, buf.length);
                if (n < 0) {
                    break;
                }
                bos.write(buf, 0, n);
            }
            return bos.toByteArray();
        } finally {
            in.close();
        }
    }
    
    private static void initCipher(String name) throws GeneralSecurityException {
        try {
            int d = name.indexOf('/');
            String a;
            if (d > 0) {
                a = name.substring(0, d);
            } else {
                a = name;
            }
            KeyGenerator keygen = KeyGenerator.getInstance(a);
            keygen.init(new SecureRandom());
            Key key = keygen.generateKey();
            Cipher enccipher = Cipher.getInstance(name);
            Cipher deccipher = Cipher.getInstance(name);
            enccipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] ivp = enccipher.getIV();
            deccipher.init(Cipher.DECRYPT_MODE, key, ivp == null ? null : new IvParameterSpec(ivp));
        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    @Test
    public void testSupportedCiphers() throws Exception {
        System.out.println("### Supported Ciphers ###");
        System.out.println("cipherName");
        for (String name : CIPHER_NAMES) {
            if (name != null) {
                try {
                    initCipher(name);
                    System.out.println(name);
                } catch (Exception e) {
                    fail("no such cipher: " + name);
                }
            }
        }
    }
    @Test
    public void testGerKeyGens() throws Exception {
        KeyGenerator keygen;
        keygen = KeyGenerator.getInstance("RC4");
        keygen.init(new SecureRandom());
        keygen = KeyGenerator.getInstance("RC4");
        keygen.init(new SecureRandom());
        keygen = KeyGenerator.getInstance("DES");
        keygen.init(new SecureRandom());
        keygen = KeyGenerator.getInstance("DES");
        keygen.init(new SecureRandom());
        keygen = KeyGenerator.getInstance("AES");
        keygen.init(new SecureRandom());
        keygen = KeyGenerator.getInstance("AES");
        keygen.init(new SecureRandom());
    }
    @Test
    public void testWriteReadRoundtripWithCipher() throws Exception {
        System.out.println("### WriteReadRoundTrip ###");
        System.out.println("cipherName;terminate;size[KB];time[ms];rate[MB/s]");
        for (int f = 1; f < 8; f <<= 2) {
            for (String name : CIPHER_NAMES) {
                try {
                    invokeWriteRead(f, name, false);
                } catch (AssertionError ae) {
                    // see if the error goes away when the cipher is closed
                    invokeWriteRead(f, name, true);
                }
            }
        }
    }
    
    @Test
    public void testWriteReadChunkWithCipher() throws Exception {
        System.out.println("### WriteReadChunk ###");
        System.out.println("cipherName;terminate");
        for (String name : CIPHER_NAMES) {
            try {
                invokeWriteReadChunk(name, false);
            } catch (AssertionError ae) {
                // see if the error goes away when the cipher is closed
                invokeWriteReadChunk(name, true);
            }
        }
    }
}
