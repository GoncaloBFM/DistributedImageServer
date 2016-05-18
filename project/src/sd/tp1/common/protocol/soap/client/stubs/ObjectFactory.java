
package sd.tp1.common.protocol.soap.client.stubs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sd.tp1.common.protocol.soap.client.stubs package. 
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

    private final static QName _StopResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "stopResponse");
    private final static QName _Start_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "start");
    private final static QName _CreateAlbumResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "createAlbumResponse");
    private final static QName _GetTypeResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getTypeResponse");
    private final static QName _LoadListOfAlbums_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadListOfAlbums");
    private final static QName _LoadPictureDataResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadPictureDataResponse");
    private final static QName _DeleteAlbumResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "deleteAlbumResponse");
    private final static QName _GetMetadata_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getMetadata");
    private final static QName _UploadPicture_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "uploadPicture");
    private final static QName _LoadListOfAlbumsResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadListOfAlbumsResponse");
    private final static QName _GetMetadataResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getMetadataResponse");
    private final static QName _LoadListOfPicturesResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadListOfPicturesResponse");
    private final static QName _CreateAlbum_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "createAlbum");
    private final static QName _GetServerId_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getServerId");
    private final static QName _IsRunningResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "isRunningResponse");
    private final static QName _DeletePictureResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "deletePictureResponse");
    private final static QName _DeleteAlbum_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "deleteAlbum");
    private final static QName _GetUrl_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getUrl");
    private final static QName _UploadPictureResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "uploadPictureResponse");
    private final static QName _GetType_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getType");
    private final static QName _Stop_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "stop");
    private final static QName _StartResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "startResponse");
    private final static QName _DeletePicture_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "deletePicture");
    private final static QName _LoadPictureData_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadPictureData");
    private final static QName _LoadListOfPictures_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "loadListOfPictures");
    private final static QName _GetUrlResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getUrlResponse");
    private final static QName _GetServerIdResponse_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "getServerIdResponse");
    private final static QName _IsRunning_QNAME = new QName("http://server.soap.protocol.common.tp1.sd/", "isRunning");
    private final static QName _LoadPictureDataResponseReturn_QNAME = new QName("", "return");
    private final static QName _UploadPictureArg2_QNAME = new QName("", "arg2");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sd.tp1.common.protocol.soap.client.stubs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoadListOfPictures }
     * 
     */
    public LoadListOfPictures createLoadListOfPictures() {
        return new LoadListOfPictures();
    }

    /**
     * Create an instance of {@link DeletePicture }
     * 
     */
    public DeletePicture createDeletePicture() {
        return new DeletePicture();
    }

    /**
     * Create an instance of {@link StartResponse }
     * 
     */
    public StartResponse createStartResponse() {
        return new StartResponse();
    }

    /**
     * Create an instance of {@link LoadPictureData }
     * 
     */
    public LoadPictureData createLoadPictureData() {
        return new LoadPictureData();
    }

    /**
     * Create an instance of {@link GetServerIdResponse }
     * 
     */
    public GetServerIdResponse createGetServerIdResponse() {
        return new GetServerIdResponse();
    }

    /**
     * Create an instance of {@link IsRunning }
     * 
     */
    public IsRunning createIsRunning() {
        return new IsRunning();
    }

    /**
     * Create an instance of {@link GetUrlResponse }
     * 
     */
    public GetUrlResponse createGetUrlResponse() {
        return new GetUrlResponse();
    }

    /**
     * Create an instance of {@link DeleteAlbumResponse }
     * 
     */
    public DeleteAlbumResponse createDeleteAlbumResponse() {
        return new DeleteAlbumResponse();
    }

    /**
     * Create an instance of {@link GetMetadata }
     * 
     */
    public GetMetadata createGetMetadata() {
        return new GetMetadata();
    }

    /**
     * Create an instance of {@link LoadListOfAlbumsResponse }
     * 
     */
    public LoadListOfAlbumsResponse createLoadListOfAlbumsResponse() {
        return new LoadListOfAlbumsResponse();
    }

    /**
     * Create an instance of {@link UploadPicture }
     * 
     */
    public UploadPicture createUploadPicture() {
        return new UploadPicture();
    }

    /**
     * Create an instance of {@link Start }
     * 
     */
    public Start createStart() {
        return new Start();
    }

    /**
     * Create an instance of {@link StopResponse }
     * 
     */
    public StopResponse createStopResponse() {
        return new StopResponse();
    }

    /**
     * Create an instance of {@link CreateAlbumResponse }
     * 
     */
    public CreateAlbumResponse createCreateAlbumResponse() {
        return new CreateAlbumResponse();
    }

    /**
     * Create an instance of {@link GetTypeResponse }
     * 
     */
    public GetTypeResponse createGetTypeResponse() {
        return new GetTypeResponse();
    }

    /**
     * Create an instance of {@link LoadListOfAlbums }
     * 
     */
    public LoadListOfAlbums createLoadListOfAlbums() {
        return new LoadListOfAlbums();
    }

    /**
     * Create an instance of {@link LoadPictureDataResponse }
     * 
     */
    public LoadPictureDataResponse createLoadPictureDataResponse() {
        return new LoadPictureDataResponse();
    }

    /**
     * Create an instance of {@link UploadPictureResponse }
     * 
     */
    public UploadPictureResponse createUploadPictureResponse() {
        return new UploadPictureResponse();
    }

    /**
     * Create an instance of {@link GetUrl }
     * 
     */
    public GetUrl createGetUrl() {
        return new GetUrl();
    }

    /**
     * Create an instance of {@link DeleteAlbum }
     * 
     */
    public DeleteAlbum createDeleteAlbum() {
        return new DeleteAlbum();
    }

    /**
     * Create an instance of {@link Stop }
     * 
     */
    public Stop createStop() {
        return new Stop();
    }

    /**
     * Create an instance of {@link GetType }
     * 
     */
    public GetType createGetType() {
        return new GetType();
    }

    /**
     * Create an instance of {@link DeletePictureResponse }
     * 
     */
    public DeletePictureResponse createDeletePictureResponse() {
        return new DeletePictureResponse();
    }

    /**
     * Create an instance of {@link CreateAlbum }
     * 
     */
    public CreateAlbum createCreateAlbum() {
        return new CreateAlbum();
    }

    /**
     * Create an instance of {@link GetServerId }
     * 
     */
    public GetServerId createGetServerId() {
        return new GetServerId();
    }

    /**
     * Create an instance of {@link IsRunningResponse }
     * 
     */
    public IsRunningResponse createIsRunningResponse() {
        return new IsRunningResponse();
    }

    /**
     * Create an instance of {@link GetMetadataResponse }
     * 
     */
    public GetMetadataResponse createGetMetadataResponse() {
        return new GetMetadataResponse();
    }

    /**
     * Create an instance of {@link LoadListOfPicturesResponse }
     * 
     */
    public LoadListOfPicturesResponse createLoadListOfPicturesResponse() {
        return new LoadListOfPicturesResponse();
    }

    /**
     * Create an instance of {@link SharedAlbumPicture }
     * 
     */
    public SharedAlbumPicture createSharedAlbumPicture() {
        return new SharedAlbumPicture();
    }

    /**
     * Create an instance of {@link LogicClockMetadata }
     * 
     */
    public LogicClockMetadata createLogicClockMetadata() {
        return new LogicClockMetadata();
    }

    /**
     * Create an instance of {@link SharedAlbum }
     * 
     */
    public SharedAlbum createSharedAlbum() {
        return new SharedAlbum();
    }

    /**
     * Create an instance of {@link SharedPicture }
     * 
     */
    public SharedPicture createSharedPicture() {
        return new SharedPicture();
    }

    /**
     * Create an instance of {@link MetadataBundle }
     * 
     */
    public MetadataBundle createMetadataBundle() {
        return new MetadataBundle();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "stopResponse")
    public JAXBElement<StopResponse> createStopResponse(StopResponse value) {
        return new JAXBElement<StopResponse>(_StopResponse_QNAME, StopResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Start }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "start")
    public JAXBElement<Start> createStart(Start value) {
        return new JAXBElement<Start>(_Start_QNAME, Start.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "createAlbumResponse")
    public JAXBElement<CreateAlbumResponse> createCreateAlbumResponse(CreateAlbumResponse value) {
        return new JAXBElement<CreateAlbumResponse>(_CreateAlbumResponse_QNAME, CreateAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getTypeResponse")
    public JAXBElement<GetTypeResponse> createGetTypeResponse(GetTypeResponse value) {
        return new JAXBElement<GetTypeResponse>(_GetTypeResponse_QNAME, GetTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadListOfAlbums }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadListOfAlbums")
    public JAXBElement<LoadListOfAlbums> createLoadListOfAlbums(LoadListOfAlbums value) {
        return new JAXBElement<LoadListOfAlbums>(_LoadListOfAlbums_QNAME, LoadListOfAlbums.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadPictureDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadPictureDataResponse")
    public JAXBElement<LoadPictureDataResponse> createLoadPictureDataResponse(LoadPictureDataResponse value) {
        return new JAXBElement<LoadPictureDataResponse>(_LoadPictureDataResponse_QNAME, LoadPictureDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "deleteAlbumResponse")
    public JAXBElement<DeleteAlbumResponse> createDeleteAlbumResponse(DeleteAlbumResponse value) {
        return new JAXBElement<DeleteAlbumResponse>(_DeleteAlbumResponse_QNAME, DeleteAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getMetadata")
    public JAXBElement<GetMetadata> createGetMetadata(GetMetadata value) {
        return new JAXBElement<GetMetadata>(_GetMetadata_QNAME, GetMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadPicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "uploadPicture")
    public JAXBElement<UploadPicture> createUploadPicture(UploadPicture value) {
        return new JAXBElement<UploadPicture>(_UploadPicture_QNAME, UploadPicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadListOfAlbumsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadListOfAlbumsResponse")
    public JAXBElement<LoadListOfAlbumsResponse> createLoadListOfAlbumsResponse(LoadListOfAlbumsResponse value) {
        return new JAXBElement<LoadListOfAlbumsResponse>(_LoadListOfAlbumsResponse_QNAME, LoadListOfAlbumsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getMetadataResponse")
    public JAXBElement<GetMetadataResponse> createGetMetadataResponse(GetMetadataResponse value) {
        return new JAXBElement<GetMetadataResponse>(_GetMetadataResponse_QNAME, GetMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadListOfPicturesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadListOfPicturesResponse")
    public JAXBElement<LoadListOfPicturesResponse> createLoadListOfPicturesResponse(LoadListOfPicturesResponse value) {
        return new JAXBElement<LoadListOfPicturesResponse>(_LoadListOfPicturesResponse_QNAME, LoadListOfPicturesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "createAlbum")
    public JAXBElement<CreateAlbum> createCreateAlbum(CreateAlbum value) {
        return new JAXBElement<CreateAlbum>(_CreateAlbum_QNAME, CreateAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getServerId")
    public JAXBElement<GetServerId> createGetServerId(GetServerId value) {
        return new JAXBElement<GetServerId>(_GetServerId_QNAME, GetServerId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsRunningResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "isRunningResponse")
    public JAXBElement<IsRunningResponse> createIsRunningResponse(IsRunningResponse value) {
        return new JAXBElement<IsRunningResponse>(_IsRunningResponse_QNAME, IsRunningResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "deletePictureResponse")
    public JAXBElement<DeletePictureResponse> createDeletePictureResponse(DeletePictureResponse value) {
        return new JAXBElement<DeletePictureResponse>(_DeletePictureResponse_QNAME, DeletePictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "deleteAlbum")
    public JAXBElement<DeleteAlbum> createDeleteAlbum(DeleteAlbum value) {
        return new JAXBElement<DeleteAlbum>(_DeleteAlbum_QNAME, DeleteAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUrl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getUrl")
    public JAXBElement<GetUrl> createGetUrl(GetUrl value) {
        return new JAXBElement<GetUrl>(_GetUrl_QNAME, GetUrl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadPictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "uploadPictureResponse")
    public JAXBElement<UploadPictureResponse> createUploadPictureResponse(UploadPictureResponse value) {
        return new JAXBElement<UploadPictureResponse>(_UploadPictureResponse_QNAME, UploadPictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getType")
    public JAXBElement<GetType> createGetType(GetType value) {
        return new JAXBElement<GetType>(_GetType_QNAME, GetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Stop }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "stop")
    public JAXBElement<Stop> createStop(Stop value) {
        return new JAXBElement<Stop>(_Stop_QNAME, Stop.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "startResponse")
    public JAXBElement<StartResponse> createStartResponse(StartResponse value) {
        return new JAXBElement<StartResponse>(_StartResponse_QNAME, StartResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "deletePicture")
    public JAXBElement<DeletePicture> createDeletePicture(DeletePicture value) {
        return new JAXBElement<DeletePicture>(_DeletePicture_QNAME, DeletePicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadPictureData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadPictureData")
    public JAXBElement<LoadPictureData> createLoadPictureData(LoadPictureData value) {
        return new JAXBElement<LoadPictureData>(_LoadPictureData_QNAME, LoadPictureData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadListOfPictures }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "loadListOfPictures")
    public JAXBElement<LoadListOfPictures> createLoadListOfPictures(LoadListOfPictures value) {
        return new JAXBElement<LoadListOfPictures>(_LoadListOfPictures_QNAME, LoadListOfPictures.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUrlResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getUrlResponse")
    public JAXBElement<GetUrlResponse> createGetUrlResponse(GetUrlResponse value) {
        return new JAXBElement<GetUrlResponse>(_GetUrlResponse_QNAME, GetUrlResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServerIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "getServerIdResponse")
    public JAXBElement<GetServerIdResponse> createGetServerIdResponse(GetServerIdResponse value) {
        return new JAXBElement<GetServerIdResponse>(_GetServerIdResponse_QNAME, GetServerIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsRunning }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.soap.protocol.common.tp1.sd/", name = "isRunning")
    public JAXBElement<IsRunning> createIsRunning(IsRunning value) {
        return new JAXBElement<IsRunning>(_IsRunning_QNAME, IsRunning.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = LoadPictureDataResponse.class)
    public JAXBElement<byte[]> createLoadPictureDataResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_LoadPictureDataResponseReturn_QNAME, byte[].class, LoadPictureDataResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = UploadPicture.class)
    public JAXBElement<byte[]> createUploadPictureArg2(byte[] value) {
        return new JAXBElement<byte[]>(_UploadPictureArg2_QNAME, byte[].class, UploadPicture.class, ((byte[]) value));
    }

}
