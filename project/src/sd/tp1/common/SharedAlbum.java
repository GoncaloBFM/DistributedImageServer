package sd.tp1.common;

/**
 * Represents a shared album.
 */
public class SharedAlbum extends LogicClockMetadata implements Album {
	private String name;

	public SharedAlbum(){}

	public SharedAlbum(String name, String serverId) {
		super(serverId);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

