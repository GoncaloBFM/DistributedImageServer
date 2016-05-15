package sd.tp1.server.replication.metadata;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.server.DataManager;
import sd.tp1.server.DataOperationHandler;

import java.io.*;
import java.util.UUID;

/**
 * Created by apontes on 5/15/16.
 */
public class FileMetadataManager implements MetadataManager {

    private final static File METADATA_ROOT = new File(".metadata");

    private final static String METADATA_EXT = ".meta";
    private final static String SERVER_METADATA_FILE = "server";

    private final static String ALBUM_METADATA = "album-%s";
    private final static String PICTURE_METADATA = "picture-%s\\%s";

    private File root = METADATA_ROOT;

    private DataManager dataManager;
    private ServerMetadata serverMetadata;

    public FileMetadataManager(DataManager dataManager, File root){
        this.root = root;
        this.dataManager = dataManager;

        dataManager.addDataOperationHandler(new DataOperationHandler() {
            @Override
            public void onPictureUpload(Album album, Picture picture){
                Metadata meta = getOrDefault(getMetadata(album, picture), buildMetadata(album, picture));
                meta.setDeleted(false);

                setMetadata(album, picture, meta);
            }

            @Override
            public void onPictureDelete(Album album, Picture picture) {
                Metadata meta = getMetadata(album, picture);
                meta.setDeleted(true);

                setMetadata(album, picture, meta);
            }

            @Override
            public void onAlbumCreate(Album album) {
                Metadata meta = getOrDefault(getMetadata(album), buildMetadata(album));
                meta.setDeleted(false);

                setMetadata(album, meta);
            }

            @Override
            public void onAlbumDelete(Album album) {
                Metadata meta = getMetadata(album);
                meta.setDeleted(true);

                setMetadata(album, meta);

            }
        });
    }

    private static <T> T getOrDefault(T elem, T ifNull){
        return elem == null ? ifNull : elem;
    }

    private static String buildIdentifier(Album album, Picture picture){
        return String.format(PICTURE_METADATA, album.getName(), picture.getPictureName());
    }

    private static String buildIdentifier(Album album){
        return String.format(ALBUM_METADATA, album);
    }

    private Metadata buildMetadata(Album album, Picture picture){
        return new Metadata(buildIdentifier(album, picture), this.serverMetadata);
    }

    private Metadata buildMetadata(Album album){
        return new Metadata(buildIdentifier(album), this.serverMetadata);
    }

    private File serverFile(){
        return new File(this.root, SERVER_METADATA_FILE);
    }

    private File albumFile(Album album){
        return new File(this.root,
                buildIdentifier(album) + METADATA_EXT);
    }

    private File pictureFile(Album album, Picture picture){
        return new File(this.root,
                buildIdentifier(album, picture) + METADATA_EXT);
    }

    private ServerMetadata getServerMetadata(){
        if(this.serverMetadata != null)
            return this.serverMetadata;

        try {
            File file = serverFile();
            if(!file.exists()){
                ServerMetadata meta = new ServerMetadata(UUID.randomUUID().toString());
                this.setServerMetadata(meta);
                return meta;
            }
            else{
                ObjectInput in = new ObjectInputStream(new FileInputStream(file));
                ServerMetadata meta = (ServerMetadata) in.readObject();
                in.close();

                return meta;
            }
        } catch (FileNotFoundException e) {
            //Already checked
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            //No res possible
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            //No res possible
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setServerMetadata(ServerMetadata meta){
        File file = serverFile();
        writeMetadata(file, meta);
    }

    @Override
    public Metadata getMetadata(Album album) {
        return readMetadata(albumFile(album));
    }

    @Override
    public Metadata getMetadata(Album album, Picture picture) {
        return readMetadata(pictureFile(album, picture));
    }

    @Override
    public void setMetadata(Album album, Metadata metadata) {
        writeMetadata(albumFile(album), metadata);
    }

    @Override
    public void setMetadata(Album album, Picture picture, Metadata metadata) {
        writeMetadata(pictureFile(album, picture), metadata);
    }

    private void writeMetadata(File file, Object meta){
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(meta);
            out.flush();
            out.close();

        } catch (IOException e) {
            //No res possible
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Metadata readMetadata(File file){
        if(!file.exists())
            return null;

        try{
            ObjectInput in = new ObjectInputStream(new FileInputStream(file));
            return (Metadata) in.readObject();

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
