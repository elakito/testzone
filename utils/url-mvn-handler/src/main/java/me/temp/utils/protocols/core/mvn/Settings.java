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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class Settings {
    private String localRepository;
    /* profile-id */
    private String activeProfile;
    /* profile-id -> url */ 
    private Map<String, String> remoteRepositories = new HashMap<String, String>();
    
    public String getLocalRepository() {
        return localRepository;
    }
    
    public void setLocalRepository(String localRepository) {
        this.localRepository = localRepository;
    }

    public String getRemoteRepository() {
        return remoteRepositories.get(activeProfile);
    }

    public String addRemoteRepository(String id, String remoteRepository) {
        return remoteRepositories.put(id, remoteRepository);
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }
}
