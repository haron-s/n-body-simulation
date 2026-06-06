package nbody.model.physics;

import nbody.model.body.Body;
import nbody.model.vector.Vec2;

class Gravity {
    // Scaled gravitational constant (not physically accurate).
    // Increased to produce stronger accelerations and faster-moving bodies in the simulation.
    // In reality, G is an observed constant.
    private static final double G = 20;

    /**
     * Calculates the magnitude of the gravitational acceleration caused by another body.
     * @param otherMass Mass of the body pulling the target
     * @param distance The distance from the center of the two masses
     * @return Acceleration magnitude (m/s²)
     */
    private static double calculateAccelMagnitude(double otherMass, double distance) {
        return (G * otherMass)/(distance * distance);
    }

    /**
     * Calculates and applies the mutual gravitational acceleration between two bodies.
     * <p>
     * This method computes the interaction and applies equal and opposite changes
     * to both bodies. Determines the acceleration magnitudes based on each body's mass,
     * and updates the internal acceleration vectors of {@code a} and {@code b} in-place.
     * </p>
     * @param a the first body to update
     * @param b the second body to update
     */
    static void applyGravity(Body a, Body b) {
        Vec2 posA = a.getPosition();
        Vec2 posB = b.getPosition();

        // Direction from A to B (used for force direction)
        Vec2 direction = new Vec2(
            posB.getX() - posA.getX(),
            posB.getY() - posA.getY()
        );

        double distance = direction.length();
        if (distance <= 0.0) return;
        // Skip gravity if bodies are overlapping (collision handled separately)
        if (distance < a.getRadius() + b.getRadius()) { return; }

        double accelA = calculateAccelMagnitude(b.getMass(), distance);
        double accelB = calculateAccelMagnitude(a.getMass(), distance);

        direction.normalize();

        // Apply equal and opposite accelerations
        a.getAcceleration().addScaled(direction, accelA);
        b.getAcceleration().addScaled(direction, -accelB);
    }
}
