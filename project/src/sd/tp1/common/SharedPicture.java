package sd.tp1.common;

import java.io.Serializable;

/**
 * Represents a shared picture.
 */
public class SharedPicture extends LogicClockMetadata implements Picture, Serializable {

	private String pictureName;

	public SharedPicture(){}

	public SharedPicture(String pictureName, String serverId)
	{
		super(serverId);
		this.pictureName = pictureName;
	}

	public SharedPicture(Picture picture){
		super(picture.getAuthorId());

		pictureName = picture.getPictureName();
		setVersion(picture.getVersion());
		setDeleted(picture.isDeleted());
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
}
