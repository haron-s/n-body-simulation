package nbody.view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code HelpPanel} class is a utility used to create a standardized display of
 * simulation controls.
 * <p>
 * It provides information on keyboard and mouse interactions such as pausing and
 * spawning bodies.
 * </p>
 */
class HelpPanel {
  /**
   * Creates and returns a panel containing the control instructions.
   * @return a {@link JPanel} containing the instruction text
   */
  static JPanel createHelpPanel() {
    JPanel helpPanel = new JPanel();
    helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
    helpPanel.setBackground(Color.BLACK);

    String[] instructions = {
        "Controls:",
        "ESC - Pause Simulation",
        "Left Click - Spawn Body",
        "Right Click - Set Spawn Mass"
    };

    for (String text : instructions) {
      JLabel label = new JLabel(text);
      label.setForeground(Color.GRAY);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      label.setFont(new Font("Monospaced", Font.PLAIN, 12));
      helpPanel.add(label);
    }

    return helpPanel;
  }
}
