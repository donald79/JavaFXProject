package com.strona2;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineAuthenticationException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.internal.Transport;
import com.taskadapter.redmineapi.internal.URIConfigurator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import com.taskadapter.redmineapi.internal.Transport;
import com.taskadapter.redmineapi.internal.URIConfigurator;
import com.taskadapter.redmineapi.internal.comm.redmine.RedmineAuthenticator;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
// Library https://github.com/taskadapter/redmine-java-api

public class RedmineData {

    String uri = "https://redmine.jaggi.com.pl";
    String apiAccessKey = "636b54544bdc9982bd11ca073d58956a809eb342";
    String projectKey = "ppm";
    Integer queryId = 12; // any before I set up query in Redmine --> id:12 (Search all)
    HttpClient httpClient;
    RedmineManager redmineManager;
    IssueManager issueManager;
    ProjectManager projectManager;
    List<Issue> issues;
    List<Project> listOfProjectsFromRedmine;
    Issue issue;
    String exceptionMessage;
    boolean isLoginAndPasswordCorrect=false;

    public void logingIn(String login, String password) {
        try {
            exceptionMessage = "";
            System.out.println("try: " + login + " " + password);          
            httpClient = createHttpClient_AcceptsUntrustedCerts();
//             mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
//             mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey, httpClient);

         redmineManager = RedmineManagerFactory.createWithUserAuth( uri,  "karol.kolanczak", "karolek79",  httpClient);     
//            mgr = RedmineManagerFactory.createWithUserAuth(uri, login, password, httpClient);

            issueManager = redmineManager.getIssueManager();
            issues = issueManager.getIssues(projectKey, queryId);   
            projectManager = redmineManager.getProjectManager();
            listOfProjectsFromRedmine=projectManager.getProjects();
            
            isLoginAndPasswordCorrect=true;
            System.out.println("login and password correct");
          
//            return issues;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Inside class 'RedmineData': " + e);
        } catch (KeyStoreException e) {
            System.out.println("Inside class 'RedmineData': " + e);
        } catch (KeyManagementException e) {
            System.out.println("Inside class 'RedmineData': " + e);
        } catch (RedmineAuthenticationException e) {
            isLoginAndPasswordCorrect=false;
            exceptionMessage = "Login or password is not correct";
            System.out.println("Inside class 'RedmineData': " + e);
            
        } catch (RedmineException e) {
            System.out.println("Inside class 'RedmineData': " + e);
        }
//        return null;
    }

    // from author: of article: "How to ignore SSL certificate errors in Apache HttpClient 4.4"  
    // http://literatejava.com/networks/ignore-ssl-certificate-errors-apache-httpclient-4-4/
    public HttpClient createHttpClient_AcceptsUntrustedCerts() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();
        // setup a Trust Strategy that allows all certificates.
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        b.setSSLContext(sslContext);
        // don't check Hostnames, either.
        //-- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        // -- need to create an SSL Socket Factory, to use our weakened "trust strategy"and create a Registry, to register it.
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        // now, we create connection-manager using our Registry. -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        b.setConnectionManager(connMgr);
        // finally, build the HttpClient;
        HttpClient httpClient = b.build();
        return httpClient;
    }
     public List<Project> getProjectsFromRedmine(){           
         System.out.println("22222"+listOfProjectsFromRedmine);
            return listOfProjectsFromRedmine;
      
    }
    public void printAll(List<Issue> issues) {
        for (Issue issueValue : issues) {
            System.out.println(issueValue);
        }
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public Issue searchIssueById(int id) {

        try {
            exceptionMessage = "";
            return issueManager.getIssueById(id);
        } catch (RedmineAuthenticationException ex) {
            exceptionMessage = "user has no proper permissions";
            System.out.println("searchIssueById - RedmineAuthenticationException: " + ex);
        } catch (NotFoundException ex) {
            exceptionMessage = "id not found";
            System.out.println("searchIssueById - NotFoundException: " + ex);
        } catch (RedmineException ex) {
            exceptionMessage = "user has no proper permissions";
            System.out.println("searchIssueById - RedmineException: " + ex);
        }
        return null;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

}
