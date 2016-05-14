package sd.tp1.server.replication.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.discovery.ServiceAnnouncer;
import sd.tp1.server.DataManager;
import sd.tp1.server.DataOperationHandler;
import sd.tp1.server.replication.metadata.FileMetadataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.metadata.ResourceSource;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.UUID;

/**
 * Created by apontes on 5/13/16.
 */
public class UDPReplicableServer {

    private static File ROOT = new File(".metadata");
    private static ResourceSource getResourceSource() throws IOException {
        try {
            DataInput input = new DataInputStream(new FileInputStream(new File(ROOT, ".server-id")));

            return new ResourceSource(input.readLine());
        } catch (FileNotFoundException e) {
            PrintStream output = new PrintStream(new FileOutputStream(new File(ROOT, ".server-id")));
            String serverId = UUID.randomUUID().toString();
            output.println(serverId);
            output.flush();
            output.close();

            return new ResourceSource(serverId);
        }
    }

    private ResourceSource source;
    private File root;

    private DataManager dataManager;
    private MetadataManager metadataManager;

    UDPReplicableServer(DataManager dataManager) throws IOException {
        this(ROOT, getResourceSource(), dataManager);
    }

    UDPReplicableServer(File root, ResourceSource source, DataManager dataManager){
        this.source = source;
        this.dataManager = dataManager;
        this.metadataManager = new FileMetadataManager(source, dataManager, root);


    }
}
