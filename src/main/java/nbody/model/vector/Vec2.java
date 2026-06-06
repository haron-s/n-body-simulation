package nbody.model.vector;

import java.util.Objects;
// Reference for vector math concepts
// https://natureofcode.com/vectors/
/**
 * A mutable 2-dimensional vector.
 */
public class Vec2 implements ReadableVec2 {
    private double x;
    private double y;

    /**
     * Creates a two-dimensional vector with the given x and y component
     * @param x x-component
     * @param y y-component
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Returns the length (magnitude) of this vector.
     * @return the vector's length
     */
    public double length(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Mutates this vector in place, scaling it to unit length (1).
     * If this vector has zero length, it is left unchanged.
     * @return this vector for chaining methods
     */
    public Vec2 normalize(){
        double length = length();
        if(length == 0.0){return this;}

        this.x /= length;
        this.y /= length;
        return this;
    }

    /**
     * Adds the given vector to this vector.
     *
     * @param other vector to add
     * @return this vector for chaining methods
     * @throws NullPointerException if {@code other} is null.
     */
    public Vec2 add(Vec2 other){
        Objects.requireNonNull(other, "other cannot be null");
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    /**
     * Subtracts the given vector from this vector.
     *
     * @param other vector to subtract
     * @return this vector for chaining methods
     * @throws NullPointerException if {@code other} is null.
     */
    public Vec2 sub(Vec2 other){
        Objects.requireNonNull(other, "other cannot be null");
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    /**
     * Adds a scaled version of the given vector to this vector.
     *
     * @param other vector to scale and add
     * @param scale the scalar multiplier applied to {@code other}
     * br>
     * p>Example:
     * @code vector.addScaled(other, 0.5) adds half of {@code other} to {@code vector}.
     *
     * <p>Performs the operation: {@code this += other * scale}.
     * @return this vector for chaining methods
     * @throws NullPointerException if {@code other} is null.
     */
    public Vec2 addScaled(Vec2 other, double scale) {
        Objects.requireNonNull(other, "other cannot be null");
        this.x += other.x * scale;
        this.y += other.y * scale;
        return this;
    }

    /**
     * Multiplies both components of this vector by the given scalar.
     * @param scale the scalar value to multiply the components by
     * @return @return this vector for chaining methods
     */
    public Vec2 scale(double scale){
        this.x *= scale;
        this.y *= scale;
        return this;
    }

    /**
     * Sets this vector's components to given components
     * @param x x-component
     * @param y y-component
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets this vector's components to given vector's components
     * @param other vector
     * @throws NullPointerException if {@code other} is null.
     */
    public void set(Vec2 other) {
        Objects.requireNonNull(other, "other cannot be null");
        this.x = other.x;
        this.y = other.y;
    }
    /**
     * Sets this vector's {@code x} and {@code y} component to {@code 0.0}
     */
    public void zero() {
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Returns a new vector that is perpendicular to this vector,
     * by rotating it 90 degrees counterclockwise
     * @return a perpendicular vector rotated 90 degrees counterclockwise
     */
    public Vec2 perpendicular(){
        return new Vec2(-this.y, this.x);
    }

    /**
     * Creates and returns a copy of this vector.
     *
     * @return a new {@code Vec2} with the same components as this vector
     */
    public Vec2 copy() {
        return new Vec2(this.x, this.y);
    }
}
