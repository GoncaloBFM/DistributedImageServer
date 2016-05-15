package sd.tp1.server;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gbfm on 4/1/16.
 */
public class FileDataManager extends AbstractDataManager {

    private File root;

    public FileDataManager() throws NotDirectoryException {
        this(new File("."));
    }

    public FileDataManager(File root) throws NotDirectoryException {
        if(!root.isDirectory())
            throw new NotDirectoryException(root.getAbsolutePath());
        this.root = root;
    }

    @Override
    public List<SharedAlbum> loadListOfAlbums() {
        List<SharedAlbum> list = new LinkedList<>();
        for(File iFile : root.listFiles(x -> x.isDirectory() && !x.getName().contains(".delete")))
            list.add(new SharedAlbum(iFile.getName()));

        return list;
    }

    @Override
    public List<SharedPicture> loadListOfPictures(SharedAlbum album) {
        List<SharedPicture> list = new LinkedList<>();
        File dir = openAlbum(album);
        if (!dir.isDirectory()) {
            return null;
        }
        for(File iFile : dir.listFiles(x -> !x.isDirectory() && !x.getName().contains(".delete")))
            list.add(new SharedPicture(iFile.getName()));

        return list;

    }

    @Override
    public byte[] loadPictureData(SharedAlbum album, SharedPicture picture) {
        try {
            return Files.readAllBytes(openPicture(album, picture).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SharedAlbum createAlbumNoNotify(String name) {
        File folder = new File(root, name);
        if(!folder.exists() && folder.mkdir()) {
            SharedAlbum album = new SharedAlbum(folder.getName());
            return album;
        }

        return null;
    }

    @Override
    public SharedAlbum createAlbum(String name) {
        SharedAlbum album = createAlbumNoNotify(name);

        if(album != null)
            notifyAlbumCreate(album);

        return album;
    }

    @Override
    public SharedPicture uploadPictureNoNotify(SharedAlbum album, String name, byte[] data) {
        File file = openPicture(album, name);
        if(!file.exists()) {
            //openAlbum(album).mkdir();
            try {
                Files.write(file.toPath(), data);
                SharedPicture picture = new SharedPicture(name);
                return picture;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data) {
        SharedPicture picture = this.uploadPictureNoNotify(album, name, data);

        if(picture != null)
            this.notifyPictureUpload(album, picture);

        return picture;
    }

    @Override
    public void deleteAlbumNoNotify(SharedAlbum album) {
        File folder = openAlbum(album);
        folder.renameTo(new File(folder.getAbsolutePath() + ".delete"));
    }

    @Override
    public void deleteAlbum(SharedAlbum album) {
        this.deleteAlbumNoNotify(album);
        super.notifyAlbumDelete(album);
    }

    @Override
    public boolean deletePictureNoNotify(SharedAlbum album, SharedPicture picture) {
        File file = openPicture(album, picture);
        file.renameTo(new File(file.getAbsolutePath() + ".delete"));

        return true;
    }

    @Override
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        boolean deleted = this.deletePictureNoNotify(album, picture);

        if(deleted)
            super.notifyPictureDelete(album, picture);

        return deleted;
    }

    private File openAlbum(SharedAlbum album){
        return this.openAlbum(album.getName());
    }

    private File openAlbum(String albumName){
        return new File(root, albumName);
    }

    private File openPicture(String albumName, String pictureName){
        return new File(openAlbum(albumName), pictureName);
    }

    private File openPicture(SharedAlbum album, String pictureName){
        return openPicture(album.getName(), pictureName);
    }

    private File openPicture(SharedAlbum album, SharedPicture picture){
        return openPicture(album.getName(), picture.getPictureName());
    }
}

