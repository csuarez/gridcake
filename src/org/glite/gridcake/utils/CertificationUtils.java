/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glite.gridcake.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.cert.X509Certificate;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import org.glite.security.delegation.GrDProxyGenerator;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;

/**
 * A set of utils for manage certificates.
 * @author csuarez
 */
public final class CertificationUtils {

    /**
     * Private constructor.
     */
    private CertificationUtils() { }

    /**
     * Transforms an array of X509Certificate to string.
     * @param certificateChain Array of X509Certificates.
     * @return The certificateChain converted to a string.
     * @throws IOException If something fails.
     */
    public static String certificateChainToString(final X509Certificate[] certificateChain) throws IOException {
        Writer certificateChainWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(certificateChainWriter);
        for (X509Certificate certificate : certificateChain) {
            pemWriter.writeObject(certificate);
        }
        pemWriter.close();
        return certificateChainWriter.toString();
    }

    /**
     * Transforms a X509Certificate to string.
     * @param certificate X509Certificate.
     * @return The certificate converted to a string.
     * @throws IOException If something fails.
     */
    public static String certificateToString(final X509Certificate certificate) throws IOException {
        Writer certificateWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(certificateWriter);
        pemWriter.writeObject(certificate);
        pemWriter.close();
        return certificateWriter.toString();
    }
    
    /**
     * Transforms a PKCS10CertificationRequest to string.
     * @param csr PKCS10CertificationRequest.
     * @return The certificate converted to a string.
     * @throws IOException If something fails.
     */
    public static String csrToString(final PKCS10CertificationRequest csr) throws IOException {
        Writer certificateWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(certificateWriter);
        pemWriter.writeObject(csr);
        pemWriter.close();
        return certificateWriter.toString();
    }

    /**
     * Sign an CSR with a GRID proxy.
     * @param csr CSR to sign.
     * @param gliteProxy GRID proxy (as a GlobusCredential instance) for sign the CSR.
     * @param lifetime Lifetime of the CSR signed.
     * @return The CSR signed.
     * @throws Exception If something fails.
     */
    public static byte[] signCSR(final PKCS10CertificationRequest csr,
            final GlobusCredential gliteProxy, final int lifetime)
            throws Exception {

        GrDProxyGenerator proxyGenerator = new GrDProxyGenerator();
        proxyGenerator.setLifetime(lifetime / 3600 / 1000);
        ExtendedGSSCredential proxyConverted = new GlobusGSSCredentialImpl(gliteProxy, GSSCredential.INITIATE_AND_ACCEPT);
        byte[] signedCSR = proxyGenerator.x509MakeProxyCert(csrToString(csr).getBytes(), proxyConverted.export(ExtendedGSSCredential.IMPEXP_OPAQUE), "null");
        return signedCSR;
    }

    /**
     * Converts a GlobusCredential to string.
     * @param gliteProxy The GlobusCredential to convert.
     * @return The GlobusCredential converted.
     * @throws IOException If something fails.
     */
    public static String globusCredentialToString(final GlobusCredential gliteProxy) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gliteProxy.save(outputStream);
        return new String(outputStream.toByteArray());
    }
}
