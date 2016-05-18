package sd.tp1.common;

import sd.tp1.common.discovery.HeartbeatDiscovery;
import sd.tp1.common.discovery.ServiceHandler;
import sd.tp1.common.protocol.Endpoint;
import sd.tp1.common.protocol.rest.client.RestClient;
import sd.tp1.common.protocol.rest.ssl.client.RestSSLClient;
import sd.tp1.common.protocol.soap.client.SoapClient;

import java.net.MalformedURLException;
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
        public Endpoint create(URL url) throws ClientFactoryException {
            return new SoapClient(url);
        }
    },
    REST("42845_43178_REST", 6968){
        @Override
        public Endpoint create(URL url) throws ClientFactoryException {
            return new RestClient(url);
        }
    },
    REST_SSL("42845_43178_REST_SSL", 6967){
        @Override
        public Endpoint create(URL url) throws ClientFactoryException {
            try {
                return new RestSSLClient(url);
            } catch (MalformedURLException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
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

    abstract public Endpoint create(URL url) throws ClientFactoryException;


    public static Endpoint create(String service, URL url) throws ClientFactoryException {
        for(ClientFactory cfI : ClientFactory.values())
            if(cfI.service.equals(service))
                return cfI.create(url);

        throw new InvalidServiceException();
    }

    public static class ClientFactoryException extends Exception{
        ClientFactoryException(String message){
            super(message);
        }
        ClientFactoryException(Throwable e){
            super(e);
        }
    }

    public static class InvalidServiceException extends ClientFactoryException {
        InvalidServiceException() {
            super("Invalid service exception");
        }
    }
}
