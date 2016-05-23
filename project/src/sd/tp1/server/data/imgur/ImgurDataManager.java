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
import org.apache.commons.codec.language.Soundex;
import org.apache.kafka.connect.source.SourceTask;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gbfm on 5/17/16.
 */
public class ImgurDataManager extends FileMetadataManager implements DataManager {

    //IN THE FILE
    //API_KEY = "01c07ff99455d21";
    //API_SECRET = "82dbdf420a192a4f7c65ba2b116a473f3b19b518";
    //PIN = "159078386d0371c22ae1d3598ffdc148c28e656c";


    //username = gazdesnake@hotmail.com
    //password = 123456

    private static final int ID_REFRESH_DELAY_SECONDS = 5;
    private static final int ID_REFRESH_INITIAL_DELAY_SECONDS = 5;

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

    private Lock lock = new ReentrantLock(false);

    public ImgurDataManager() throws NotDirectoryException {
        super();
        connect();
        readRegistry();
        startPoll();

    }

    public ImgurDataManager(File root) throws NotDirectoryException {
        super(root);
        connect();
        readRegistry();
        startPoll();
    }

    private void startPoll() {
        updateAlbumIds();
        updatePictureIdsForAllAlbums();
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate((Runnable) () -> {
            try {
                updateAlbumIds();
                updatePictureIdsForAllAlbums();
                writeRegistry();
            }catch (Throwable e) {
                e.getStackTrace();
            }
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
            out.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private void connect() {
        File file = new File(root, CONNECTION_FILE );

        FileReader in = null;
        try {
            in = (new FileReader(file));
            connection = new Gson().fromJson(in, ConnectionInfo.class);
            service = new ServiceBuilder().apiKey(connection.apiKey).apiSecret(connection.apiSecret)
                    .build(ImgurApi.instance());
            accessToken = new OAuth2AccessToken(connection.pin);
        } catch (FileNotFoundException e) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Api Key: ");
            String apiKey = scanner.nextLine();
            System.out.println("Api Secret");
            String apiSecret = scanner.nextLine();
            service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                    .build(ImgurApi.instance());
            System.out.println(service.getAuthorizationUrl());
            System.out.println("URL Code");
            String code = scanner.nextLine();
            accessToken = service.getAccessToken(code);
            accessToken = new OAuth2AccessToken(accessToken.getAccessToken());

            try {
                file.createNewFile();
                PrintStream out = new PrintStream(new FileOutputStream(file));
                out.println(gson.toJson(new ConnectionInfo(apiKey,apiSecret, accessToken.getAccessToken())));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


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
        LinkedList<SharedAlbum> albums = new LinkedList<>();
        lock.lock();
        for (String name : albumResgistry.getNames()) {
            albums.add(new SharedAlbum(name, getServerId()));
        }
        lock.unlock();
        return albums;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        if(!needUpdate(album))
            return false;

        String albumId = albumResgistry.getId(album.getName());
        if (albumId==null) {
            return false;
        }
        JSONObject response = sendRequest(ME+"/album/" + albumId, Verb.DELETE);
        boolean result = (Boolean)response.get("success");

        lock.lock();
        if (result) {
            albumResgistry.removeById(albumId);
            deleteAlbum(album);
            lock.unlock();
            return true;
        }
        lock.unlock();

        return false;
    }

    private void updatePictureIdsForAllAlbums() {
        for (String albumId : albumResgistry.getIds()) {
            JSONObject response = sendRequest("/album/" + albumId + "/images", Verb.GET);
            if ((boolean) response.get("success")) {
                JSONArray pictures = (JSONArray) response.get("data");
                lock.lock();
                updatePictureIdsForAlbum(albumResgistry.getPictureRegistry(albumId), pictures, albumResgistry.getName(albumId), albumId);
                lock.unlock();
            }
        }
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        LinkedList<SharedPicture> pictures = new LinkedList<>();
        lock.lock();
        String albumId = albumResgistry.getId(album);
        for (String name : albumResgistry.getPictureRegistry(albumId).getNames()) {
            pictures.add(new SharedPicture(name, getServerId()));
        }
        lock.unlock();
        return pictures;
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
        if(!super.needUpdate(album))
            return false;

        JSONObject response = sendRequest("/album",Verb.POST, new Param("title", album.getName()));
        if(!(boolean)response.get("success")) {
            return false;
        }
        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = album.getName();
        lock.lock();
        albumResgistry.updateEntry(id, name);
        lock.unlock();

        super.createAlbum(album);

        return true;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(!super.needUpdate(album, picture))
            return false;

        JSONObject response = sendRequest("/image", Verb.POST, new Param("name", picture.getPictureName()),
                new Param("image", Base64.encodeBase64String(data)),
                new Param("album", albumResgistry.getId(album.getName())));
        if (!(boolean)response.get("success")) {
            return false;
        }

        lock.lock();
        String albumId = albumResgistry.getId(album.getName());
        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = picture.getPictureName();
        albumResgistry.getPictureRegistry(albumId).updateEntry(id, name);
        lock.unlock();

        super.uploadPicture(album,picture,data);

        return true;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(!super.needUpdate(album, picture))
            return false;

        lock.lock();
        String albumId = albumResgistry.getId(album.getName());
        ImgurPictureRegistry pictureRegistry = albumResgistry.getPictureRegistry(albumId);
        String id = pictureRegistry.getId(picture.getPictureName());
        boolean result = (boolean)sendRequest("/image/"+id,Verb.DELETE).get("success");
        if(result) {
            pictureRegistry.removeById(id);
        }
        lock.unlock();

        super.deletePicture(album, picture);

        return result;
    }

    private void updateAlbumIds() {


        JSONObject response = sendRequest(ME + "/albums", Verb.GET);
        if (!(boolean) response.get("success")) {
            return ;
        }
        JSONArray albums = (JSONArray) response.get("data");
        Iterator iterator = albums.iterator();

        ImgurAlbumRegistry newRegistry = new ImgurAlbumRegistry();
        LinkedList<JSONObject> albumsToAdd = new LinkedList<>();
        lock.lock();
        while (iterator.hasNext()) {


            JSONObject album = (JSONObject) iterator.next();
            String albumId = album.get("id").toString();

            if (albumResgistry.hasId(albumId)) {
                String oldName = albumResgistry.getName(albumId);
                newRegistry.updateEntry(albumId, oldName);
                newRegistry.setPictureRegistry(albumId, albumResgistry.getPictureRegistry(albumId));
                albumResgistry.removeById(albumId);
            } else {
                albumsToAdd.add(album);
            }

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
        lock.unlock();

    }

    private void updatePictureIdsForAlbum(ImgurPictureRegistry registry, JSONArray pictures, String albumName, String albumId) {
        ImgurPictureRegistry newRegistry = new ImgurPictureRegistry();
        LinkedList<JSONObject> pictureToAdd = new LinkedList<>();

        lock.lock();
        if(pictures != null) {
            Iterator iterator = pictures.iterator();
            while (iterator.hasNext()) {
                JSONObject picture = (JSONObject) iterator.next();
                String pictureId = picture.get("id").toString();

                if (registry.hasId(pictureId)) {
                    String oldName = registry.removeById(pictureId);
                    newRegistry.updateEntry(pictureId, oldName);
                } else {
                    pictureToAdd.add(picture);

                }

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

        lock.unlock();
    }



}
