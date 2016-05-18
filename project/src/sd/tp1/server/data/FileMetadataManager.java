package sd.tp1.server.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sd.tp1.common.data.*;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by apontes on 5/17/16.
 */
public class FileMetadataManager implements MetadataManager {
    private final static String META_EXT = ".meta";
    private final static String SERVER_ID_FILE = ".server-id";

    protected final File root;
    protected String serverId;

    public FileMetadataManager() throws NotDirectoryException {
        this(new File("."));
    }

    public FileMetadataManager(File root) throws NotDirectoryException {
        if(!root.isDirectory())
            throw new NotDirectoryException(root.getAbsolutePath());
        this.root = root;
    }

    protected boolean isNewer(Metadata m1, Metadata m2){
        if(m1 == null)
            return false;

        if(m2 == null)
            return true;

        return m1.compareTo(m2) > 0;
    }

    protected boolean isLocalNewer(Album album){
        Album local = readAlbumMeta(album.getName());
        if(local == null)
            return false;

        return local.compareTo(album) > 0;
    }

    protected boolean isLocalNewer(Album album, Picture picture){
        Picture local = readPictureMeta(album.getName(), picture.getPictureName());
        if(local == null)
            return false;

        return local.compareTo(picture) > 0;
    }

    protected String readServerId(){
        if(serverId != null)
            return serverId;

        try{
            File file = new File(root, SERVER_ID_FILE);
            if(file.exists()){
                Scanner in = new Scanner(new FileInputStream(file));
                serverId = in.nextLine();
                in.close();
            }
            else{
                file.createNewFile();
                PrintStream out = new PrintStream(new FileOutputStream(file));
                serverId = UUID.randomUUID().toString();
                out.println(serverId);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverId;
    }

    protected SharedAlbum readAlbumMeta(String album){
        try {
            File file = new File(root, album + META_EXT);
            if(!file.exists())
                return null;

            //ObjectInput in = new ObjectInputStream(new FileInputStream(file));
            //SharedAlbum sharedAlbum = (SharedAlbum) in.readObject();
            FileReader in = (new FileReader(file));
            SharedAlbum sharedAlbum = new Gson().fromJson(in, SharedAlbum.class);

            in.close();

            return sharedAlbum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected SharedPicture readPictureMeta(String album, String picture){
        try {
            File file = new File(openAlbum(album), picture + META_EXT);
            if(!file.exists())
                return null;

            //ObjectInput in = new ObjectInputStream(new FileInputStream(file));
            //SharedPicture sharedPicture = (SharedPicture) in.readObject();
            FileReader in = (new FileReader(file));
            SharedPicture sharedPicture = new Gson().fromJson(in, SharedPicture.class);

            in.close();

            return sharedPicture;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeAlbumMeta(Album album){
        try{
            File file = new File(root, album.getName() + META_EXT);
            //ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            //out.writeObject(album);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            PrintStream out = new PrintStream(new FileOutputStream(file));
            out.println(gson.toJson(album));

            out.flush();
            out.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writePictureMeta(Album album, Picture picture){
        writeAlbumMeta(album);
        try{
            File file = new File(openAlbum(album), picture.getPictureName() + META_EXT);
            //ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            //out.writeObject(picture);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            PrintStream out = new PrintStream(new FileOutputStream(file));
            out.println(gson.toJson(picture));

            out.flush();
            out.close();

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createAlbum(Album album) {
        Album local = readAlbumMeta(album.getName());
        if(isNewer(local, album))
            return false;

        writeAlbumMeta(album);
        return true;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        Picture local = readPictureMeta(album.getName(), picture.getPictureName());
        if(isNewer(local, picture))
            return false;

        writePictureMeta(album, picture);
        return true;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        Album local = readAlbumMeta(album.getName());
        if(isNewer(local, album))
            return false;

        writeAlbumMeta(album);
        return true;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        Picture local = readPictureMeta(album.getName(), picture.getPictureName());
        if(isNewer(local, picture))
            return false;

        writePictureMeta(album, picture);
        return true;
    }

    @Override
    public String getServerId() {
        return readServerId();
    }

    @Override
    public MetadataBundle getMetadata() {
        List<Album> albumList = new LinkedList<>();
        List<AlbumPicture> pictureList = new LinkedList<>();

        for(File file : this.root.listFiles()){
            if(file.isDirectory()){
                //Album folder

                SharedAlbum album = readAlbumMeta(file.getName());
                if(album == null)
                    continue;

                albumList.add(album);

                for(File innerFile : file.listFiles()){
                    if(!isMetafile(innerFile)){
                        //Picture file
                        SharedPicture picture = readPictureMeta(album.getName(), innerFile.getName());
                        if(picture == null)
                            continue;

                        pictureList.add(new SharedAlbumPicture(album, picture));
                    }
                }
            }
        }

        return new MetadataBundle(albumList, pictureList);
    }

    @Override
    public Album getAlbum(String albumName) {
        return this.readAlbumMeta(albumName);
    }

    @Override
    public Picture getPicture(String albumName, String pictureName) {
        return this.readPictureMeta(albumName, pictureName);
    }

    @Override
    public void setAlbum(Album album) {
        if(isNewer(album))
            this.writeAlbumMeta(album);
    }

    @Override
    public void setPicture(Album album, Picture picture) {
        if(isNewer(album, picture))
            this.writePictureMeta(album, picture);
    }

    @Override
    public boolean isNewer(Album album) {
        if(album == null)
            return false;

        Album actual = readAlbumMeta(album.getName());
        if(actual == null)
            return false; //TODO discuss

        return actual.compareTo(actual) < 0;
    }

    @Override
    public boolean isNewer(Album album, Picture picture) {
        if(album == null || picture == null)
            return false;

        Picture actual = readPictureMeta(album.getName(), picture.getPictureName());
        if(actual == null)
            return false; //TODO discuss

        return actual.compareTo(actual) < 0;
    }

    protected boolean isMetafile(File file){
        return file.getName().endsWith(META_EXT);
    }

    protected File openAlbum(Album album){
        return this.openAlbum(album.getName());
    }

    protected File openAlbum(String albumName){
        return new File(root, albumName);
    }

}