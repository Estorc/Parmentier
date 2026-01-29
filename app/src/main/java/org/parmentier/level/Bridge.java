package org.parmentier.level;

public class Bridge {

    public static class Direction {
        public static final int TOP = 0;
        public static final int RIGHT = 1;
        public static final int BOTTOM = 2;
        public static final int LEFT = 3;
    }

    public final static int MAX_STATE = 3;

    private int length;
    private int direction;
    private int state;
    private Node from;
    private Node to;

    public Bridge(Node from, Node to, int direction) {
        this.from = from;
        this.to = to;

        int xLength = Math.abs(from.getPosition()[0] - to.getPosition()[0]);
        int yLength = Math.abs(from.getPosition()[1] - to.getPosition()[1]);
        int length = xLength + yLength - 1;
        if (xLength > 0) {
            this.length = xLength - 1;
        } else {
            this.length = yLength - 1;
        }

        this.direction = direction;
        
        this.state = 0;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public int getLength() {
        return length;
    }

    public int getDirection() {
        return direction;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void toggleState() {
        this.state = (this.state + 1) % MAX_STATE;
    }
}