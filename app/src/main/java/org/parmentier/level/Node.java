/** ********************************************************************************
 * Represents a node in the Parmentier puzzle game.
 * A node has a position, a value, and can be connected to other nodes via bridges.
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
import java.util.ArrayList;

import org.parmentier.math.IVector;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a node in the Parmentier puzzle game.
 * A node has a position, a value, and can be connected to other nodes via bridges.
 */
public class Node {
    /**
     * The value of the node, representing its significance in the puzzle.
     * This value is used to determine the number of bridges that must connect to this node.
     */
    private final int value;

    /**
     * The position of the node in the grid, represented as an IVector (x, y).
     * The position is used to identify the node's location within the level.
     */
    private final IVector position;

    /**
     * The size of a node.
     */
    public static final int SIZE = 64;

    /**
     * The list of bridges connected to this node.
     * These bridges represent the connections between this node and other nodes in the level.
     */
    private final ArrayList<Bridge> bridges;
    
    /**
     * Constructs a Node with the specified position and value.
     * @param x The x-coordinate of the node's position.
     * @param y The y-coordinate of the node's position.
     * @param value The value of the node.
     */
    public Node(int x, int y, int value) {
        this.position = new IVector(x, y);
        this.value = value;
        this.bridges = new ArrayList<>();
    }

    /**
     * Gets the value of the node.
     * @return The value of the node.
     */
    public int getValue() {
        return value;
    }

    /**
     * Adds a bridge to the list of bridges connected to this node.
     * @param b The bridge to add.
     */
    public void addBridge(Bridge b) {
        bridges.add(b);
    }
    
    /**
     * Creates and adds a bridge from this node to another specified node.
     * The direction of the bridge is determined based on the relative positions of the two nodes.
     * @param other The node to which the bridge will connect.
     */
    public void addBridgeTo(Node other) {
        int direction;
        if (this.position.getX() == other.position.getX()) {
            if (this.position.getY() < other.position.getY()) {
                direction = Bridge.Direction.BOTTOM;
            } else {
                direction = Bridge.Direction.TOP;
            }
        } else {
            if (this.position.getX() < other.position.getX()) {
                direction = Bridge.Direction.RIGHT;
            } else {
                direction = Bridge.Direction.LEFT;
            }
        }
        Bridge bridge = new Bridge(this, other, direction);
        bridges.add(bridge);
        other.addBridge(bridge);
    }
    
    /**
     * Gets the position of the node.
     * @return The position of the node as an array [x, y].
     */
    public IVector getPosition() {
        return position;
    }

    public IVector getCanvasPosition() {
        return new IVector(position.getX() * SIZE + SIZE/2, position.getY() * SIZE + SIZE/2);
    }

    /**
     * Gets the list of bridges connected to this node.
     * @return The list of bridges.
     */
    public ArrayList<Bridge> getBridges() {
        return bridges;
    }

    /**
     * Gets the bridge connected to this node in the specified direction.
     * @param direction The direction of the bridge to retrieve.
     * @return The bridge in the specified direction, or null if no such bridge exists.
     */
    public Bridge getBridge(int direction) {
        for (Bridge b : bridges) {
            int dir = direction;
            if (b.getFrom() != this) {
                dir = (direction + 2) % 4;
            }
            if (b.getDirection() == dir) {
                return b;
            }
        }
        return null;
    }

    /**
     * Gets the bridge connecting this node to the specified node.
     * @param to The node to which the bridge connects.
     * @return The bridge connecting this node to the specified node, or null if no such bridge exists.
     */
    public Bridge getBridge(Node to) {
        for (Bridge b : bridges) {
            if ((b.getFrom() == this && b.getTo() == to) || (b.getFrom() == to && b.getTo() == this)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Checks whether the node's current bridges correspond to the solution
     */
    public boolean checkState(){
        boolean result = true;
        for (Bridge b : bridges) {
            result = result && b.checkState(); // every bridge must return true
        }
        return result;
    }

    /**
     * Returns the amount of extra bridges connected to this node compared to the solution
     */
    public int countErrors(){
        int errors = 0;
        for (Bridge b : bridges) {
            errors = errors + b.countErrors();
        }
        return errors;
    }

    /**
     * Draws the node on the provided GraphicsContext at the specified offset.
     * @param gc The GraphicsContext to draw on.
     */
    public void draw(GraphicsContext gc) {
        long x = getCanvasPosition().getX();
        long y = getCanvasPosition().getY();
        gc.setLineWidth(2);

        boolean isDarkMode = org.parmentier.game.Settings.currentStrokeColor.equals(javafx.scene.paint.Color.WHITE);

        if (isDarkMode) {
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.setStroke(javafx.scene.paint.Color.GRAY);
        } else {
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.setStroke(javafx.scene.paint.Color.BLACK);
        }
        
        gc.fillOval(x - SIZE/2, y - SIZE/2, SIZE, SIZE);
        gc.strokeOval(x - SIZE/2, y - SIZE/2, SIZE, SIZE);
        
        gc.setFill(org.parmentier.game.Settings.currentStrokeColor);
        gc.fillText(Integer.toString(value), x - 4, y + 4);
    }
    
}