package fr.imtminesales.calculette.view;

import fr.imtminesales.calculette.controller.CalculatorController;
import fr.imtminesales.calculette.controller.CalculatorView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public final class CalculatorGUI implements CalculatorView {
    private final TextField accumulatorField = new TextField();
    private final ListView<String> stackView = new ListView<>();
    private CalculatorController controller;

    public void show(Stage stage, CalculatorController controller) {
        this.controller = controller;

        accumulatorField.setPromptText("Accumulateur");
        accumulatorField.setAlignment(Pos.CENTER_RIGHT);
        accumulatorField.setStyle("-fx-font-size: 24px; -fx-font-family: monospace;");
        accumulatorField.setOnAction(event -> controller.push(accumulatorField.getText()));

        stackView.setPlaceholder(new Label("Pile vide"));
        stackView.setFocusTraversable(false);
        stackView.setStyle("-fx-font-size: 17px; -fx-font-family: monospace;");
        VBox.setVgrow(stackView, Priority.ALWAYS);

        CalculatorKeypad keypad = new CalculatorKeypad(accumulatorField, controller);
        HBox stackCommands = new HBox(8,
                commandButton("POP", controller::pop),
                commandButton("DROP", controller::drop),
                commandButton("SWAP", controller::swap),
                commandButton("DROP ALL", controller::dropAll));
        stackCommands.setAlignment(Pos.CENTER);
        stackCommands.getChildren().forEach(node -> HBox.setHgrow(node, Priority.ALWAYS));

        VBox center = new VBox(10,
                new Label("Pile (sommet en haut)"), stackView,
                new Label("Accumulateur"), accumulatorField,
                keypad, stackCommands);
        center.setPadding(new Insets(16));

        BorderPane root = new BorderPane(center);
        Scene scene = new Scene(root, 420, 670);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.clear();
            }
        });

        stage.setTitle("Calculette RPN - IMT Mines Ales");
        stage.setMinWidth(390);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
        accumulatorField.requestFocus();
    }

    @Override
    public void changeAccumulator(String value) {
        accumulatorField.setText(value);
        accumulatorField.positionCaret(value.length());
    }

    @Override
    public void changeStackData(List<String> values) {
        stackView.getItems().setAll(values);
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation impossible");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        accumulatorField.requestFocus();
    }

    private Button commandButton(String text, Runnable command) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> command.run());
        return button;
    }
}
