package org.parmentier.math;

public class Segment {
    static public boolean isIntersecting(Vector a, Vector b, Vector c, Vector d) {

        double denominator = ((b.getX() - a.getX()) * (d.getY() - c.getY())) - ((b.getY() - a.getY()) * (d.getX() - c.getX()));
        double numerator1  = ((a.getY() - c.getY()) * (d.getX() - c.getX())) - ((a.getX() - c.getX()) * (d.getY() - c.getY()));
        double numerator2  = ((a.getY() - c.getY()) * (b.getX() - a.getX())) - ((a.getX() - c.getX()) * (b.getY() - a.getY()));

        // Collinear case
        if (denominator == 0 && numerator1 == 0 && numerator2 == 0) {
            return onSegment(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY()) ||
                onSegment(a.getX(), a.getY(), b.getX(), b.getY(), d.getX(), d.getY()) ||
                onSegment(c.getX(), c.getY(), d.getX(), d.getY(), a.getX(), a.getY()) ||
                onSegment(c.getX(), c.getY(), d.getX(), d.getY(), b.getX(), b.getY());
        }

        // Parallel but not collinear
        if (denominator == 0) return false;

        double r = numerator1 / denominator;
        double s = numerator2 / denominator;

        return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
    }

    static public boolean isIntersecting(IVector a, IVector b, IVector c, IVector d) {
        return isIntersecting(new Vector(a.getX(), a.getY()), new Vector(b.getX(), b.getY()), new Vector(c.getX(), c.getY()), new Vector(d.getX(), d.getY()));
    }

    static private boolean onSegment(double ax, double ay, double bx, double by, double px, double py) {
        return px >= Math.min(ax, bx) && px <= Math.max(ax, bx) &&
            py >= Math.min(ay, by) && py <= Math.max(ay, by);
    }
}
