package sd.tp1.common.data;

import java.util.List;

public interface GalleryContentProvider {

	/*
	 * Returns the list of available albums. The provider must filter any duplicates.
	 */
	public List<Album> getListOfAlbums();
	
	/*
	 * Returns the list of pictures belonging to the given album. The provider must filter any duplicates.
	 */
	public List<Picture> getListOfPictures(Album album );
	
	/*
	 * Returns the bitmap data of a given picture, in a given album.
	 */
	public byte[] getPictureData( Album album, Picture picture);
	
	/*
	 * Creates an album with the given name. 
	 * If null is returned the GUI ignores the result.
	 */
	public Album createAlbum( String name ) ;
	
	/*
	 * Requests the content provider to create a new picture, with a given name and data, to be stored in the given album.
	 * If null is return the GUI ignores the result.
	 */
	public Picture uploadPicture( Album album, String name, byte[] data ) ;
	
	/*
	 * Requests the content provider to delete an album.
	 */
	public void deleteAlbum( Album album );

	/*
	 * Requests the content provider to delete the given picture, in the given album.
	 * If true is returned, the GUI is updated and the picture is removed from the album view.
	 */
	public boolean deletePicture( Album album, Picture picture );

}
