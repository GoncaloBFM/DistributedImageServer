package sd.tp1.client.cloud;

import javafx.application.Application;
import javafx.stage.Stage;
import sd.tp1.client.gui.impl.GalleryWindow;

/*
 * Launches the local shared gallery application.
 */
public class CloudClientRun extends Application {

	GalleryWindow window;
	
	public CloudClientRun() {
		window = new GalleryWindow( new CacheCloudClient());
	}	
	
	
    public static void main(String[] args){
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		window.start(primaryStage);
	}
}
