package sd.tp1.server.soap;

import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 3/21/16.
 */
@WebService
public class SoapServer {

    private File root;

    public SoapServer() throws NotDirectoryException {
        this(new File("."));
    }

    public SoapServer(File root) throws NotDirectoryException {
        if(!root.isDirectory())
            throw new NotDirectoryException(root.getAbsolutePath());
        this.root = root;
    }

    @WebMethod
    public List<SharedAlbum> getListOfAlbums() {
        List<SharedAlbum> list = new LinkedList<>();
        for(File iFile : root.listFiles(x -> x.isDirectory() && !x.getName().contains(".delete")))
            list.add(new SharedAlbum(iFile.getName()));

        return list;
    }

    @WebMethod
    public List<SharedPicture> getListOfPictures(SharedAlbum album) {
        List<SharedPicture> list = new LinkedList<>();
        for(File iFile : openAlbum(album).listFiles(x -> x.isDirectory() && !x.getName().contains(".delete")))
            list.add(new SharedPicture(iFile.getName()));

        return list;

    }

    @WebMethod
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture) throws IOException {
        return Files.readAllBytes(openPicture(album, picture).toPath());
    }

    @WebMethod
    public SharedAlbum createAlbum(String name) throws IOException {
        File folder = new File(root, name);
        if(folder.mkdir()){
            return new SharedAlbum(folder.getName());
        }
        else {
            throw new IOException();
        }
    }

    @WebMethod
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data) throws IOException {
        Files.write(openPicture(album, name).toPath(), data);
        return new SharedPicture(name);
    }

    @WebMethod
    public void deleteAlbum(SharedAlbum album) throws IOException {
        File folder = openAlbum(album);
        if (!folder.renameTo(new File(folder.getAbsolutePath() + ".delete")));
            throw new IOException();
    }

    @WebMethod
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) throws IOException {
        File file = openPicture(album, picture);
        if( !file.renameTo(new File(file.getAbsolutePath() + ".delete")));
            throw new IOException();
    }

    private File openAlbum(SharedAlbum album){
        return this.openAlbum(album.getName());
    }

    private File openAlbum(String albumName){
        return new File(root, albumName);
    }

    private File openPicture(String albumName, String pictureName){
        return new File(openAlbum(albumName), pictureName);
    }

    private File openPicture(SharedAlbum album, String pictureName){
        return openPicture(album.getName(), pictureName);
    }

    private File openPicture(SharedAlbum album, SharedPicture picture){
        return openPicture(album.getName(), picture.getName());
    }
}
