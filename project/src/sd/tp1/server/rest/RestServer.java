package sd.tp1.server.rest;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.common.rest_envelops.DeletePictureEnvelop;
import sd.tp1.common.rest_envelops.UploadPictureEnvelop;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.NotDirectoryException;
import java.util.List;
import java.util.logging.Logger;

@Path("/{serverName}")
public class RestServer {

    private static final Logger logger = Logger.getLogger(RestServer.class.getSimpleName());


    private DataManager dataManager;

    public RestServer(@PathParam("serverName") String serverName) throws NotDirectoryException {
        this.dataManager = new FileDataManager();
    }

    public RestServer(@PathParam("serverName") String serverName, DataManager dataManager ) {
        this.dataManager = dataManager;
    }

    private Response accessControllAllowOrigin(Response response){
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        return response;
    }

    @POST
    @Path("/createAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlbum(SharedAlbum sharedAlbum) {
        logger.info("createAlbum" + "(album=" + sharedAlbum.getName() + ")");

        return this.dataManager.createAlbum(sharedAlbum) ?
            Response.ok().build() :
                Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/getListOfAlbums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums() {
        logger.info("getListOfAlbums");
        List<SharedAlbum> listOfAlbums = this.dataManager.loadListOfAlbums();
        if(listOfAlbums == null)
            return Response.serverError().build();

        SharedAlbum[] array = listOfAlbums.toArray(new SharedAlbum[listOfAlbums.size()]);
        return Response.ok(array).build();
    }

    @GET
    @Path("/getListOfPictures/{album}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfPictures(@PathParam("album") String album) {
        logger.info("getListOfPictures" + "(album=" + album +")");
        List<SharedPicture> listOfPictures = this.dataManager.loadListOfPictures(album);
        if (listOfPictures != null) {
            SharedPicture[] array = listOfPictures.toArray(new SharedPicture[listOfPictures.size()]);
            return Response.ok(array).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/deleteAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAlbum(SharedAlbum album) {
        logger.info("deleteAlbum" + "(album=" + album+")");

        return this.dataManager.deleteAlbum(album) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/deletePicture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePicture(DeletePictureEnvelop env) {
        logger.info("deletePicture" + "(album=" + env.album+", picture=" + env.picture+")");

        return this.dataManager.deletePicture(env.album, env.picture) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/getPictureData/{album}/{picture}")
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture) {
        logger.info("getPictureData"+"(album=" + album+", picture=" + picture+")");
        byte[] bytes = dataManager.loadPictureData(album, picture);
        return Response.ok(bytes).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/uploadPicture")
    public Response uploadPicture(UploadPictureEnvelop env) {
        logger.info("uploadPicture" + "(album=" + env.album+", picture=" + env.picture+")");

        return dataManager.uploadPicture(env.album, env.picture, env.data) ?
            Response.ok().build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/getServerId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerId(){

        String serverId = dataManager.getServerId();

        return serverId != null ?
                Response.ok(serverId).build()
                : Response.serverError().build();
    }
}
