package sd.tp1.client.cloud;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Picture;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;
import sd.tp1.client.gui.Gui;
import sd.tp1.client.gui.GuiGalleryContentProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
	Map<String, CloudAlbum> albumsMap;

	CloudClient(){
		HashServerManager.getServerManager().addServerHandler(new ServerHandler() {
			@Override
			public void serverAdded(Server server) {
				gui.updateAlbums();
			}

			@Override
			public void serverLost(Server server) {
				if(serverAlbumMap != null){
					List<CloudAlbum> albums = serverAlbumMap.get(server);
					for(CloudAlbum album : albums) {
						album.remServer(server);
						if(album.getServers().size() <= 0) {
							gui.updateAlbums();
							return;
						}
					}
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
		HashMap<String, CloudAlbum> albums = new HashMap<>();
		Map<Server, List<CloudAlbum>> albumMap = new ConcurrentHashMap<>();
		albumsMap = new ConcurrentHashMap<>();

		Collection<Server> servers = HashServerManager.getServerManager().getServers();
		for(Server s : servers){
			//Verify if should be a Set
			List<Album> albumList = s.loadListOfAlbums();
			if(albumList == null)
				continue;

			List<CloudAlbum> albumServer = new LinkedList<>();
			for(Album album : albumList){
				CloudAlbum cloudAlbum = albums.get(album.getName());

				if(cloudAlbum == null){
					cloudAlbum = new CloudAlbum(album.getName(), album.getAuthorId());
					cloudAlbum.setVersion(album.getVersion());
					cloudAlbum.setDeleted(album.isDeleted());

					albums.put(album.getName(), cloudAlbum);
					this.albumsMap.put(album.getName(), cloudAlbum);
				}
				else if(cloudAlbum.compareTo(album) <0){
					cloudAlbum.setVersion(album.getVersion());
					cloudAlbum.setAuthorId(album.getAuthorId());
					cloudAlbum.setDeleted(album.isDeleted());
				}

				albumServer.add(cloudAlbum);
				cloudAlbum.addServer(s);
			}

			albumMap.put(s, albumServer);
		}

		this.serverAlbumMap = albumMap;

		return albums.values().parallelStream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
	}

	/**
	 * Returns the list of pictures for the given album.
	 * On error this method should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) {
		Map<String, CloudPicture> pictureMap = new HashMap<>();

		CloudAlbum cloudAlbum = (CloudAlbum) album;

		if(cloudAlbum.getServers().size() <= 0){
			gui.updateAlbums();
			return Collections.EMPTY_LIST;
		}

		for(Server s : cloudAlbum.getServers()){
			List<Picture> pictures = s.loadListOfPictures(album.getName());
			if(pictures == null)
				continue;

			for(Picture p : pictures){
				CloudPicture cloudPicture = pictureMap.get(p.getPictureName());

				if(cloudPicture == null){
					cloudPicture = new CloudPicture(p.getPictureName(), cloudAlbum, p.getAuthorId());
					cloudPicture.setVersion(p.getVersion());
					cloudPicture.setDeleted(p.isDeleted());

					pictureMap.put(p.getPictureName(), cloudPicture);
					cloudAlbum.getPictures().add(cloudPicture);
				}
				else if(cloudPicture.compareTo(p) < 0){
					cloudPicture.setVersion(p.getVersion());
					cloudPicture.setAuthorId(p.getAuthorId());
					cloudPicture.setDeleted(p.isDeleted());
				}

				cloudPicture.addServer(s);
			}
		}

		return pictureMap.values().parallelStream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
	}

	/**
	 * Returns the contents of picture in album.
	 * On error this method should return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {
		CloudPicture cloudPicture = (CloudPicture) picture;

		for(Server s : cloudPicture.getServers()){
			byte[] data = s.loadPictureData(album.getName(), picture.getPictureName());
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
		CloudAlbum album = this.albumsMap.get(name);
		if(album != null && !album.isDeleted())
			return null;

		Iterator<Server>  list = HashServerManager.getServerManager().getServerToCreateAlbum().iterator();

		int i = 0;
		while (list.hasNext() &&  i <NUMBER_OF_TRIES) {
			i++;
			Server server = list.next();
			//Album album = server.createAlbum(name);
			String serverId = server.getServerId();
			if(serverId == null)
				continue;

			if(album == null)
				album = new CloudAlbum(name, serverId);
			else {
				album.setDeleted(false);
				album.updateVersion(serverId);
			}

			if(server.createAlbum(album)) {
				album.addServer(server);
				albumsMap.put(album.getName(), album);
				return album;
			}
		}

		return null;
	}

	/**
	 * Delete an existing album.
	 */
	@Override
	public void deleteAlbum(Album album) {
		CloudAlbum cloudAlbum = (CloudAlbum) album;

		if(album.isDeleted()) {
			this.albumsMap.remove(album);
			return;
		}

		album.setDeleted(true);

		boolean first = true;
		for(Server s : cloudAlbum.getServers()){
			if(first) {
				album.updateVersion(s.getServerId());
				first = false;
			}

			s.deleteAlbum(album);
		}


		this.albumsMap.remove(album);
	}

	/**
	 * Add a new picture to an album.
	 * On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {
		if(album == null || album.isDeleted() || data == null)
			return null;

		CloudAlbum cloudAlbum = (CloudAlbum) album;
		CloudPicture picture = null;
		for(CloudPicture p : cloudAlbum.getPictures())
			if(p.getPictureName().equals(name)) {
				picture = p;
				break;
			}

		//Already uploaded
		//if(picture != null && !picture.isDeleted())
		//	return null;

		Collection<Server> availableServers =  HashServerManager.getServerManager().getServerToUploadPicture(cloudAlbum);

		for (Server server : availableServers) {
			String serverId = server.getServerId();
			if(serverId == null)
				continue;

			if(picture == null){
				picture = new CloudPicture(name, cloudAlbum, serverId);
			}
			else{
				picture.setDeleted(false);
				picture.updateVersion(serverId);
			}

			if(server.uploadPicture(album, picture, data)) {
				picture.addServer(server);
				cloudAlbum.addServer(server);
				cloudAlbum.getPictures().add(picture);
				return picture;
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
		if(album == null || picture == null)
			return false;

		boolean del = true;

		//CloudAlbum cloudAlbum = (CloudAlbum) album;
		//cloudAlbum.getPictures().remove(picture);

		//if(picture.isDeleted())
		//	return false;

		boolean first = true;
		for(Server s : ((CloudPicture) picture).getServers()) {
			if(first){
				picture.setDeleted(true);
				picture.updateVersion(s.getServerId());
				first = false;
			}

			del = s.deletePicture(album, picture) && del;
		}

		return del;
	}
}

