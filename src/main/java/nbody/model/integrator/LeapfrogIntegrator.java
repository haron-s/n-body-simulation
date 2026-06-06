package nbody.model.integrator;

import nbody.model.body.Body;
import nbody.model.physics.AccelerationSolver;

import java.util.List;
import java.util.Objects;

/**
 * An integrator using the Leapfrog (Kick-Drift-Kick) algorithm.
 * <p>
 * It follows a three-step update:
 * <ol>
 * <li><b>Half-Kick:</b> Update velocity by half a time step using current acceleration.</li>
 * <li><b>Drift:</b> Update position by a full time step using the new velocity.</li>
 * <li><b>Half-Kick:</b> Recalculate forces and update velocity by another half step.</li>
 * </ol>
 * </p>
 */
public final class LeapfrogIntegrator implements Integrator {
    @Override
    public void step(List<Body> bodies, double deltaTime) {
        Objects.requireNonNull(bodies, "bodies cannot be null");

        // half step
        for (Body body : bodies) {
            body.getVelocity().addScaled(
                    body.getAcceleration(), deltaTime/2
            );
        }
        // drift
        for (Body body : bodies) {
            body.getPosition().addScaled(
                    body.getVelocity(), deltaTime
            );
        }
        // half step
        AccelerationSolver.computeAccelerations(bodies);
        for (Body body : bodies) {
            body.getVelocity().addScaled(
                    body.getAcceleration(), deltaTime/2
            );
        }

    }
}
