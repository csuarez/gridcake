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

import org.glite.gridcake.cream.JDLCREAMUtils;
import org.glite.gridcake.utils.IOUtils;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author csuarez
 */
public class JDLCREAMUtilsTest extends TestCase {

    public JDLCREAMUtilsTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetOutput() throws Exception {
        System.out.println("testGetOutput");
        String jdl = IOUtils.fileToString("resources/full-output.jdl");
        List<String> output = JDLCREAMUtils.getOutputToDownload(jdl);
        assertEquals(output.get(0), "/tmp/std.out");
        assertEquals(output.get(1), "/tmp/std.err");
    }
    
    @Test
    public void testGetNoneOutput() throws Exception {
        System.out.println("testGetNoneOutput");
        String jdl = IOUtils.fileToString("resources/no-output.jdl");
        List<String> output = JDLCREAMUtils.getOutputToDownload(jdl);
        assertEquals(output.size(), 0);
    }
    
    @Test (expected = Exception.class)
    public void testGetOutputNoBase() throws Exception {
        System.out.println("testGetOutputNoBase");
        String jdl = IOUtils.fileToString("resources/no-base.jdl");
        List<String> output = JDLCREAMUtils.getOutputToDownload(jdl);
    }
    
    @Test
    public void testGetInput() throws Exception {
        System.out.println("testGetInput");
        String jdl = IOUtils.fileToString("resources/full-input.jdl");
        List<String> input = JDLCREAMUtils.getInputToUpload(jdl);
        assertEquals(input.get(0), "/tmp/std.out");
        assertEquals(input.get(1), "/tmp/std.err");
    }
    
    @Test
    public void testGetNoneInput() throws Exception {
        System.out.println("testGetNoneInput");
        String jdl = IOUtils.fileToString("resources/no-input.jdl");
        List<String> input = JDLCREAMUtils.getInputToUpload(jdl);
        assertEquals(input.size(), 0);
    }
    
    @Test (expected = Exception.class)
    public void testGetInputNoBase() throws Exception {
        System.out.println("testGetInputNoBase");
        String jdl = IOUtils.fileToString("resources/no-base.jdl");
        List<String> input = JDLCREAMUtils.getInputToUpload(jdl);
    }

    @Test
    public void testIsFolderUri() {
        System.out.println("testIsFolderUri");
        boolean result = JDLCREAMUtils.isFolderUri("foo/");
        assertTrue(result);
    }
    
    @Test
    public void testIsNotFolderUri() {
       System.out.println("testIsNotFolderUri");
        boolean result = JDLCREAMUtils.isFolderUri("foo");
        assertFalse(result); 
    }
    
    @Test
    public void testIsValidGsiFtpUriNoPath() throws Exception {
        System.out.println("testIsValidGsiFtpUriNoPath");
        boolean result = JDLCREAMUtils.isValidGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811");
        assertFalse(result);
    }
    
    @Test
    public void testIsValidGsiFtpUriBadInit() throws Exception {
        System.out.println("testIsValidGsiFtpUriBadInit");
        boolean result = JDLCREAMUtils.isValidGsiFtpUri("gsxiftp://se-test.ceta-ciemat.es:2811/");
        assertFalse(result);
    }
    
    @Test
    public void testIsValidGsiFtpUriNotEnoughSlashes() throws Exception {
        System.out.println("testIsValidGsiFtpUriNotEnoughSlashes");
        boolean result = JDLCREAMUtils.isValidGsiFtpUri("gsiftp:/se-test.ceta-ciemat.es:2811/");
        assertFalse(result);
    }
    
     @Test
    public void testIsValidGsiFtpUriTooManySlashes() throws Exception {
        System.out.println("testIsValidGsiFtpUriTooManySlashes");
        boolean result = JDLCREAMUtils.isValidGsiFtpUri("gsiftp:////se-test.ceta-ciemat.es:2811/");
        assertFalse(result);
    }
    
     @Test
    public void testParseGsiFtpUriSingleFolder() throws Exception {
        System.out.println("testParseGsiFtpUriSingleFolder");
        String result = JDLCREAMUtils.getPathFromGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811/tmp/");
        assertEquals("/tmp/", result);
    }

    @Test
    public void testParseGsiFtpUriSingleFile() throws Exception {
        System.out.println("testParseGsiFtpUriSingleFile");
        String result = JDLCREAMUtils.getPathFromGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811/tmp");
        assertEquals("/tmp", result);
    }

      @Test
    public void testParseGsiFtpUriRoot() throws Exception {
        System.out.println("testParseGsiFtpUriRoot");
        String result = JDLCREAMUtils.getPathFromGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811/");
        assertEquals("/", result);
    }
@Test
    public void testParseGsiFtpUriMultipleFile() throws Exception {
        System.out.println("testParseGsiFtpUriMultipleFile");
        String result = JDLCREAMUtils.getPathFromGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811/tmp/foo");
        assertEquals("/tmp/foo", result);
    }

    @Test
    public void testParseGsiFtpUriMultipleFolder() throws Exception {
        System.out.println("testParseGsiFtpUriMultipleFolder");
        String result = JDLCREAMUtils.getPathFromGsiFtpUri("gsiftp://se-test.ceta-ciemat.es:2811/tmp/foo/");
        assertEquals("/tmp/foo/", result);
    }
    
    
    
}
