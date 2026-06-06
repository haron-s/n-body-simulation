package nbody.model.body;

import nbody.model.physics.CollisionHandler;
import nbody.model.vector.Vec2;

import java.util.Objects;


/**
 * Represents a celestial body in the simulation.
 */
public class Body implements ViewableBody {
    private double mass;
    private double radius;
    private final Vec2 position;
    private final Vec2 velocity;
    private final Vec2 acceleration;

    /**
     * Constructs a new Body with specified mass, position, and velocity.
     * Acceleration is initialized to zero, and radius is calculated
     * based on the square root of the mass.
     *
     * @param mass The mass of the body.
     * @param position The initial position vector of the body.
     * @param velocity The initial velocity vector of the body.
     */
    protected Body(double mass, Vec2 position, Vec2 velocity) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new Vec2(0.0, 0.0);
        this.radius = calculateRadius();
    }

    /**
     * Absorbs another body into this one.
     * The resulting velocity is calculated based on conservation of momentum,
     * mass and radius are updated accordingly.
     *
     * @param other The other {@code Body} to be absorbed.
     * @throws NullPointerException if {@code other} is null.
     */
    public void absorb(Body other) {
        Objects.requireNonNull(other, "other cannot be null");

        Vec2 newVelocity = CollisionHandler.calculateMergedVelocity(
            this.mass, this.velocity,
            other.mass, other.velocity
        );

        this.velocity.set(newVelocity);
        this.mass += other.mass;
        this.radius = calculateRadius();
    }

    private double calculateRadius() {return Math.sqrt(this.mass) * 1.5;}

    @Override
    public Vec2 getPosition() {return position;}
    @Override
    public Vec2 getVelocity() {return velocity;}
    @Override
    public double getRadius() {return radius;}
    public double getMass() {return mass;}
    public Vec2 getAcceleration() {return acceleration;}
}
