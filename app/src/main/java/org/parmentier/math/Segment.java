/** ********************************************************************************
 * Serves as a utility class for performing geometric calculations related to line segments,
 * such as determining if two segments intersect. This class provides static methods that can be
 * used throughout the game to handle collision detection and other geometric operations involving segments.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier.math
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier.math;

/**
 * Serves as a utility class for performing geometric calculations related to line segments,
 * such as determining if two segments intersect. This class provides static methods that can be
 * used throughout the game to handle collision detection and other geometric operations involving segments.
 */
public class Segment {
    /**
     * Determines if two line segments, defined by their endpoints, intersect with each other.
     * The method uses the concept of orientation and the cross product to check for intersection.
     * It also handles special cases such as collinear points and overlapping segments.
     * @param a The starting point of the first segment.
     * @param b The ending point of the first segment.
     * @param c The starting point of the second segment.
     * @param d The ending point of the second segment.
     * @return true if the segments intersect, false otherwise.
     */
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

    /**
     * Overloaded method that determines if two line segments, defined by their endpoints as IVector objects, intersect with each other.
     * This method converts the IVector endpoints to Vector objects and calls the main isIntersecting method to perform the intersection check.
     * @param a The starting point of the first segment as an IVector.
     * @param b The ending point of the first segment as an IVector.
     * @param c The starting point of the second segment as an IVector.
     * @param d The ending point of the second segment as an IVector.
     * @return true if the segments intersect, false otherwise.
     */
    static public boolean isIntersecting(IVector a, IVector b, IVector c, IVector d) {
        return isIntersecting(new Vector(a.getX(), a.getY()), new Vector(b.getX(), b.getY()), new Vector(c.getX(), c.getY()), new Vector(d.getX(), d.getY()));
    }

    /**
     * Helper method to determine if a point (px, py) lies on the line segment defined by endpoints (ax, ay) and (bx, by).
     * This method checks if the point is within the bounding box of the segment and if it is collinear with the endpoints.
     * @param ax The x-coordinate of the first endpoint of the segment.
     * @param ay The y-coordinate of the first endpoint of the segment.
     * @param bx The x-coordinate of the second endpoint of the segment.
     * @param by The y-coordinate of the second endpoint of the segment.
     * @param px The x-coordinate of the point to check.
     * @param py The y-coordinate of the point to check.
     * @return true if the point lies on the segment, false otherwise.
     */
    static private boolean onSegment(double ax, double ay, double bx, double by, double px, double py) {
        return px >= Math.min(ax, bx) && px <= Math.max(ax, bx) &&
            py >= Math.min(ay, by) && py <= Math.max(ay, by);
    }
}
