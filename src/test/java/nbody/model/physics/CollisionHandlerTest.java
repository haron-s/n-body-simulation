package nbody.model.physics;

import nbody.model.body.Body;
import nbody.model.body.BodyFactory;
import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollisionHandlerTest {

  private static final double DELTA = 1e-9;

  @Test
  void resolveCollisionsWithNullThrowsException() {
    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> CollisionHandler.resolveCollisions(null)
    );

    assertEquals("bodies list cannot be null", exception.getMessage());
  }

  @Test
  void resolveCollisionsWithEmptyListDoesNotThrow() {
    assertDoesNotThrow(() ->
        CollisionHandler.resolveCollisions(new ArrayList<>())
    );
  }

  @Test
  void resolveCollisionsNotOverlapping() {
    Body a = BodyFactory.createCustomBody(
        100.0,
        new Vec2(0.0, 0.0),
        new Vec2(1.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        100.0,
        new Vec2(1000.0, 0.0),
        new Vec2(-1.0, 0.0)
    );

    List<Body> bodies = new ArrayList<>(List.of(a, b));

    CollisionHandler.resolveCollisions(bodies);

    assertEquals(2, bodies.size());
    assertTrue(bodies.contains(a));
    assertTrue(bodies.contains(b));
  }

  @Test
  void resolveCollisionsOverlappingBodies() {
    Body larger = BodyFactory.createCustomBody(
        100.0,
        new Vec2(0.0, 0.0),
        new Vec2(1.0, 0.0)
    );
    Body smaller = BodyFactory.createCustomBody(
        25.0,
        new Vec2(1.0, 0.0),
        new Vec2(0.0, 2.0)
    );

    List<Body> bodies = new ArrayList<>(List.of(larger, smaller));

    CollisionHandler.resolveCollisions(bodies);

    assertEquals(1, bodies.size());
    assertSame(larger, bodies.get(0));
    assertFalse(bodies.contains(smaller));

    assertEquals(125.0, larger.getMass(), DELTA);
    assertEquals(Math.sqrt(125.0) * 1.5, larger.getRadius(), DELTA);
  }

  @Test
  void resolveCollisionsKeepsMoreMassiveBody() {
    Body smaller = BodyFactory.createCustomBody(
        25.0,
        new Vec2(0.0, 0.0),
        new Vec2(4.0, 0.0)
    );
    Body larger = BodyFactory.createCustomBody(
        100.0,
        new Vec2(1.0, 0.0),
        new Vec2(0.0, 1.0)
    );

    List<Body> bodies = new ArrayList<>(List.of(smaller, larger));

    CollisionHandler.resolveCollisions(bodies);

    assertEquals(1, bodies.size());
    assertSame(larger, bodies.get(0));
    assertFalse(bodies.contains(smaller));

    assertEquals(125.0, larger.getMass(), DELTA);
  }

  @Test
  void resolveCollisionsEqual() {
    // the more first body of the two in the list should remain after absorption
    Body first = BodyFactory.createCustomBody(
        50.0,
        new Vec2(0.0, 0.0),
        new Vec2(1.0, 0.0)
    );
    Body second = BodyFactory.createCustomBody(
        50.0,
        new Vec2(1.0, 0.0),
        new Vec2(0.0, 1.0)
    );

    List<Body> bodies = new ArrayList<>(List.of(first, second));

    CollisionHandler.resolveCollisions(bodies);

    assertEquals(1, bodies.size());
    assertSame(first, bodies.get(0));
    assertFalse(bodies.contains(second));

    assertEquals(100.0, first.getMass(), DELTA);
  }

  @Test
  void resolveCollisionsUsesMomentumConservation() {
    Body a = BodyFactory.createCustomBody(
        10.0,
        new Vec2(0.0, 0.0),
        new Vec2(2.0, 0.0)
    );
    Body b = BodyFactory.createCustomBody(
        30.0,
        new Vec2(1.0, 0.0),
        new Vec2(0.0, 4.0)
    );

    List<Body> bodies = new ArrayList<>(List.of(a, b));

    CollisionHandler.resolveCollisions(bodies);

    assertEquals(1, bodies.size());
    assertSame(b, bodies.get(0));

    assertEquals(0.5, b.getVelocity().getX(), DELTA);
    assertEquals(3.0, b.getVelocity().getY(), DELTA);
  }

  @Test
  void calculateMergedVelocity() {
    Vec2 result = CollisionHandler.calculateMergedVelocity(
        10.0, new Vec2(2.0, 0.0),
        30.0, new Vec2(0.0, 4.0)
    );

    assertEquals(0.5, result.getX(), DELTA);
    assertEquals(3.0, result.getY(), DELTA);
  }

  @Test
  void calculateMergedVelocityDoesNotMutateInputVectors() {
    Vec2 v1 = new Vec2(2.0, 0.0);
    Vec2 v2 = new Vec2(0.0, 4.0);

    CollisionHandler.calculateMergedVelocity(10.0, v1, 30.0, v2);

    assertEquals(2.0, v1.getX(), DELTA);
    assertEquals(0.0, v1.getY(), DELTA);
    assertEquals(0.0, v2.getX(), DELTA);
    assertEquals(4.0, v2.getY(), DELTA);
  }
}