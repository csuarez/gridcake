/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
