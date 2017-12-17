package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;

// TODO: the tool currently shows results from the tmp folder
// instead of showing the real folder location of the jar file
// TODO2: create a preferences file that holds the last searched folder and expression?
// TODO3: delete tmp on exit?
// TODO4: save 'tmp delete' as preference?
// TODO5: add support for zip, 7z, rar, arj etc.  Note looks like direct search inside zip works natively.
// TODO6: change the logic to run as a separate thread, maybe with progress bar or hourglass during search
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			Stage stage; 
		    Parent root;
			root = FXMLLoader.load(getClass().getResource("SearchGUIScene.fxml"));
			Scene scene = new Scene(root,1200,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
