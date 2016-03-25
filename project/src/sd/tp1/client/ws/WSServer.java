
package sd.tp1.client.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "WSServer", targetNamespace = "http://ws.server.tp1.sd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WSServer {


    /**
     * 
     * @return
     *     returns java.util.List<sd.tp1.client.ws.SharedAlbum>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getListOfAlbums", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetListOfAlbums")
    @ResponseWrapper(localName = "getListOfAlbumsResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetListOfAlbumsResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/getListOfAlbumsRequest", output = "http://ws.server.tp1.sd/WSServer/getListOfAlbumsResponse")
    public List<SharedAlbum> getListOfAlbums();

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<sd.tp1.client.ws.SharedPicture>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getListOfPictures", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetListOfPictures")
    @ResponseWrapper(localName = "getListOfPicturesResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetListOfPicturesResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/getListOfPicturesRequest", output = "http://ws.server.tp1.sd/WSServer/getListOfPicturesResponse")
    public List<SharedPicture> getListOfPictures(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns byte[]
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPictureData", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetPictureData")
    @ResponseWrapper(localName = "getPictureDataResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.GetPictureDataResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/getPictureDataRequest", output = "http://ws.server.tp1.sd/WSServer/getPictureDataResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://ws.server.tp1.sd/WSServer/getPictureData/Fault/IOException")
    })
    public byte[] getPictureData(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        SharedPicture arg1)
        throws IOException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns sd.tp1.client.ws.SharedAlbum
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createAlbum", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.CreateAlbum")
    @ResponseWrapper(localName = "createAlbumResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.CreateAlbumResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/createAlbumRequest", output = "http://ws.server.tp1.sd/WSServer/createAlbumResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://ws.server.tp1.sd/WSServer/createAlbum/Fault/IOException")
    })
    public SharedAlbum createAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws IOException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns sd.tp1.client.ws.SharedPicture
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "uploadPicture", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.UploadPicture")
    @ResponseWrapper(localName = "uploadPictureResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.UploadPictureResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/uploadPictureRequest", output = "http://ws.server.tp1.sd/WSServer/uploadPictureResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://ws.server.tp1.sd/WSServer/uploadPicture/Fault/IOException")
    })
    public SharedPicture uploadPicture(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        byte[] arg2)
        throws IOException_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws IOException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "deleteAlbum", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.DeleteAlbum")
    @ResponseWrapper(localName = "deleteAlbumResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.DeleteAlbumResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/deleteAlbumRequest", output = "http://ws.server.tp1.sd/WSServer/deleteAlbumResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://ws.server.tp1.sd/WSServer/deleteAlbum/Fault/IOException")
    })
    public void deleteAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0)
        throws IOException_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deletePicture", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.DeletePicture")
    @ResponseWrapper(localName = "deletePictureResponse", targetNamespace = "http://ws.server.tp1.sd/", className = "sd.tp1.client.ws.DeletePictureResponse")
    @Action(input = "http://ws.server.tp1.sd/WSServer/deletePictureRequest", output = "http://ws.server.tp1.sd/WSServer/deletePictureResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://ws.server.tp1.sd/WSServer/deletePicture/Fault/IOException")
    })
    public boolean deletePicture(
        @WebParam(name = "arg0", targetNamespace = "")
        SharedAlbum arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        SharedPicture arg1)
        throws IOException_Exception
    ;

}
