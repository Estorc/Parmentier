package org.parmentier.level;

public class Bridge {

    public static class Direction {
        public static final int VERTICAL = 0;
        public static final int HORIZONTAL = 1;
    }

    public static class State {
        public static final int NONE = 0;
        public static final int SINGLE = 1;
        public static final int DOUBLE = 2;
    }

    private int length;
    private int direction;
    private int state;
    private Node from;
    private Node to;

    public Bridge(Node from, Node to) {
        this.from = from;
        this.to = to;

        int xLength = Math.abs(from.getPosition()[0] - to.getPosition()[0]);
        int yLength = Math.abs(from.getPosition()[1] - to.getPosition()[1]);
        int length = xLength + yLength - 1;
        if (xLength > 0) {
            this.length = xLength - 1;
            this.direction = Direction.HORIZONTAL;
        } else {
            this.length = yLength - 1;
            this.direction = Direction.VERTICAL;
        }
        
        this.state = State.NONE;
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
        if (state == State.NONE) {
            state = State.SINGLE;
        } else if (state == State.SINGLE) {
            state = State.DOUBLE;
        } else {
            state = State.NONE;
        }
    }
}