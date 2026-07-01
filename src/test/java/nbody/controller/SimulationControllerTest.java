package nbody.controller;

import nbody.model.Simulation;
import nbody.model.body.Body;
import nbody.model.integrator.Integrator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationControllerTest {

  private static class TestIntegrator implements Integrator {
    @Override
    public void step(List<Body> bodies, double deltaTime) {
      // Does nothing for simplified testing.
    }
  }

  private static class TestSimulation extends Simulation {
    boolean resetCalled = false;
    boolean addBodyCalled = false;

    int resetBodyCount = -1;
    Body addedBody;

    TestSimulation() {
      super(new TestIntegrator(), List.of());
    }

    @Override
    public void reset(List<Body> newBodies) {
      this.resetCalled = true;
      this.resetBodyCount = newBodies.size();
    }

    @Override
    public void addBody(Body body) {
      this.addBodyCalled = true;
      this.addedBody = body;
    }
  }

  @Test
  void controllerStartsStopped() {
    SimulationController controller = new SimulationController(
        new TestSimulation(),
        1.0 / 60.0
    );

    assertTrue(controller.isStopped());
    assertFalse(controller.isRunning());
    assertFalse(controller.isPaused());
  }

  @Test
  void startNewSimulationResetsSimulationAndStartsRunning() {
    TestSimulation simulation = new TestSimulation();
    SimulationController controller = new SimulationController(
        simulation,
        1.0 / 60.0
    );

    controller.startNewSimulation(25, 200.0);

    assertTrue(controller.isRunning());
    assertFalse(controller.isPaused());
    assertFalse(controller.isStopped());
    assertTrue(simulation.resetCalled);
    assertEquals(25, simulation.resetBodyCount);
  }

  @Test
  void togglePauseSwitchesBetweenRunningAndPaused() {
    SimulationController controller = new SimulationController(
        new TestSimulation(),
        1.0 / 60.0
    );

    controller.startNewSimulation(10, 100.0);
    assertTrue(controller.isRunning());

    controller.togglePause();

    assertTrue(controller.isPaused());
    assertFalse(controller.isRunning());
    assertFalse(controller.isStopped());

    controller.togglePause();

    assertTrue(controller.isRunning());
    assertFalse(controller.isPaused());
    assertFalse(controller.isStopped());
  }

  @Test
  void togglePauseDoesNothingWhenStopped() {
    SimulationController controller = new SimulationController(
        new TestSimulation(),
        1.0 / 60.0
    );

    controller.togglePause();

    assertTrue(controller.isStopped());
    assertFalse(controller.isRunning());
    assertFalse(controller.isPaused());
  }

  @Test
  void stopClearsSimulationAndChangesStateToStopped() {
    TestSimulation simulation = new TestSimulation();
    SimulationController controller = new SimulationController(
        simulation,
        1.0 / 60.0
    );

    controller.startNewSimulation(10, 100.0);
    controller.stop();

    assertTrue(controller.isStopped());
    assertFalse(controller.isRunning());
    assertFalse(controller.isPaused());
    assertTrue(simulation.resetCalled);
    assertEquals(0, simulation.resetBodyCount);
  }

  @Test
  void resumeDoesNothingWhenStopped() {
    SimulationController controller = new SimulationController(
        new TestSimulation(),
        1.0 / 60.0
    );

    controller.resume();

    assertTrue(controller.isStopped());
    assertFalse(controller.isRunning());
    assertFalse(controller.isPaused());
  }

  @Test
  void pauseDoesNothingWhenStopped() {
    SimulationController controller = new SimulationController(
        new TestSimulation(),
        1.0 / 60.0
    );

    controller.pause();

    assertTrue(controller.isStopped());
    assertFalse(controller.isRunning());
    assertFalse(controller.isPaused());
  }

  @Test
  void handleSpawnAtAddsBodyAtSimulationCoordinates() {
    TestSimulation simulation = new TestSimulation();
    SimulationController controller = new SimulationController(
        simulation,
        1.0 / 60.0
    );

    controller.handleSpawnAt(600, 450, 1000, 800);

    assertTrue(simulation.addBodyCalled);
    assertNotNull(simulation.addedBody);
    assertEquals(100.0, simulation.addedBody.getPosition().getX(), 1e-9);
    assertEquals(50.0, simulation.addedBody.getPosition().getY(), 1e-9);
  }

  @Test
  void setSpawnMassAffectsNextSpawnedBody() {
    TestSimulation simulation = new TestSimulation();
    SimulationController controller = new SimulationController(
        simulation,
        1.0 / 60.0
    );

    controller.setSpawnMass(250.0);
    controller.handleSpawnAt(500, 400, 1000, 800);

    assertTrue(simulation.addBodyCalled);
    assertEquals(250.0, simulation.addedBody.getMass(), 1e-9);
  }
}
