package sd.tp1.server.replication.metadata;

/**
 * Created by apontes on 5/10/16.
 */
public class LamportLogicalWatch implements Version{

    protected int version;
    protected String id;

    LamportLogicalWatch(String id){
        this(id, 0);
    }

    LamportLogicalWatch(String id, int version){
        this.version = version;
        this.id = id;
    }

    @Override
    public Version next() {
        return new LamportLogicalWatch(this.id, this.version +1);
    }

    @Override
    public int compareTo(Version o) {
        try {
            LamportLogicalWatch x = (LamportLogicalWatch) o;
            return (this.version != x.version) ? this.version - x.version : this.id.compareTo(x.id);
        }
        catch(ClassCastException e){
            throw new IncompatibleVersionException();
        }
    }
}
