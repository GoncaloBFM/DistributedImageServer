package sd.tp1.client.cloud;

import sd.tp1.client.cloud.cache.HashCachedServer;
import sd.tp1.client.cloud.discovery.HeartbeatDiscovery;
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
        public Server create(URL url) throws ClientFactoryException {
            return this.wrap(new SoapServerWrapper(url));
        }
    },
    REST("42845_43178_REST", 6968){
        @Override
        public Server create(URL url) throws ClientFactoryException {
            return this.wrap(new RestServerWrapper(url));
        }
    },
    REST_SSL("42845_43178_REST_SSL", 6967){
        @Override
        public Server create(URL url) throws ClientFactoryException {
            try {
                return this.wrap(new RestSSLServerWrapper(url));
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

    abstract public Server create(URL url) throws ClientFactoryException;

    protected Server wrap(Server server) throws ClientFactoryException {
        return new HashCachedServer(server);
    }

    public static Server create(String service, URL url) throws ClientFactoryException {
        for(ClientFactory cfI : ClientFactory.values())
            if(cfI.service.equals(service))
                return cfI.create(url);

        throw new InvalidServiceException();
    }

    static class ClientFactoryException extends Exception{
        ClientFactoryException(String message){
            super(message);
        }
        ClientFactoryException(Throwable e){
            super(e);
        }
    }

    static class InvalidServiceException extends ClientFactoryException {
        InvalidServiceException() {
            super("Invalid service exception");
        }
    }
}
