/** ********************************************************************************
 * Represents a 2D vector with basic vector operations such as addition,
 * subtraction, scaling, and dot product.
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
 * Represents a 2D vector with basic vector operations such as addition,
 * subtraction, scaling, and dot product.
 */
public class Vector {
    /**
     * The x-coordinate of the vector, representing its horizontal component.
     */
    final private double x;

    /**
     * The y-coordinate of the vector, representing its vertical component.
     */
    final private double y;

    /**
     * Constructs a Vector with the specified x and y coordinates.
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the vector.
     * @return The x-coordinate of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the vector.
     * @return The y-coordinate of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Adds another Vector to this vector, returning a new Vector that represents the sum of the two vectors.
     * @param other The Vector to be added to this vector.
     * @return A new Vector that is the sum of this vector and the other vector.
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another Vector from this vector, returning a new Vector that represents the difference between the two vectors.
     * @param other The Vector to be subtracted from this vector.
     * @return A new Vector that is the difference between this vector and the other vector.
     */
    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    /**
     * Scales this vector by a given scalar, returning a new Vector that represents the scaled vector.
     * @param scalar The scalar value by which to scale this vector.
     * @return A new Vector that is the result of scaling this vector by the given scalar.
     */
    public Vector scale(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    /**
     * Calculates the dot product of this vector with another vector, returning a double value that represents the result of the dot product operation.
     * @param other The Vector with which to calculate the dot product.
     * @return The dot product of this vector and the other vector.
     */
    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates the squared magnitude of this vector, which is the sum of the squares of its components.
     * This is useful for performance reasons when comparing magnitudes without needing the actual length.
     * @return The squared magnitude of the vector.
     */
    public double magnitude2() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Calculates the magnitude (length) of this vector, which is the square root of the sum of the squares of its x and y components.
     * @return The magnitude of this vector.
     */
    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Normalizes this vector, returning a new Vector that has the same direction but a magnitude of 1.
     * If the magnitude of the vector is zero, a zero vector is returned to avoid division by zero.
     * @return A new Vector that is the normalized version of this vector.
     */
    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vector(0, 0);
        }
        return new Vector(this.x / mag, this.y / mag);
    }

    /**
     * Calculates the angle in radians between this vector and another vector using the dot product and magnitudes of the vectors.
     * @param other The Vector to which the angle is calculated.
     * @return The angle in radians between this vector and the other vector.
     */
    public double angleTo(Vector other) {
        double dotProduct = this.dot(other);
        double magProduct = this.magnitude() * other.magnitude();
        if (magProduct == 0) {
            return 0;
        }
        return Math.acos(dotProduct / magProduct);
    }

    /**
     * Returns a string representation of the vector in the format "Vector(x, y)".
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector(" + x + ", " + y + ")";
    }

    /**
     * Checks if this Vector is equal to another object. Two Vectors are considered equal if they have the same x and y coordinates.
     * @param obj The object to compare with this Vector.
     * @return true if the object is a Vector with the same coordinates as this vector, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector vector = (Vector) obj;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    /**
     * Returns a hash code value for the Vector, which is computed based on its x and y coordinates.
     * @return A hash code value for this Vector.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }

    
}
