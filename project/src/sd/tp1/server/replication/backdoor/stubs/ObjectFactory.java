
package sd.tp1.server.replication.backdoor.stubs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sd.tp1.server.replication.backdoor.stubs package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendMetadata_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "sendMetadata");
    private final static QName _GetServerMetadataResponse_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "getServerMetadataResponse");
    private final static QName _GetPictureData_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "getPictureData");
    private final static QName _GetPictureDataResponse_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "getPictureDataResponse");
    private final static QName _SendMetadataResponse_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "sendMetadataResponse");
    private final static QName _GetServerMetadata_QNAME = new QName("http://backdoor.replication.server.tp1.sd/", "getServerMetadata");
    private final static QName _GetPictureDataResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sd.tp1.server.replication.backdoor.stubs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetServerMetadata }
     * 
     */
    public GetServerMetadata createGetServerMetadata() {
        return new GetServerMetadata();
    }

    /**
     * Create an instance of {@link GetPictureData }
     * 
     */
    public GetPictureData createGetPictureData() {
        return new GetPictureData();
    }

    /**
     * Create an instance of {@link GetPictureDataResponse }
     * 
     */
    public GetPictureDataResponse createGetPictureDataResponse() {
        return new GetPictureDataResponse();
    }

    /**
     * Create an instance of {@link SendMetadataResponse }
     * 
     */
    public SendMetadataResponse createSendMetadataResponse() {
        return new SendMetadataResponse();
    }

    /**
     * Create an instance of {@link GetServerMetadataResponse }
     * 
     */
    public GetServerMetadataResponse createGetServerMetadataResponse() {
        return new GetServerMetadataResponse();
    }

    /**
     * Create an instance of {@link SendMetadata }
     * 
     */
    public SendMetadata createSendMetadata() {
        return new SendMetadata();
    }

    /**
     * Create an instance of {@link Metadata }
     * 
     */
    public Metadata createMetadata() {
        return new Metadata();
    }

    /**
     * Create an instance of {@link ServerMetadata }
     * 
     */
    public ServerMetadata createServerMetadata() {
        return new ServerMetadata();
    }

    /**
     * Create an instance of {@link SharedPicture }
     * 
     */
    public SharedPicture createSharedPicture() {
        return new SharedPicture();
    }

    /**
     * Create an instance of {@link LamportLogicClock }
     * 
     */
    public LamportLogicClock createLamportLogicClock() {
        return new LamportLogicClock();
    }

    /**
     * Create an instance of {@link SharedAlbum }
     * 
     */
    public SharedAlbum createSharedAlbum() {
        return new SharedAlbum();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "sendMetadata")
    public JAXBElement<SendMetadata> createSendMetadata(SendMetadata value) {
        return new JAXBElement<SendMetadata>(_SendMetadata_QNAME, SendMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "getServerMetadataResponse")
    public JAXBElement<GetServerMetadataResponse> createGetServerMetadataResponse(GetServerMetadataResponse value) {
        return new JAXBElement<GetServerMetadataResponse>(_GetServerMetadataResponse_QNAME, GetServerMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictureData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "getPictureData")
    public JAXBElement<GetPictureData> createGetPictureData(GetPictureData value) {
        return new JAXBElement<GetPictureData>(_GetPictureData_QNAME, GetPictureData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictureDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "getPictureDataResponse")
    public JAXBElement<GetPictureDataResponse> createGetPictureDataResponse(GetPictureDataResponse value) {
        return new JAXBElement<GetPictureDataResponse>(_GetPictureDataResponse_QNAME, GetPictureDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "sendMetadataResponse")
    public JAXBElement<SendMetadataResponse> createSendMetadataResponse(SendMetadataResponse value) {
        return new JAXBElement<SendMetadataResponse>(_SendMetadataResponse_QNAME, SendMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://backdoor.replication.server.tp1.sd/", name = "getServerMetadata")
    public JAXBElement<GetServerMetadata> createGetServerMetadata(GetServerMetadata value) {
        return new JAXBElement<GetServerMetadata>(_GetServerMetadata_QNAME, GetServerMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetPictureDataResponse.class)
    public JAXBElement<byte[]> createGetPictureDataResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetPictureDataResponseReturn_QNAME, byte[].class, GetPictureDataResponse.class, ((byte[]) value));
    }

}
