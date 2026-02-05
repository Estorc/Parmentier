/** ********************************************************************************
 * Represents a node in the Parmentier puzzle game.
 * A node has a position, a value, and can be connected to other nodes via bridges.
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

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;

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
     * The position of the node in the level grid, represented as an array [x, y].
     * The position is used to identify the node's location within the level.
     */
    private final int[] position;

    /**
     * The visual representation of the node as a circle.
     * This circle is used in the graphical interface to display the node.
     */
    private Circle circle;

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
        this.position = new int[]{x, y};
        this.value = value;
        this.bridges = new ArrayList<>();
    }

    /**
     * Gets the visual representation of the node as a circle.
     * @return The circle representing the node.
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * Sets the visual representation of the node as a circle.
     * @param circle The circle to represent the node.
     */
    public void setCircle(Circle circle) {
        this.circle = circle;
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
        if (this.position[0] == other.position[0]) {
            if (this.position[1] < other.position[1]) {
                direction = Bridge.Direction.BOTTOM;
            } else {
                direction = Bridge.Direction.TOP;
            }
        } else {
            if (this.position[0] < other.position[0]) {
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
    public int[] getPosition() {
        return position;
    }

    public int[] getCanvasPosition() {
        return new int[]{position[0] * 32 + 16, position[1] * 32 + 16};
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
     * Draws the node on the provided GraphicsContext at the specified offset.
     * @param gc The GraphicsContext to draw on.
     */
    public void draw(GraphicsContext gc) {
        int x = getCanvasPosition()[0];
        int y = getCanvasPosition()[1];
        gc.setLineWidth(2);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillOval(x - 16, y - 16, 32, 32);
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.strokeOval(x - 16, y - 16, 32, 32);
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillText(Integer.toString(value), x - 4, y + 4);
    }

    
}