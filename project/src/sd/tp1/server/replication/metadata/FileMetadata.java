package sd.tp1.server.replication.metadata;

import java.io.*;
import java.util.*;

/**
 * Created by apontes on 5/11/16.
 */
public class FileMetadata implements PersistenceMetadata {

    private final static File ROOT = new File(".metadata");

    private String identifier;
    private Version version;

    private File file;

    private boolean isDeleted = false;
    private Set<ResourceSource> sources = new LinkedHashSet<>();

    public FileMetadata(String identifier, ResourceSource source){
        this(ROOT, identifier, source);
    }

    public FileMetadata(File root, String identifier, ResourceSource source){
        this.identifier = identifier;
        this.file = new File(root, this.identifier);
        this.version = new LamportLogicalWatch(source.getServerId(), 0);
        this.load();
    }

    @Override
    public void store() {
        try{
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(this.file));

            out.writeObject(this);

            out.flush();
            out.close();
        }
        catch (IOException e){
            throw new PersistenceIOException(e);
        }

    }

    @Override
    public void load() {
        if(!this.file.exists())
            return;

        try{
            ObjectInput in = new ObjectInputStream(new FileInputStream(this.file));

            FileMetadata read = (FileMetadata) in.readObject();

            this.identifier = read.identifier;
            this.version = read.version;
            this.file = read.file;
            this.isDeleted = read.isDeleted;
            this.sources = read.sources;

            in.close();
        }
        catch (ClassCastException | ClassNotFoundException | IOException e){
            throw new PersistenceIOException(e);
        }
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public void incVersion() {
        this.version = this.version.next();
    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setDeleted(boolean isDeleted) {
        this.incVersion();
        this.isDeleted = isDeleted;
    }

    @Override
    public Set<ResourceSource> getSources() {
        return this.sources;
    }
}
