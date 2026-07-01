package nbody.model;

import nbody.model.body.Body;
import nbody.model.body.BodyFactory;
import nbody.model.body.ViewableBody;
import nbody.model.integrator.Integrator;
import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

  private static final double DELTA = 1e-9;

  private static class TestIntegrator implements Integrator {
    @Override
    public void step(List<Body> bodies, double deltaTime) {
      // does nothing for easier testing
    }
  }

  private static class TestListener implements Simulation.Listener {
    List<? extends ViewableBody> bodies;
    double time;
    boolean called = false;

    @Override
    public void onStepCompleted(List<? extends ViewableBody> bodies, double currentTime) {
      this.bodies = bodies;
      this.time = currentTime;
      this.called = true;
    }
  }

  @Test
  void constructorComputesInitialAccelerations() {
    Body a = BodyFactory.createCustomBody(10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0));
    Body b = BodyFactory.createCustomBody(10.0, new Vec2(100.0, 0.0), new Vec2(0.0, 0.0));

    new Simulation(new TestIntegrator(), List.of(a, b));

    assertTrue(a.getAcceleration().getX() > 0.0);
    assertTrue(b.getAcceleration().getX() < 0.0);
  }

  @Test
  void resetNullListTest() {
    Simulation simulation = new Simulation(new TestIntegrator(), List.of());

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> simulation.reset(null)
    );

    assertEquals("newBodies list cannot be null", exception.getMessage());
  }

  @Test
  void stepTest() {
    Simulation simulation = new Simulation(new TestIntegrator(), List.of());
    TestListener listener = new TestListener();

    simulation.addListener(listener);
    simulation.step(0.25);

    assertTrue(listener.called);
    assertEquals(0.25, listener.time, DELTA);
  }

  @Test
  void stepSnapshotTest() {
    Body initialBody = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );

    Simulation simulation = new Simulation(new TestIntegrator(), List.of(initialBody));
    TestListener listener = new TestListener();

    simulation.addListener(listener);
    simulation.step(1.0);

    assertNotNull(listener.bodies);
    assertEquals(1, listener.bodies.size());

    simulation.addBody(
        BodyFactory.createCustomBody(20.0, new Vec2(100.0, 0.0), new Vec2(0.0, 0.0))
    );

    assertEquals(1, listener.bodies.size());
  }

  @Test
  void addBodyNextStepSnapshotTest() {
    Simulation simulation = new Simulation(new TestIntegrator(), List.of());
    Body body = BodyFactory.createCustomBody(10.0, new Vec2(1.0, 2.0), new Vec2(0.0, 0.0));
    TestListener listener = new TestListener();

    simulation.addBody(body);
    simulation.addListener(listener);
    simulation.step(1.0);

    assertEquals(1, listener.bodies.size());
    assertSame(body, listener.bodies.get(0));
  }

  @Test
  void resetTest() {
    Body oldBody = BodyFactory.createCustomBody(10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0));
    Simulation simulation = new Simulation(new TestIntegrator(), List.of(oldBody));

    simulation.step(5.0);

    Body newBody = BodyFactory.createCustomBody(20.0, new Vec2(10.0, 0.0), new Vec2(0.0, 0.0));
    simulation.reset(List.of(newBody));

    TestListener listener = new TestListener();
    simulation.addListener(listener);
    simulation.step(0.5);

    assertEquals(0.5, listener.time, DELTA);
    assertEquals(1, listener.bodies.size());
    assertSame(newBody, listener.bodies.get(0));
  }

  @Test
  void stepCallsIntegratorWithProvidedDeltaTime() {
    class RecordingIntegrator implements Integrator {
      double testTime;

      @Override
      public void step(List<Body> bodies, double deltaTime) {
        this.testTime = deltaTime;
      }
    }

    RecordingIntegrator integrator = new RecordingIntegrator();
    Simulation simulation = new Simulation(integrator, new ArrayList<>());

    simulation.step(0.125);

    assertEquals(0.125, integrator.testTime, DELTA);
  }
}
