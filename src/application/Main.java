package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;

// TODO: the tool currently shows results from the tmp folder
// instead of showing the real folder location of the jar file
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
