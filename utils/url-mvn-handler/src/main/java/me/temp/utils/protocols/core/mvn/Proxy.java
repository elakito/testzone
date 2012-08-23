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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 */
public class Proxy {
    private Settings localSettings;
    private Settings globalSettings;
    private URL localRepoURL;
    private URL remoteRepoURL;

    public void init() throws IOException {
        SettingsReader reader = new SettingsReader();
        File file = new File(System.getProperty("user.home"), ".m2/settings.xml");
        if (file.exists()) {
            localSettings = reader.read(new FileInputStream(file));                
        }
        
        if (System.getenv("M2_HOME") != null) {
            file =  new File(System.getenv("M2_HOME"), "conf/settings.xml");
            if (file.exists()) {
                globalSettings = reader.read(new FileInputStream(file));                
            }
        }
        
        String repo = null;
        if (localSettings != null) {
            repo = localSettings.getLocalRepository();
        }
        if (repo == null && globalSettings != null) {
            repo = globalSettings.getLocalRepository();
        }
        localRepoURL = repo != null ? new URL("file://" + repo) : null;        

        if (localSettings != null) {
            repo = getRemoteRepositoryUrl(localSettings);
        }
        if (repo == null && globalSettings != null) {
            repo = getRemoteRepositoryUrl(globalSettings);
        }
        remoteRepoURL = repo != null ? new URL(repo) : null;
    }
    
    private String getRemoteRepositoryUrl(Settings settings) {
        String repo = settings.getRemoteRepository();
        return repo.endsWith("/") ? repo : repo + "/";
    }

    public URL getArtifactURL(String gid, String aid, String vid) throws MalformedURLException {
        if (localRepoURL != null) {
            URL u = new URL(localRepoURL, encodeArtifactName(gid, aid, vid));
            File f = new File(u.getFile());
            if (f.exists()) {
                return u;
            }
        }
        if (remoteRepoURL != null) {
            return new URL(remoteRepoURL, encodeArtifactName(gid, aid, vid));
        }
        return null;
    }

    public URL getArtifactURL(String id) throws MalformedURLException {
        final String[] parts = id.split("/");
        return getArtifactURL(parts[0], parts[1], parts[2]);
    }
    
    private static String encodeArtifactName(String gid, String aid, String vid) {
        StringBuffer buf = new StringBuffer();
        buf.append(gid.replace('.', '/')).append('/').
            append(aid).append('/').
            append(vid).append('/').
            append(aid).append('-').append(vid).append(".jar");
        return buf.toString();
    }
    
    
    public URL getLocalRepositoryURL() {
        return localRepoURL;
    }

    public URL getRemoteRepositoryURL() {
        return remoteRepoURL;
    }

}
