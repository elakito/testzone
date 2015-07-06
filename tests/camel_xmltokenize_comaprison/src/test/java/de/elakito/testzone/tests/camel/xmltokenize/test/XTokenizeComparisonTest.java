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
import java.util.Iterator;

import org.apache.camel.support.XMLTokenExpressionIterator;

public class XTokenizeComparisonTest extends AbstractXMLTokenizeComparisonTest {
    @Override
    protected TestInstance createTokenIterator(String type, char mode) {
        TestInstance ti = null;
        if ("rss".equals(type)) {
            ti = new TestTokenIterator("//item", mode);
        } else if ("parts".equals(type)) {
            ti = new TestTokenIterator("//*:Part", mode);
        }
            
        return ti;
    }

    @Override
    protected String getTokenizerName() {
        return "xtokenize";
    }

    // the stax based xml tokenizer of camel 2.14 that works for any xml structure
    private static class TestTokenIterator extends XMLTokenExpressionIterator implements TestInstance {

        public TestTokenIterator(String path, char mode) {
            super(path, mode);
        }

        @Override
        public Iterator<?> createIterator(InputStream in, String charset) {
            try {
                return super.createIterator(in, charset);
            } catch (Exception e) {
                // ignore
                return null;
            }
        }
        
    }
}
