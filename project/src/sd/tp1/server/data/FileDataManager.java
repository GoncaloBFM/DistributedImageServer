package sd.tp1.server.data;

import sd.tp1.common.data.*;
import sd.tp1.common.notifier.EventHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by everyone on 4/1/16.
 */
public class FileDataManager extends FileMetadataManager implements DataManager {


    private Queue<EventHandler> handlers = new ConcurrentLinkedDeque<>();

    public FileDataManager() throws NotDirectoryException {
    }

    public FileDataManager(File root) throws NotDirectoryException {
        super(root);
    }

    @Override
    public List<SharedAlbum> loadListOfAlbums() {
        List<SharedAlbum> list = new LinkedList<>();
        for(File iFile : root.listFiles(x -> x.isDirectory()
                        //&& !x.getName().endsWith(".delete")
                        && !x.getName().startsWith("."))){

            SharedAlbum album = readAlbumMeta(iFile.getName());
            if(album != null)
                list.add(album);
        }

        return list;
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String albumName) {
        Album album = readAlbumMeta(albumName);
        if(album == null)
            return null;

        List<SharedPicture> list = new LinkedList<>();
        File dir = openAlbum(album);
        if (!dir.isDirectory()) {
            return null;
        }

        for(File iFile : dir.listFiles(x -> !x.isDirectory()
                //&& !x.getName().endsWith(".delete")
                && !x.getName().startsWith("."))){

            SharedPicture picture = readPictureMeta(albumName, iFile.getName());
            if(picture != null)
                list.add(picture);
        }

        return list;

    }

    @Override
    public byte[] loadPictureData(String albumName, String pictureName) {

        Picture picture = readPictureMeta(albumName, pictureName);
        if(picture == null)
            return null;

        try {
            if(!picture.isDeleted())
                return Files.readAllBytes(openPicture(albumName, pictureName).toPath());

            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addEventHandler(EventHandler eventHandler) {
        handlers.add(eventHandler);
    }

    private void notifyAlbumUpdate(String album){
        handlers.forEach(x -> x.onAlbumUpdate(album));
    }

    private void notifyPictureUpdate(String album, String picture){
        handlers.forEach(x -> x.onPictureUpdate(album, picture));
    }

    @Override
    public boolean createAlbum(Album album) {
        if(!super.createAlbum(album))
            return false;

        File folder = new File(root, album.getName());
        if(folder.exists() || folder.mkdir()) {
            notifyAlbumUpdate(album.getName());
            return true;
        }

        return false;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(!super.uploadPicture(album, picture, data))
            return false;

        File file = openPicture(album, picture);
        try {
            Files.write(file.toPath(), data);
            notifyPictureUpdate(album.getName(), picture.getPictureName());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAlbum(Album album) {
        if(super.deleteAlbum(album)){
            notifyAlbumUpdate(album.getName());
            return true;
        }

        return false;

        ///File folder = openAlbum(album);
        //return folder.renameTo(new File(folder.getAbsolutePath() + ".delete"));
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(super.deletePicture(album, picture)){
            notifyPictureUpdate(album.getName(), picture.getPictureName());
            return true;
        }

        return false;

        //File file = openPicture(album, picture);
        //return file.renameTo(new File(file.getAbsolutePath() + ".delete"));
    }

    private File openPicture(String albumName, String pictureName){
        return new File(openAlbum(albumName), pictureName);
    }

    private File openPicture(Album album, String pictureName){
        return openPicture(album.getName(), pictureName);
    }

    private File openPicture(Album album, Picture picture){
        return openPicture(album.getName(), picture.getPictureName());
    }

    @Override
    public void dispose(String album) {
        super.dispose(album);
        openAlbum(album).delete();
    }

    @Override
    public void dispose(String album, String picture) {
        super.dispose(album, picture);
        openPicture(album, picture).delete();
    }
}

