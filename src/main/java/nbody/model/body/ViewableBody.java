package nbody.model.body;

import nbody.model.vector.ReadableVec2;

/**
 * A read-only interface, intended for use by the view layer.
 * @see Body
 */
public interface ViewableBody {
  /**
   * Returns the current position of the body as a read-only vector.
   * @return the {@link ReadableVec2} representing the body's position.
   */
  ReadableVec2 getPosition();
  /**
   * Returns the current velocity of the body as a read-only vector.
   * @return the {@link ReadableVec2} representing the body's velocity.
   */
  ReadableVec2 getVelocity();
  /**
   * Returns the radius of the body.
   * @return the radius.
   */
  double getRadius();
}