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

    public Bridge(int length, int direction) {
        this.length = length;
        this.direction = direction;
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