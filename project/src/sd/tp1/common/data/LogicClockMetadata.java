package sd.tp1.common.data;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

/**
 * Created by apontes on 5/17/16.
 */
public class LogicClockMetadata implements Metadata, Serializable {

    private boolean deleted;
    private int version;
    private String authorId;

    public LogicClockMetadata(){
    }

    public LogicClockMetadata(String authorId){
        this.deleted = false;
        this.version = 0;
        this.authorId = authorId;
    }

    public boolean isDeleted(){
        return deleted;
    }

    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAuthorId() {
        return authorId;
    }

    @Override
    public void updateVersion(String authorId) {
        this.version++;
        this.authorId = authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public int compareTo(Metadata o) {
        if(o instanceof LogicClockMetadata) {
            LogicClockMetadata x = (LogicClockMetadata) o;
            if(this.version - x.version != 0)
                return this.version - x.version;

            return this.authorId.compareTo(x.authorId);
        }

        else
            throw new NotImplementedException();
    }
}

