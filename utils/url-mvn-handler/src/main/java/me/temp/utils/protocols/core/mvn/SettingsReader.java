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

import javax.xml.namespace.QName;
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
                    if ("settings".equals(lcname) || "profiles".equals(lcname) || "activeProfiles".equals(lcname)) {
                        continue;
                    } else if ("localRepository".equals(lcname)) {
                        settings.setLocalRepository(reader.getElementText());
                    } else if ("profile".equals(lcname)) {
                        String id = null;
                        while (reader.hasNext()) {
                            event = reader.nextEvent();
                            if (event.isStartElement()) {
                                lcname = event.asStartElement().getName().getLocalPart();
                                if ("repositories".equals(lcname)) {
                                    continue;
                                } else if ("id".equals(lcname)) {
                                    id = reader.getElementText();
                                } else if ("repository".equals(lcname)) {
                                    while (reader.hasNext()) {
                                        event = reader.nextEvent();
                                        if (event.isStartElement()) {
                                            lcname = event.asStartElement().getName().getLocalPart();
                                            if ("url".equals(lcname)) {
                                                settings.addRemoteRepository(id, reader.getElementText());
                                            }
                                        } else if (event.isEndElement() 
                                            && "repository".equals(event.asEndElement().getName().getLocalPart())) {
                                            break;
                                        }
                                    }
                                } else {
                                    skipElement(reader);
                                }
                            } else if (event.isEndElement() 
                                && "profile".equals(event.asEndElement().getName().getLocalPart())) {
                                break;
                            }
                        }
                    } else if ("activeProfile".equals(lcname)) {
                        settings.setActiveProfile(reader.getElementText());
                    } else {
                        skipElement(reader);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }

        return settings;
    }

    private void skipElement(XMLEventReader reader) throws XMLStreamException {
        int level = 1;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                level++;
            } else if (event.isEndElement()) {
                level--;
            }
            if (level == 0) {
                break;
            }
        }
    }

}
