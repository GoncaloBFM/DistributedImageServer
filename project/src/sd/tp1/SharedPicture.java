package sd.tp1;

/**
 * Represents a shared picture.
 */
public class SharedPicture implements Picture {
	public String name;

	public SharedPicture(){}

	public SharedPicture(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
