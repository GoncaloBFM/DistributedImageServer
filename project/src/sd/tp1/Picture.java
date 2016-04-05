package sd.tp1;

/**
 * Created by apontes on 3/15/16.
 */ /*
 * The GUI expects objects representing pictures to implement this interface.
 *
 * The implementing class can include any data the content provider might
 * find useful to associate with a picture, for instance its bitmap data or
 * where to get it from.
 */
public interface Picture {
    String getPictureName();
}
