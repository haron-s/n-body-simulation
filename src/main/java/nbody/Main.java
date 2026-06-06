package nbody;

import nbody.controller.KeyboardHandler;
import nbody.controller.MouseHandler;
import nbody.controller.SimulationController;
import nbody.model.Simulation;
import nbody.model.integrator.LeapfrogIntegrator;
import nbody.view.NbodyView;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(new LeapfrogIntegrator(), new ArrayList<>());

        NbodyView view = new NbodyView();
        SimulationController controller = new SimulationController(sim, 1.0/240.0); // how often sim will update per second

        view.setController(controller);
        sim.addListener(view); // registers view as listener to the simulation model for updates

        MouseHandler mouseCtrl = new MouseHandler(controller, view);
        KeyboardHandler keyCtrl = new KeyboardHandler(controller, view);

        view.addMouseListener(mouseCtrl);
        view.addKeyListener(keyCtrl);

        // simulation logic in a dedicated background thread
        new Thread(controller::start).start();
    }
}