package sd.tp1.client.gui;

import sd.tp1.common.GalleryContentProvider;

/*
 * The GUI will request content to display from an object
 * implementing this interface.
 * 
 * Down calls allow the GUI to request data and delete albums/picture or
 * create new ones.
 * 
 * The GUI provides up calls (Gui interface) to allow the content provider
 * to update the GUI programmatically without user intervention.
 * 
 */
public interface GuiGalleryContentProvider extends GalleryContentProvider{

	/*
	 * Registers the GUI in the content provider to allow
	 * the provider to update the GUI programmatically (cf. Gui interface)
	 */
	public void register(Gui gui);

}
