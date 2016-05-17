package sd.tp1.common;

/**
 * Represents a shared picture.
 */
public class SharedPicture extends LogicClockMetadata implements Picture {

	private String pictureName;

	public SharedPicture(){}

	public SharedPicture(String pictureName, String serverId)
	{
		super(serverId);
		this.pictureName = pictureName;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
}
