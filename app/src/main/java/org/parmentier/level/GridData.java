/** ********************************************************************************
 * Represents a level in the Parmentier puzzle game.
 * A level consists of a grid of nodes, each potentially connected by bridges.
 * The level is loaded from a text file where each character represents a node or empty space.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier.level
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier.level;

import java.io.IOException;
import java.util.List;
import java.nio.file.Path;

import org.parmentier.Save;

/**
 * Represents a level in the Parmentier puzzle game.
 * A level consists of a grid of nodes, each potentially connected by bridges.
 * The level is loaded from a text file where each character represents a node or empty space.
 */
public class GridData {
    /**
     * The 2D array representing the grid of nodes in the level.
     * Each cell in the array can either be a Node object or null (representing empty space).
     * The grid is constructed based on the contents of a level file, where each character
     * corresponds to a node's value or an empty space.
     */
    final private Node[][] levelArray;

    /**
     * The name of the level, which can be used for display purposes or to identify the level in a list of levels.
     */
    private String name;

    /**
     * Access to the save manager
     */
    private Save saveManager;
    
    /**
     * Constructs a Level object by loading the level data from the specified file.
     * 
     * @param path The path of the file containing the level data.
     */
    public GridData(String idLevel) {
	this.levelArray = null;
        this.saveManager = new Save();
	System.out.println(" ---- Save : Initialized ----");
        String content = this.saveManager.openSave(idLevel);
	System.out.println(" ---- Save : Opened ----");
	Boolean exist = this.saveManager.lastLevelOpened();
	System.out.println(" ---- Save : exist = " + exist.toString() + " ---- ");
	System.out.println(" ---- Save : content = " + content + " ---- ");
	this.loadLevelFromFile(content, exist);
    }

    /**
     * Loads the level data from a text file and constructs the 2D array of nodes.
     * 
     * @param level content or path of the level
     * @param newLevel whether the 'level' has to be read as a new level or as a save 
     * @return A 2D array of Node objects representing the level
     */
    public Node[][] loadLevelFromFile(String level, Boolean newLevel) {
	
	try {
	

	    String fileName = "";
	    String[] parts = null;
	    String[] node_lines = null;
	    String[] bridge_lines = null;
	    Node[][] array = null;
	    
	    if (newLevel){
		// Path path = Path.get(level);
                // String name = fileName.substring(0, fileName.lastIndexOf('.')); // extrait 
		String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(level)));
		
		content = content.replace("\r\n", "\n"); // règle les sauts de lignes sur windows
	        parts = content.split("-\n"); // sépare la sauvegarde des noeuds et des ponts
		node_lines = parts[0].split("\n");
		bridge_lines = parts[1].split("\n");
		array = new Node[node_lines.length][];
	    } else {
		String content = level;
	        parts = content.split("-");
		node_lines = parts[0].split("!");
		bridge_lines = parts[1].split("!");
		array = new Node[node_lines.length][];
	    }

	    System.out.println(" ---- Begin Lines ---- ");
            // chargement des nodes sauvegardés et des bridges possibles horizontalement
            for (int i = 0; i < node_lines.length; i++) {
                Node lastNode = null;
                for (int j = 0; j < node_lines[i].length(); j++) {
                    int value = Character.getNumericValue(node_lines[i].charAt(j));
                    array[i] = array[i] == null ? new Node[node_lines[i].length()] : array[i];
                    if (value > 0) {
                        // création de node
                        array[i][j] = new Node(j, i, value);
                        // création de bridge possible
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }
	    System.out.println(" ---- End Lines  ---- ");

	    System.out.println(" ---- Begin Briges  ---- ");
            // chargement de tous les bridges possibles verticalement
            for (int j = 0; j < node_lines[0].length(); j++) {
                Node lastNode = null;
                for (int i = 0; i < node_lines.length; i++) {
                    if (array[i][j] != null) {
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }

	    System.out.println(" ---- Begin Save ---- ");
            // chargement des bridges sauvegardés
            for (int i = 0; i < node_lines.length; i++) {
		System.out.println(" ---- Loop " + Integer.toString(i)+ " ---- ");
                for (int j = 0; j < bridge_lines[i].length(); j += 4) {

                    int col = j / 4;
                    Node from = array[i][col];
                    if (from == null) continue;

                    int upState = Character.getNumericValue(bridge_lines[i].charAt(j));
                    int upSolution = Character.getNumericValue(bridge_lines[i].charAt(j + 1));
                    int rightState = Character.getNumericValue(bridge_lines[i].charAt(j + 2));
                    int rightSolution = Character.getNumericValue(bridge_lines[i].charAt(j + 3));

                    if (upState != 0) {
                        from.getBridge(0).setState(upState);
                        from.getBridge(0).setSolutionState(upSolution);
                    }

                    if (rightState != 0) {
                        from.getBridge(1).setState(rightState);
                        from.getBridge(1).setSolutionState(rightSolution);
                    }
                }
            }
	    System.out.println(" ---- End Save ---- ");
            return array;
	  } catch (IOException e) {
	    System.err.println("Level file not found: ");
	    return new Node[0][0];
	  }
    }

    public List<Node> getNodes() {
        return java.util.Arrays.stream(levelArray)
            .flatMap(java.util.Arrays::stream)
            .filter(node -> node != null)
            .toList();
    }

    public void saveState() {
        StringBuilder nodeBuilder = new StringBuilder();
        StringBuilder bridgeBuilder = new StringBuilder();

        for (int i = 0; i < levelArray.length; i++) {
            for (int j = 0; j < levelArray[i].length; j++) {
                Node node = levelArray[i][j];

                // 4 bits: up state, up solution, right state, right solution
                if (node != null) {
                    nodeBuilder.append(node.getValue());
                    Bridge up = node.getBridge(0);
                    if (up != null) {
                        bridgeBuilder.append(up.getState());
                        bridgeBuilder.append(up.getSolutionState());
                    } else {
                        bridgeBuilder.append("00");
                    }
                    Bridge right = node.getBridge(1);
                    if (right != null) {
                        bridgeBuilder.append(right.getState());
                        bridgeBuilder.append(right.getSolutionState());
                    } else {
                        bridgeBuilder.append("00");
                    }

                } else {
                    nodeBuilder.append('0');
                    bridgeBuilder.append("0000");
                }
            }

            nodeBuilder.append("!"); 
            bridgeBuilder.append("!"); // Creates "One Liner" data
        }

        String saveData = nodeBuilder.toString() + "-" + bridgeBuilder.toString();
	this.saveManager.overwriteSave(saveData);
	
/*
	// Create the "saves" directory if it doesn't exists
        java.nio.file.Path savesDir = java.nio.file.Paths.get("saves");
        try {
            if (!java.nio.file.Files.exists(savesDir)) {
                java.nio.file.Files.createDirectory(savesDir);
            }
         } catch (java.io.IOException e) {
            System.err.println("Error creating saves directory: " + e.getMessage());
        }

        java.nio.file.Path path = java.nio.file.Paths.get("saves/" + this.name + ".sav");
        try {
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createFile(path);
            }
            java.nio.file.Files.write(path, saveData.getBytes());
            System.out.println("Game state saved to saves/" + this.name + ".sav");

        } catch (java.io.IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
*/
    }

    /**
     * Returns whether the current state corresponds to the solution.
     */
    public boolean checkState() {
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null){ 
                    if (node.checkState() == false){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the amount of errors in the current state compared to the solution.
     */
    public int countErrors() {
        int errors = 0;
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null){ 
                    errors += node.countErrors();
                }
            }
        }
        // on divise par 2 car chaque pont en trop est compté deux fois (ses deux noeuds le considérent comme une erreur)
        return errors / 2;
    }


    /**
     * Gets the value of the node at the specified row and column.
     * 
     * @param row The row index of the node.
     * @param col The column index of the node.
     * @return The value of the node at the specified position.
     * @throws IndexOutOfBoundsException if the row or column index is out of bounds.
     * @throws IllegalArgumentException if there is no node at the specified position.
     */
    public int getValueAt(int row, int col) {
        if (row < 0 || row >= levelArray.length || col < 0 || col >= levelArray[0].length) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        if (levelArray[row][col] == null) {
            throw new IllegalArgumentException("No node at the specified position");
        }
        return levelArray[row][col].getValue();
    }

    /**
     * Gets the node at the specified row and column.
     * 
     * @param row The row index of the node.
     * @param col The column index of the node.
     * @return The node at the specified position.
     * @throws IndexOutOfBoundsException if the row or column index is out of bounds.
     */
    public Node getNodeAt(int row, int col) {
        if (row < 0 || row >= levelArray.length || col < 0 || col >= levelArray[0].length) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        return levelArray[row][col];
    }

    /**
     * Prints the level to the console for debugging purposes.
     * Each node's value is printed, along with its bridges.
     */
    public void printLevel() {
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null) {
                    System.out.print(node.getValue() + " ");
                    for (Bridge bridge : node.getBridges()) {
                        System.out.print("[" + (bridge.getDirection() == Bridge.Direction.RIGHT ? "H" : "V") + ":" + bridge.getLength() + "] ");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * Gets the width of the level (number of columns).
     * @return The width of the level.
     */
    public int getWidth() {
        return levelArray.length;
    }

    /**
     * Gets the height of the level (number of rows).
     * @return The height of the level.
     */
    public int getHeight() {
        return levelArray[0].length;
    }

    public boolean isCorner(Node node) {
        long x = node.getPosition().getX();
        long y = node.getPosition().getY();
        return (x == 0 && y == 0) || (x == 0 && y == getHeight() - 1) || (x == getWidth() - 1 && y == 0) || (x == getWidth() - 1 && y == getHeight() - 1);
    }

    public boolean isEdge(Node node) {
        long x = node.getPosition().getX();
        long y = node.getPosition().getY();
        return (x == 0 || y == 0 || x == getWidth() - 1 || y == getHeight() - 1) && !isCorner(node);
    }

    public boolean isCenter(Node node) {
        return !isEdge(node) && !isCorner(node);
    }
}
