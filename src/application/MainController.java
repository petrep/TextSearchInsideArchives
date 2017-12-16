package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {
	@FXML
	TextArea searchResultTextArea;
	String searchResultText;

	public void Search() throws IOException {
		System.out.println("MainController starting");
		searchResultTextArea.setText("executing MainController");
		searchResultText = "";

		// String searchedExpression = "BinaryDecoder";
		String searchedExpression = "there-was-an-error-when-trying-to-validate-your-form";

		// String searchPath = "c:\\liferay\\canbedeleted\\";
		String searchPath = "/home/peter/tools/TextSearchInsideArchives";
		
		String default_tmp = System.getProperty("java.io.tmpdir");
        System.out.println("tmp "+default_tmp);
        String folder = default_tmp + System.getProperty("file.separator") + "TextSearchInsideArchives";
        System.out.println("folder: " + folder);
        
        // At program start, we delete our temporary folder
        recursiveDelete(new File(folder));

		File[] files = new File(searchPath).listFiles();
		searchFiles(files, searchedExpression);

		

		
		
        // At program end, we delete our temporary folder
//        recursiveDelete(new File(folder));

	}

	private void searchFiles(File[] files, String searchedExpression) throws IOException {
		for (File file : files) {
			String fileName = file.getName();
			String extension = "";
			boolean canBeAddedToTheGoToResultList;
			if (file.isDirectory()) {
				System.out.println("Directory: " + fileName);
				searchFiles(file.listFiles(), searchedExpression); // Calls same
																	// method
																	// again.
			} else {
//				System.out.println("File: " + file.getCanonicalPath());

				extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

				if (extension.equals("jar")) {
					System.out.println("starting to handle a jar: " + file.getCanonicalPath());
					canBeAddedToTheGoToResultList = false;
					
					String default_tmp = System.getProperty("java.io.tmpdir");
			        System.out.println("tmp "+default_tmp);
			        File myTempFolder = new File(default_tmp + System.getProperty("file.separator") + "TextSearchInsideArchives");
					
					extractFiles(file, myTempFolder, searchedExpression);

//					while (jarContents.hasMoreElements()) {
//						JarEntry jarSubFile = jarContents.nextElement();
						// System.out.println("Jar Element = " + jarSubFile);
//						File jarSubFile = extract(jarContents.nextElement().toString()); 
						// searchTextWithinFile(jarSubFile.,
						// searchedExpression);
//						System.out.println("KK" + jarSubFile.getName());
//						searchTextWithinFile(jarSubFile, searchedExpression);
//					}
					System.out.println("finished handling a jar");

				}
				
//				canBeAddedToTheGoToResultList = true;
//				searchTextWithinFile(file, searchedExpression);
			}
		}

	}

	public void searchTextWithinFile(File file, String searchedExpression) throws IOException {
		Scanner scanner = new Scanner(file);
		
		searchResultTextArea.setWrapText(true);
//		System.out.println("FILENAME: " + file.getName());
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains(searchedExpression)) {
				// a match!
				System.out.println("I found " + searchedExpression + " in file " + file.getName());
				searchResultText += "I found " + searchedExpression + " in file " + file.getCanonicalPath() + System.getProperty("line.separator");
				searchResultTextArea.setText(searchResultText);
//				TextArea.setText(System.getProperty("line.separator"));
				break;
			}
		}
		scanner.close();

	}

	public File extract(String filePath) {
		System.out.println("extracting filePath: " + filePath);
		try {
			File f = File.createTempFile(filePath, null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			InputStream classIS = getClass().getClassLoader().getResourceAsStream(filePath);
			// While the input stream has bytes
			while ((i = classIS.read(byteArray)) > 0) {
				// Write the bytes to the output stream
				resourceOS.write(byteArray, 0, i);
			}
			// Close streams to prevent errors
			classIS.close();
			resourceOS.close();
			return f;
		} catch (Exception e) {
			System.out.println(
					"Error occurred.\nError Description:\n"
							+ e.getMessage());
			return null;
		}
	}
	
	public static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists()){
        	System.out.println("the temp dir does not exist");
            return;
        }
        
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
        System.out.println("Deleted file/folder: "+file.getAbsolutePath());
    }
	
	public final void extractFiles(File jarFile, File destPath, String searchedExpression)
	        throws IOException {
	    JarInputStream jin = new JarInputStream(new FileInputStream(jarFile));
	    ZipEntry entry = jin.getNextEntry();

	    while (entry != null) {
	        String fileName = entry.getName();
	        String entryExtension = "";
	        File tmpFile = new File(fileName);

	            File targetFile = new File(destPath.toString()
	                    + File.separator
	                    + tmpFile.toString());
	            File parent = targetFile.getParentFile();

	            if (!parent.exists()) {
	                parent.mkdirs();
	            }
	            if (!entry.isDirectory()) {
	                targetFile.createNewFile();
//	                targetFile.deleteOnExit();
	                dumpFile(jin, targetFile);
	                entryExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
					if (entryExtension.equals("jar")) {
						System.out.println("JAAAR" + entry.getName());
						extractFiles(targetFile, destPath, searchedExpression);
					} else {
						searchTextWithinFile(targetFile, searchedExpression);
					}

//	                targetFile.deleteOnExit();
	            } else if (!targetFile.exists()) {
	                targetFile.mkdirs();
	            }
	            jin.closeEntry();
	        entry = jin.getNextEntry();
	    }
	    jin.close();
	}
	
	private static final void dumpFile(JarInputStream jin, File targetFile) throws IOException { 
        OutputStream out = new FileOutputStream(targetFile); 
        byte[] buffer = new byte[1024]; 
        int len = 0; 
        while ((len = jin.read(buffer, 0, buffer.length)) != -1) { 
            out.write(buffer, 0, len); 
        } 
        out.flush(); 
        out.close(); 
    } 

}
