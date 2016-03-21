package sd.tp1;

/**
 * Represents a shared album.
 */
public class SharedAlbum implements Album {
	String name;

	public SharedAlbum(){}

	public SharedAlbum(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
