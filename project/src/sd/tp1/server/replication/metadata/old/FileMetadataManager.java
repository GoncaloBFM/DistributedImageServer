package sd.tp1.server.replication.metadata.old;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.server.DataManager;
import sd.tp1.server.DataOperationHandler;
import sd.tp1.server.replication.metadata.ServerMetadata;

import java.io.File;

/**
 * Created by apontes on 5/13/16.
 */
public class FileMetadataManager implements MetadataManager {

    private static final File ROOT = new File(".metadata");

    private File root;
    private DataManager dataManager;
    private ServerMetadata source;

    public FileMetadataManager(ServerMetadata source, DataManager dataManager){
        this(source, dataManager, ROOT);
    }

    public FileMetadataManager(ServerMetadata source, DataManager dataManager, File root){
        this.source = source;
        this.dataManager = dataManager;
        this.root = root;

        this.dataManager.addDataOperationHandler(new DataOperationHandler() {
            @Override
            public void onPictureUpload(Album album, Picture picture) {
                PersistenceMetadata metadata = getMetadata(album, picture);
                metadata.setDeleted(false);
                metadata.store();
            }

            @Override
            public void onPictureDelete(Album album, Picture picture) {
                PersistenceMetadata metadata = getMetadata(album, picture);
                metadata.setDeleted(true);
                metadata.store();
            }

            @Override
            public void onAlbumCreate(Album album) {
                PersistenceMetadata metadata = getMetadata(album);
                metadata.setDeleted(false);
                metadata.store();
            }

            @Override
            public void onAlbumDelete(Album album) {
                PersistenceMetadata metadata = getMetadata(album);
                metadata.setDeleted(true);
                metadata.store();
            }
        });

    }

    private String albumIdentifier(Album album){
        return String.format("album: %s", album.getName());
    }

    private String pictureIdentifier(Album album, Picture picture){
        return String.format("picture: %s \\ %s", album.getName(), picture.getPictureName());
    }

    @Override
    public PersistenceMetadata buildMetadata(Album album) {
        PersistenceMetadata metadata = new FileMetadata(this.root, albumIdentifier(album), source);
        metadata.getSources().add(this.source);
        metadata.setVersion(new LamportLogicalWatch(this.source.getServerId()));

        return metadata;
    }

    @Override
    public PersistenceMetadata buildMetadata(Album album, Picture picture) {
        PersistenceMetadata metadata = new FileMetadata(this.root, pictureIdentifier(album, picture), source);
        metadata.getSources().add(this.source);
        metadata.setVersion(new LamportLogicalWatch(this.source.getServerId()));

        return metadata;
    }

    @Override
    public PersistenceMetadata buildAndStoreMetadata(Album album) {
        PersistenceMetadata metadata = this.buildMetadata(album);
        storeMetadata(metadata);

        return metadata;
    }

    @Override
    public PersistenceMetadata buildAndStoreMetadata(Album album, Picture picture) {
        PersistenceMetadata metadata = this.buildMetadata(album, picture);
        storeMetadata(metadata);

        return metadata;
    }

    @Override
    public PersistenceMetadata getMetadata(Album album) {
        PersistenceMetadata metadata = new FileMetadata(root, albumIdentifier(album), source);
        metadata.load();

        return metadata;
    }

    @Override
    public PersistenceMetadata getMetadata(Album album, Picture picture) {
        PersistenceMetadata metadata = new FileMetadata(root, pictureIdentifier(album, picture), source);
        metadata.load();

        return metadata;
    }

    @Override
    public void storeMetadata(PersistenceMetadata metadata) {
        metadata.store();
    }

    @Override
    public boolean needUpdate(Metadata metadata) {
        return new FileMetadata(this.root, metadata.getIdentifier(), source)
                .getVersion()
                .compareTo(metadata.getVersion()) < 0;
    }
}
