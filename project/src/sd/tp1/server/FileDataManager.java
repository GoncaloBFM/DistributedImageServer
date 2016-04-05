package sd.tp1.server;

import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gbfm on 4/1/16.
 */
public class FileDataManager implements DataManager {

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
    public SharedAlbum createAlbum(String name) {
        File folder = new File(root, name);
        if(!folder.exists() && folder.mkdir()) {
            return new SharedAlbum(folder.getName());
        }
        return null;
    }

    @Override
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data) {
        File file = openPicture(album, name);
        if(!file.exists()) {
            openAlbum(album).mkdir();
            try {
                Files.write(file.toPath(), data);
                return new SharedPicture(name);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    @Override
    public void deleteAlbum(SharedAlbum album) {
        File folder = openAlbum(album);
        folder.renameTo(new File(folder.getAbsolutePath() + ".delete"));
    }

    @Override
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        File file = openPicture(album, picture);
        file.renameTo(new File(file.getAbsolutePath() + ".delete"));
        return true;
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
        return openPicture(album.getName(), picture.getName());
    }
}

