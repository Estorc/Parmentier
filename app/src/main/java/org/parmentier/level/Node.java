package org.parmentier.level;

import org.parmentier.level.Bridge;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class Node {
    private int value;
    private int[] position;
    private Circle circle;
    private ArrayList<Bridge> bridges;
    
    public Node(int x, int y, int value) {
        this.position = new int[]{x, y};
        this.value = value;
        this.bridges = new ArrayList<>();
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public int getValue() {
        return value;
    }

    public void addBridge(Bridge b) {
        bridges.add(b);
    }

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

    public int[] getPosition() {
        return position;
    }

    public ArrayList<Bridge> getBridges() {
        return bridges;
    }

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

    public Bridge getBridge(Node from, Node to) {
        for (Bridge b : bridges) {
            if ((b.getFrom() == from && b.getTo() == to) || (b.getFrom() == to && b.getTo() == from)) {
                return b;
            }
        }
        return null;
    }

    
}