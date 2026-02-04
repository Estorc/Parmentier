/** ********************************************************************************
 * Represents a bridge structure that connects two nodes in a level.
 * 
 * A bridge has a direction, length, and state that determines its visual representation.
 * The bridge can exist in multiple states (0 to MAX_STATE-1) which can be toggled to create
 * dynamic level interactions.
 * 
 * The Direction inner class defines the four possible orientations for a bridge:
 * TOP, RIGHT, BOTTOM, and LEFT.
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

/**
 * Represents a bridge structure that connects two nodes in a level.
 * 
 * A bridge has a direction, length, and state that determines its visual representation.
 * The bridge can exist in multiple states (0 to MAX_STATE-1) which can be toggled to create
 * dynamic level interactions.
 */
public class Bridge {

    /**
     * Defines the possible directions for a bridge.
     * 
     * TOP:    0
     * RIGHT:  1
     * BOTTOM: 2
     * LEFT:   3
     * 
     * These constants are used to specify the orientation of the bridge
     * within the level.
     */
    public static class Direction {
        public static final int TOP = 0;
        public static final int RIGHT = 1;
        public static final int BOTTOM = 2;
        public static final int LEFT = 3;
    }

    /**
     * The maximum number of states a bridge can have.
     * Each state corresponds to a different visual representation of the bridge.
     */
    public final static int MAX_STATE = 3;

    /**
     * The length of the bridge, representing the number of segments it spans.
     * The length is calculated based on the distance between the two nodes it connects.
     */
    private final int length;

    /**
     * The direction of the bridge, represented by one of the constants defined in the Direction class.
     * This indicates the orientation of the bridge within the level.
     */
    private final int direction;

    /**
     * The current state of the bridge, ranging from 0 to MAX_STATE-1.
     * This state determines the visual representation of the bridge.
     */
    private int state;

    /**
     * The starting node of the bridge.
     */
    private final Node from;

    /**
     * The ending node of the bridge.
     */
    private final Node to;

    /**
     * Constructs a Bridge object connecting two nodes with a specified direction.
     * 
     * @param from      The starting node of the bridge.
     * @param to        The ending node of the bridge.
     * @param direction The direction of the bridge, using the constants defined in the Direction class.
     */
    public Bridge(Node from, Node to, int direction) {
        this.from = from;
        this.to = to;

        int xLength = Math.abs(from.getPosition()[0] - to.getPosition()[0]);
        int yLength = Math.abs(from.getPosition()[1] - to.getPosition()[1]);
        if (xLength > 0) {
            this.length = xLength - 1;
        } else {
            this.length = yLength - 1;
        }

        this.direction = direction;
        
        this.state = 0;
    }

    /**
     * Gets the starting node of the bridge.
     * @return The starting node of the bridge.
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Gets the ending node of the bridge.
     * @return The ending node of the bridge.
     */
    public Node getTo() {
        return to;
    }

    /**
     * Gets the length of the bridge.
     * @return The length of the bridge.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the direction of the bridge.
     * @return The direction of the bridge.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets the current state of the bridge.
     * @return The current state of the bridge.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the current state of the bridge.
     * @param state The new state of the bridge.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Toggles the state of the bridge to the next state.
     * If the current state is the maximum state, it wraps around to 0.
     */
    public void toggleState() {
        this.state = (this.state + 1) % MAX_STATE;
    }

    /**
     * Draws the bridge on the provided GraphicsContext.
     * @param gc    The GraphicsContext to draw on.
     * @param preview If true, draws the bridge in preview mode (e.g., semi-transparent).
     */
    public void draw(javafx.scene.canvas.GraphicsContext gc, boolean preview) {
        if (preview) {
            gc.setGlobalAlpha(0.5);
            gc.setStroke(javafx.scene.paint.Color.GRAY);
            gc.setLineWidth(4);
        } else {
            gc.setStroke(javafx.scene.paint.Color.BLACK);
            gc.setLineWidth(2);
        }
        int fromX = from.getCanvasPosition()[0];
        int fromY = from.getCanvasPosition()[1];
        int toX = to.getCanvasPosition()[0];
        int toY = to.getCanvasPosition()[1];
        gc.strokeLine(fromX, fromY, toX, toY);
        if (preview) {
            gc.setGlobalAlpha(1.0);
        }
    }
}