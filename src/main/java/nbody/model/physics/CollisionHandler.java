package nbody.model.physics;

import nbody.model.body.Body;
import nbody.model.vector.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles detection and resolution of collisions between bodies.
 * <p>
 * This implementation resolves collisions by having the more massive body
 * absorb the smaller one, conserving momentum in the process.
 * </p>
 */
public class CollisionHandler {

  /**
   * Iterates through all pairs of bodies and resolves any overlaps.
   * * @param bodies The list of bodies to check for collisions.
   * @throws NullPointerException if {@code bodies} is null.
   */
  public static void resolveCollisions(List<Body> bodies) {
    Objects.requireNonNull(bodies, "bodies list cannot be null");

    List<Body> toRemove = new ArrayList<>();

    for (int i = 0; i < bodies.size(); i++) {
      Body a = bodies.get(i);
      if (toRemove.contains(a)) continue;

      for (int j = i + 1; j < bodies.size(); j++) {
        Body b = bodies.get(j);
        if (toRemove.contains(b)) continue;

        if (isColliding(a, b)) {
          if (a.getMass() >= b.getMass()) {
            a.absorb(b);
            toRemove.add(b);
          } else {
            b.absorb(a);
            toRemove.add(a);
            break;
          }
        }
      }
    }
    bodies.removeAll(toRemove);
  }

  /**
   * Checks if two bodies are physically overlapping.
   */
  private static boolean isColliding(Body a, Body b) {
    double dx = a.getPosition().getX() - b.getPosition().getX();
    double dy = a.getPosition().getY() - b.getPosition().getY();
    double distanceSq = dx * dx + dy * dy;
    double radiusSum = a.getRadius() + b.getRadius();
    return distanceSq < (radiusSum * radiusSum);
  }

  /**
   * Calculates the resulting velocity vector when two bodies collide and merge (inelastic collision).
   * <p>
   * This calculation is based on the Law of Conservation of Momentum:
   * {@code (m1 * v1 + m2 * v2) / (m1 + m2)}. The resulting vector represents the
   * velocity of the new combined mass.
   * </p>
   *
   * @param mass1     The mass of the first body.
   * @param velocity1 The velocity vector of the first body.
   * @param mass2     The mass of the second body.
   * @param velocity2 The velocity vector of the second body.
   * @return A new {@link Vec2} representing the velocity of the merged mass.
   * @throws ArithmeticException if the total mass is zero.
   */
  public static Vec2 calculateMergedVelocity(double mass1, Vec2 velocity1, double mass2, Vec2 velocity2) {
    double totalMass = mass1 + mass2;

    Vec2 momentum1 = velocity1.copy();
    momentum1.scale(mass1);
    Vec2 momentum2 = velocity2.copy();
    momentum2.scale(mass2);

    momentum1.add(momentum2);
    momentum1.scale(1.0 / totalMass);

    return momentum1;
  }
}