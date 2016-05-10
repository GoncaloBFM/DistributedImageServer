package sd.tp1.client.cloud;

import sd.tp1.client.cloud.cache.HashCachedServer;
import sd.tp1.client.cloud.discovery.HeartbeatDiscovery;
import sd.tp1.client.cloud.discovery.ServiceDiscovery;
import sd.tp1.client.cloud.discovery.ServiceHandler;
import sd.tp1.client.cloud.rest.RestServerWrapper;
import sd.tp1.client.cloud.rest.ssl.RestSSLServerWrapper;
import sd.tp1.client.cloud.soap.SoapServerWrapper;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by apontes on 5/10/16.
 */
public enum ClientFactory {
    SOAP("42845_43178_SOAP", 6969){
        @Override
        protected Server createInstance(URL url){
            return new SoapServerWrapper(url);
        }
    },
    REST("42845_43178_REST", 6968){
        @Override
        protected Server createInstance(URL url){
            return new RestServerWrapper(url);
        }
    },
    REST_SSL("42845_43178_REST_SSL", 6967){
        @Override
        protected Server createInstance(URL url) throws ClientFactoryException {
            try {
                return new RestSSLServerWrapper(url);
            } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
                throw new ClientFactoryException(e);
            }
        }
    };

    private String service;
    private int port;

    ClientFactory(String service, int port) {
        this.service = service;
        this.port = port;
    }

    public static void startDiscovery(ServiceHandler handler){
        for(ClientFactory cfI : ClientFactory.values())
            new HeartbeatDiscovery(cfI.service, cfI.port).discoverService(handler);
    }

    abstract protected Server createInstance(URL url) throws ClientFactoryException;

    public Server create(URL url) throws ClientFactoryException {
        return new HashCachedServer(this.createInstance(url));
    }

    static class ClientFactoryException extends Exception{
        ClientFactoryException(String message){
            super(message);
        }

        ClientFactoryException(Throwable e){
            super(e);
        }
    }
}
