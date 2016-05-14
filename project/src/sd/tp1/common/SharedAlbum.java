package sd.tp1.common;

/**
 * Represents a shared album.
 */
public class SharedAlbum implements Album {
	public String name;

	public SharedAlbum(){}

	public SharedAlbum(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
