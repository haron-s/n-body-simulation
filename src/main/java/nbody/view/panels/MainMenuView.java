package nbody.view.panels;

import nbody.controller.SimulationController;
import javax.swing.*;
import java.awt.*;

/**
 * The {@code MainMenuView} class provides the initial main menu screen for the simulation.
 * <p>
 * It allows users to specify parameters such as the initial body count and spread before
 * starting the simulation. It also displays a help panel with control instructions.
 * </p>
 */
public class MainMenuView extends JPanel {
  private final JTextField countField = new JTextField("100", 5);
  private final JTextField spreadField = new JTextField("200", 5);
  private final SimulationController controller;
  private final Runnable onStartCallback;

  /**
   * Constructs a new {@code MainMenuView}.
   * @param controller the {@link SimulationController} used to resetWithRandomCluster the simulation with user-defined values
   * @param onStartCallback a {@link Runnable} to be executed when the user starts the simulation
   */
  public MainMenuView(SimulationController controller, Runnable onStartCallback) {
    this.controller = controller;
    this.onStartCallback = onStartCallback;

    setLayout(new GridBagLayout());
    setBackground(Color.BLACK);
    initUI();
  }

  /**
   * Initializes the user interface components using {@link GridBagLayout}.
   * <p>
   * This includes the title, input fields for simulation parameters, the start button,
   * and a help panel anchored at the bottom.
   * </p>
   */
  private void initUI() {
    JLabel title = new JLabel("N-Body Simulator");
    title.setFont(new Font("Arial", Font.BOLD, 48));
    title.setForeground(Color.WHITE);

    JLabel author = new JLabel("By Haron");
    author.setFont(new Font("Arial", Font.PLAIN, 16));
    author.setForeground(Color.WHITE);

    styleTextField(this.countField);
    styleTextField(this.spreadField);

    JButton startBtn = new JButton("Start Simulation");
    startBtn.addActionListener(e -> handleStart());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(10, 0, 10, 0);

    gbc.gridy = 0;
    add(title, gbc);

    gbc.gridy = 1;
    add(author, gbc);

    gbc.gridy = 2;
    add(createInputRow("Body Count: ", this.countField), gbc);

    gbc.gridy = 3;
    add(createInputRow("Initial Spread: ", this.spreadField), gbc);

    gbc.gridy = 4;
    add(startBtn, gbc);

// help text
    gbc.gridy = 5;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.PAGE_END;
    add(HelpPanel.createHelpPanel(), gbc);
  }

  /**
   * Creates a row containing a label and a text field for parameter input.
   * @param labelText the text to display in the label
   * @param field the {@link JTextField} for user input
   * @return a {@link JPanel} containing the formatted input row
   */
  private JPanel createInputRow(String labelText, JTextField field) {
    JPanel row = new JPanel();
    row.setBackground(Color.BLACK);
    JLabel label = new JLabel(labelText);
    label.setForeground(Color.WHITE);
    row.add(label);
    row.add(field);
    return row;
  }

  /**
   * Applies consistent visual styling to the provided text field.
   * @param field the {@link JTextField} to style
   */
  private void styleTextField(JTextField field) {
    field.setBackground(Color.DARK_GRAY);
    field.setForeground(Color.WHITE);
    field.setCaretColor(Color.WHITE);
  }

  /**
   * Validates user input and triggers the simulation start.
   * <p>
   * If the input is valid, it calls {@code controller.resetWithRandomCluster()} and executes the
   * {@code onStartCallback}. Otherwise, it displays an error message.
   * </p>
   */
  private void handleStart() {
    try {
      int count = Integer.parseInt(this.countField.getText());
      double spread = Double.parseDouble(this.spreadField.getText());
      if (count <= 0 || spread <= 0) throw new NumberFormatException();

      this.controller.startNewSimulation(count, spread);
      this.onStartCallback.run();
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Please enter valid positive numbers for count and spread.");
    }
  }
}