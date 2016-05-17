package sd.tp1.server;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.util.*;

/**
 * Created by gbfm on 5/17/16.
 */
public class ImgurDataManager implements DataManager{

    final String apiKey = "01c07ff99455d21";
    final String apiSecret = "82dbdf420a192a4f7c65ba2b116a473f3b19b518";
    final String pin = "159078386d0371c22ae1d3598ffdc148c28e656c";
    final String link = "https://api.imgur.com/3/account/me/";
    JSONParser parser = new JSONParser();
    HashMap<String, String> pictureNameIdMap = new HashMap<>();
    HashMap<String, String> albumNameIdMap = new HashMap<>();

    final OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
            .build(ImgurApi.instance());

    final OAuth2AccessToken accessToken = new OAuth2AccessToken(pin);


    private JSONObject sendRequest(String request) {
        OAuthRequest albumsReq = new OAuthRequest(Verb.GET, link+request, service);
        service.signRequest(accessToken, albumsReq);
        Response albumsRes = albumsReq.send();

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
        JSONArray albums = (JSONArray) sendRequest("albums").get("data");
        Iterator iterator = albums.iterator();
        LinkedList<SharedAlbum> result = new LinkedList<>();
        albumNameIdMap = new HashMap<>();
        while (iterator.hasNext()) {
            JSONObject album = (JSONObject) iterator.next();
            String pictureName = album.get("title").toString();
            String pictureId = album.get("id").toString();
            //result.add(new SharedAlbum(pictureId));
            albumNameIdMap.put(pictureName, pictureId);
            System.out.println("id : " + ((JSONObject)iterator.next()).get("title"));
        }
        return result;
    }

    @Override
    public void deleteAlbum(Album album) {
        OAuthRequest albumsReq = new OAuthRequest(Verb.DELETE, link+albumNameIdMap.get(album.getName()), service);
        service.signRequest(accessToken, albumsReq);
        Response albumsRes = albumsReq.send();

        JSONObject res = null;
        try {
            res = (JSONObject) parser.parse(albumsRes.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        return null;
    }

    @Override
    public byte[] loadPictureData(Album album, Picture picture) {
        return new byte[0];
    }

    @Override
    public void createAlbum(Album album) {

    }

    @Override
    public void uploadPicture(Album album, Picture picture, byte[] data) {

    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        return false;
    }
}
