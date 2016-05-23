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

    public static final String PASSWORD = "p@ssword66";
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
        this.url = new URL(url.getProtocol(), url.getHost(), url.getPort(), "/" + serverPath);
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

    private Response responsePointcut(Response response){
        return accessControllAllowOrigin(response);
    }

    protected boolean checkPassword(String password){
        return PASSWORD.equals(password);
    }

    @POST
    @Path("/createAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlbum(SharedAlbum album, @CookieParam("password") String password) {
        logger.createAlbum(album);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        return responsePointcut(this.dataManager.createAlbum(album) ?
            Response.ok().build() :
                Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Path("/getListOfAlbums")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums(@CookieParam("password") String password) {
        logger.loadListOfAlbums();

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        List<SharedAlbum> listOfAlbums = this.dataManager.loadListOfAlbums();
        if(listOfAlbums == null)
            return responsePointcut(Response.serverError().build());

        SharedAlbum[] array = listOfAlbums.toArray(new SharedAlbum[listOfAlbums.size()]);
        return responsePointcut(Response.ok(array).build());
    }

    @GET
    @Path("/getListOfPictures/{album}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfPictures(@PathParam("album") String album, @CookieParam("password") String password) {
        logger.loadListOfPictures(album);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        List<SharedPicture> listOfPictures = this.dataManager.loadListOfPictures(album);
        if (listOfPictures != null) {
            SharedPicture[] array = listOfPictures.toArray(new SharedPicture[listOfPictures.size()]);
            return responsePointcut(Response.ok(array).build());
        }

        return responsePointcut(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @POST
    @Path("/deleteAlbum")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAlbum(SharedAlbum album, @CookieParam("password") String password) {
        logger.deleteAlbum(album);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        return this.dataManager.deleteAlbum(album) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/deletePicture")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePicture(DeletePictureEnvelop env, @CookieParam("password") String password) {
        logger.deletePicture(env.album, env.picture);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        return responsePointcut(this.dataManager.deletePicture(env.album, env.picture) ?
            Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/getPictureData/{album}/{picture}")
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture, @CookieParam("password") String password) {
        logger.loadPictureData(album, picture);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        byte[] bytes = dataManager.loadPictureData(album, picture);
        return responsePointcut(Response.ok(bytes).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/uploadPicture")
    public Response uploadPicture(UploadPictureEnvelop env, @CookieParam("password") String password) {
        logger.uploadPicture(env.album, env.picture, env.data);

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        return responsePointcut(dataManager.uploadPicture(env.album, env.picture, env.data) ?
            Response.ok().build()
                : Response.status(Response.Status.BAD_REQUEST).build());
    }

    @GET
    @Path("/getServerId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerId(@CookieParam("password") String password){
        logger.getServerId();

        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        String serverId = dataManager.getServerId();

        return responsePointcut(serverId != null ?
                Response.ok(serverId).build()
                : Response.serverError().build());
    }

    @GET
    @Path("/getMetadata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetadata(@CookieParam("password") String password){
        if(!checkPassword(password))
            return responsePointcut(Response.status(Response.Status.UNAUTHORIZED).build());

        logger.getMetadata();
        return responsePointcut(Response.ok(dataManager.getMetadata()).build());
    }
}
