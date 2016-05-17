package sd.tp1.common.protocol.rest.server;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.data.SharedPicture;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.common.protocol.LoggedAbstractEndpoint;
import sd.tp1.common.protocol.rest.envelops.DeletePictureEnvelop;
import sd.tp1.common.protocol.rest.envelops.UploadPictureEnvelop;
import sd.tp1.common.data.DataManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Path("/{serverPath}")
public class RestServer implements EndpointServer{

    public final static String SERVER_TYPE = "REST";

    private final LoggedAbstractEndpoint logger = new LoggedAbstractEndpoint(RestServer.class.getSimpleName()) {
        @Override
        public URL getUrl() {
            return url;
        }
    };

    protected URI uri;
    protected URL url;
    protected DataManager dataManager;
    private HttpServer httpServer;

    public RestServer(@PathParam("serverPath") String serverPath, URI uri, DataManager dataManager ) throws MalformedURLException {
        this.dataManager = dataManager;

        this.uri = uri;
        URL url = uri.toURL();
        this.url = new URL(url.getProtocol(), url.getHost(), url.getPort(), serverPath);
    }

    protected HttpServer startScript(){
        ResourceConfig config = new ResourceConfig();
        config.register(this);
        return JdkHttpServerFactory.createHttpServer(uri, config);
    }

    @Override
    public void start(){
        if(httpServer != null)
            return;

        this.httpServer = startScript();
    }

    @Override
    public void stop(){
        if(httpServer == null)
            return;

        this.httpServer.stop(0);
        this.httpServer = null;
    }

    @Override
    public boolean isRunning(){
        return this.httpServer != null;
    }

    @Override
    public String getType() {
        return SERVER_TYPE;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    private Response accessControllAllowOrigin(Response response){
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        return response;
    }

    @POST
    @Path("/createAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlbum(SharedAlbum album) {
        logger.createAlbum(album);

        return this.dataManager.createAlbum(album) ?
            Response.ok().build() :
                Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/getListOfAlbums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums() {
        logger.loadListOfAlbums();

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
        logger.loadListOfPictures(album);

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
        logger.deleteAlbum(album);

        return this.dataManager.deleteAlbum(album) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/deletePicture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePicture(DeletePictureEnvelop env) {
        logger.deletePicture(env.album, env.picture);

        return this.dataManager.deletePicture(env.album, env.picture) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/getPictureData/{album}/{picture}")
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture) {
        logger.loadPictureData(album, picture);

        byte[] bytes = dataManager.loadPictureData(album, picture);
        return Response.ok(bytes).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/uploadPicture")
    public Response uploadPicture(UploadPictureEnvelop env) {
        logger.uploadPicture(env.album, env.picture, env.data);

        return dataManager.uploadPicture(env.album, env.picture, env.data) ?
            Response.ok().build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/getServerId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerId(){
        logger.getServerId();

        String serverId = dataManager.getServerId();

        return serverId != null ?
                Response.ok(serverId).build()
                : Response.serverError().build();
    }
}
