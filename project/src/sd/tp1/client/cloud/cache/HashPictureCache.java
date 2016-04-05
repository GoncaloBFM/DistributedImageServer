package sd.tp1.client.cloud.cache;

import sd.tp1.Album;
import sd.tp1.Picture;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by apontes on 4/5/16.
 */
public class HashPictureCache implements PictureCache{
    private static final int MAX_RAM = 100 * 1024 * 1024; //100MB

    private final Map<String, CachedPicture> pictureMap = new ConcurrentHashMap<>();
    //TODO confirm sort;
    private final TreeSet<CachedPicture> pictureRamList = new TreeSet<>((x, y) -> x.length() - y.length());

    private int ramLength = 0;
    private long diskLength = 0L;

    private Lock lock = new ReentrantLock();

    private String hashCode(Album album, Picture picture){
        return String.format("%s//%s", album.getName(), picture.getPictureName());
    }

    @Override
    public byte[] get(Album album, Picture picture) {
        CachedPicture cachedPicture = this.pictureMap.get(hashCode(album, picture));
        if(cachedPicture != null
                && cachedPicture.getPictureName().equals(picture.getPictureName())
                && cachedPicture.getAlbumName().equals(album.getName())
                && !cachedPicture.isDirty())

            return cachedPicture.get();

        return null;

    }

    @Override
    public void put(Album album, Picture picture, byte[] content) {
        CachedPicture cachedPicture = this.pictureMap.get(hashCode(album, picture));
        int diskDiff = 0;
        int ramDiff = 0;

        synchronized (this.pictureRamList) {
            if(cachedPicture == null){
                cachedPicture = new CachedPicture(album, picture, content);
                this.pictureMap.put(hashCode(album, picture), cachedPicture);
                this.pictureRamList.add(cachedPicture);

                ramDiff += cachedPicture.length();
            }
            else{
                if(cachedPicture.isOnDisk())
                    diskDiff -= cachedPicture.length();

                if(cachedPicture.isOnRam())
                    ramDiff -= cachedPicture.length();

                cachedPicture.recache(content);
                this.pictureRamList.add(cachedPicture);

                ramDiff += cachedPicture.length();
            }
        }

        this.lock.lock();
        this.ramLength += ramDiff;
        this.diskLength += diskDiff;
        this.lock.unlock();
    }

    @Override
    public void clear(Album album, Picture picture) {
        CachedPicture cachedPicture = this.pictureMap.remove(hashCode(album, picture));
        if(cachedPicture != null)
        synchronized (this.pictureRamList){
            this.pictureRamList.remove(cachedPicture);
        }
    }

    private void swap(int free){
        long diskDiff = 0;
        int ramDiff = 0;

        synchronized (this.pictureRamList){
            while(this.ramLength + free > MAX_RAM){
                CachedPicture cachedPicture = this.pictureRamList.pollFirst();
                if(cachedPicture == null)
                    return;

                cachedPicture.swap();
                diskDiff += cachedPicture.length();
                ramDiff -= cachedPicture.length();
            }
        }

        this.lock.lock();
        this.ramLength += ramDiff;
        this.diskLength += diskDiff;
        this.lock.unlock();
    }

    @Override
    public void clear() {
        this.pictureMap.clear();
        System.gc();
    }

    @Override
    public long length() {
        return this.lengthOnDisk() + this.lengthOnRam();
    }

    @Override
    public int lengthOnRam() {
        return this.ramLength;
    }

    @Override
    public long lengthOnDisk() {
        return this.diskLength;
    }
}
