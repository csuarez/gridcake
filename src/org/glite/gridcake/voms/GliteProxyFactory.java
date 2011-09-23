/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
