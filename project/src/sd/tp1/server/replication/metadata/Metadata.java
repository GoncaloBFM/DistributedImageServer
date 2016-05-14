package sd.tp1.server.replication.metadata;

import java.net.URL;
import java.util.Set;

/**
 * Created by apontes on 5/11/16.
 */
public interface Metadata {
    String getIdentifier();

    Version getVersion();
    void setVersion(Version version);

    void incVersion();

    boolean isDeleted();
    void setDeleted(boolean isDeleted);

    Set<ResourceSource> getSources();
}
