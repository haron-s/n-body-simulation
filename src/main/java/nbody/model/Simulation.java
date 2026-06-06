package nbody.model;

import nbody.model.body.Body;
import nbody.model.integrator.Integrator;
import nbody.model.physics.AccelerationSolver;
import nbody.model.physics.CollisionHandler;
import nbody.model.body.ViewableBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * The engine of the N-Body simulation.
 * <p>
 * This class handles the interaction between the {@link Integrator}, the
 * {@link CollisionHandler}, and UI {@link Listener}s.
 * </p>
 */
public class Simulation {
    private final Integrator integrator;
    private final List<Body> bodies;
    private double time = 0.0;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Listener for objects that want to be notified after each simulation step.
     */
    public interface Listener {
        // Uses a simple observer/listener pattern
        // https://refactoring.guru/design-patterns/observer
        /**
         * Called after each simulation step.
         * @param viewableBodies A read-only view of the current bodies.
         * @param currentTime The total simulation time.
         */
        void onStepCompleted(List<? extends ViewableBody> viewableBodies, double currentTime);
    }

    /**
     * Constructs a new Simulation with a specific integrator and initial bodies.
     * @param integrator The numerical method used to advance the simulation.
     * @param bodies The initial collection of bodies.
     */
    public Simulation(Integrator integrator, List<Body> bodies) {
        this.integrator = integrator;
        this.bodies = new ArrayList<>(bodies);
        AccelerationSolver.computeAccelerations(this.bodies);
    }

    /**
     * Registers a listener to be notified of simulation updates.
     * @param l The listener to register.
     */
    public void addListener(Listener l) {
        this.listeners.add(l);
    }

    /**
     * Resets the simulation state with a new set of bodies.
     * @param newBodies The list of bodies to load.
     * @throws NullPointerException if {@code newBodies} is null.
     */
    public void reset(List<Body> newBodies) {
        Objects.requireNonNull(newBodies, "newBodies list cannot be null");
        synchronized(this.bodies) {
            this.bodies.clear();
            this.bodies.addAll(newBodies);
            this.time = 0.0;
            AccelerationSolver.computeAccelerations(this.bodies);
        }
    }

    /**
     * Advances the simulation by a fixed time step.
     * <p>
     * This method executes the integration step and resolves any collisions
     * before notifying listeners of the new state.
     * </p>
     * @param timeStep The time increment (dt) for this step.
     */
    public void step(double timeStep) {
        List<ViewableBody> snapshot;
        double snapshotTime;

        synchronized (this.bodies) {
            this.integrator.step(this.bodies, timeStep);
            CollisionHandler.resolveCollisions(this.bodies);

            this.time += timeStep;
            snapshotTime = this.time;
            // shallow copy, constant deep copies can be expensive for high number of bodies
            // ViewableBody interface should only expose primitive getters
            snapshot = List.copyOf(this.bodies);
        }
        for (Listener l : this.listeners) {
            l.onStepCompleted(snapshot, snapshotTime);
        }
    }

    /**
     * Adds a single body to the simulation.
     * @param body The body to add.
     */
    public void addBody(Body body) {
        synchronized(this.bodies) {
            this.bodies.add(body);
        }
    }
}