package nbody.controller;

import java.util.List;
import nbody.model.Simulation;
import nbody.model.body.BodyFactory;
import nbody.model.vector.Vec2;

/**
 * Acts as the controller for the N-Body simulation.
 * <p>
 * The controller owns the simulation and turns user actions
 * into model updates.
 * </p>
 */
public class SimulationController {
  private enum SimulationState {
    STOPPED,
    RUNNING,
    PAUSED
  }

  private final Simulation simulation;
  private final double fixedDt;
  private double currentSpawnMass = 100.0;
  private SimulationState state = SimulationState.STOPPED;

  /**
   * Constructs a new simulation controller.
   *
   * @param simulation the simulation model to control
   * @param fixedDt the fixed timestep, in seconds, used for physics updates
   */
  public SimulationController(Simulation simulation, double fixedDt) {
    this.simulation = simulation;
    this.fixedDt = fixedDt;
  }

  /**
   * Starts the controller's update loop.
   * <p>
   * Uses a fixed timestep with an accumulator so that physics updates remain
   * stable even if frame timing varies. The loop itself continues running for
   * the lifetime of the application, but the simulation only advances while the
   * controller state is {@link SimulationState#RUNNING}.
   * </p>
   */
  public void start() {
    // Reference for fixed-timestep loop and decoupling simulation from rendering
    // https://andreleite.com/posts/2025/game-loop/fixed-timestep-game-loop/
    double accumulator = 0.0;
    long last = System.nanoTime();

    while (true) {
      long now = System.nanoTime();
      double frameTime = (now - last) / 1e9;
      last = now;

      if (frameTime > 0.25) {
        frameTime = 0.25;
      }

      accumulator += frameTime;

      while (accumulator >= this.fixedDt) {
        if (this.state == SimulationState.RUNNING) {
          this.simulation.step(this.fixedDt);
        }
        accumulator -= this.fixedDt;
      }

      // Small sleep to reduce CPU usage.
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  /**
   * Starts a new simulation using a randomly generated cluster of bodies.
   * <p>
   * Any previous simulation state is replaced. After this method is called, the
   * simulation immediately begins running.
   * </p>
   *
   * @param count the number of bodies to generate
   * @param spread the distribution radius for the generated cluster
   */
  public void startNewSimulation(int count, double spread) {
    this.simulation.reset(BodyFactory.createRandomCluster(count, spread));
    this.state = SimulationState.RUNNING;
  }

  /**
   * Stops the current simulation session.
   * <p>
   * Stopping means there is no active simulation session. The body list is
   * cleared, and the controller will not resume until a new simulation is
   * started.
   * </p>
   */
  public void stop() {
    this.simulation.reset(List.of());
    this.state = SimulationState.STOPPED;
  }

  /**
   * Pauses the active simulation session.
   * <p>
   * Has no effect if the simulation is already paused or stopped.
   * </p>
   */
  public void pause() {
    if (this.state == SimulationState.RUNNING) {
      this.state = SimulationState.PAUSED;
    }
  }

  /**
   * Resumes a paused simulation session.
   * <p>
   * Has no effect if the simulation is running or stopped.
   * </p>
   */
  public void resume() {
    if (this.state == SimulationState.PAUSED) {
      this.state = SimulationState.RUNNING;
    }
  }

  /**
   * Toggles the active simulation session between running and paused.
   * <p>
   * Has no effect while stopped.
   * </p>
   */
  public void togglePause() {
    if (this.state == SimulationState.RUNNING) {
      pause();
    } else if (this.state == SimulationState.PAUSED) {
      resume();
    }
  }

  /**
   * Returns whether the simulation is currently running.
   *
   * @return true if the simulation is running
   */
  public boolean isRunning() {
    return this.state == SimulationState.RUNNING;
  }

  /**
   * Returns whether the simulation is currently paused.
   *
   * @return true if the simulation is paused
   */
  public boolean isPaused() {
    return this.state == SimulationState.PAUSED;
  }

  /**
   * Returns whether the simulation is currently stopped.
   *
   * @return true if no active simulation session exists
   */
  public boolean isStopped() {
    return this.state == SimulationState.STOPPED;
  }

  /**
   * Sets the mass to be used for the next body spawned through user input.
   *
   * @param mass the mass value for future spawned bodies
   */
  public void setSpawnMass(double mass) {
    this.currentSpawnMass = mass;
  }

  /**
   * Translates view coordinates to simulation coordinates and adds a new body to
   * the current simulation.
   *
   * @param screenX the x-coordinate from the mouse event
   * @param screenY the y-coordinate from the mouse event
   * @param viewWidth the current viewport width
   * @param viewHeight the current viewport height
   */
  public void handleSpawnAt(int screenX, int screenY, int viewWidth, int viewHeight) {
    double worldX = screenX - (viewWidth / 2.0);
    double worldY = screenY - (viewHeight / 2.0);

    this.simulation.addBody(BodyFactory.createCustomBody(
        this.currentSpawnMass,
        new Vec2(worldX, worldY),
        new Vec2(0, 0)
    ));
  }
}