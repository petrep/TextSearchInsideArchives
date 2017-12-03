package application;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class MainController {
	@FXML
	TextArea TextArea;
	public void Search(){
		System.out.println("MainController starting");
		TextArea.setText("executing MainController");
		
		
		
	}
}
