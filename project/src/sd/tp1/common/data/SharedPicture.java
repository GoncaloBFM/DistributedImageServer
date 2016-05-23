package sd.tp1.common.data;

import sun.security.provider.SHA;

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

	@Override
	public int hashCode() {
		return pictureName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Picture)
			return pictureName.equals(((Picture) obj).getPictureName());

		return super.equals(obj);
	}
}
