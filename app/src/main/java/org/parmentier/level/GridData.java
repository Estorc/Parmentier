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
     * Constructs a Level object by loading the level data from the specified file.
     * 
     * @param path The path of the file containing the level data.
     */
    public GridData(java.nio.file.Path path) {
        levelArray = loadLevelFromFile(path);
    }

    /**
     * Loads the level data from a text file and constructs the 2D array of nodes.
     * 
     * @param path The path of the file containing the level data.
     * @return A 2D array of Node objects representing the level.
     */
    private Node[][] loadLevelFromFile(java.nio.file.Path path) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(path));
            content = content.replace("\r\n", "\n"); // règle les sauts de lignes sur windows
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
            for (int i = 0; i < node_lines.length; i ++) {
                for (int j = 0; j < bridge_lines[i].length(); j+=4)
                    // FORMAT: HAUT-DROIT
                    for(int k = 0; k < 2; k++){
                        int state = Character.getNumericValue(bridge_lines[i].charAt(j + k)); // state is 0 (no bridge), 1 (one bridge) or 2 (two bridges)
                        int solution = Character.getNumericValue(bridge_lines[i].charAt(j + k + 2));
                        if(state != 0) {
                            Node from = array[i][j / 2];
                            int dir = k;
                            from.getBridge(dir).setState(state);
                            from.getBridge(dir).setSolutionState(solution);
                        }
                    }
            }

            return array;
        } catch (IOException e) {
            System.err.println("Level file not found: " + path);
            return new Node[0][0];
        }
    }

    /**
     * to do: change saveState to correspond to the new loading system
     */

    public void saveState() {
        StringBuilder nodeBuilder = new StringBuilder();
        StringBuilder bridgeBuilder = new StringBuilder();
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null) {
                    nodeBuilder.append(node.getValue());
                    for (int dir = 0; dir < 4; dir++) {
                        Bridge bridge = node.getBridge(dir);
                        if (bridge != null) {
                            bridgeBuilder.append(bridge.getState());
                        } else {
                            bridgeBuilder.append('0');
                        }
                    }
                } else {
                    nodeBuilder.append('0');
                    bridgeBuilder.append("0000");
                }
            }
            nodeBuilder.append('\n');
            bridgeBuilder.append('\n');
        }
        String saveData = nodeBuilder.toString() + "-\n" + bridgeBuilder.toString();
        // create save.txt if it doesn't exist
        java.nio.file.Path path = java.nio.file.Paths.get("save.txt");
        if (!java.nio.file.Files.exists(path)) {
            try {
                java.nio.file.Files.createFile(path);
            } catch (java.io.IOException e) {
                System.err.println("Error creating save file: " + e.getMessage());
                return;
            }
        }
        // save to file
        try {
            path = java.nio.file.Paths.get("save.txt");
            java.nio.file.Files.write(path, saveData.getBytes());
            System.out.println("Game state saved to save.txt");
        } catch (java.io.IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    /**
     * Checks if the current state of the level is equal to the saved solution.
     * 
     */
    public boolean checkState() {
        boolean result = true;
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null){ 
                    result = result && node.checkState();
                }
            }
        }
        System.out.println(result);
        return result;
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
}