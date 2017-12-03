package application;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class MainController {
	@FXML
	TextArea TextArea;
	public void Search() throws FileNotFoundException{
		System.out.println("MainController starting");
		TextArea.setText("executing MainController");
		
		String searchedExpression = "BinaryDecoder";
		
		File[] files = new File("c:\\liferay\\canbedeleted\\").listFiles();
		searchFiles(files, searchedExpression);
		
		

	}
	private void searchFiles(File[] files, String searchedExpression) throws FileNotFoundException {
		for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            searchFiles(file.listFiles(), searchedExpression); // Calls same method again.
	        } else {
	            System.out.println("File: " + file.getName());
	            
	            Scanner scanner = new Scanner(file);
	    		while (scanner.hasNextLine()) {
	    		   final String lineFromFile = scanner.nextLine();
	    		   if(lineFromFile.contains(searchedExpression)) { 
	    		       // a match!
	    		       System.out.println("I found " +searchedExpression+ " in file " +file.getName());
	    		       break;
	    		   }
	    		}
	        }
	    }
		
		
	}
}
