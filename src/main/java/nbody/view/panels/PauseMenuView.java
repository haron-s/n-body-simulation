package nbody.view.panels;

import nbody.controller.SimulationController;
import javax.swing.*;
import java.awt.*;

import static nbody.view.panels.HelpPanel.createHelpPanel;

/**
 * The {@code PauseMenuView} class provides an overlay menu displayed when the
 * simulation is paused.
 * <p>
 * It offers options to resume the current simulation or quit to the main menu,
 * alongside a reminder of the controls.
 * </p>
 */
public class PauseMenuView extends JPanel {
  private final SimulationController controller;
  private final Runnable onResume;
  private final Runnable onQuit;

  /**
   * Constructs a new {@code PauseMenuView}.
   * @param controller the {@link SimulationController} to manage the pause state
   * @param onResume a {@link Runnable} executed when the user resumes the simulation
   * @param onQuit a {@link Runnable} executed when the user returns to the main menu
   */
  public PauseMenuView(SimulationController controller, Runnable onResume, Runnable onQuit) {
    this.controller = controller;
    this.onResume = onResume;
    this.onQuit = onQuit;

    setLayout(new GridBagLayout());
    setBackground(Color.BLACK);
    initUI();
  }

  /**
   * Initializes the pause menu UI components.
   * <p>
   * Components include the "PAUSED" header, resume and quit buttons,
   * and a help panel.
   * </p>
   */
  private void initUI() {
    JLabel label = new JLabel("PAUSED");
    label.setFont(new Font("Arial", Font.BOLD, 36));
    label.setForeground(Color.WHITE);

    JButton resumeBtn = new JButton("Resume");
    resumeBtn.addActionListener(e -> {
      this.controller.togglePause();
      this.onResume.run();
    });

    JButton menuBtn = new JButton("Quit to Main Menu");
    menuBtn.addActionListener(e -> {
      this.controller.stop();
      this.onQuit.run();
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(10, 0, 10, 0);

    gbc.gridy = 0;
    add(label, gbc);

    gbc.gridy = 1;
    add(resumeBtn, gbc);

    gbc.gridy = 2;
    add(menuBtn, gbc);

    // help Text at the bottom
    gbc.gridy = 3;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.PAGE_END;
    add(createHelpPanel(), gbc);
  }
}