package sd.tp1.common;

/**
 * Represents a shared picture.
 */
public class SharedPicture implements Picture {
	public String pictureName;

	public SharedPicture(){}

	public SharedPicture(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getPictureName() {
		return pictureName;
	}
}
