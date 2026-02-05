/** ********************************************************************************
 * Represents a 2D integer vector with basic vector operations such as addition,
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
 * Represents a 2D integer vector with basic vector operations such as addition,
 * subtraction, scaling, and dot product.
 */
public class IVector {
    /**
     * The x-coordinate of the vector, representing its horizontal component.
     */
    final private long x;

    /**
     * The y-coordinate of the vector, representing its vertical component.
     */
    final private long y;

    /**
     * Constructs an IVector with the specified x and y coordinates.
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     */
    public IVector(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the vector.
     * @return The x-coordinate of the vector.
     */
    public long getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the vector.
     * @return The y-coordinate of the vector.
     */
    public long getY() {
        return y;
    }

    /**
     * Adds another IVector to this vector, returning a new IVector that represents the sum of the two vectors.
     * @param other The IVector to be added to this vector.
     * @return A new IVector that is the result of adding the other vector to this vector.
     */
    public IVector add(IVector other) {
        return new IVector(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another IVector from this vector, returning a new IVector that represents the difference between the two vectors.
     * @param other The IVector to be subtracted from this vector.
     * @return A new IVector that is the result of subtracting the other vector from
     */
    public IVector subtract(IVector other) {
        return new IVector(this.x - other.x, this.y - other.y);
    }

    /**
     * Scales this vector by a given scalar value, returning a new IVector that represents the scaled vector.
     * @param scalar The value by which to scale the vector.
     * @return A new IVector that is the result of scaling this vector by the given
     */
    public IVector scale(long scalar) {
        return new IVector(this.x * scalar, this.y * scalar);
    }

    /**
     * Calculates the dot product of this vector with another IVector, returning a long value that represents the result of the dot product operation.
     * @param other The IVector with which to calculate the dot product.
     * @return The result of the dot product operation between this vector and the other vector.
     */
    public long dot(IVector other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates the squared magnitude of this vector, which is the sum of the squares of its components.
     * This is useful for performance reasons when comparing magnitudes without needing the actual length.
     * @return The squared magnitude of the vector.
     */
    public long magnitude2() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Calculates the magnitude (length) of this vector, which is the square root of the sum of the squares of its components.
     * @return The magnitude of the vector.
     */
    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Normalizes this vector, returning a new IVector that has the same direction but a magnitude of 1.
     * If the magnitude of the vector is zero, a zero vector is returned to avoid division by zero.
     * @return A new IVector that is the normalized version of this vector.
     */
    public IVector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new IVector(0, 0);
        }
        return new IVector((long)(this.x / mag), (long)(this.y / mag));
    }

    /**
     * Calculates the angle in radians between this vector and another vector using the dot product and magnitudes of the vectors.
     * @param other The IVector to which the angle is calculated.
     * @return The angle in radians between this vector and the other vector.
     */
    public double angleTo(IVector other) {
        double dotProduct = this.dot(other);
        double magProduct = this.magnitude() * other.magnitude();
        if (magProduct == 0) {
            return 0;
        }
        return Math.acos(dotProduct / magProduct);
    }

    /**
     * Returns a string representation of the vector in the format "IVector(x, y)".
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "IVector(" + x + ", " + y + ")";
    }

    /**
     * Checks if this IVector is equal to another object. Two IVectors are considered equal if they have the same x and y coordinates.
     * @param obj The object to compare with this IVector.
     * @return true if the object is an IVector with the same coordinates as this vector, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IVector vector = (IVector) obj;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    /**
     * Returns a hash code value for the IVector, which is computed based on its x and y coordinates.
     * @return A hash code value for this IVector.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }
}
