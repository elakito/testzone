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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * copied from me.dev.misc/xml's test package me.dev.misc.xml.util
 * TODO move this class to some reusable test-utils package
 */
public final class TestDataGenerator {
    private TestDataGenerator() {
    }
  
    /**
     * This utility class can generate an input stream for TokenIterator tests.
     * 
     * @param item the repeatable string that may contain variable place holders
     * @param repeat the number of repetition
     * @param head the header part
     * @param tail the trailer part
     * @param values the optional iterator for the variables referenced in the item string
     * @param charset the charset
     */
    public static InputStream createTokenDataInputStream(String item, int repeat, String head, String tail, Iterator<Object[]> values, String charset) 
        throws IOException {
        return new TokenDataInputStream(item, repeat, head, tail, values, charset);
    }

    static class TokenDataInputStream extends InputStream {
        private String vitem;
        private byte[] item;
        private byte[] head;
        private byte[] tail;
        private int repeat;
        private Iterator<Object[]> values;
        private String charset;
        
        private byte[] buffer;
        private int bpos;
        private int stage; // 0, head, 1, item, 2, tail, 3
        private int pos;
        private int rcount;
        
        private static final int BUFFER_SIZE = 4096;
        
        public TokenDataInputStream(String item, int repeat, String head, String tail, Iterator<Object[]> values, String charset) 
            throws IOException {
            this.buffer = new byte[BUFFER_SIZE];
            this.values = values;
            this.vitem = item;
            if (charset != null) {
                this.item = values == null ? vitem.getBytes(charset) : null;
                this.head = head.getBytes(charset);
                this.tail = tail.getBytes(charset);
            } else {
                this.item = values == null ? vitem.getBytes() : null;
                this.head = head.getBytes();
                this.tail = tail.getBytes();
            }
            this.repeat = repeat;
        }

        @Override
        public int read() throws IOException {
            byte[] ba = new byte[1];
            return read(ba, 0, 1) == 1 ? ba[0] : -1;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int tlen = 0;
            while (len > 0) {
                int n = ensureBuffering(len);
                if (n < 0) {
                    break;
                }
                int clen = len > n ? n : len;
                System.arraycopy(buffer, 0, b, off, clen);
                System.arraycopy(buffer, clen, buffer, 0, buffer.length - clen);
                bpos -= clen;
                len -= clen;
                off += clen;
                tlen += clen;
            }

            return tlen > 0 ? tlen : -1;
        }
        
        private int ensureBuffering(int size) {
            if (size < bpos) {
                return bpos;
            }
            
            // refill the buffer as more buffering is requested than the current buffer status
            if (stage == 0) {
                // head
                int len = head.length - pos;
                if (len > buffer.length - bpos) {
                    len = buffer.length - bpos;
                } else {
                    stage++;
                }
                System.arraycopy(head, pos, buffer, bpos, len);
                bpos += len;
                if (bpos == buffer.length) {
                    pos += len;
                    pos %= head.length;
                    return buffer.length;
                } else {
                    pos = 0;
                }
            }
            
            while (stage == 1) {
                // item
                if (pos == 0) {
                    prepareItemBytes();
                }
                if (rcount < repeat) {
                    int len = item.length - pos;
                    if (len > buffer.length - bpos) {
                        len = buffer.length - bpos;
                    } else {
                        rcount++;
                    }
                    System.arraycopy(item, pos, buffer, bpos, len);
                    bpos += len;
                    if (bpos == buffer.length) {
                        pos += len;
                        pos %= item.length;
                        return buffer.length;
                    } else {
                        pos = 0;
                    }
                } else {
                    stage++;
                }
            }

            if (stage == 2) {
                // tail
                int len = tail.length - pos;
                if (len > buffer.length - bpos) {
                    len = buffer.length - bpos;
                } else {
                    stage++;
                }
                System.arraycopy(tail, pos, buffer, bpos, len);
                bpos += len;
                if (bpos == buffer.length) {
                    pos += len;
                    pos %= tail.length;
                    return buffer.length;
                } else {
                    pos = 0;
                }
            }
            
            return bpos == 0 ? -1 : bpos;
        }

        private void prepareItemBytes() {
            try {
                if (values != null) {
                    if (values.hasNext()) {
                        String v = MessageFormat.format(vitem, values.next());
                        item = charset == null ? v.getBytes() : v.getBytes(charset);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        }
    }
}
