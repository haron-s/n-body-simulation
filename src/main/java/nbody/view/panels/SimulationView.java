package nbody.view.panels;

import nbody.model.body.ViewableBody;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Class responsible for rendering the current state of the simulation.
 * <p>
 * This view displays a collection of {@link ViewableBody} instances by drawing
 * them as circles in a 2D coordinate system centered in the panel.
 * </p>
 * <p>
 * The view is passive and does not modify simulation state. It expects to receive
 * immutable or snapshot data from the model layer.
 * </p>
 */
public class SimulationView extends JPanel {
  private List<? extends ViewableBody> bodies = List.of();

  /**
   * Constructs a new simulation view panel with a black background.
   */
  public SimulationView() {
    setBackground(Color.BLACK);
    setOpaque(true);
  }

  /**
   * Updates the set of bodies to be rendered and repaints.
   *
   * @param newBodies the list of bodies to render
   */
  public void updateBodies(List<? extends ViewableBody> newBodies) {
    this.bodies = newBodies;
    repaint();
  }

  /**
   * Renders the current bodies onto the panel.
   * <p>
   * The coordinate system is translated so that the origin (0,0) is at the center
   * of the panel. Each body is drawn as a filled circle using its position and radius.
   * </p>
   *
   * @param g the graphics context used for painting
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.WHITE);

    for (ViewableBody body : bodies) {
      double radius = body.getRadius();
      double x = body.getPosition().getX() - radius;
      double y = body.getPosition().getY() - radius;

      g2d.fillOval((int) x, (int) y, (int) (2 * radius), (int) (2 * radius));
    }
  }
}