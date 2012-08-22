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

package me.temp.utils.protocols.core.mvn;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * A simplified SettingsReader to avoid dependency to org.apache.maven.settings and org.plexus.util  
 */
public class SettingsReader {

    public Settings read(InputStream inputStream) throws IOException {
        Settings settings = new Settings();
        
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLEventReader reader = factory.createXMLEventReader(inputStream);
            
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    String lcname = event.asStartElement().getName().getLocalPart();
                    if ("localRepository".equals(lcname)) {
                        settings.setLocalRepository(reader.getElementText());
                    } else if ("mirror".equals(lcname)) {
                        String url = null;
                        while (reader.hasNext()) {
                            event = reader.nextEvent();
                            if (event.isStartElement()) {
                                lcname = event.asStartElement().getName().getLocalPart();
                                if ("url".equals(lcname)) {
                                    url = reader.getElementText();
                                } else if ("mirrorOf".equals(lcname)) {
                                    String[] names = reader.getElementText().split(",");
                                    for (String n : names) {
                                        settings.addRemoteRepository(n, url);
                                    }
                                }
                            } else if (event.isEndElement() 
                                && "mirror".equals(event.asEndElement().getName().getLocalPart())) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }

        return settings;
    }

}
