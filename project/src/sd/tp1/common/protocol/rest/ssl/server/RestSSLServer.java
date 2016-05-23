package sd.tp1.common.protocol.rest.ssl.server;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.common.data.DataManager;
import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.data.SharedPicture;
import sd.tp1.common.protocol.rest.envelops.DeletePictureEnvelop;
import sd.tp1.common.protocol.rest.envelops.UploadPictureEnvelop;
import sd.tp1.common.protocol.rest.server.RestServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by everyone on 5/17/16.
 */
public class RestSSLServer extends RestServer {

    public static final String SERVER_TYPE = "REST_SSL";
    public static final String PASSWORD = "p@ssword66";

    private static final Logger logger = Logger.getLogger(RestSSLServer.class.getName());

    private final File keystore;
    private final char[] jksPassword;
    private final char[] keyPassword;

    private SSLContext sslContext;

    public RestSSLServer(String serverPath, URI uri, DataManager dataManager, File keystore, char[] jksPassword, char[] keyPassword) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        super(serverPath, uri, dataManager);

        this.keystore = keystore;
        this.jksPassword = jksPassword;
        this.keyPassword = keyPassword;
        this.setupSSLContext();
    }

    private void setupSSLContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        this.sslContext = SSLContext.getInstance("TLSv1");
        KeyStore keyStore = KeyStore.getInstance("JKS");

        InputStream is = new FileInputStream(keystore);
        keyStore.load(is, jksPassword);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyPassword);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
    }

    @Override
    protected HttpServer startScript(){
        try{
            ResourceConfig config = new ResourceConfig();
            config.register(this);
            return JdkHttpServerFactory.createHttpServer(uri, config, sslContext);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getType() {
        return SERVER_TYPE;
    }
}
