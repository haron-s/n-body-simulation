package nbody.view;

import nbody.model.Simulation;
import nbody.model.body.ViewableBody;
import nbody.controller.SimulationController;
import nbody.view.panels.MainMenuView;
import nbody.view.panels.PauseMenuView; // Import the new class
import nbody.view.panels.SimulationView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The {@code NbodyView} class represents the main GUI for the N-Body simulation.
 * It implements {@link Simulation.Listener} to receive updates from the model and
 * utilizes a {@link CardLayout} to switch between the main menu, simulation view,
 * and pause screen.
 */
public class NbodyView extends JFrame implements Simulation.Listener {
    private enum ViewState {
        MENU,
        SIMULATION,
        PAUSE
    }
    private ViewState currentView = ViewState.MENU;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final SimulationView simPanel = new SimulationView();
    private SimulationController controller;

    /**
     * Constructs a new {@code NbodyView} frame.
     */
    public NbodyView() {
        setTitle("N-Body Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.container.setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1000, 800));

        add(this.container);
        setFocusable(true);
    }

    /**
     * Links the simulation controller and initializes the sub-panels.
     * This method sets up the {@link MainMenuView} and {@link PauseMenuView},
     * adds them to the card layout, and makes the frame visible.
     *
     * @param controller the {@link SimulationController} used to bridge the view and model
     * @exception IllegalStateException controller may only be set once
     */
    public void setController(SimulationController controller) {
        if (this.controller != null) {
            throw new IllegalStateException("Controller has already been set.");
        }

        this.controller = controller;

        MainMenuView mainMenuView = new MainMenuView(controller, this::showSimulation);
        PauseMenuView pauseMenuView = new PauseMenuView(
            controller,
            this::showSimulation,
            this::showMenu
        );

        this.container.add(mainMenuView, "MENU");
        this.container.add(this.simPanel, "SIM");
        this.container.add(pauseMenuView, "PAUSE");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Switches the view to the Menu card.
     */
    public void showMenu() {
        this.cardLayout.show(this.container, "MENU");
        this.currentView = ViewState.MENU;
    }

    /**
     * Switches the view to the Pause card.
     */
    public void showPause() {
        this.cardLayout.show(this.container, "PAUSE");
        this.currentView = ViewState.PAUSE;
    }

    /**
     * Switches the view to the active Simulation card.
     * Requests focus for the window to ensure input listeners capture events correctly.
     */
    public void showSimulation() {
        this.cardLayout.show(this.container, "SIM");
        this.currentView = ViewState.SIMULATION;
        this.requestFocusInWindow();
    }

    /**
     * Triggered when a simulation step is completed.
     * Updates the simulation panel with the latest body data.
     *
     * @param viewableBodies a list of bodies to be rendered
     * @param currentTime the current elapsed time of the simulation
     */
    @Override
    public void onStepCompleted(List<? extends ViewableBody> viewableBodies, double currentTime) {
        SwingUtilities.invokeLater(() -> this.simPanel.updateBodies(viewableBodies));
    }

    /**
     * Checks if the current ViewState is SIMULATION.
     * @return Returns whether the Simulation card is currently being displayed.
     */
    public boolean isSimulationShowing() {
        return this.currentView == ViewState.SIMULATION;
    }

    /**
     * Checks if the current ViewState is MENU.
     * @return Returns whether the MENU card is currently being displayed.
     */
    public boolean isMenuShowing() {
        return this.currentView == ViewState.MENU;
    }

    /**
     * Checks if the current ViewState is PAUSE.
     * @return Returns whether the PAUSE card is currently being displayed.
     */
    public boolean isPauseShowing() {
        return this.currentView == ViewState.PAUSE;
    }
}