package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gbfm on 4/1/16.
 */
public class FileDataManager implements DataManager {

    private final static String META_EXT = ".meta";

    private File root;
    private String serverId = "abc-def-ghi";

    public FileDataManager() throws NotDirectoryException {
        this(new File("."));
    }

    public FileDataManager(File root) throws NotDirectoryException {
        if(!root.isDirectory())
            throw new NotDirectoryException(root.getAbsolutePath());
        this.root = root;
    }

    private SharedAlbum readAlbumMeta(String album){
        try {
            File file = new File(root, album + META_EXT);
            if(!file.exists())
                return null;

            ObjectInput in = new ObjectInputStream(new FileInputStream(file));
            SharedAlbum sharedAlbum = (SharedAlbum) in.readObject();
            in.close();

            return sharedAlbum;
        } catch (ClassNotFoundException| IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SharedPicture readPictureMeta(String album, String picture){
        try {
            File file = new File(openAlbum(album), picture + META_EXT);
            if(!file.exists())
                return null;

            ObjectInput in = new ObjectInputStream(new FileInputStream(file));
            SharedPicture sharedPicture = (SharedPicture) in.readObject();
            in.close();

            return sharedPicture;

        } catch (ClassNotFoundException| IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeAlbumMeta(Album album){
        try{
            File file = new File(root, album.getName() + META_EXT);
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(album);
            out.flush();
            out.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writePictureMeta(Album album, Picture picture){
        writeAlbumMeta(album);
        try{
            File file = new File(openAlbum(album), picture.getPictureName() + META_EXT);
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(picture);
            out.flush();
            out.close();

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SharedAlbum> loadListOfAlbums() {
        List<SharedAlbum> list = new LinkedList<>();
        for(File iFile : root.listFiles(x -> x.isDirectory() && !x.getName().contains(".delete") && !x.getName().startsWith("."))){
            list.add(readAlbumMeta(iFile.getName()));
        }

        return list;
    }

    @Override
    public List<SharedPicture> loadListOfPictures(String album) {
        List<SharedPicture> list = new LinkedList<>();
        File dir = openAlbum(album);
        if (!dir.isDirectory()) {
            return null;
        }
        for(File iFile : dir.listFiles(x -> !x.isDirectory() && !x.getName().contains(".delete") && !x.getName().startsWith("."))){
            SharedPicture picture = readPictureMeta(album, iFile.getName());
            if(!picture.isDeleted())
                list.add(picture);
        }

        return list;

    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        try {
            if(!readPictureMeta(album, picture).isDeleted())
                return Files.readAllBytes(openPicture(album, picture).toPath());

            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void createAlbum(Album album) {
        File folder = new File(root, album.getName());

        if(!folder.exists() && folder.mkdir()) {
            //SharedAlbum album = new SharedAlbum(folder.getName());
            writeAlbumMeta(album);
        }
    }

    @Override
    public void uploadPicture(Album album, Picture picture, byte[] data) {
        File file = openPicture(album, picture.getPictureName());
        if(!file.exists()) {
            //openAlbum(album).mkdir();
            try {
                Files.write(file.toPath(), data);

                writeAlbumMeta(album);
                writePictureMeta(album, picture);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteAlbum(Album album) {
        File folder = openAlbum(album);
        folder.renameTo(new File(folder.getAbsolutePath() + ".delete"));
        writeAlbumMeta(album);
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        File file = openPicture(album, picture);
        file.renameTo(new File(file.getAbsolutePath() + ".delete"));

        writeAlbumMeta(album);
        writePictureMeta(album, picture);

        return true;
    }

    private File openAlbum(Album album){
        return this.openAlbum(album.getName());
    }

    private File openAlbum(String albumName){
        return new File(root, albumName);
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
}

