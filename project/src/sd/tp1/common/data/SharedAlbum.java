package sd.tp1.common.data;

import java.io.Serializable;

/**
 * Represents a shared album.
 */
public class SharedAlbum extends LogicClockMetadata implements Album, Serializable {
	private String name;

	public SharedAlbum(){}

	public SharedAlbum(String name, String serverId) {
		super(serverId);
		this.name = name;
	}

	public SharedAlbum(Album album){
		super(album.getAuthorId());
		this.name = album.getName();
		this.setVersion(album.getVersion());
		this.setDeleted(album.isDeleted());
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Album)
			return name.equals(((Album)obj).getName());

		return super.equals(obj);
	}
}

