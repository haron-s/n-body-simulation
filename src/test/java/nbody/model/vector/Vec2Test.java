package nbody.model.vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vec2Test {

  private static final double DELTA = 1e-9;

  @Test
  void constructorTest() {
    Vec2 v = new Vec2(3.0, -4.0);

    assertEquals(3.0, v.getX(), DELTA);
    assertEquals(-4.0, v.getY(), DELTA);
  }

  @Test
  void lengthMagnitudeTest() {
    Vec2 v = new Vec2(3.0, 4.0);

    assertEquals(5.0, v.length(), DELTA);
  }

  @Test
  void normalizeTest() {
    Vec2 v = new Vec2(3.0, 4.0);

    Vec2 result = v.normalize();

    assertSame(v, result);
    assertEquals(1.0, v.length(), DELTA);
    assertEquals(0.6, v.getX(), DELTA);
    assertEquals(0.8, v.getY(), DELTA);
  }

  @Test
  void normalizeZeroTest() {
    Vec2 v = new Vec2(0.0, 0.0);

    Vec2 result = v.normalize();

    assertSame(v, result);
    assertEquals(0.0, v.getX(), DELTA);
    assertEquals(0.0, v.getY(), DELTA);
  }

  @Test
  void addMutateTest() {
    // should mutate in-place
    Vec2 v = new Vec2(1.0, 2.0);
    Vec2 other = new Vec2(3.0, 4.0);

    Vec2 result = v.add(other);

    assertSame(v, result);
    assertEquals(4.0, v.getX(), DELTA);
    assertEquals(6.0, v.getY(), DELTA);
  }

  @Test
  void addNullThrowsException() {
    Vec2 v = new Vec2(1.0, 2.0);

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> v.add(null)
    );

    assertEquals("other cannot be null", exception.getMessage());
  }

  @Test
  void subMutateTest() {
    // should mutate in-place
    Vec2 v = new Vec2(5.0, 7.0);
    Vec2 other = new Vec2(2.0, 3.0);

    Vec2 result = v.sub(other);

    assertSame(v, result);
    assertEquals(3.0, v.getX(), DELTA);
    assertEquals(4.0, v.getY(), DELTA);
  }

  @Test
  void subNullThrowsException() {
    Vec2 v = new Vec2(1.0, 2.0);

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> v.sub(null)
    );

    assertEquals("other cannot be null", exception.getMessage());
  }

  @Test
  void addScaledMutateTest() {
    // should mutate in-place
    Vec2 v = new Vec2(1.0, 2.0);
    Vec2 other = new Vec2(10.0, -4.0);

    Vec2 result = v.addScaled(other, 0.5);

    assertSame(v, result);
    assertEquals(6.0, v.getX(), DELTA);
    assertEquals(0.0, v.getY(), DELTA);
  }

  @Test
  void addScaledNullThrowsException() {
    Vec2 v = new Vec2(1.0, 2.0);

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> v.addScaled(null, 0.5)
    );

    assertEquals("other cannot be null", exception.getMessage());
  }

  @Test
  void scaleMutateTest() {
    // should mutate in-place
    Vec2 v = new Vec2(2.0, -3.0);

    Vec2 result = v.scale(4.0);

    assertSame(v, result);
    assertEquals(8.0, v.getX(), DELTA);
    assertEquals(-12.0, v.getY(), DELTA);
  }

  @Test
  void setTest() {
    Vec2 v = new Vec2(1.0, 2.0);

    v.set(8.0, -9.0);

    assertEquals(8.0, v.getX(), DELTA);
    assertEquals(-9.0, v.getY(), DELTA);
  }

  @Test
  void setVectorCopiesValues() {
    Vec2 v = new Vec2(1.0, 2.0);
    Vec2 other = new Vec2(8.0, -9.0);

    v.set(other);

    assertEquals(8.0, v.getX(), DELTA);
    assertEquals(-9.0, v.getY(), DELTA);
  }

  @Test
  void setVectorNullThrowsException() {
    Vec2 v = new Vec2(1.0, 2.0);

    NullPointerException exception = assertThrows(
        NullPointerException.class,
        () -> v.set(null)
    );

    assertEquals("other cannot be null", exception.getMessage());
  }

  @Test
  void zeroTest() {
    Vec2 v = new Vec2(5.0, -7.0);

    v.zero();

    assertEquals(0.0, v.getX(), DELTA);
    assertEquals(0.0, v.getY(), DELTA);
  }

  @Test
  void perpendicularTest() {
    // should mutate in-place and
    // rotate 90 degrees counterclockwise
    Vec2 v = new Vec2(3.0, 4.0);

    Vec2 p = v.perpendicular();

    assertNotSame(v, p);
    assertEquals(-4.0, p.getX(), DELTA);
    assertEquals(3.0, p.getY(), DELTA);
  }

  @Test
  void copyTest() {
    // should not mutate original
    Vec2 v = new Vec2(3.0, 4.0);

    Vec2 copy = v.copy();

    assertNotSame(v, copy);
    assertEquals(3.0, copy.getX(), DELTA);
    assertEquals(4.0, copy.getY(), DELTA);

    copy.set(100.0, 200.0);

    assertEquals(3.0, v.getX(), DELTA);
    assertEquals(4.0, v.getY(), DELTA);
  }
}