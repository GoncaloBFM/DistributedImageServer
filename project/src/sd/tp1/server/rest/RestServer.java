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
    @Path("/createAlbum/{albumName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlbum(@PathParam("albumName") String albumName) {
        logger.info("createAlbum" + "(album=" + albumName + ")");
        SharedAlbum album = this.dataManager.createAlbum(albumName);
        if (album != null) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
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
        List<SharedPicture> listOfPictures = this.dataManager.loadListOfPictures(new SharedAlbum(album));
        if (listOfPictures != null) {
            SharedPicture[] array = listOfPictures.toArray(new SharedPicture[listOfPictures.size()]);
            return Response.ok(array).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/deleteAlbum/{album}")
    public Response deleteAlbum(@PathParam("album") String album) {
        logger.info("deleteAlbum" + "(album=" + album+")");
        this.dataManager.deleteAlbum(new SharedAlbum(album));
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletePicture/{album}/{picture}")
    public Response deletePicture(@PathParam("album") String album, @PathParam("picture") String picture) {
        logger.info("deletePicture" + "(album=" + album+", picture=" + picture+")");
        if (this.dataManager.deletePicture(new SharedAlbum(album), new SharedPicture(picture))) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/getPictureData/{album}/{picture}")
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture) {
        logger.info("getPictureData"+"(album=" + album+", picture=" + picture+")");
        byte[] bytes = dataManager.loadPictureData(new SharedAlbum(album), new SharedPicture(picture));
        return Response.ok(bytes).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/uploadPicture/{album}/{picture}")
    public Response uploadPicture(@PathParam("album") String album,@PathParam("picture") String picture, byte[] data) {
        logger.info("uploadPicture" + "(album=" + album+", picture=" + picture+")");
        SharedPicture newPicture = dataManager.uploadPicture(new SharedAlbum(album), picture, data);
        if(newPicture != null) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/search/{val}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("val") String val){
        List<SharedAlbum> albumList = dataManager.loadListOfAlbums();
        List<String> pictureList = new LinkedList<>();
        for(SharedAlbum album : albumList) {
            List<SharedPicture> list = dataManager.loadListOfPictures(album);
            if(list == null)
                continue;

            pictureList.addAll(list.stream()
                    .filter(picture -> picture.getPictureName().contains(val))
                    .map( p -> String.format("/getPictureData/%s/%s", album.getName(), p.getPictureName()))
                    .collect(Collectors.toList()));
        }

        return this.accessControllAllowOrigin(
                Response.ok(pictureList.toArray(new String[pictureList.size()])).build());
    }
}
