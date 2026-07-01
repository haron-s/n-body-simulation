package nbody.model.physics;

import nbody.model.body.Body;
import nbody.model.body.BodyFactory;

import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GravityTest {

  private static final double DELTA = 1e-9;

  @Test
  void applyGravityDirectionTest() {
    Body a = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        10.0, new Vec2(100.0, 0.0), new Vec2(0.0, 0.0)
    );

    Gravity.applyGravity(a, b);

    // A should accelerate toward +x, B toward -x
    assertTrue(a.getAcceleration().getX() > 0.0);
    assertTrue(b.getAcceleration().getX() < 0.0);

    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);
  }

  @Test
  void applyGravityMagnitudeTest() {
    Body light = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );
    Body heavy = BodyFactory.createCustomBody(
        100.0, new Vec2(100.0, 0.0), new Vec2(0.0, 0.0)
    );

    Gravity.applyGravity(light, heavy);

    // Light body should feel stronger acceleration
    assertTrue(
        Math.abs(light.getAcceleration().getX()) >
            Math.abs(heavy.getAcceleration().getX())
    );
  }

  @Test
  void applyGravityOverlapping() {
    Body a = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        10.0, new Vec2(0.5, 0.0), new Vec2(0.0, 0.0)
    );

    Gravity.applyGravity(a, b);

    assertEquals(0.0, a.getAcceleration().getX(), DELTA);
    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getX(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);
  }

  @Test
  void applyGravityDoesNothingWhenDistanceIsZero() {
    Body a = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        10.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );

    Gravity.applyGravity(a, b);

    assertEquals(0.0, a.getAcceleration().getX(), DELTA);
    assertEquals(0.0, a.getAcceleration().getY(), DELTA);
    assertEquals(0.0, b.getAcceleration().getX(), DELTA);
    assertEquals(0.0, b.getAcceleration().getY(), DELTA);
  }

  @Test
  void applyGravityEqualMasses() {
    Body a = BodyFactory.createCustomBody(
        50.0, new Vec2(0.0, 0.0), new Vec2(0.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        50.0, new Vec2(100.0, 0.0), new Vec2(0.0, 0.0)
    );

    Gravity.applyGravity(a, b);

    assertEquals(
        Math.abs(a.getAcceleration().getX()),
        Math.abs(b.getAcceleration().getX()),
        DELTA
    );
  }
}