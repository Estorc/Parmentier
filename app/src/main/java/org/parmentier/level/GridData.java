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
import java.io.InputStream;
import java.util.List;

import org.parmentier.game.Game;

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
     * The elapsed seconds stored in the save file for this level.
     */
    private int savedChrono = 0;

    /**
     * Gets the elapsed seconds stored in the save file for this level.
     * 
     * @return the elapsed seconds stored in the save file
     */
    public int getsavedChrono() {
        return savedChrono;
    }

    /**
     * Sets the elapsed seconds stored in the save file for this level.
     * 
     * @param savedChrono the elapsed seconds to set
     */
    public void setsavedChrono(int savedChrono) {
        this.savedChrono = savedChrono;
    }
    
    /**
     * Constructs a Level object by loading the level data from the specified file.
     * 
     * @param fileStream The InputStream of the file containing the level data. The file should be formatted in
     * a specific way, where the first part contains the node values and the second part contains the bridge states,
     * separated by a line with a single dash ("-").
     */
    public GridData(InputStream fileStream, String fileName) {
        levelArray = loadLevel(fileStream, fileName);
    }

    /**
     * Loads the level data from a text file and constructs the 2D array of nodes.
     * 
     * @param path The path of the file containing the level data.
     * @return A 2D array of Node objects representing the level.
     */
    private Node[][] loadLevelFromFile(java.nio.file.Path path) {
      try {
            return loadLevel(java.nio.file.Files.newInputStream(path), path.getFileName().toString().replaceFirst("[.][^.]+$", ""));
        } catch (IOException e) {
            System.err.println("Level file not found: " + path);
            return new Node[0][0];
        }
    }

    /**
     * Loads the level data from an InputStream and constructs the 2D array of nodes.
     *
     * @param fileStream The InputStream of the file containing the level data. The file should be formatted in
     * a specific way, where the first part contains the node values and the second part contains the bridge states,
     * separated by a line with a single dash ("-").
     * @param fileName The name of the file being loaded, used to set the level's name based on the file name (without extension).
     *
     * @return A 2D array of Node objects representing the level.
     */
    private Node[][] loadLevel(InputStream fileStream, String fileName) {
        try {
            this.name = fileName;
            String content = new String(fileStream.readAllBytes());
            content = content.replace("\r\n", "\n"); // règle les sauts de lignes sur windows

            int savedTime = 0;
            if (content.startsWith("TIME:")) {
                int idx = content.indexOf('\n');
                if (idx >= 0) {
                    String timeLine = content.substring(0, idx).trim();
                    try {
                        savedTime = Integer.parseInt(timeLine.substring(5));
                    } catch (NumberFormatException ignored) {
                        savedTime = 0;
                    }
                    content = content.substring(idx + 1);
                }
            }
            this.savedChrono = savedTime;

            String[] parts = content.split("-\n"); // sépare la sauvegarde des noeuds et des ponts
            String[] node_lines = parts[0].split("\n");
            String[] bridge_lines = parts[1].split("\n");
            Node[][] array = new Node[node_lines.length][];

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

            // chargement des bridges sauvegardés
            for (int i = 0; i < node_lines.length; i++) {
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
                    }
                    if (upSolution != 0) {
                        from.getBridge(0).setSolutionState(upSolution);
                    }
                    
                    if (rightState != 0) {
                        from.getBridge(1).setState(rightState);
                    }

                    if (rightSolution != 0) {
                        from.getBridge(1).setSolutionState(rightSolution);
                    }
                }
            }

            return array;
        } catch (IOException e) {
            return new Node[0][0];
        }
    }


    /**
     * Saves the current state of the level to a file.
     * The state includes the elapsed time, value of each node and the state of each bridge.
     * The state is saved in a text format where the first part contains the node values and the second part contains the bridge states,
     * separated by a line with a single dash ("-").
     * The method creates a "saves" directory if it does not exist and saves the state to a file named after the level's name with a ".sav" extension.
     */
    public void saveState(int elapsedSeconds) {
        this.savedChrono = elapsedSeconds;
        saveState();
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

            nodeBuilder.append('\n');
            bridgeBuilder.append('\n');
        }

        String saveData = "TIME:" + this.savedChrono + "\n" + nodeBuilder.toString() + "-\n" + bridgeBuilder.toString();
        // Create the "saves" directory if it doesn't exists
        java.nio.file.Path savesDir = java.nio.file.Paths.get("saves");
        try {
            if (!java.nio.file.Files.exists(savesDir)) {
                java.nio.file.Files.createDirectory(savesDir);
            }
         } catch (java.io.IOException e) {
            System.err.println("Error creating saves directory: " + e.getMessage());
        }

        java.nio.file.Path path = java.nio.file.Paths.get("saves/" + Game.getInstance().getCurrentUserName() + "_" + this.name + ".sav");
        try {
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createFile(path);
            }
            java.nio.file.Files.write(path, saveData.getBytes());
            System.out.println("Game state saved to saves/" + this.name + ".sav");

        } catch (java.io.IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    /**
     * Returns the level name that was passed during loading.
     * @return name of the level
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a list of all the nodes in the level.
     * This method iterates through the 2D array of nodes and collects all non-null nodes into a list, which is then returned to the caller.
     * @return A list of all the nodes in the level.
     */
    public List<Node> getNodes() {
        return java.util.Arrays.stream(levelArray)
            .flatMap(java.util.Arrays::stream)
            .filter(node -> node != null)
            .toList();
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

    /**
     * Determines if the given node is located at a corner of the grid.
     * A node is considered a corner if it is located at one of the four corners of the grid (top-left, top-right, bottom-left, bottom-right).
     * @param node The node to check.
     * @return true if the node is located at a corner of the grid, false otherwise.
     */
    public boolean isCorner(Node node) {
        long x = node.getPosition().getX();
        long y = node.getPosition().getY();
        return (x == 0 && y == 0) || (x == 0 && y == getHeight() - 1) || (x == getWidth() - 1 && y == 0) || (x == getWidth() - 1 && y == getHeight() - 1);
    }

    /**
     * Determines if the given node is located at an edge of the grid (but not a corner).
     * A node is considered an edge if it is located on the outer border of the grid (top row, bottom row, left column, right column)
     * but is not a corner.
     * @param node The node to check.
     * @return true if the node is located at an edge of the grid, false otherwise.
     */
    public boolean isEdge(Node node) {
        long x = node.getPosition().getX();
        long y = node.getPosition().getY();
        return (x == 0 || y == 0 || x == getWidth() - 1 || y == getHeight() - 1) && !isCorner(node);
    }

    /**
     * Determines if the given node is located at the center of the grid (not an edge or a corner).
     * A node is considered a center if it is not located on the outer border of the grid and is not a corner.
     * @param node The node to check.
     * @return true if the node is located at the center of the grid, false otherwise.
     */
    public boolean isCenter(Node node) {
        return !isEdge(node) && !isCorner(node);
    }
}
