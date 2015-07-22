package org.shm.monitoring.service;

import org.shm.monitoring.domain.ProjectConfiguration;
import org.springframework.security.crypto.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.shm.monitoring.web.dto.HttpResponse;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;

@Service
@Transactional
public class TestConnectionService {

    private final Logger log = LoggerFactory.getLogger(TestConnectionService.class);


    /**
     * @param projectConfiguration
     * @return
     */
    public HttpResponse testUrl(ProjectConfiguration projectConfiguration) {

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setProjectConfiguration(projectConfiguration);

        try {

            URL url = new URL(projectConfiguration.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (projectConfiguration.getUrl().startsWith("https")) {
                if (httpURLConnection instanceof HttpsURLConnection) {
                    // Tell the url connection object to use our socket factory which bypasses security checks
                    final SSLSocketFactory sslSocketFactory = getSslSocketFactory();
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactory);
                } else {
                    log.warn("KO httpURLConnection instanceof HttpsURLConnection : " + httpURLConnection.getClass());
                }
            }


            httpURLConnection.setRequestMethod(projectConfiguration.getRequestMethod().name());

            log.info("  httpURLConnection.getRequestMethod()" + httpURLConnection.getRequestMethod());

            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(true);
            // httpURLConnection.setFollowRedirects(true);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; fr; rv:1.9.2.10) Gecko/20100914 Firefox/3.6.10");

            if (projectConfiguration.getContentType() != null && !"".equals(projectConfiguration.getContentType().trim())) {
                httpURLConnection.setRequestProperty("Content-Type", projectConfiguration.getContentType());
                log.warn("OVERIDE Content Type : " +  projectConfiguration.getContentType());
            } else {
                httpURLConnection.setRequestProperty("Content-Type", "application/xml; charset=utf-8");

            }

            httpURLConnection.setRequestProperty("Accept", "*/*");

            if (projectConfiguration.getSoap()!=null && projectConfiguration.getSoap()) {
                log.warn("SOAP !! : ");
                httpURLConnection.setRequestProperty("SOAPAction", "\"\"");
                httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            }


            if (projectConfiguration.getLogin() != null) {
                byte[] loginpassword = (projectConfiguration.getLogin() + ":" + projectConfiguration.getPassword2()).getBytes("iso-8859-1");
                String base64LoginPassword = Base64.encode(loginpassword).toString();
                httpURLConnection.setRequestProperty("Authorization", "Basic " + base64LoginPassword);
            } else {
                // mode stat
                httpURLConnection.setRequestProperty("Host", "www.google-analytics.com");
            }


            //log.debug("httpURLConnection.getRequestMethod():"+ httpURLConnection.getRequestMethod());
            //log.debug("httpURLConnection.getRequestProperties():"+ httpURLConnection.getRequestProperties());


            if (projectConfiguration.getPost() != null && projectConfiguration.getPost()!=null && !"".equals(projectConfiguration.getPost().trim())) {
                log.warn("POST  !! : "+ projectConfiguration.getPost());
                // Send post request
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(projectConfiguration.getPost());
                wr.flush();
                wr.close();
            }

            // get input connection
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            httpResponse.setCode(httpURLConnection.getResponseCode());
            httpResponse.setMessage(httpURLConnection.getResponseMessage());
            httpResponse.setResponse(result);
        } catch (Exception e) {

            httpResponse.setMessage(e.getMessage());
            final Writer writer = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            httpResponse.setStackTrace(writer.toString());
            httpResponse.setMessage(e.getMessage());
            log.warn("une erreur :" +e.getMessage());
        }
        return httpResponse;
    }

    private SSLSocketFactory getSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            }

            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }




}
