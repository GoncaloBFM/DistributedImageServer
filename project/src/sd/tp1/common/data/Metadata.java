package sd.tp1.common.data;

/**
 * Created by everyone on 5/17/16.
 */
public interface Metadata extends Comparable<Metadata> {

    boolean isDeleted();
    void setDeleted(boolean delete);

    int getVersion();

    String getAuthorId();
    void updateVersion(String authorId);
}
