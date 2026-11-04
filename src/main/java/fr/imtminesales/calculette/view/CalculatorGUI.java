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
import javafx.scene.layout.GridPane;
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

        GridPane keypad = buildKeypad();
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

    private GridPane buildKeypad() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        String[][] keys = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "x"},
                {"1", "2", "3", "-"},
                {"0", ".", "+/-", "+"}
        };

        for (int row = 0; row < keys.length; row++) {
            for (int column = 0; column < keys[row].length; column++) {
                String key = keys[row][column];
                Button button = new Button(key);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setMinHeight(48);
                button.setStyle("-fx-font-size: 17px;");
                configureKey(button, key);
                grid.add(button, column, row);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
            }
        }

        Button push = new Button("PUSH");
        push.setMaxWidth(Double.MAX_VALUE);
        push.setMinHeight(44);
        push.setDefaultButton(true);
        push.setOnAction(event -> controller.push(accumulatorField.getText()));
        grid.add(push, 0, 4, 3, 1);
        GridPane.setHgrow(push, Priority.ALWAYS);

        Button clear = commandButton("CLEAR", controller::clear);
        clear.setMinHeight(44);
        grid.add(clear, 3, 4);
        GridPane.setHgrow(clear, Priority.ALWAYS);
        return grid;
    }

    private void configureKey(Button button, String key) {
        switch (key) {
            case "+" -> button.setOnAction(event -> controller.add(accumulatorField.getText()));
            case "-" -> button.setOnAction(event -> controller.subtract(accumulatorField.getText()));
            case "x" -> button.setOnAction(event -> controller.multiply(accumulatorField.getText()));
            case "/" -> button.setOnAction(event -> controller.divide(accumulatorField.getText()));
            case "+/-" -> button.setOnAction(event -> toggleSign());
            default -> button.setOnAction(event -> appendToEntry(key));
        }
    }

    private void appendToEntry(String value) {
        String current = accumulatorField.getText();
        if (value.equals(".") && (current.contains(".") || current.contains(","))) {
            return;
        }
        accumulatorField.appendText(value);
        accumulatorField.requestFocus();
    }

    private void toggleSign() {
        String value = accumulatorField.getText();
        if (value.startsWith("-")) {
            accumulatorField.setText(value.substring(1));
        } else if (!value.isBlank()) {
            accumulatorField.setText("-" + value);
        }
        accumulatorField.positionCaret(accumulatorField.getText().length());
        accumulatorField.requestFocus();
    }

    private Button commandButton(String text, Runnable command) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> command.run());
        return button;
    }
}
