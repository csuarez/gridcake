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

package org.glite.gridcake.gridftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.globus.ftp.DataSinkStream;
import org.globus.ftp.DataSourceStream;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.Marker;
import org.globus.ftp.MarkerListener;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;

/**
 * Class for easy use of a GridFTP service.
 * @author csuarez
 */
public class GridFTPService {

    /**
     * Hostname of the storage element.
     */
    private String hostname;
    
    /**
     * Port of the GRIDFtp service.
     */
    private int port;

    /**
     * Public constructor.
     * @param hostname Hostname of the storage element.
     * @param port Port of the GRIDFtp service.
     */
    public GridFTPService(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Downloads a file from a storage element using GRIDFtp.
     * @param proxy Credentials to access to the service as a GlobusCredential object.
     * @param urlTarget URL of the file to download.
     * @return The file as a byte array.
     * @throws Exception If something fails.
     */
    public byte[] download(GlobusCredential proxy, String urlTarget) throws Exception {
        byte[] digitalContent = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataSinkStream datasink = new DataSinkStream(bos);
        GSSCredential credential = new GlobusGSSCredentialImpl(proxy, GSSCredential.INITIATE_AND_ACCEPT);
        GridFTPClient client = new GridFTPClient(hostname, port);
        client.authenticate(credential);
        client.setType(org.globus.ftp.Session.TYPE_IMAGE);
        client.setPassiveMode(true);
        client.setMode(org.globus.ftp.Session.MODE_STREAM);
        client.get(urlTarget.toString(), datasink, new MarkerListener() {

            public void markerArrived(Marker arg0) {
            }
        });
        client.close();
        digitalContent = bos.toByteArray();

        return digitalContent;
    }

    /**
     * Uploads a file to a storage element using GRIDFtp.
     * @param proxy Credentials to access to the service as a GlobusCredential object.
     * @param digitalContent The file we want to upload as a byte array.
     * @param urlTarget URL of the path where we want to upload our file.
     * @throws Exception If something fails.
     */
    public void upload(GlobusCredential proxy, byte[] digitalContent, String urlTarget) throws Exception {
        GSSCredential credential = new GlobusGSSCredentialImpl(proxy, GSSCredential.INITIATE_AND_ACCEPT);

        ByteArrayInputStream bis = new ByteArrayInputStream(digitalContent);
        DataSourceStream source = new DataSourceStream(bis);
        GridFTPClient client = new GridFTPClient(hostname, port);
        client.authenticate(credential);
        client.setType(org.globus.ftp.Session.TYPE_IMAGE);
        client.setPassiveMode(true);
        client.setMode(org.globus.ftp.Session.MODE_STREAM);
        client.extendedPut(urlTarget.toString(), source,
                new MarkerListener() {

                    public void markerArrived(Marker arg0) {
                    }
                });
        client.close();
    }

    /**
     * Deletes a file from a storage element using GRIDFtp.
     * @param proxy Credentials to access to the service as a GlobusCredential object.
     * @param urlTarget URL of the file to download.
     * @throws Exception If something fails.
     */
    public void delete(GlobusCredential proxy, String urlTarget) throws Exception {
        GSSCredential credential = new GlobusGSSCredentialImpl(proxy, GSSCredential.INITIATE_AND_ACCEPT);
        GridFTPClient client = new GridFTPClient(hostname, port);
        client.authenticate(credential);
        client.setPassiveMode(true);
        client.setType(org.globus.ftp.Session.TYPE_IMAGE);
        client.setMode(org.globus.ftp.Session.MODE_STREAM);
        client.deleteFile(urlTarget.toString());
        client.close();
    }
}
