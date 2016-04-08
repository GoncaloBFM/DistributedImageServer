package sd.tp1.client.cloud;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;
import sd.tp1.client.gui.Gui;
import sd.tp1.client.gui.GuiGalleryContentProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by apontes on 3/21/16.
 */ /*
 * This class provides the album/picture content to the gui/main application.
 *
 * Project 1 implementation should complete this class.
 */
public class CloudClient implements GuiGalleryContentProvider {
	private static int NUMBER_OF_TRIES = 5;
	protected Gui gui;

	Map<Server, List<CloudAlbum>> serverAlbumMap;
	Set<String> albumSet;

	CloudClient() {
		this(true);
	}

	CloudClient(boolean guiHandler){
		if(guiHandler) {
			HashServerManager.getServerManager().addServerHandler(new ServerHandler() {
				@Override
				public void serverAdded(Server server) {
					gui.updateAlbums();
				}

				@Override
				public void serverLost(Server server) {
					gui.updateAlbums();
				}
			});
		}

		HashServerManager.getServerManager().addServerHandler(new ServerHandler() {
			@Override
			public void serverAdded(Server server) {
				//
			}

			@Override
			public void serverLost(Server server) {
				if(serverAlbumMap != null){
					List<CloudAlbum> albums = serverAlbumMap.get(server);
					for(CloudAlbum album : albums)
						album.remServer(server);
				}
			}
		});
	}


	/**
	 *  Downcall from the GUI to register itself, so that it can be updated via upcalls.
	 */
	@Override
	public void register(Gui gui) {
		if( this.gui == null ) {
			this.gui = gui;
		}
	}

	/**
	 * Returns the list of albums in the system.
	 * On error this method should return null.
	 */
	@Override
	public List<Album> getListOfAlbums() {
		List<Album> lst = new LinkedList<>();
		HashMap<String, CloudAlbum> albums = new HashMap<>();
		Map<Server, List<CloudAlbum>> albumMap = new ConcurrentHashMap<>();

		Collection<Server> servers = HashServerManager.getServerManager().getServers();
		servers.size();
		for(Server s : servers){
			//Verify if should be a Set
			List<Album> albumList = s.getListOfAlbums();
			if(albumList == null)
				continue;

			List<CloudAlbum> albumServer = new LinkedList<>();
			for(Album album : albumList){
				CloudAlbum cloudAlbum = albums.get(album.getName());

				if(cloudAlbum == null){
					cloudAlbum = new CloudAlbum(album.getName());
					albums.put(album.getName(), cloudAlbum);
					this.albumSet.add(album.getName());
					lst.add(cloudAlbum);
				}

				albumServer.add(cloudAlbum);
				cloudAlbum.addServer(s);
			}

			albumMap.put(s, albumServer);
		}

		this.serverAlbumMap = albumMap;
		return lst;
	}

	/**
	 * Returns the list of pictures for the given album.
	 * On error this method should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) {
		List<Picture> lst = new LinkedList<>();
		Map<String, CloudPicture> pictureMap = new HashMap<>();

		CloudAlbum cloudAlbum = (CloudAlbum) album;
		for(Server s : cloudAlbum.getServers()){
			List<Picture> pictures = s.getListOfPictures(album);
			if(pictures == null)
				continue;

			for(Picture p : pictures){
				CloudPicture cloudPicture = pictureMap.get(p.getPictureName());

				if(cloudPicture == null){
					cloudPicture = new CloudPicture(p.getPictureName(), cloudAlbum);
					pictureMap.put(p.getPictureName(), cloudPicture);
					cloudAlbum.getPictures().add(cloudPicture);
					lst.add(cloudPicture);
				}

				cloudPicture.addServer(s);
			}
		}

		return lst;
	}

	/**
	 * Returns the contents of picture in album.
	 * On error this method should return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {
		CloudPicture cloudPicture = (CloudPicture) picture;

		for(Server s : cloudPicture.getServers()){
			byte[] data = s.getPictureData(album, picture);
			if(data != null)
				return data;
		}

		return null;
	}

	/**
	 * Create a new album.
	 * On error this method should return null.
	 */
	@Override
	public Album createAlbum(String name) {
		if(this.albumSet.contains(name))
			return null;

		Iterator<Server>  list = HashServerManager.getServerManager().getServerToCreateAlbum().iterator();

		int i = 0;
		CloudAlbum cloudAlbum = null;
		while (list.hasNext() &&  i <NUMBER_OF_TRIES) {
			i++;
			Server server = list.next();
			Album album = server.createAlbum(name);
			if(album != null) {
				cloudAlbum = new CloudAlbum(album.getName());
				cloudAlbum.addServer(server);
				break;
			}
		}

		return cloudAlbum;

	}

	/**
	 * Delete an existing album.
	 */
	@Override
	public void deleteAlbum(Album album) {
		CloudAlbum cloudAlbum = (CloudAlbum) album;

		for(Server s : cloudAlbum.getServers()){
			s.deleteAlbum(album);
		}

		this.albumSet.remove(album);
	}

	/**
	 * Add a new picture to an album.
	 * On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {
		CloudAlbum cloudAlbum = (CloudAlbum) album;
		if(cloudAlbum.containsPicture(name))
			return null;

		Collection<Server> availableServers =  HashServerManager.getServerManager().getServerToUploadPicture(cloudAlbum);

		for (Server server : availableServers) {
			Picture p = server.uploadPicture(album, name, data);
			if(p != null){
				CloudPicture cloudPicture = new CloudPicture(p.getPictureName(), cloudAlbum);
				cloudPicture.addServer(server);
				cloudAlbum.getPictures().add(cloudPicture);
				return cloudPicture;
			}
		}

		return null;
	}

	/**
	 * Delete a picture from an album.
	 * On error this method should return false.
	 */
	@Override
	public boolean deletePicture(Album album, Picture picture) {
		boolean del = true;

		CloudAlbum cloudAlbum = (CloudAlbum) album;
		cloudAlbum.getPictures().remove(picture);

		CloudPicture cloudPicture = (CloudPicture) picture;
		for(Server s : cloudPicture.getServers())
			del = s.deletePicture(album, picture) && del;

		return del;
	}
}

