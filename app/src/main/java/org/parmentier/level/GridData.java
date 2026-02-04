/** ********************************************************************************
 * Represents a level in the Parmentier puzzle game.
 * A level consists of a grid of nodes, each potentially connected by bridges.
 * The level is loaded from a text file where each character represents a node or empty space.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier.level
 * @copyright Copyright (c) 2026 Parmentier MIT License.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************
 * Copyright (c) 2026 Parmentier.
 * This file is licensed under the MIT License.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ***********************************************************************************/

package org.parmentier.level;

import java.io.FileNotFoundException;

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
     * @param filename The name of the file containing the level data.
     */
    public GridData(String filename) {
        levelArray = loadLevelFromFile(filename);
    }

    /**
     * Loads the level data from a text file and constructs the 2D array of nodes.
     * 
     * @param filename The name of the file containing the level data.
     * @return A 2D array of Node objects representing the level.
     */
    private Node[][] loadLevelFromFile(String filename) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(getClass().getClassLoader().getResource(filename).toURI());
            String content = new String(java.nio.file.Files.readAllBytes(path));
            String[] lines = content.split("\n");
            Node[][] array = new Node[lines.length][];
            
            for (int i = 0; i < lines.length; i++) {
                Node lastNode = null;
                for (int j = 0; j < lines[i].length(); j++) {
                    int value = Character.getNumericValue(lines[i].charAt(j));
                    array[i] = array[i] == null ? new Node[lines[i].length()] : array[i];
                    if (value > 0) {
                        array[i][j] = new Node(j, i, value);
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }

            for (int j = 0; j < lines[0].length(); j++) {
                Node lastNode = null;
                for (int i = 0; i < lines.length; i++) {
                    if (array[i][j] != null) {
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }

            return array;
        } catch (FileNotFoundException e) {
            System.err.println("Level file not found: " + filename);
            return new Node[0][0];
        } catch (java.io.IOException | java.net.URISyntaxException e) {
            System.err.println("Error reading level file: " + filename);
            return new Node[0][0];
        }
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