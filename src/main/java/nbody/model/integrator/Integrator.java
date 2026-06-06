package nbody.model.integrator;

import nbody.model.body.Body;


import java.util.List;

/**
 * Interface for numerical integration methods used in the simulation.
 * Implementations update the positions and velocities of bodies over a time step.
 */
public interface Integrator {
    /**
     * Updates the physical state of all bodies by one time step.
     * <p>
     * This method mutates the position and velocity vectors of each {@link Body}
     * in the list and performs an acceleration recalculation
     * </p>
     *
     * @param bodies The list of bodies to update.
     * @param deltaTime The time increment (dt) for this simulation step.
     * @throws NullPointerException if {@code bodies} is null.
     */
    void step(List<Body> bodies, double deltaTime);
}
