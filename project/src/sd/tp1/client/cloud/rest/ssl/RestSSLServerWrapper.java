package sd.tp1.client.cloud.rest.ssl;

import sd.tp1.client.cloud.rest.RestServerWrapper;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Created by gbfm on 4/4/16.
 */
public class RestSSLServerWrapper extends RestServerWrapper {

    public RestSSLServerWrapper(URL url) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        super(RestSSLServerWrapper.class.getSimpleName(), url, getTarget(url));
    }

    static public WebTarget getTarget(URL url) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        SSLContext sc = SSLContext.getInstance("TLSv1");

        TrustManager[] trustAllCerts = { new InsecureTrustManager() };
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        Client client = ClientBuilder.newBuilder()
                .hostnameVerifier(new InsecureHostnameVerifier())
                .sslContext(sc)
                .build();

        return client.target(url.toURI());
    }

    static public class InsecureHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    static public class InsecureTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            Arrays.asList( chain ).forEach( i -> {
                System.err.println( "type: " + i.getType() + "from: " + i.getNotBefore() + " to: " + i.getNotAfter() );
            });
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}

