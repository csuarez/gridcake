/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glite.gridcake.cream;

import org.glite.gridcake.utils.CertificationUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import javax.xml.rpc.ServiceException;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.glite.ce.creamapi.ws.cream2.CREAMLocator;
import org.glite.ce.creamapi.ws.cream2.CREAMPort;
import org.glite.ce.creamapi.ws.cream2.types.JobDescription;
import org.glite.ce.creamapi.ws.cream2.types.JobFilter;
import org.glite.ce.creamapi.ws.cream2.types.JobId;
import org.glite.ce.creamapi.ws.cream2.types.JobRegisterResult;
import org.glite.ce.creamapi.ws.cream2.types.JobStatusResult;
import org.glite.ce.creamapi.ws.cream2.types.Result;
import org.glite.security.delegation.Delegation;
import org.glite.security.delegation.DelegationServiceLocator;
import org.glite.security.trustmanager.OpensslTrustmanager;
import org.glite.security.trustmanager.OpensslTrustmanagerFactory;
import org.globus.gsi.GlobusCredential;

/**
 * Class for a easy use of a CREAM2 service.
 * @author csuarez
 */
public class CREAMService {

    /**
     * Object to interact with a CREAM2 service.
     */
    private CREAMPort cream2Service;
    
    /**
     * Object to interact with a CREAM delegation service.
     */
    private Delegation delegationService;
    
    /**
     * Sufix of an URL CREAM2 service.
     */
    private static final String CREAM2_SERVICE_URL_SUFIX = "/CREAM2";
    
    /**
     * Sufix of an URL CREAM delegation service.
     */
    private static final String DELEGATION_SERVICE_URL_SUFIX = "/gridsite-delegation";

    /**
     * Public constructor.
     * @param url The URL of the CREAM service.
     * @throws ServiceException If there is some problem connecting with the CREAM service.
     * @throws MalformedURLException If the input is a malformed URL.
     */
    public CREAMService(String url) throws ServiceException, MalformedURLException {
        DelegationServiceLocator delegationLocator = new DelegationServiceLocator();
        delegationService = delegationLocator.getGridsiteDelegation(new URL(url + DELEGATION_SERVICE_URL_SUFIX));
        CREAMLocator cream2Locator = new CREAMLocator();
        cream2Service = cream2Locator.getCREAM2(new URL(url + CREAM2_SERVICE_URL_SUFIX));
    }

    /**
     * Delegates a proxy in a CREAM service.
     * @param gliteProxy The gLite proxy to delegate.
     * @param lifetime The delegation duration.
     * @return The delegation ID.
     * @throws Exception If something fails.
     */
    public String delegateProxy(GlobusCredential gliteProxy, int lifetime) throws Exception {
        String delegationId = String.valueOf(System.currentTimeMillis());
        delegateProxyWithCustomDelegationId(gliteProxy, delegationId, lifetime); //TODO: add random factor
        return delegationId;
    }

    /**
     * Delegates a proxy in a CREAM service with a custom delegation ID.
     * @param gliteProxy The gLite proxy to delegate.
     * @param delegationId The delegation ID.
     * @param lifetime The delegation duration.
     * @throws Exception If something fails.
     */
    public void delegateProxyWithCustomDelegationId(GlobusCredential gliteProxy, String delegationId, int lifetime) throws Exception {
        PKCS10CertificationRequest csr = doGetProxyRequest(delegationId);
        doPutProxy(csr, gliteProxy, lifetime, delegationId);
    }

    /**
     * Registers a job.
     * @param jobDescription A JobDescription object.
     * @return The registered job indo.
     * @throws RemoteException  If something fails.
     */
    public JobId registerJob(JobDescription jobDescription) throws RemoteException {
        //jobDescription.setDelegationProxy(delegationProxy);
        JobRegisterResult[] results = cream2Service.jobRegister(new JobDescription[]{jobDescription});
        return results[0].getJobId();
    }

    /**
     * Gets the status of a job.
     * @param jobId The job ID.
     * @param delegationId The delegation ID.
     * @return The job status.
     * @throws RemoteException If something fails. 
     */
    public JobStatusResult jobStatus(JobId jobId, String delegationId) throws RemoteException {
        JobFilter jobFilter = makeSimpleJobFilter(jobId, delegationId);
        JobStatusResult[] results = jobStatusAdvanced(jobFilter);
        return results[0];

    }

    /**
     * Get the status of some jobs using a complex job filter.
     * @param jobFilter A job filter.
     * @return The jobs statuses.
     * @throws RemoteException If something fails.
     */
    public JobStatusResult[] jobStatusAdvanced(JobFilter jobFilter) throws RemoteException {
        return cream2Service.jobStatus(jobFilter);
    }

    /**
     * Stars some jobs using a complex job filter.
     * @param jobFilter A job filter.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result[] startJobAdvanced(JobFilter jobFilter) throws RemoteException {
        return cream2Service.jobStart(jobFilter);
    }

    /**
     * Start a job.
     * @param jobId The job ID.
     * @param delegationId The delegation ID.
     * @return The operation result.
     * @throws RemoteException If somnething fails.
     */
    public Result startJob(JobId jobId, String delegationId) throws RemoteException {
        JobFilter jobFilter = makeSimpleJobFilter(jobId, delegationId);
        Result[] results = startJobAdvanced(jobFilter);
        return results[0];

    }

    /**
     * Suspends some jobs using a complex job filter.
     * @param jobFilter A job filter.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result[] suspendJobAdvanced(JobFilter jobFilter) throws RemoteException {
        return cream2Service.jobSuspend(jobFilter);
    }

    /**
     * Suspends a job.
     * @param jobId The job ID.
     * @param delegationId The delegation ID.
     * @return The operation result.
     * @throws RemoteException  If something fails.
     */
    public Result pauseJob(JobId jobId, String delegationId) throws RemoteException {
        JobFilter jobFilter = makeSimpleJobFilter(jobId, delegationId);
        Result[] results = suspendJobAdvanced(jobFilter);
        return results[0];
    }

    /**
     * Resumes a job using a complex job filter.
     * @param jobFilter A job filter.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result[] resumeJobAdvanced(JobFilter jobFilter) throws RemoteException {
        return cream2Service.jobResume(jobFilter);
    }

    /**
     * Resumes a job.
     * @param jobId The job ID.
     * @param delegationId The delegation ID.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result resumeJob(JobId jobId, String delegationId) throws RemoteException {
        JobFilter jobFilter = makeSimpleJobFilter(jobId, delegationId);
        Result[] results = resumeJobAdvanced(jobFilter);
        return results[0];
    }

    /**
     * Purges some jobs using a complex job filter.
     * @param jobFilter The job filter.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result[] purgeJobAdvanced(JobFilter jobFilter) throws RemoteException {
        return cream2Service.jobPurge(jobFilter);
    }

    /**
     * Purges a job.
     * @param jobId The job ID.
     * @param delegationId The delegation ID.
     * @return The operation result.
     * @throws RemoteException If something fails.
     */
    public Result purgeJob(JobId jobId, String delegationId) throws RemoteException {
        JobFilter jobFilter = makeSimpleJobFilter(jobId, delegationId);
        Result[] results = purgeJobAdvanced(jobFilter);
        return results[0];
    }

    /**
     * Makes a simple job filter.
     * @param jobId A job ID.
     * @param delegationId A delegation Id.
     * @return The job filter.
     */
    private static JobFilter makeSimpleJobFilter(JobId jobId, String delegationId) {
        JobFilter jobFilter = new JobFilter();
        jobFilter.setJobId(new JobId[]{jobId});
        jobFilter.setDelegationId(delegationId);
        return jobFilter;
    }

    /**
     * Does a putProxy operation on a CREAM service.
     * @param csr The CSR returned by a CREAM service.
     * @param gliteProxy Our gLite proxy.
     * @param lifetime The delegation duration.
     * @param delegationId The delegation ID.
     * @throws Exception If something fails.
     */
    private void doPutProxy(PKCS10CertificationRequest csr, GlobusCredential gliteProxy, int lifetime, String delegationId) throws Exception {
        byte[] signedCSR = CertificationUtils.signCSR(csr, gliteProxy, lifetime);
       // String csrSignedAsString = CertificationUtils.certificateToString(signedCSR);
       // String proxyAsString = CertificationUtils.globusCredentialToString(gliteProxy);
        delegationService.putProxy(delegationId, new String(signedCSR));
    }

    /**
     * Does a getProxy operation in a CREAM service.
     * @param delegationId The delegation ID.
     * @return A CSR returned by the CREAM service.
     * @throws Exception If something fails.
     */
    private PKCS10CertificationRequest doGetProxyRequest(String delegationId) throws Exception {
        String csrAsString = delegationService.getProxyReq(delegationId);
        Reader csrReader = new StringReader(csrAsString);
        PEMReader r = new PEMReader(csrReader);
        PKCS10CertificationRequest csr = (PKCS10CertificationRequest) r.readObject();
        return csr;
    }
}
