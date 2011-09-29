/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.glite.gridcake.myproxy;

import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * Class that interacts with a MyProxy server.
 * @author csuarez
 */
public class MyProxyService {
    
    /**
     * User for the proxy registration.
     */
    private String user;
    
    /**
     * Password for the proxy registration.
     */
    private String password;
    
    /**
     * MyProxy Globus object to interact with the service.
     */
    private MyProxy server;

    /**
     * Public constructor.
     * @param server MyProxy object correctly configured (hostname and port must be setted!).
     * @param user User for the proxy registration.
     * @param password Password for the proxy registration.
     */
    public MyProxyService(final MyProxy server, final String user, final String password) {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieves a proxy from MyProxy server.
     * @param lifetime Lifetime of the proxy (milliseconds).
     * @return The proxy as a GlobusCredential object.
     * @throws MyProxyException If something fails.
     */
    public final GlobusCredential retrieveProxy(final int lifetime) throws MyProxyException {
        // Initialize the credential class object
        GSSCredential credential = null;

        GlobusCredential proxy = null;
        // Retrieve delegated credentails from MyProxy Server anonymously (without local credentials)
        credential = server.get(user, password, lifetime);

        if (credential != null) {
            if (credential instanceof GlobusGSSCredentialImpl) {
                proxy = ((GlobusGSSCredentialImpl) credential).getGlobusCredential();
            } else {
                throw new MyProxyException("Expected GlobusGSSCredential!");
            }
        } else {
            throw new MyProxyException("No credentials?!");
        }

        return proxy;
    }

    /**
     * Registers a proxy in a MyProxy server.
     * @param proxy The proxy to register as a GlobusCredential object.
     * @param lifetime Lifetime of the proxy (milliseconds).
     * @throws MyProxyException  If something fails.
     */
    public final void registerProxy(final GlobusCredential proxy, final int lifetime) throws MyProxyException {
        try {
            // Initialize the credential class object
            GSSCredential credential = null;

            // Get credential from local proxy.
            credential = new GlobusGSSCredentialImpl(proxy, GSSCredential.DEFAULT_LIFETIME);

            // If we successfully got a credential, then put it to MyProxy Server.
            if (credential != null) {
                // Register delegated credentails to the MyProxy Server using local credentials
                server.put(credential, user, password, lifetime);
            }
        } catch (GSSException ex) {
            throw new MyProxyException(ex.getMessage());
        }
    }
}
