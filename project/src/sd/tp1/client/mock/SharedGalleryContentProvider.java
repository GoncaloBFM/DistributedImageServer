package sd.tp1.client.mock;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.client.cloud.HashServerManager;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.ServerHandler;
import sd.tp1.client.gui.Gui;
import sd.tp1.client.gui.GuiGalleryContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apontes on 3/21/16.
 */ /*
 * This class provides the album/picture content to the gui/main application.
 *
 * Project 1 implementation should complete this class.
 */
public class SharedGalleryContentProvider implements GuiGalleryContentProvider {

	Gui gui;

	SharedGalleryContentProvider() {
		// TODO: code to do when shared gallery starts
		HashServerManager.getServerManager().addServerHandler(new ServerHandler() {
			@Override
			public void serverAdded(Server server) {
				if(gui != null)
					gui.updateAlbums();
			}

			@Override
			public void serverLost(Server server) {
				if(gui != null)
					gui.updateAlbums();
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
		// TODO: obtain remote information
		List<Album> lst = new ArrayList<Album>();
		lst.add( new SharedAlbum("SD"));
		lst.add( new SharedAlbum("RC"));
		return lst;
	}

	/**
	 * Returns the list of pictures for the given album.
	 * On error this method should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) {
		// TODO: obtain remote information
		List<Picture> lst = new ArrayList<Picture>();
		lst.add( new SharedPicture("aula 1"));
		lst.add( new SharedPicture("aula 2"));
		lst.add( new SharedPicture("aula 3"));
		return lst;
	}

	/**
	 * Returns the contents of picture in album.
	 * On error this method should return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {
		// TODO: obtain remote information
		return null;
	}

	/**
	 * Create a new album.
	 * On error this method should return null.
	 */
	@Override
	public Album createAlbum(String name) {
		// TODO: contact servers to create album
		return new SharedAlbum(name);
	}

	/**
	 * Delete an existing album.
	 */
	@Override
	public void deleteAlbum(Album album) {
		// TODO: contact servers to delete album
	}

	/**
	 * Add a new picture to an album.
	 * On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {
		// TODO: contact servers to add picture name with contents data
		return new SharedPicture(name);
	}

	/**
	 * Delete a picture from an album.
	 * On error this method should return false.
	 */
	@Override
	public boolean deletePicture(Album album, Picture picture) {
		// TODO: contact servers to delete picture from album
		return true;
	}
}

