package sd.tp1.common.data;

/**
 * Created by apontes on 3/15/16.
 */ /*
 * The GUI expects objects representing albums to implement this interface.
 *
 * The implementing class can include any data the content provider might
 * find useful to associate with an album, such as the list of pictures that
 * it contains.
 */
public interface Album extends Metadata{
    String getName();

    default boolean equals(Album album){
        return this.getName().equals(album.getName());
    }
}
