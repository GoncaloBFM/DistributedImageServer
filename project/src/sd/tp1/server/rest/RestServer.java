package sd.tp1.server.rest;

import sd.tp1.Picture;
import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;
import sd.tp1.server.HeartbeatAnnouncer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

@Path("/{serverName}")
public class RestServer {

    private String root;
    private DataManager dataManager;

    public RestServer(@PathParam("album") String serverName) throws NotDirectoryException {
        this.dataManager = new FileDataManager();
    }

    public RestServer(@PathParam("album") String serverName, File root) throws NotDirectoryException {
        this.dataManager = new FileDataManager(root);
        this.root = root.toString();
    }

    @POST
    @Path("/createAlbum/{albumName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlbum(@PathParam("albumName") String albumName) {
        SharedAlbum album = this.dataManager.createAlbum(albumName);
        if (album != null) {
            return Response.ok(album).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/getListOfAlbums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums() {
        List<SharedAlbum> listOfAlbums = this.dataManager.loadListOfAlbums();
        SharedAlbum[] array = listOfAlbums.toArray(new SharedAlbum[listOfAlbums.size()]);
        return Response.ok(array).build();
    }

    @GET
    @Path("/getListOfPictures/{album}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfPictures(@PathParam("album") String album) {
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
        this.dataManager.deleteAlbum(new SharedAlbum(album));
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletePicture/{album}/{picture}")
    public Response deletePicture(@PathParam("album") String album, @PathParam("picture") String picture) {
        if (this.dataManager.deletePicture(new SharedAlbum(album), new SharedPicture(picture))) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/getPictureData/{album}/{picture}")
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture) {
        byte[] bytes = dataManager.loadPictureData(new SharedAlbum(album), new SharedPicture(picture));
        if (bytes != null) {
            return Response.ok(bytes).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/uploadPicture/{album}/{picture}")
    public Response uploadPicture(@PathParam("album") String album,@PathParam("picture") String picture, byte[] data) {
        SharedPicture newPicture = dataManager.uploadPicture(new SharedAlbum(album), picture, data);
        if(newPicture != null) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
