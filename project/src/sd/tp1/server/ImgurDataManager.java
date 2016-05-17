package sd.tp1.server;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.sun.org.apache.regexp.internal.RE;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.util.*;

/**
 * Created by gbfm on 5/17/16.
 */
public class ImgurDataManager extends FileMetadataManager implements DataManager{

    //IN THE FILE
    //API_KEY = "01c07ff99455d21";
    //API_SECRET = "82dbdf420a192a4f7c65ba2b116a473f3b19b518";
    //PIN = "159078386d0371c22ae1d3598ffdc148c28e656c";


    //username = Gazdesnake
    //password = conacona

    private static final String LINK = "https://api.imgur.com/3";
    private static final String ME = "/account/me";
    private static final String CONNECTION_FILE = "con";

    HashMap<String, String> pictureNameIdMap = new HashMap<>();
    HashMap<String, String> albumNameIdMap = new HashMap<>();

    JSONParser parser = new JSONParser();

    private final String apiKey;
    private final String apiSecret;
    private final String pin;

    private final OAuth20Service service;
    private final OAuth2AccessToken accessToken;

    public ImgurDataManager() throws IOException {
        this(new File("."));
    }

    public ImgurDataManager(File root) throws IOException {
        super(root);

        File file = new File(root, CONNECTION_FILE );
        Scanner in;
        try {
            in = new Scanner(new FileInputStream(file));

            apiKey = in.nextLine();
            apiSecret = in.nextLine();
            pin = in.nextLine();

            service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                    .build(ImgurApi.instance());
            accessToken = new OAuth2AccessToken(pin);

            in.close();

        } catch (FileNotFoundException e) {
            throw new IOException("Server connection data not found");
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

        for (Param pair: params) {
            req.addParameter(pair.paramName, pair.paramValue);
        }
        service.signRequest(accessToken, req);
        Response albumsRes = req.send();

        JSONObject res = null;
        try {
            res = (JSONObject) parser.parse(albumsRes.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }


    @Override
    public List<SharedAlbum> loadListOfAlbums() {
        JSONObject response = sendRequest(ME+"albums/ids", Verb.GET);
        if (!(boolean)response.get("success")) {
            return null;
        }
        JSONArray albums = (JSONArray) response.get("data");
        Iterator iterator = albums.iterator();
        LinkedList<SharedAlbum> result = new LinkedList<>();
        albumNameIdMap = new HashMap<>();
        while (iterator.hasNext()) {
            JSONObject album = (JSONObject) iterator.next();
            String albumId = album.get("id").toString();
            String albumName = album.get("title").toString();
            addUnique(albumName, albumId, albumNameIdMap);
            //result.add(new SharedAlbum(albumName));
        }
        return result;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        if(!super.deleteAlbum(album))
            return false;

        JSONObject response = sendRequest(ME+"/albums/" + albumNameIdMap.get(album.getName()), Verb.DELETE);
        return (boolean)response.get("success");
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        JSONObject response = sendRequest("/album/" + albumNameIdMap.get(album) + "/images", Verb.GET);
        if (!(boolean)response.get("success")) {
            return null;
        }
        JSONArray albums = (JSONArray) response.get("data");
        Iterator iterator = albums.iterator();
        LinkedList<SharedPicture> result = new LinkedList<>();
        while (iterator.hasNext()) {
            JSONObject picture = (JSONObject) iterator.next();
            String pictureId = picture.get("id").toString();
            String pictureName = picture.get("title").toString();
            addUnique(pictureName, pictureId, pictureNameIdMap);
            //result.add(new SharedPicture(pictureId));
        }
        return result;
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {

        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new URL("http://imgur.com/" + pictureNameIdMap.get(picture)).openStream());

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
    public boolean createAlbum(Album album) {
        if(!super.createAlbum(album))
            return false;

        JSONObject response = sendRequest("/album",Verb.POST, new Param("title", album.getName()));
        if(!(boolean)response.get("success")) {
            return false;
        }
        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = album.getName();
        addUnique(name, id, albumNameIdMap);

        return true;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(!super.uploadPicture(album, picture, data))
            return false;

        JSONObject response = sendRequest("/image", Verb.POST, new Param("name", picture.getPictureName()), new Param("image", Base64.encodeBase64String(data)));
        if (!(boolean)response.get("success")) {
            return false;
        }

        String id = (String) ((JSONObject)response.get("data")).get("id");
        String name = picture.getPictureName();
        addUnique(name, id, pictureNameIdMap);

        return true;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(!super.deletePicture(album, picture))
            return false;

        boolean result = (boolean)sendRequest("/image/"+pictureNameIdMap.get(picture.getPictureName()),Verb.DELETE).get("success");
        pictureNameIdMap.remove(picture.getPictureName());

        return result;
    }



    private void addUnique(String name, String id, Map<String, String> map) {
        if(map.containsKey(name)) {
            name += id;
        }
        map.put(name, id);
    }


    private void updatePictureIds() {
        updateIds("/images", pictureNameIdMap);
    }

    private void updateAlbumIds() {
        updateIds("/albums", albumNameIdMap);
    }

    private void updateIds(String query, HashMap<String,String> map) {
        JSONObject response = sendRequest(query, Verb.GET);
        if (!(boolean)response.get("success")) {
            return;
        }
        JSONArray array = (JSONArray) response.get("data");
        for (Object raw : array) {
            JSONObject object = (JSONObject) raw;
            String objectId = object.get("id").toString();
            String objectName = object.get("title").toString();
            addUnique(objectName, objectId, map);
        }
    }

}
