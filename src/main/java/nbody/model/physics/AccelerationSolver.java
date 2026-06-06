package nbody.model.physics;

import nbody.model.body.Body;

import java.util.List;
import java.util.Objects;

/**
 * A utility class responsible for calculating the gravitational accelerations
 * of all bodies within the simulation.
 * * @see Gravity
 */
public class AccelerationSolver {
    /**
     * Calculates and updates the gravitational acceleration for all bodies in the list it receives.
     * <p>
     * This method uses a nested loop to calculate interactions between all unique pairs
     * of bodies.
     * </p>
     * Updates the acceleration of both bodies in each pair in-place.</li>
     * </ol>
     *
     * @param bodies A list of {@link Body} objects to process. Must not be null or contain nulls.
     * @throws NullPointerException if {@code bodies} is null.
     * @throws RuntimeException potentially thrown if an element within the list is null during iteration.
     */
    public static void computeAccelerations(List<Body> bodies) {
        Objects.requireNonNull(bodies, "bodies cannot be null");

        setAccelerationsZero(bodies);

        int n = bodies.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Gravity.applyGravity(bodies.get(i), bodies.get(j));
            }
        }
    }

    private static void setAccelerationsZero(List<Body> bodies) {
        for (Body body : bodies) {
            body.getAcceleration().zero();
        }
    }
}
