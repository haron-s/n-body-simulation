package nbody.model.body;

import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodyTest {

  private static final double DELTA = 1e-9;

  @Test
  void constructorInitialValuesTest() {
    Body body = new Body(
        100.0,
        new Vec2(10.0, 20.0),
        new Vec2(3.0, 4.0)
    );

    assertEquals(100.0, body.getMass(), DELTA);
    assertEquals(10.0, body.getPosition().getX(), DELTA);
    assertEquals(20.0, body.getPosition().getY(), DELTA);
    assertEquals(3.0, body.getVelocity().getX(), DELTA);
    assertEquals(4.0, body.getVelocity().getY(), DELTA);

    assertEquals(0.0, body.getAcceleration().getX(), DELTA);
    assertEquals(0.0, body.getAcceleration().getY(), DELTA);

    assertEquals(15.0, body.getRadius(), DELTA);
  }

  @Test
  void absorbTestValues() {
    Body a = new Body(100.0, new Vec2(0, 0), new Vec2(0, 0));
    Body b = new Body(25.0, new Vec2(0, 0), new Vec2(0, 0));

    a.absorb(b);

    assertEquals(125.0, a.getMass(), DELTA);
    assertEquals(Math.sqrt(125.0) * 1.5, a.getRadius(), DELTA);
  }

  @Test
  void absorbConservesMomentum() {
    Body a = new Body(10.0, new Vec2(0, 0), new Vec2(2.0, 0.0));
    Body b = new Body(30.0, new Vec2(0, 0), new Vec2(0.0, 4.0));

    a.absorb(b);

    // Expected merged velocity:
    // vx = (10*2 + 30*0) / 40 = 0.5
    // vy = (10*0 + 30*4) / 40 = 3.0
    assertEquals(0.5, a.getVelocity().getX(), DELTA);
    assertEquals(3.0, a.getVelocity().getY(), DELTA);
  }

  @Test
  void absorbDoesNotChangePosition() {
    Body a = new Body(10.0, new Vec2(5.0, -3.0), new Vec2(1.0, 1.0));
    Body b = new Body(20.0, new Vec2(100.0, 100.0), new Vec2(0.0, 0.0));

    a.absorb(b);

    assertEquals(5.0, a.getPosition().getX(), DELTA);
    assertEquals(-3.0, a.getPosition().getY(), DELTA);
  }

  @Test
  void absorbThrowsNullPointerException() {
    Body body = new Body(10.0, new Vec2(0, 0), new Vec2(0, 0));

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> body.absorb(null)
    );

    assertEquals("other cannot be null", exception.getMessage());
  }
}