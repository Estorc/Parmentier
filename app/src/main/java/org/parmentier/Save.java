package org.parmentier;

import org.parmentier.game.Game;

import java.io.File;        // Import the File class

import java.io.IOException; // Import IOException to handle errors
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile; // for the seek method()
import java.io.InputStream;

/*
 * a '.sav' file will look like : 
 * -levelName-;-built-save-\n
 * with 'levelName' the string of the current level and
 * 'built-save' the second string corresponding to the actual file
 */
 

public class Save {

    private String filePath;
    private File file;
    private Boolean newLevel;
    private String currentLevel;

    public Save () {
	// DECLARATION 
		this.filePath = org.parmentier.Parmentier.SAVES_DIR + Game.getInstance().getCurrentUserName() + ".sav";
		this.newLevel = false;
		this.currentLevel = "";
	// BEGIN
		try {
		    this.file = new File(this.filePath); // Create File object
		    if (this.file.createNewFile()) {
			System.out.println("File created: " + this.file.getName());
		    } else {
			System.out.println("File already exists.");
		    }
		}
		catch (IOException e) {
		    System.out.println("An error occurred.");
		    e.printStackTrace(); 
		}
	//END
    }
    
    public InputStream openSave (String idLevel) {
		this.currentLevel = idLevel;
		try {
		    // DECLARATION
		    FileReader fr = new FileReader(this.file);
			InputStream file = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(this.filePath));
		    Boolean match = false;

			char c = '\0';
			String save = "";
			int countSemiColon = 0;
			Boolean time = false; 
		    // BEGIN
       		    for (int i = fr.read() ; i != -1  ; i = fr.read()) {

				c = (char) i;
				if (c == '\n') {
				    if (match) {
						this.newLevel = false;
						save = save.trim();
						String momPath = org.parmentier.Parmentier.SAVES_DIR + "momentanous.txt";
						File mom = new File(momPath); // Create File object

						try{
							if (mom.createNewFile()) {
								System.out.println("File created: " + mom.getName());
							} else {
								System.out.println("File already exists. Deletion");
								mom.delete();
								mom.createNewFile();
							}
						} catch (IOException e) {
							    System.out.println("An error occurred.");
							    e.printStackTrace(); 
						}

						FileWriter fw = new FileWriter(mom, false);
						save = save.replace("!", "\n");
						save = save.replace("-", "-\n");
						fw.write(save);
						fw.close();
						return java.nio.file.Files.newInputStream(java.nio.file.Paths.get(momPath));

				    } else {
						save = "";
					}
				}
				else if (c == ';' || time) {
					countSemiColon++;
					if (countSemiColon == 1){
						if (idLevel.equals(save)){
							System.out.println("---- Match ----");
							match = true;
							save = "";
						}
					} else if (countSemiColon == 2){
						save = save + c;
					}
				} else {
				    save = save + c;
				}
		    }
			fr.close();
			file.close();
		    // if it reached that point, no save could be found. Return the original save file.
			InputStream input; 
		    try {
            	input = getClass().getResourceAsStream("/levels/" + this.currentLevel + ".lvl");
				this.newLevel = true;
				return input;
          	} 	
				catch (Exception e) {
            	System.err.println("Erreur lors du chargement du niveau : " + e.getMessage());
            	this.currentLevel = null;
            	throw new RuntimeException("Failed to load level: " + this.currentLevel);
          }
    	}
		catch (java.lang.Exception e) {
		    return null;
		}
    }
    
    public void overwriteSave (String save){
		if (this.currentLevel.equals("")){
		    System.out.println("Error : You cannot overwrite this level. No level have been accessed.");	
		}

		try {
		    FileReader fr = new FileReader(this.file);
		    String str  = ""; 
		    long pos = 0;
		    char c = ' ';
		    for (int i = fr.read() ; i != -1 ; i = fr.read()) {
		        c = (char) i;
				if (c == '\n') {
			    	str = "";
			    	pos = pos + 1;
				}
				else if (c == ';') {
				    pos = pos + 1;
			    	if (this.currentLevel.equals(str)){ // we found the specific save of the level	        
						RandomAccessFile raf = new RandomAccessFile(this.file, "rw");
						raf.seek(pos);
						raf.writeBytes(save);
						raf.close();
						return;
			   		}
				} else {
			    str = str + c;
			    pos = pos + 1;
				}
		    }
			fr.close();
		    //  if it reaches that point, no save could be found. Appending that new save
		    FileWriter fw = new FileWriter(this.file, true);
		    fw.write(this.currentLevel + ";" + save);
		    fw.close();
		} catch (java.lang.Exception e) {
		    System.out.println("ERROR : couldn't write in save file :" + e.getMessage());
		}
		this.currentLevel = "";
    }

    public Boolean isNewLevel(){
		return this.newLevel;
    }

}
