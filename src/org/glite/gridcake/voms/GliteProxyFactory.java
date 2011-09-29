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

package org.glite.gridcake.voms;

import jlite.GridAPIException;
import jlite.GridSession;
import jlite.GridSessionFactory;
import org.glite.voms.contact.VOMSProxyBuilder;
import org.globus.gsi.GlobusCredential;

/**
 * A factory of gLite proxies.
 * @author csuarez
 */
public class GliteProxyFactory {

    /**
     * Generates a gLite proxy.
     * @param config Information about the VOMS.
     * @return A gLite proxy.
     * @throws GridAPIException If something fails. 
     */
    public static GlobusCredential generateGliteProxy(GliteProxyFactoryConfig config) throws GridAPIException {
        GridSession grid = GridSessionFactory.create(config.getGridSessionConfig());

        boolean limited = false;

        GlobusCredential proxy = grid.createProxy(
                new String[]{config.getVoName()},
                config.getLifetime(),
                VOMSProxyBuilder.DEFAULT_PROXY_TYPE,
                limited);

        return proxy;
    }
}
