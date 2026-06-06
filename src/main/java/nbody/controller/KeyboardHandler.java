package nbody.controller;

import nbody.view.NbodyView;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The {@code KeyboardHandler} class handles keyboard interactions for the N-Body simulation.
 * It extends {@link KeyAdapter} to listen for key presses that control the simulation's
 * state, such as pausing and resuming.
 */
public class KeyboardHandler extends KeyAdapter {
  private final SimulationController controller;
  private final NbodyView view;

  /**
   * Constructs a new KeyboardHandler.
   *
   * @param controller the controller to trigger state changes
   * @param view the view to update the UI based on state changes
   */
  public KeyboardHandler(SimulationController controller, NbodyView view) {
    this.controller = controller;
    this.view = view;
  }

  /**
   * Invoked when a key has been pressed.
   * <p>
   * This implementation listens for the {@code KeyEvent.VK_ESCAPE} key to toggle
   * the simulation's pause state. When toggled, it updates both the
   * {@link SimulationController} logic and the {@link NbodyView} display.
   * </p>
   *
   * @param e the event to be processed
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      if (view.isMenuShowing()) {
        return; // ignore in main menu
      }
      controller.togglePause();
      if (controller.isPaused()) {
        view.showPause();
      } else {
        view.showSimulation();
      }
    }
  }
}