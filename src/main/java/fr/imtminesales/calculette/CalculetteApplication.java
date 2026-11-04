package fr.imtminesales.calculette;

import fr.imtminesales.calculette.controller.CalculatorController;
import fr.imtminesales.calculette.model.CalculatorModel;
import fr.imtminesales.calculette.view.CalculatorGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public final class CalculetteApplication extends Application {
    @Override
    public void start(Stage stage) {
        CalculatorGUI view = new CalculatorGUI();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);
        view.show(stage, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
