package nbody.model.vector;

/**
 * A read-only interface for a 2-dimensional vector.
 * <p>
 * This interface provides a non-mutating view of vector components.
 * </p>
 * @see Vec2
 */
public interface ReadableVec2 {
  double getX();
  double getY();
}
