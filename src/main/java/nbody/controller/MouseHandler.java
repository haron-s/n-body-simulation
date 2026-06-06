package nbody.controller;

import nbody.view.NbodyView;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The {@code MouseHandler} class handles mouse interactions within the N-Body simulation.
 * It extends {@link MouseAdapter} to listen for mouse clicks that trigger simulation
 * actions.
 */
public class MouseHandler extends MouseAdapter {
  private final SimulationController controller;
  private final NbodyView view;

  /**
   * Constructs a new MouseHandler.
   *
   * @param controller the controller to send commands to
   * @param view the UI and view
   */
  public MouseHandler(SimulationController controller, NbodyView view) {
    this.controller = controller;
    this.view = view;
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   * <p>
   * If the left mouse button is pressed, it triggers a body spawn at the click coordinates.
   * If the right mouse button is pressed, it opens a dialog to set the mass for future spawns.
   * </p>
   *
   * @param e the event to be processed
   */
  @Override
  public void mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.controller.handleSpawnAt(e.getX(), e.getY(), this.view.getWidth(), this.view.getHeight());
    } else if (SwingUtilities.isRightMouseButton(e)) {
      showMassInputDialog();
    }
  }

  private void showMassInputDialog() {
    String input = JOptionPane.showInputDialog(this.view, "Next body mass:");
    if (input != null && !input.isEmpty()) {
      try {
        this.controller.setSpawnMass(Double.parseDouble(input));
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this.view, "Invalid number.");
      }
    }
  }
}