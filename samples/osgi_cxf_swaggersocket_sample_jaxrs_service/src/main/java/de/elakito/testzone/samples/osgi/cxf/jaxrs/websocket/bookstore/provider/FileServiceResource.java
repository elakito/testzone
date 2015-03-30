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

package de.elakito.testzone.samples.osgi.cxf.jaxrs.websocket.bookstore.provider;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import java.io.InputStream;

@Path("/")
public class FileServiceResource {

    @Path("/jquery/{id}")
    @GET
    @Produces("application/javascript")
    public InputStream getJQuery(@PathParam("id") PathSegment ps) {
        return FileServiceResource.class.getResourceAsStream("/jquery/" + ps.getPath());
    }

    @GET
    @Produces("text/html")
    public InputStream getIndex() {
        return FileServiceResource.class.getResourceAsStream("/index.html");
    }

    @Path("/fallback")
    @GET
    @Produces("text/html")
    public InputStream getIndexFallback() {
        return FileServiceResource.class.getResourceAsStream("/index_fallback.html");
    }
}
