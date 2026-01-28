package org.parmentier.level;

import org.parmentier.level.Bridge;
import java.util.ArrayList;

public class Node {
    private int value;
    private int[] position;
    private ArrayList<Bridge> bridge;
    
    public Node(int x, int y, int value) {
        this.position = new int[]{x, y};
        this.value = value;
        this.bridge = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public void addBridgeTo(Node other) {
        bridge.add(new Bridge(this, other));
    }

    public int[] getPosition() {
        return position;
    }

    public ArrayList<Bridge> getBridges() {
        return bridge;
    }

    public Bridge getBridge(Node from, Node to) {
        int xDiff = to.position[0] - from.position[0];
        int yDiff = to.position[1] - from.position[1];

        if (xDiff == 0 && yDiff != 0) {
            return bridge.get(Bridge.Direction.HORIZONTAL);
        } else if (yDiff == 0 && xDiff != 0) {
            return bridge.get(Bridge.Direction.VERTICAL);
        }
        return null;
    }
}