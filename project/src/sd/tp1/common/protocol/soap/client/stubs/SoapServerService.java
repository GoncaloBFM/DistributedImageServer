
package sd.tp1.common.protocol.soap.client.stubs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SoapServerService", targetNamespace = "http://server.soap.protocol.common.tp1.sd/", wsdlLocation = "http://localhost:8080/FileServer?wsdl")
public class SoapServerService
    extends Service
{

    private final static URL SOAPSERVERSERVICE_WSDL_LOCATION;
    private final static WebServiceException SOAPSERVERSERVICE_EXCEPTION;
    private final static QName SOAPSERVERSERVICE_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "SoapServerService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/FileServer?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SOAPSERVERSERVICE_WSDL_LOCATION = url;
        SOAPSERVERSERVICE_EXCEPTION = e;
    }

    public SoapServerService() {
        super(__getWsdlLocation(), SOAPSERVERSERVICE_QNAME);
    }

    public SoapServerService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SOAPSERVERSERVICE_QNAME, features);
    }

    public SoapServerService(URL wsdlLocation) {
        super(wsdlLocation, SOAPSERVERSERVICE_QNAME);
    }

    public SoapServerService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SOAPSERVERSERVICE_QNAME, features);
    }

    public SoapServerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SoapServerService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SoapServer
     */
    @WebEndpoint(name = "SoapServerPort")
    public SoapServer getSoapServerPort() {
        return super.getPort(new QName("http://server.soap.protocol.common.tp1.sd/", "SoapServerPort"), SoapServer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SoapServer
     */
    @WebEndpoint(name = "SoapServerPort")
    public SoapServer getSoapServerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://server.soap.protocol.common.tp1.sd/", "SoapServerPort"), SoapServer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SOAPSERVERSERVICE_EXCEPTION!= null) {
            throw SOAPSERVERSERVICE_EXCEPTION;
        }
        return SOAPSERVERSERVICE_WSDL_LOCATION;
    }

}
