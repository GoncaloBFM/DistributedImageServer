package sd.tp1.server.rest;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/{serverName}")
public class RestServer {

    private static final Logger logger = Logger.getLogger(RestServer.class.getSimpleName());


    private String root;
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
        this.dataManager.createAlbum(sharedAlbum);
        return Response.ok().build();
    }

    @GET
    @Path("/getListOfAlbums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums() {
        logger.info("getListOfAlbums");
        List<SharedAlbum> listOfAlbums = this.dataManager.loadListOfAlbums();
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
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/deleteAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAlbum(SharedAlbum album) {
        logger.info("deleteAlbum" + "(album=" + album+")");
        this.dataManager.deleteAlbum(album);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletePicture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePicture(SharedAlbum album, SharedPicture picture) {
        logger.info("deletePicture" + "(album=" + album+", picture=" + picture+")");
        if (this.dataManager.deletePicture(album, picture)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
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
    public Response uploadPicture(SharedAlbum album, SharedPicture picture, byte[] data) {
        logger.info("uploadPicture" + "(album=" + album+", picture=" + picture+")");
        dataManager.uploadPicture(album, picture, data);
        return Response.ok().build();
    }
}
