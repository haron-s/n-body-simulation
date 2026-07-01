package nbody.model.integrator;

import nbody.model.body.Body;
import nbody.model.body.BodyFactory;
import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeapfrogIntegratorTest {

  private static final double DELTA = 1e-9;

  @Test
  void stepWithNullBodiesThrowsException() {
    LeapfrogIntegrator integrator = new LeapfrogIntegrator();

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> integrator.step(null, 1.0)
    );

    assertEquals("bodies cannot be null", exception.getMessage());
  }

  @Test
  void stepWithEmptyListDoesNotThrow() {
    LeapfrogIntegrator integrator = new LeapfrogIntegrator();

    assertDoesNotThrow(() -> integrator.step(new ArrayList<>(), 1.0));
  }

  @Test
  void stepMoveSingleBodyTest() {
    // checks if integrator moves bodies correctly when acceleration does not change
    LeapfrogIntegrator integrator = new LeapfrogIntegrator();

    Body body = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(2.0, -3.0)
    );

    integrator.step(List.of(body), 2.0);

    assertEquals(4.0, body.getPosition().getX(), DELTA);
    assertEquals(-6.0, body.getPosition().getY(), DELTA);

    assertEquals(2.0, body.getVelocity().getX(), DELTA);
    assertEquals(-3.0, body.getVelocity().getY(), DELTA);
  }

  @Test
  void stepAppliesInitialAccelerationHalfKickBeforeDrift() {
    LeapfrogIntegrator integrator = new LeapfrogIntegrator();

    Body body = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    body.getAcceleration().set(4.0, 0.0);

    integrator.step(List.of(body), 2.0);

    // Half-kick: velocity += acceleration * dt/2 = 4 * 1 = 4
    // Drift: position += velocity * dt = 4 * 2 = 8
    // Then acceleration is recomputed. With one body, it becomes zero.
    assertEquals(8.0, body.getPosition().getX(), DELTA);
    assertEquals(0.0, body.getPosition().getY(), DELTA);

    assertEquals(4.0, body.getVelocity().getX(), DELTA);
    assertEquals(0.0, body.getVelocity().getY(), DELTA);
  }

  @Test
  void stepRecomputesAcceleration() {
    LeapfrogIntegrator integrator = new LeapfrogIntegrator();

    Body a = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    Body b = BodyFactory.createCustomBody(
        10.0,
        new Vec2(100.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    integrator.step(List.of(a, b), 1.0);

    assertNotEquals(0.0, a.getAcceleration().getX(), DELTA);
    assertNotEquals(0.0, b.getAcceleration().getX(), DELTA);

    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);

    assertTrue(a.getAcceleration().getX() > 0.0);
    assertTrue(b.getAcceleration().getX() < 0.0);
  }
}