
package sd.tp1.server.replication.backdoor.stubs;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SOAPReplicationServerBackdoor", targetNamespace = "http://backdoor.replication.server.tp1.sd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SOAPReplicationServerBackdoor {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns byte[]
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPictureData", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.GetPictureData")
    @ResponseWrapper(localName = "getPictureDataResponse", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.GetPictureDataResponse")
    @Action(input = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/getPictureDataRequest", output = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/getPictureDataResponse")
    public byte[] getPictureData(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        SharedPicture arg1);

    /**
     * 
     * @return
     *     returns sd.tp1.server.replication.backdoor.stubs.ServerMetadata
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServerMetadata", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.GetServerMetadata")
    @ResponseWrapper(localName = "getServerMetadataResponse", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.GetServerMetadataResponse")
    @Action(input = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/getServerMetadataRequest", output = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/getServerMetadataResponse")
    public ServerMetadata getServerMetadata();

    /**
     * 
     * @param arg1
     * @param arg0
     */
    @WebMethod
    @RequestWrapper(localName = "sendMetadata", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.SendMetadata")
    @ResponseWrapper(localName = "sendMetadataResponse", targetNamespace = "http://backdoor.replication.server.tp1.sd/", className = "sd.tp1.server.replication.backdoor.stubs.SendMetadataResponse")
    @Action(input = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/sendMetadataRequest", output = "http://backdoor.replication.server.tp1.sd/SOAPReplicationServerBackdoor/sendMetadataResponse")
    public void sendMetadata(
        @WebParam(name = "arg0", targetNamespace = "")
        ServerMetadata arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        List<Metadata> arg1);

}
