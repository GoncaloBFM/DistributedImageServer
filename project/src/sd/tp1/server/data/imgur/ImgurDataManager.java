package sd.tp1.server.data.imgur;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
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

    private static final String SEPARATOR = "/";
    private static final String CONNECTION_FILE = "imgur.con";
    private static final String LINK = "https://api.imgur.com/3";
    private static final String ME = "/account/me";

    private ImgurRegistry pictureRegistry = new ImgurRegistry();
    private ImgurRegistry albumResgistry = new ImgurRegistry();

    private OAuth20Service service;
    private OAuth2AccessToken accessToken;


    private ConnectionInfo connection;
    JSONParser parser = new JSONParser();

    public ImgurDataManager() throws IOException {
        super();
        connect();


    }

    public ImgurDataManager(File root) throws FileNotFoundException, NotDirectoryException {
        super(root);
        connect();
    }

    private void loadImgurRegistries() {

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

        JSONObject response = sendRequest(ME+"/albums/" + albumResgistry.getId(album.getName()), Verb.DELETE);
        return (boolean)response.get("success");
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        JSONObject response = sendRequest("/album/" + albumResgistry.getId(album) + "/images", Verb.GET);
        if (!(boolean)response.get("success")) {
            return null;
        }
        JSONArray pictures = (JSONArray) response.get("data");
        Iterator iterator = pictures.iterator();
        LinkedList<SharedPicture> result = new LinkedList<>();


        while (iterator.hasNext()) {
            JSONObject picture = (JSONObject) iterator.next();
            String pictureId = picture.get("id").toString();
            String pictureName = picture.get("title").toString();
            String albumId = picture.get("album").toString();


            if (!pictureRegistry.hasId(pictureId)) {
                pictureRegistry.updateEntry(pictureId, albumResgistry.getName(albumId)+pictureName);
            }
            result.add(new SharedPicture(pictureName, getServerId()));
        }

        return result;
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {

        JSONObject response = sendRequest("/image/" + pictureRegistry.getId(album + SEPARATOR + picture), Verb.GET);
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

        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = picture.getPictureName();
        pictureRegistry.updateEntry(id, album.getName()+SEPARATOR+name);

        return true;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(!super.deletePicture(album, picture))
            return false;

        String id = pictureRegistry.getId(album.getName()+SEPARATOR+picture.getPictureName());
        boolean result = (boolean)sendRequest("/image/"+id,Verb.DELETE).get("success");
        pictureRegistry.removeById(id);

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

        ImgurRegistry newRegistry = new ImgurRegistry();
        HashMap<String, String> albumsToAdd = new HashMap<>();
        while (iterator.hasNext()) {
            JSONObject album = (JSONObject) iterator.next();
            String albumId = album.get("id").toString();
            String albumName = album.get("title").toString();


            if (albumResgistry.hasId(albumId)) {
                String oldName = albumResgistry.removeById(albumId);
                newRegistry.updateEntry(albumId, oldName);
            } else {
                albumsToAdd.put(albumId, albumName);
            }

            result.add(new SharedAlbum(albumName, getServerId()));
        }

        for (String idToRemove : albumResgistry.getIdNameMap().keySet()) {
            super.deleteAlbum(new SharedAlbum(albumResgistry.getName(idToRemove), getServerId()));
        }

        for (String idToAdd : albumsToAdd.keySet()) {
            String albumName = albumsToAdd.get(idToAdd);
            newRegistry.updateEntry(idToAdd, albumsToAdd.get(idToAdd));
            SharedAlbum newAlbum = new SharedAlbum(albumName, getServerId());
            super.createAlbum(newAlbum);
        }

        albumResgistry = newRegistry;

        return result;
    }

    private void updatePictureIds(String albumFilter) {
        JSONObject response = sendRequest(ME + "/images", Verb.GET);
        if (!(boolean) response.get("success")) {
            return;
        }
        JSONArray pictures = (JSONArray) response.get("data");
        Iterator iterator = pictures.iterator();
        LinkedList<SharedPicture> result = new LinkedList<>();

        ImgurRegistry newRegistry = new ImgurRegistry();
        HashMap<String, String> picturesToAdd = new HashMap<>();
        while (iterator.hasNext()) {
            JSONObject picture = (JSONObject) iterator.next();
            String pictureId = picture.get("id").toString();
            String pictureName = picture.get("title").toString();
            String albumId = picture.get("album").toString();


            if (pictureRegistry.hasId(pictureId)) {
                String old = pictureRegistry.removeById(pictureId);
                newRegistry.updateEntry(pictureId, old);
            } else {
                picturesToAdd.put(pictureId, albumResgistry.getId(albumId)+pictureName);
            }

        }

        for (String idToRemove : albumResgistry.getIdNameMap().keySet()) {
            String[] value = albumResgistry.getId(idToRemove).split(SEPARATOR);
            String pictureName = value[1];
            String albumName = value[0];
            super.deletePicture(new SharedAlbum(albumName, getServerId()),new SharedPicture(pictureName, getServerId()));
        }

        for (String idToAdd : picturesToAdd.keySet()) {
            newRegistry.updateEntry(idToAdd, picturesToAdd.get(idToAdd));

            String[] value = picturesToAdd.get(idToAdd).split(SEPARATOR);
            String pictureName = value[1];
            String albumName = value[0];

            super.uploadPicture(new SharedAlbum(albumName, getServerId()),new SharedPicture(pictureName, getServerId()), null);
        }

        albumResgistry = newRegistry;

   }



}
