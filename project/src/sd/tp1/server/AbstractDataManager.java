package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 5/14/16.
 */
public abstract class AbstractDataManager implements DataManager{

    private List<DataOperationHandler> handlerList = new LinkedList<>();

    protected void notifyPictureUpload(SharedAlbum album, SharedPicture picture){
        this.handlerList.forEach(x -> x.onPictureUpload(album, picture));
    }

    protected void notifyPictureDelete(SharedAlbum album, SharedPicture picture){
        this.handlerList.forEach(x -> x.onPictureDelete(album, picture));
    }

    protected void notifyAlbumCreate(SharedAlbum album){
        this.handlerList.forEach(x -> x.onAlbumCreate(album));
    }

    protected void notifyAlbumDelete(SharedAlbum album){
        this.handlerList.forEach(x -> x.onAlbumDelete(album));
    }

    @Override
    public void addDataOperationHandler(DataOperationHandler handler) {
        if(!this.handlerList.contains(handler))
            this.handlerList.add(handler);
    }
}
