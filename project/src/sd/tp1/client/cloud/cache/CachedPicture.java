package sd.tp1.client.cloud.cache;

import sd.tp1.Album;
import sd.tp1.Picture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by apontes on 4/5/16.
 */
public class CachedPicture extends CachedObject<byte[]> implements Picture, CachedBlob {

    private static final long DEFAULT_TTL = 60000;

    private static final File CACHE_DIR = new File(".pictureCache");
    static{
        if(!CACHE_DIR.exists()){
          if(CACHE_DIR.mkdir()){
              CACHE_DIR.deleteOnExit();
          }
          else
              throw new RuntimeException("Cannot create picture cache directory: " + CACHE_DIR.getAbsolutePath());
        }
        else if (!CACHE_DIR.isDirectory())
            throw new RuntimeException("Picture cache directory is a file: " + CACHE_DIR.getAbsolutePath());

    }

    private int length;
    private File file;

    private String pictureName, albumName;



    CachedPicture(Album album, Picture picture, byte[] content){
        this(album, picture, content, DEFAULT_TTL);
    }

    CachedPicture(Album album, Picture picture, byte[] content, long ttl) {
        super(content, ttl);

        this.albumName = album.getName();
        this.pictureName = picture.getPictureName();
    }


    @Override
    public int length() {
        return this.length;
    }

    @Override
    public synchronized void swap() {
        File dir;
        synchronized (CACHE_DIR){
            dir = new File(CACHE_DIR, albumName);
            if(!dir.exists()){
                if(!dir.mkdir())
                    throw new RuntimeException("Can't create album folder to cache picture");

                dir.deleteOnExit();
            }
        }

        this.file = new File(dir, this.pictureName);
        try {
            Files.write(file.toPath(), super.content);
            this.file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            super.isDirty();
        }

        //Free RAM
        super.content = null;
    }

    @Override
    public boolean isOnRam() {
        return super.content != null;
    }

    @Override
    public boolean isOnDisk() {
        return this.file != null && this.file.exists();
    }

    public String getPictureName() {
        return this.pictureName;
    }

    @Override
    public synchronized void recache(byte[] object, long ttl) {
        super.recache(object, ttl);
        this.length = object.length;

        if(this.file != null){
            this.file.delete();
            this.file = null;
        }
    }

    @Override
    public synchronized byte[] get() {
        byte[] content = this.content;
        if(content != null)
            return content;

        if(this.file != null){
            try {
                return Files.readAllBytes(this.file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        if(this.file != null)
            this.file.delete();

        super.finalize();
    }


    @Override
    public int hashCode() {
        return String.format("%s//%s", this.pictureName, this.albumName).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CachedPicture){
            CachedPicture picture = (CachedPicture) obj;
            return picture.pictureName.equals(this.pictureName) && picture.albumName.equals(this.albumName);
        }

        return super.equals(obj);
    }

    public String getAlbumName(){
        return this.albumName;
    }
}
