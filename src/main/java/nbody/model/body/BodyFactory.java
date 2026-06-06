package nbody.model.body;

import nbody.model.vector.Vec2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A factory class responsible for instantiating {@link Body} objects.
 */
public class BodyFactory {
  private static final Random RNG = new Random();

  /**
   * Generates a list of bodies arranged in a random cluster.
   * <p>
   * Positions are distributed using a Gaussian distribution around the origin,
   * scaled by the {@code spread} parameter. Mass and velocity are also randomized.
   * </p>
   *
   * @param count  The number of bodies to create.
   * @param spread The standard deviation for the position distribution.
   * @return A list containing {@code count} randomized {@link Body} objects.
   */
  public static List<Body> createRandomCluster(int count, double spread) {
    List<Body> bodies = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      double mass = RNG.nextDouble() * 100 + 10;
      Vec2 pos = new Vec2(RNG.nextGaussian() * spread, RNG.nextGaussian() * spread);
      Vec2 vel = new Vec2(RNG.nextGaussian() * 5, RNG.nextGaussian() * 5);

      bodies.add(new Body(mass, pos, vel));
    }
    return bodies;
  }

  /**
   * Creates a single {@link Body} with specified parameters.
   * <p>
   * This acts as a public wrapper for the {@code Body} constructor.
   * </p>
   *
   * @param mass The mass of the body.
   * @param pos  The initial position vector.
   * @param vel  The initial velocity vector.
   * @return A new {@link Body} instance.
   */
  public static Body createCustomBody(double mass, Vec2 pos, Vec2 vel) {
    return new Body(mass, pos, vel);
  }
}