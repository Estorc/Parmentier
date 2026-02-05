package org.parmentier.math;

public class IVector {
    final private long x;
    final private long y;

    public IVector(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public IVector add(IVector other) {
        return new IVector(this.x + other.x, this.y + other.y);
    }

    public IVector subtract(IVector other) {
        return new IVector(this.x - other.x, this.y - other.y);
    }

    public IVector scale(long scalar) {
        return new IVector(this.x * scalar, this.y * scalar);
    }

    public long dot(IVector other) {
        return this.x * other.x + this.y * other.y;
    }

    public long magnitude2() {
        return this.x * this.x + this.y * this.y;
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public IVector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new IVector(0, 0);
        }
        return new IVector((long)(this.x / mag), (long)(this.y / mag));
    }

    public double angleTo(IVector other) {
        double dotProduct = this.dot(other);
        double magProduct = this.magnitude() * other.magnitude();
        if (magProduct == 0) {
            return 0;
        }
        return Math.acos(dotProduct / magProduct);
    }

    @Override
    public String toString() {
        return "IVector(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IVector vector = (IVector) obj;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(x) * 31 + Long.hashCode(y);
    }
}
