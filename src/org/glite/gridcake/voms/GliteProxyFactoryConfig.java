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

import jlite.GridSessionConfig;

/**
 * Configuration object for a GliteProxyFactory.
 * @author csuarez
 */
public class GliteProxyFactoryConfig {
    /**
     * GridSessionConfig object.
     */
    private GridSessionConfig gridSessionConfig;
    
    /**
     * Proxy duration.
     */
    private int lifetime;
    
    /**
     * VO name.
     */
    private String voName;
    
    /**
     * Public construction.
     * @param gridSessionConfig GridSessionConfig correctly built.
     * @param lifetime Proxy duration.
     * @param voName VO name.
     */
    public GliteProxyFactoryConfig (GridSessionConfig gridSessionConfig, int lifetime, String voName) {
        this.lifetime = lifetime;
        this.gridSessionConfig = gridSessionConfig;
        this.voName = voName;
    }
    
  
    /**
     * Returns the VO name.
     * @return VO name.
     */
    public String getVoName() {
        return voName;
    }

    /**
     * Returns the GridSessionConfig.
     * @return GridSessionConfig.
     */
    public GridSessionConfig getGridSessionConfig() {
        return gridSessionConfig;
    }

    /**
     * Returns the proxy duration.
     * @return Proxy duration.
     */
    public int getLifetime() {
        return lifetime;
    }    
}
