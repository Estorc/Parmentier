package org.parmentier.level;

import org.parmentier.level.Bridge;

public class Node {
    private int value;
    private int[] position;
    private Bridge[] bridge;
    
    public Node(int x, int y, int value) {
        this.position = new int[]{x, y};
        this.value = value;
        this.bridge = new Bridge[4];
    }

    public int getValue() {
        return value;
    }

    public void addBridgeTo(Node other) {
        int xDiff = other.position[0] - this.position[0];
        int yDiff = other.position[1] - this.position[1];

        if (xDiff == 0 && yDiff > 0) {
            bridge[Bridge.Direction.HORIZONTAL] = new Bridge(yDiff - 1, Bridge.Direction.HORIZONTAL);
        } else if (xDiff == 0 && yDiff < 0) {
            bridge[Bridge.Direction.HORIZONTAL] = new Bridge(-yDiff - 1, Bridge.Direction.HORIZONTAL);
        } else if (yDiff == 0 && xDiff > 0) {
            bridge[Bridge.Direction.VERTICAL] = new Bridge(xDiff - 1, Bridge.Direction.VERTICAL);
        } else if (yDiff == 0 && xDiff < 0) {
            bridge[Bridge.Direction.VERTICAL] = new Bridge(-xDiff - 1, Bridge.Direction.VERTICAL);
        }
    }

    public int[] getPosition() {
        return position;
    }

    public Bridge getBridge(int direction) {
        return bridge[direction];
    }
}