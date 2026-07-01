package nbody.model.body;


import nbody.model.vector.Vec2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BodyFactoryTest {

  private static final double DELTA = 1e-9;

  @Test
  void createCustomBodyTest() {
    Vec2 position = new Vec2(10.0, -5.0);
    Vec2 velocity = new Vec2(2.5, 3.5);

    Body body = BodyFactory.createCustomBody(64.0, position, velocity);

    assertEquals(64.0, body.getMass(), DELTA);
    assertSame(position, body.getPosition());
    assertSame(velocity, body.getVelocity());
    assertEquals(Math.sqrt(64.0) * 1.5, body.getRadius(), DELTA);
  }

  @Test
  void createRandomClusterCount() {
    List<Body> bodies = BodyFactory.createRandomCluster(50, 200.0);

    assertEquals(50, bodies.size());
  }

  @Test
  void createRandomClusterWithZeroCount() {
    List<Body> bodies = BodyFactory.createRandomCluster(0, 200.0);

    assertTrue(bodies.isEmpty());
  }

  @Test
  void createRandomClusterExpectedMassRange() {
    List<Body> bodies = BodyFactory.createRandomCluster(100, 200.0);

    for (Body body : bodies) {
      assertTrue(body.getMass() >= 10.0);
      assertTrue(body.getMass() < 110.0);
      assertEquals(Math.sqrt(body.getMass()) * 1.5, body.getRadius(), DELTA);
    }
  }

  @Test
  void createRandomClusterInitializesAccelerationToZero() {
    List<Body> bodies = BodyFactory.createRandomCluster(25, 200.0);

    for (Body body : bodies) {
      assertEquals(0.0, body.getAcceleration().getX(), DELTA);
      assertEquals(0.0, body.getAcceleration().getY(), DELTA);
    }
  }

  @Test
  void createRandomClusterReturnsMutableList() {
    List<Body> bodies = BodyFactory.createRandomCluster(1, 200.0);

    assertDoesNotThrow(() -> bodies.add(
        BodyFactory.createCustomBody(10.0, new Vec2(0, 0), new Vec2(0, 0))
    ));
  }
}