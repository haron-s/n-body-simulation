package nbody.model.physics;

import nbody.model.body.Body;
import nbody.model.body.BodyFactory;

import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccelerationSolverTest {

  private static final double DELTA = 1e-9;

  @Test
  void computeAccelerationsThrowsNullPointerException() {
    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> AccelerationSolver.computeAccelerations(null)
    );

    assertEquals("bodies cannot be null", exception.getMessage());
  }

  @Test
  void computeAccelerationsWithEmptyList() {
    assertDoesNotThrow(() ->
        AccelerationSolver.computeAccelerations(new ArrayList<>())
    );
  }

  @Test
  void computeAccelerationsWithSingleBody() {
    Body body = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );
    body.getAcceleration().set(5.0, -3.0);

    AccelerationSolver.computeAccelerations(List.of(body));

    assertEquals(0.0, body.getAcceleration().getX(), DELTA);
    assertEquals(0.0, body.getAcceleration().getY(), DELTA);
  }

  @Test
  void computeAccelerationsResetsExistingAccelerations() {
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

    a.getAcceleration().set(999.0, 999.0);
    b.getAcceleration().set(-999.0, -999.0);

    AccelerationSolver.computeAccelerations(List.of(a, b));

    // previous acceleration should have no bearing on new accelerations
    assertTrue(a.getAcceleration().getX() > 0.0);
    assertTrue(b.getAcceleration().getX() < 0.0);

    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);
  }

  @Test
  void computeAccelerationsAppliesOppositeDirectionsForTwoBodies() {
    Body a = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        20.0,
        new Vec2(100.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    AccelerationSolver.computeAccelerations(List.of(a, b));

    assertTrue(a.getAcceleration().getX() > 0.0);
    assertTrue(b.getAcceleration().getX() < 0.0);

    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);
  }

  @Test
  void computeAccelerationsLargerAccelerationForLighterBody() {
    Body light = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );
    Body heavy = BodyFactory.createCustomBody(
        100.0,
        new Vec2(100.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    AccelerationSolver.computeAccelerations(List.of(light, heavy));

    assertTrue(
        Math.abs(light.getAcceleration().getX()) >
            Math.abs(heavy.getAcceleration().getX())
    );
  }

  @Test
  void computeAccelerationsWithNullBodyThrowsNullPointerException() {
    Body body = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(0.0, 0.0)
    );

    assertThrows(
        NullPointerException.class,
        () -> AccelerationSolver.computeAccelerations(List.of(body, null))
    );
  }
}