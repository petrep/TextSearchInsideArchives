package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainController {
	@FXML
	TextField searchedExpressionTextField;
	@FXML
	TextArea searchResultTextArea;
	@FXML
	Label selectedDirectoryLabel;
	String searchResultText;
	String searchPath;
	String realArchivePath;
	String displayedFullPath;
	String searchedExpression;
	String default_tmp;
	File appTempFolder;
	boolean canBeAddedToTheGoToResultList;
	boolean insideAnArchiveSearch;
	int counter;
	
	public void SelectSearchDirectory(){
		Stage stageTheLabelBelongs = (Stage) selectedDirectoryLabel.getScene().getWindow();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = 
						directoryChooser.showDialog(stageTheLabelBelongs);

		if(selectedDirectory == null){
			selectedDirectoryLabel.setText("No Directory selected");
		}else{
			selectedDirectoryLabel.setText(selectedDirectory.getAbsolutePath());
		}

//		For quicker testing, just uncomment the lines below:
//		selectedDirectoryLabel.setText("/home/peterpetrekanics/Downloads/temp4/");
//		searchedExpressionTextField.setText("JarScanner");
		
	}

	public void Search() throws IOException {
		if(searchedExpressionTextField.getText().equals("")) return;
		System.out.println("MainController starting");
		searchResultTextArea.setText("");
		searchResultTextArea.setWrapText(true);
		searchResultTextArea.appendText("Searching...");
		searchResultTextArea.appendText(System.getProperty("line.separator"));
//		searchResultText = "";
		canBeAddedToTheGoToResultList = true;

//		searchedExpression = "JarScanner";
		searchedExpression = searchedExpressionTextField.getText();
//		searchedExpression = "BinaryDecoder";
//		searchedExpression = "there-was-an-error-when-trying-to-validate-your-form";

//		searchPath = "/home/peterpetrekanics/Downloads/temp6/";
		// searchPath = "c:\\liferay\\canbedeleted\\";
//		searchPath = "/home/peter/tools/TextSearchInsideArchives";
		searchPath = selectedDirectoryLabel.getText();
		
		default_tmp = System.getProperty("java.io.tmpdir");
		appTempFolder = new File(default_tmp + System.getProperty("file.separator") + "TextSearchInsideArchives_cmd");

		// At program start, we delete our temporary folder
		recursiveDelete(appTempFolder);

		startSearch();
		
		// At program end, we delete our temporary folder
		recursiveDelete(appTempFolder);
		System.out.println("Program finished running");
		searchResultTextArea.appendText("Program finished running.");
	}

	private void startSearch() throws IOException {
		File mySearchPath = new File(searchPath);
		if (mySearchPath.exists()){
			displayedFullPath = searchPath;
//		System.out.println("displayedFullPath1: " + displayedFullPath);
			startSearchInAFolder(mySearchPath, searchedExpression, insideAnArchiveSearch);
		} else System.out.println("the search location is invalid");
	}
	
	private void startSearchInAFolder(File mySearchPath, String mySearchedExpression, boolean insideAnArchiveSearch) throws IOException {
		
		File[] folderContent = mySearchPath.listFiles();

		String mySearchPathString = mySearchPath.getPath() + System.getProperty("file.separator");
		System.out.println("mySearchPathString: " + mySearchPathString);
		if(insideAnArchiveSearch){
			System.out.println("insideAnArchiveSearch is true");
		} else {
			System.out.println("insideAnArchiveSearch is false");
		}
	
		for(File content:folderContent){
			String contentName = "";
			String extension = "";
			File tmpFolder = null;
			
			if (content.isDirectory()) {
				displayedFullPath = mySearchPathString + content.getName();
				System.out.println("looking in this directory: " + displayedFullPath);
				startSearchInAFolder(content, searchedExpression, insideAnArchiveSearch); // Calls same method again.
			} else {
				contentName = content.getName();
				extension = contentName.substring(contentName.lastIndexOf(".") + 1, contentName.length());
				if (extension.equals("zip")||extension.equals("jar")) {
					System.out.println("starting to handle an archive: " + content.getCanonicalPath());
					if(!insideAnArchiveSearch){
						realArchivePath = content.getAbsolutePath();
						System.out.println("realArchivePath: " + realArchivePath);
						insideAnArchiveSearch = true;
					}
					
					System.out.println("appTempFolder "+ appTempFolder);
					
//					Step 1 - extract
					counter++;
					tmpFolder = new File(appTempFolder.toString() + System.getProperty("file.separator") + counter);
					extractFiles(content, tmpFolder);
					
					// Step 2 - search inside exploded zip
					startSearchInAFolder(tmpFolder, mySearchedExpression, insideAnArchiveSearch);
					
					// Step 3 - delete exploded zip
//					recursiveDelete(tmpFolder);
					insideAnArchiveSearch = false;
					System.out.println("finished handling an archive");

				} else {
					displayedFullPath = mySearchPathString + content.getName();
//					System.out.println("displayedFullPath4: " + displayedFullPath);
					
//					canBeAddedToTheGoToResultList = true;
					searchTextWithinFile(content, searchedExpression, insideAnArchiveSearch, realArchivePath);
				}
			}
		}
	}
	
	public final void extractFiles(File jarFile, File destPath)
			        throws IOException {
		// PLEASE note:  this method does not support .tar, only: .zip and .jar
			JarInputStream jin = new JarInputStream(new FileInputStream(jarFile));
			ZipEntry entry = jin.getNextEntry();

			while (entry != null) {
				String fileName = entry.getName();
				File tmpFile = new File(fileName);
		
				File targetFile = new File(destPath.toString() + File.separator
				+ tmpFile.toString());
				File parent = targetFile.getParentFile();

				if (!parent.exists()) {
					parent.mkdirs();
				}
				if (!entry.isDirectory()) {
					targetFile.createNewFile();
//			targetFile.deleteOnExit();
					dumpFile(jin, targetFile);
//			targetFile.deleteOnExit();
				} else if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				jin.closeEntry();
				entry = jin.getNextEntry();
				}
			jin.close();
			}

	private final void dumpFile(JarInputStream jin, File targetFile) throws IOException {
		OutputStream out = new FileOutputStream(targetFile);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
	}
	
	
	public void searchTextWithinFile(File file, String searchedExpression, boolean insideAnArchiveSearch, String realArchivePath) throws IOException {
		
		String currentFileName = file.getName();
		System.out.println("Currently scanning this file: " + currentFileName);
		
			// First we check if the fileName itself contains the searched expression
		if(currentFileName.toLowerCase().contains(searchedExpression.toLowerCase())){
			System.out.println("I found " + searchedExpression + ", in the file: " + currentFileName + " on this path: ");
			searchResultTextArea.appendText("I found " + searchedExpression + ", in the file: " + currentFileName + " on this path: ");
			searchResultTextArea.appendText(System.getProperty("line.separator"));
			if(insideAnArchiveSearch){
				System.out.println(realArchivePath);
				searchResultTextArea.appendText(realArchivePath);
				searchResultTextArea.appendText(System.getProperty("line.separator"));
			} else {
				System.out.println(file.getCanonicalPath());
				searchResultTextArea.appendText(file.getCanonicalPath());
				searchResultTextArea.appendText(System.getProperty("line.separator"));
			}
		} else {
			Scanner scanner = new Scanner(file);
		//	searchResultTextArea.setWrapText(true);
			
			while (scanner.hasNextLine()) {
				final String lineFromFile = scanner.nextLine();
				if (lineFromFile.contains(searchedExpression)) {
//				Found a match!
//					searchResultTextArea.appendText("I found " + searchedExpression + " in file " + currentFileName);
//					searchResultTextArea.appendText(System.getProperty("line.separator"));
//					System.out.println("I found " + searchedExpression + " in file " + currentFileName);
					searchResultTextArea.appendText("I found '" + searchedExpression + "', and the full file name is:");
					searchResultTextArea.appendText(System.getProperty("line.separator"));
					System.out.println("I found " + searchedExpression + ", and the full file name is:");
					if(insideAnArchiveSearch){
						searchResultTextArea.appendText(realArchivePath);
						searchResultTextArea.appendText(System.getProperty("line.separator"));
						System.out.println(realArchivePath);
					} else {
						searchResultTextArea.appendText(file.getCanonicalPath());
						searchResultTextArea.appendText(System.getProperty("line.separator"));
						System.out.println(file.getCanonicalPath());
					}
//					searchResultText += "I found " + searchedExpression + " in file " + file.getCanonicalPath() + System.getProperty("line.separator");
//					searchResultTextArea.setText(searchResultText);
//					searchResultTextArea.setText(System.getProperty("line.separator"));
					break;
				}
			}
			scanner.close();
		}
	}

	public void recursiveDelete(File file) {
		//to end the recursive loop
		if (!file.exists()){
			System.out.println("the temp dir does not exist");
			return;
		}

//		if directory, go inside and call recursively
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
//				call recursively
				recursiveDelete(f);
			}
		}
//		call delete to delete files and empty directory
		file.delete();
		System.out.println("Deleted file/folder: "+file.getAbsolutePath());
	}
}
