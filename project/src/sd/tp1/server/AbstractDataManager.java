package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 5/14/16.
 */
public abstract class AbstractDataManager implements DataManager{

    private List<DataOperationHandler> handlerList = new LinkedList<>();

    protected void notifyPictureUpload(Album album, Picture picture){
        this.handlerList.forEach(x -> x.onPictureUpload(album, picture));
    }

    protected void notifyPictureDelete(Album album, Picture picture){
        this.handlerList.forEach(x -> x.onPictureDelete(album, picture));
    }

    protected void notifyAlbumCreate(Album album){
        this.handlerList.forEach(x -> x.onAlbumCreate(album));
    }

    protected void notifyAlbumDelete(Album album){
        this.handlerList.forEach(x -> x.onAlbumDelete(album));
    }

    @Override
    public void addDataOperationHandler(DataOperationHandler handler) {
        if(!this.handlerList.contains(handler))
            this.handlerList.add(handler);
    }
}
