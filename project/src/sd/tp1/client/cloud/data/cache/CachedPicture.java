package sd.tp1.client.cloud.data.cache;

import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;

import java.io.*;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 * Created by apontes on 4/3/16.
 */
public class CachedPicture extends CloudPicture implements Cached{

    private Cached cached = new CachedItem();
    private byte[] content;

    private CachedAlbum album;

    private File diskFile;

    public CachedPicture(String name, CachedAlbum album, byte[] content) {
        super(name, album);

        this.content = content;
        this.album = album;
    }

    @Override
    public boolean isDirty() {
        return cached.isDirty();
    }

    @Override
    public long ttl() {
        return cached.ttl();
    }

    @Override
    public long creation() {
        return cached.creation();
    }

    @Override
    public void makeDirty() {
        this.cached.makeDirty();
    }

    public void recache(byte[] content){
        this.content = content;

        if(diskFile != null) {
            diskFile.delete();
            diskFile = null;
        }
    }

    public int length(){
        if(content == null)
            return 0;

        return this.content.length;
    }

    public byte[] getContent(){
        if(this.isDirty())
            return null;

        if(this.content != null)
            return this.content;

        if(this.diskFile == null)
            return null;

        try{
            byte[] arr = new byte[(int) this.diskFile.length()];
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(this.diskFile));
            reader.read(arr, 0, arr.length);
            return arr;
        }
        catch (IOException e){
            return null;
        }
    }

    public void swap(File file) {
        if(this.content == null || this.diskFile != null)
            return;

        try{
            file.createNewFile();
            file.deleteOnExit();
            this.diskFile = file;

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(this.content, 0, this.content.length);
            out.close();

            this.content = null;
        }
        catch (IOException e){
            return;
        }
    }

    @Override
    public CachedAlbum getAlbum(){
        return this.album;
    }

    @Override
    protected void finalize() throws Throwable {
        if(this.diskFile != null)
            this.diskFile.delete();

        super.finalize();
    }
}
