package sd.tp1.common.data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apontes on 5/18/16.
 */
public class MetadataBundle implements Serializable{
    private List<SharedAlbum> albumList;
    private List<SharedAlbumPicture> pictureList;

    public MetadataBundle(List<Album> albumList, List<AlbumPicture> pictureList){
        this.albumList = albumList.stream().map(x -> new SharedAlbum(x)).collect(Collectors.toList());
        this.pictureList = pictureList.parallelStream().map(x -> new SharedAlbumPicture(x.getAlbum(), x.getPicture())).collect(Collectors.toList());
    }

    public List<SharedAlbum> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<SharedAlbum> albumList) {
        this.albumList = albumList;
    }

    public List<SharedAlbumPicture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<SharedAlbumPicture> pictureList) {
        this.pictureList = pictureList;
    }

}

