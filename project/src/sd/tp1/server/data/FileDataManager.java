package sd.tp1.server.data;

import sd.tp1.common.data.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.*;

/**
 * Created by gbfm on 4/1/16.
 */
public class FileDataManager extends FileMetadataManager implements DataManager {


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
    public boolean createAlbum(Album album) {
        if(!super.createAlbum(album))
            return false;

        File folder = new File(root, album.getName());
        return folder.exists() || folder.mkdir();
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(!super.uploadPicture(album, picture, data))
            return false;

        File file = openPicture(album, picture.getPictureName());
        try {
            Files.write(file.toPath(), data);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAlbum(Album album) {
        return super.deleteAlbum(album);

        ///File folder = openAlbum(album);
        //return folder.renameTo(new File(folder.getAbsolutePath() + ".delete"));
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        return super.deletePicture(album, picture);

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
}

