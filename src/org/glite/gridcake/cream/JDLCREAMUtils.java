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

package org.glite.gridcake.cream;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.glite.jdl.JobAd;

/**
 * Class with auxiliary methods to retrieve info from a JDL.
 * @author csuarez
 */
public class JDLCREAMUtils {

    /**
     * Gets the path outputs that have to be downloaded from the SE.
     * @param jdlAsString The JDL as string.
     * @return A list with the output paths prepared for a GridFTP client.
     * @throws Exception If something fails.
     */
    public static List<String> getOutputToDownload(final String jdlAsString) throws Exception {
        JobAd jdl = new JobAd(jdlAsString);
        List<String> outputToDownload = new ArrayList<String>();

        if (jdl.hasAttribute("OutputSandbox")) {
            if (!jdl.hasAttribute("OutputSandboxBaseDestURI")) {
                throw new Exception("No OutputSandboxBaseDestURI parameter!");
            }
            String outputSandboxBase = jdl.getString("OutputSandboxBaseDestURI");
            if (!isFolderUri(outputSandboxBase)) {
                outputSandboxBase += "/";
            }

            Vector<String> outputSandbox = jdl.getStringValue("OutputSandbox");
            outputToDownload = getGridFtpPaths(outputSandbox, outputSandboxBase);
        }
        return outputToDownload;
    }

    /**
     * Gets the GridFTP URIs from a set of paths and a GridFTP URI base.
     * @param sandbox A list of the paths to convert.
     * @param sandboxBase The URI base.
     * @return The paths converted.
     */
    private static List<String> getGridFtpPaths(final Vector<String> sandbox, final String sandboxBase) {
        List<String> gridFtpUris = new ArrayList<String>();
        for (int i = 0; i < sandbox.size(); i++) {
            String output = sandbox.elementAt(i);
            String fullPath = sandboxBase + output;
            try {
                String path = getPathFromGsiFtpUri(fullPath);
                gridFtpUris.add(path);
            } catch (Exception e) {
                //Ignore exception for local outputs.
            }
        }
        
        return gridFtpUris;
    }

    /**
     * Gets the path inputs that have to be uploaded to the SE. 
     * @param jdlAsString The JDL as string.
     * @return A list with the input paths prepared for a GridFTP client.
     * @throws Exception If something fails.
     */
    public static List<String> getInputToUpload(final String jdlAsString) throws Exception {
        JobAd jdl = new JobAd(jdlAsString);
        List<String> inputToUpload = new ArrayList<String>();
        String intputSandboxBase = "";
        if (jdl.hasAttribute("InputSandbox")) {
            if (jdl.hasAttribute("InputSandboxBaseDestURI")) {
                intputSandboxBase = jdl.getString("InputSandboxBaseDestURI");
            }
            if (!isFolderUri(intputSandboxBase)) {
                intputSandboxBase += "/";
            }

            Vector<String> inputSandbox = jdl.getStringValue("InputSandbox");
            inputToUpload = getGridFtpPaths(inputSandbox, intputSandboxBase);
        }
        return inputToUpload;
    }

    /**
     * Converts a GridFTP URI to a path to use in a GridFTP client.
     * @param uri The URI to convert.
     * @return A GridFTP URI converted.
     * @throws Exception If something fails.
     */
    protected static String getPathFromGsiFtpUri(final String uri) throws Exception {
        String[] tokens = uri.split("/");
        String uriPath = "";
        if (isValidGsiFtpUri(uri)) {
            for (int i = 3; i < tokens.length; i++) {
                uriPath += "/" + tokens[i];
            }

            if (isFolderUri(uri)) {
                uriPath += "/";
            }
            return uriPath;
        } else {
            throw new Exception("No valid URI!");
        }
    }

    /**
     * Checks if an URI it's associated to a folder.
     * @param uri The URI to check.
     * @return True if the URI is associated to a folder, False if the URI is associated to a file.
     */
    protected static boolean isFolderUri(final String uri) {
        return uri.charAt(uri.length() - 1) == '/';
    }

    /**
     * Checks if an URI is a GridFTP URI.
     * @param uri The URI to check.
     * @return True if the input URI is a valid GridFTP URI.
     */
    protected static boolean isValidGsiFtpUri(final String uri) {
        String[] tokens = uri.split("/");
        if (tokens.length < 4) {
            if (!isFolderUri(uri)) {
                return false;
            }
        }
        if (!tokens[0].equals("gsiftp:")) {
            return false;
        }

        if (!tokens[1].equals("")) {
            return false;
        }

        if (tokens[2].equals("")) {
            return false;
        }

        return true;
    }
}
