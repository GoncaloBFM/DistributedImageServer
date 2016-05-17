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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by gbfm on 5/17/16.
 */
public class ImgurDataManager implements DataManager{

    final String apiKey = "01c07ff99455d21";
    final String apiSecret = "82dbdf420a192a4f7c65ba2b116a473f3b19b518";
    final String pin = "159078386d0371c22ae1d3598ffdc148c28e656c";
    final String link = "https://api.imgur.com/3";
    JSONParser parser = new JSONParser();
    HashMap<String, String> pictureNameIdMap = new HashMap<>();
    HashMap<String, String> albumNameIdMap = new HashMap<>();

    final OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
            .build(ImgurApi.instance());

    final OAuth2AccessToken accessToken = new OAuth2AccessToken(pin);

    private class Param {
        private final String paramName;
        private final String paramValue;
        private Param(String paramName, String paramValue) {
            this.paramName = paramName;
            this.paramValue = paramValue;
        }
    }

    private JSONObject sendRequest(String request, Verb type, Param... params) {
        OAuthRequest req = new OAuthRequest(type, link+request, service);

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
        JSONArray albums = (JSONArray) sendRequest("/account/me/albums", Verb.GET).get("data");
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
    public void deleteAlbum(Album album) {
        sendRequest("/albums/" + albumNameIdMap.get(album.getName()), Verb.DELETE);
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        JSONArray albums = (JSONArray) sendRequest("/albums/" + albumNameIdMap.get(album) + "/images", Verb.GET).get("data");
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
        }

        return null;
    }

    @Override
    public void createAlbum(Album album) {
        JSONObject newAlbum = (JSONObject) sendRequest("/album",Verb.POST, new Param("title", album.getName())).get("data");
        String id = (String) newAlbum.get("id");
        String name = album.getName();
        addUnique(name, id, albumNameIdMap);

    }

    @Override
    public void uploadPicture(Album album, Picture picture, byte[] data) {
        JSONObject newPicture = (JSONObject) sendRequest("/image", Verb.POST, new Param("name", picture.getPictureName()), new Param("image", Base64.encodeBase64String(data))).get("data");
        String id = (String) newPicture.get("id");
        String name = picture.getPictureName();
        addUnique(name, id, pictureNameIdMap);
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        sendRequest("/image/"+pictureNameIdMap.get(picture.getPictureName()),Verb.DELETE);
        pictureNameIdMap.remove(picture.getPictureName());
        //TODO: CHECK IF NEEDED
        return true;
    }



    private void addUnique(String name, String id, Map<String, String> map) {
        if(map.containsKey(name)) {
            name += id;
        }
        map.put(name, id);
    }

    @Override
    public String getServerId() {
        return "abc-def-ghi-imgur";
    }
}
