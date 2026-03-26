package org.parmentier;

import org.parmentier.level.GridData;
import org.parmentier.game.Game;

import java.net.URI;
import java.io.File;        // Import the File class
import java.io.IOException; // Import IOException to handle errors
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile; // for the seek method()
import java.nio.file.Path;
import java.nio.file.FileSystems;

/*
 * a '.sav' file will look like : 
 * -levelName-;-built-save-\n
 * with 'levelName' the string of the current level and
 * 'built-save' the second string corresponding to the actual file
 */
 

public class Save {
    private String filePath;
    private File file;
    private Boolean openLastLevel;
    private String currentLevel;

    public Save () {
	// DECLARATION 
        this.filePath = "saves/" + Game.getInstance().getCurrentUserName() + ".sav";
	this.openLastLevel = false;
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
    
    public String openSave (String idLevel) {
	this.currentLevel = idLevel;
	try {
	    // DECLARATION
	    FileReader fr = new FileReader(this.file);
	    String save = "";
	    Boolean match = false;
	    
	    // BEGIN
	    System.out.println(" ---- Loop ---- ");
	    char c = '\0';
	    for (int i = fr.read() ; i != -1  ; i = fr.read()) {
		c = (char) i;
		if (c == '\n') {
		    if (match) {
			this.openLastLevel = false;
			System.out.println(" ---- FOUND ---- ");
			return save;
		    }
		}
		else if (c == ';') {
		    if (idLevel.equals(save)){
			save = "";
			match = true;
		    }
		} else {
		    save = save + c;
		}
	    }
	    System.out.println(" ---- End Loop ---- ");
	    // if it reached that point, no save could be found. Return an empty grid.
	    try {
		URI levelURI = getClass().getResource("/levels/" + this.currentLevel + ".lvl").toURI();
		java.nio.file.Path levelPath = java.nio.file.Paths.get(levelURI);
		System.out.println("Chargement du niveau depuis le fichier : " + levelPath);
		this.openLastLevel = false; 
	        return levelPath.toString();
		
	    } catch (Exception e) {
		System.err.println("Erreur lors du chargement du niveau : " + e.getMessage());
	        return null;
	    }
        }
	catch (java.lang.Exception e) {
	    return null;
	}
    }
    
    public void overwriteSave (String save){
	if (! this.currentLevel.equals("")){
	    System.out.println("Error : You cannot overwrite this level. No level have been accessed.");	}
	
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
	    //  if it reaches that point, no save could be found. Appending that new save
	    FileWriter fw = new FileWriter(this.file, true);
	    fw.write(this.currentLevel + ";" + save);
	    fw.close();
	} catch (java.lang.Exception e) {
	    System.out.println("ERROR : couldn't write in save file :" + e.getMessage());
	}
	this.currentLevel = "";
    }

    public Boolean lastLevelOpened(){
	return this.openLastLevel;
    }

}
