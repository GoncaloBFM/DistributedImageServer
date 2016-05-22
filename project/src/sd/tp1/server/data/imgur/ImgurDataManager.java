package sd.tp1.server.data.imgur;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.codec.binary.Base64;
import sd.tp1.common.data.*;
import sd.tp1.common.notifier.EventHandler;
import sd.tp1.server.data.FileMetadataManager;

import java.io.*;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by gbfm on 5/17/16.
 */
public class ImgurDataManager extends FileMetadataManager implements DataManager {

    //IN THE FILE
    //API_KEY = "01c07ff99455d21";
    //API_SECRET = "82dbdf420a192a4f7c65ba2b116a473f3b19b518";
    //PIN = "159078386d0371c22ae1d3598ffdc148c28e656c";


    //username = Gazdesnake
    //password = conacona

    private static final int ID_REFRESH_DELAY_SECONDS = 2;
    private static final int ID_REFRESH_INITIAL_DELAY_SECONDS = 2;

    private static final String REGISTRY_FILE = "imgur.reg";
    private static final String CONNECTION_FILE = "imgur.con";
    private static final String LINK = "https://api.imgur.com/3";
    private static final String ME = "/account/me";

    private ImgurAlbumRegistry albumResgistry;

    private OAuth20Service service;
    private OAuth2AccessToken accessToken;


    private ConnectionInfo connection;
    private JSONParser parser = new JSONParser();

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    public ImgurDataManager() throws IOException {
        super();
        connect();
        readRegistry();
        //startPoll();
    }

    public ImgurDataManager(File root) throws FileNotFoundException, NotDirectoryException {
        super(root);
        connect();
        readRegistry();
        //startPoll();

    }

    private void startPoll() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate((Runnable) () -> {
            updateAlbumIds();
            updatePictureIdsForAllAlbums();
            writeRegistry();
        }, ID_REFRESH_INITIAL_DELAY_SECONDS, ID_REFRESH_DELAY_SECONDS, TimeUnit.SECONDS);

    }

    private void readRegistry() {
        File registryFile = new File(REGISTRY_FILE);
        FileReader in = null;
        try {
            in = (new FileReader(registryFile));
            albumResgistry = new Gson().fromJson(in, ImgurAlbumRegistry.class);
        } catch (FileNotFoundException e) {
            albumResgistry = new ImgurAlbumRegistry();
        }
    }

    private void writeRegistry() {
        try {
            File registryFile = new File(REGISTRY_FILE);
            if(!registryFile.exists()) {
                registryFile.createNewFile();
            }
            PrintStream out = new PrintStream(new FileOutputStream(registryFile, false));
            out.println(gson.toJson(albumResgistry));
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private void connect() throws FileNotFoundException {
        File file = new File(root, CONNECTION_FILE );
        if(!file.exists()) {
            throw new FileNotFoundException("Server connection data not found");
        }

        FileReader in = (new FileReader(file));
        connection = new Gson().fromJson(in, ConnectionInfo.class);

        service = new ServiceBuilder().apiKey(connection.apiKey).apiSecret(connection.apiSecret)
                .build(ImgurApi.instance());


        accessToken = new OAuth2AccessToken(connection.pin);

    }

    private class Param {
        private final String paramName;
        private final String paramValue;
        private Param(String paramName, String paramValue) {
            this.paramName = paramName;
            this.paramValue = paramValue;
        }
    }

    private JSONObject sendRequest(String request, Verb type, Param... params) {
        OAuthRequest req = new OAuthRequest(type, LINK+request, service);

        System.out.println(req);
        for (Param pair: params) {
            req.addParameter(pair.paramName, pair.paramValue);
        }
        service.signRequest(accessToken, req);
        Response response = req.send();

        JSONObject res = null;
        try {
            res = (JSONObject) parser.parse(response.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }


    @Override
    public List<SharedAlbum> loadListOfAlbums() {
        return updateAlbumIds();
    }

    @Override
    public boolean deleteAlbum(Album album) {
        if(!super.deleteAlbum(album))
            return false;

        JSONObject response = sendRequest(ME+"/album/" + albumResgistry.getId(album.getName()), Verb.DELETE);
        boolean result = (Boolean)response.get("success");
        return result;
    }

    private void updatePictureIdsForAllAlbums() {
        for (String albumId : albumResgistry.getIds()) {
            JSONObject response = sendRequest("/album/" + albumId + "/images", Verb.GET);
            if (!(boolean) response.get("success")) {
                JSONArray pictures = (JSONArray) response.get("data");
                updatePictureIdsForAlbum(albumResgistry.getPictureRegistry(albumId), pictures, albumResgistry.getName(albumId), albumId);
            }
        }
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        String albumId = albumResgistry.getId(album);
        JSONObject response = sendRequest("/album/" + albumResgistry.getId(album) + "/images", Verb.GET);
        if (!(boolean)response.get("success")) {
            return null;
        }
        JSONArray pictures = (JSONArray) response.get("data");
        return updatePictureIdsForAlbum(albumResgistry.getPictureRegistry(albumId), pictures,album, albumId);
    }


    @Override
    public byte[] loadPictureData(String album, String picture) {

        String albumId = albumResgistry.getId(album);
        JSONObject response = sendRequest("/image/" + albumResgistry.getPictureRegistry(albumId).getId(picture), Verb.GET);
        if (!(boolean)response.get("success")) {
            return null;
        }

        InputStream inputStream = null;
        try {
            System.out.println((String)response.get("link"));
            inputStream = new BufferedInputStream(new URL((String)((JSONObject)response.get("data")).get("link")).openStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int loaded = 0;
            while ((loaded = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, loaded);
            }
            inputStream.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addEventHandler(EventHandler eventHandler) {
        //TODO implement
    }

    @Override
    public boolean createAlbum(Album album) {
        if(!super.createAlbum(album))
            return false;

        JSONObject response = sendRequest("/album",Verb.POST, new Param("title", album.getName()));
        if(!(boolean)response.get("success")) {
            return false;
        }
        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = album.getName();
        albumResgistry.updateEntry(id, name);

        return true;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(!super.uploadPicture(album, picture, data))
            return false;

        JSONObject response = sendRequest("/image", Verb.POST, new Param("name", picture.getPictureName()),
                new Param("image", Base64.encodeBase64String(data)),
                new Param("album", albumResgistry.getId(album.getName())));
        if (!(boolean)response.get("success")) {
            return false;
        }

        String albumId = albumResgistry.getId(album.getName());
        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = picture.getPictureName();
        albumResgistry.getPictureRegistry(albumId).updateEntry(id, name);

        return true;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(!super.deletePicture(album, picture))
            return false;

        String albumId = albumResgistry.getId(album.getName());
        ImgurPictureRegistry pictureRegistry = albumResgistry.getPictureRegistry(albumId);
        String id = pictureRegistry.getId(picture.getPictureName());
        boolean result = (boolean)sendRequest("/image/"+id,Verb.DELETE).get("success");
        if(result) {
            pictureRegistry.removeById(id);
        }

        return result;
    }

    private List<SharedAlbum> updateAlbumIds() {
        JSONObject response = sendRequest(ME + "/albums", Verb.GET);
        if (!(boolean) response.get("success")) {
            return null;
        }
        JSONArray albums = (JSONArray) response.get("data");
        Iterator iterator = albums.iterator();
        LinkedList<SharedAlbum> result = new LinkedList<>();

        ImgurAlbumRegistry newRegistry = new ImgurAlbumRegistry();
        LinkedList<JSONObject> albumsToAdd = new LinkedList<>();
        while (iterator.hasNext()) {
            JSONObject album = (JSONObject) iterator.next();
            String albumId = album.get("id").toString();
            String albumName = album.get("title").toString();

            if (albumResgistry.hasId(albumId)) {
                String oldName = albumResgistry.removeById(albumId);
                newRegistry.updateEntry(albumId, oldName);
            } else {
                albumsToAdd.add(album);
            }

            result.add(new SharedAlbum(albumName, getServerId()));
        }

        for (String idToRemove : albumResgistry.getIds()) {
            super.deleteAlbum(new SharedAlbum(albumResgistry.getName(idToRemove), getServerId()));
        }

        albumResgistry = newRegistry;

        for (JSONObject album : albumsToAdd) {
            String albumId = album.get("id").toString();
            String albumName = album.get("title").toString();
            albumResgistry.updateEntry(albumId, albumName);
            SharedAlbum newAlbum = new SharedAlbum(albumName, getServerId());
            super.createAlbum(newAlbum);
        }

        return result;
    }

    private List<SharedPicture> updatePictureIdsForAlbum(ImgurPictureRegistry registry, JSONArray pictures, String albumName, String albumId) {


        LinkedList<SharedPicture> result = new LinkedList<>();
        ImgurPictureRegistry newRegistry = new ImgurPictureRegistry();
        LinkedList<JSONObject> pictureToAdd = new LinkedList<>();


        if(pictures != null) {
            Iterator iterator = pictures.iterator();
            while (iterator.hasNext()) {
                JSONObject picture = (JSONObject) iterator.next();
                String pictureId = picture.get("id").toString();
                String pictureName = picture.get("name").toString();

                if (registry.hasId(pictureId)) {
                    String oldName = registry.removeById(pictureId);
                    newRegistry.updateEntry(pictureId, oldName);
                } else {
                    pictureToAdd.add(picture);

                }

                result.add(new SharedPicture(pictureName, getServerId()));
            }
        }

        for (String idToRemove : registry.getIds()) {
            super.deletePicture(new SharedAlbum(albumName, getServerId()), new SharedPicture(albumResgistry.getName(idToRemove), getServerId()));
        }

        for (JSONObject picture : pictureToAdd) {
            String pictureId = picture.get("id").toString();
            String pictureName = picture.get("name").toString();
            newRegistry.updateEntry(pictureId, pictureName);
            super.uploadPicture(new SharedAlbum(albumName, getServerId()), new SharedPicture(pictureName, getServerId()), null);
        }

        albumResgistry.setPictureRegistry(albumId, newRegistry);

        return result;
    }



}
