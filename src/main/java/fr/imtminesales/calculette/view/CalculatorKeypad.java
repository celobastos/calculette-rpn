package fr.imtminesales.calculette.view;

import fr.imtminesales.calculette.controller.CalculatorController;
import fr.imtminesales.calculette.model.ScientificOperation;
import fr.imtminesales.calculette.model.StandardOperation;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

final class CalculatorKeypad extends GridPane {
    private final TextField accumulatorField;
    private final CalculatorController controller;

    CalculatorKeypad(TextField accumulatorField, CalculatorController controller) {
        this.accumulatorField = accumulatorField;
        this.controller = controller;
        setHgap(8);
        setVgap(8);
        buildKeys();
    }

    private void buildKeys() {
        addScientificButton("sin", () -> calculate(ScientificOperation.SINE), 0, 0);
        addScientificButton("cos", () -> calculate(ScientificOperation.COSINE), 1, 0);
        addScientificButton("tan", () -> calculate(ScientificOperation.TANGENT), 2, 0);
        addScientificButton("x²", () -> calculate(ScientificOperation.SQUARE), 3, 0);
        addScientificButton("√x", () -> calculate(ScientificOperation.SQUARE_ROOT), 0, 1);
        addScientificButton("1/x", () -> calculate(ScientificOperation.RECIPROCAL), 1, 1);
        addScientificButton("ln", () -> calculate(ScientificOperation.NATURAL_LOG), 2, 1);
        addScientificButton("log", () -> calculate(ScientificOperation.DECIMAL_LOG), 3, 1);
        addScientificButton("π", () -> controller.useConstant(Math.PI), 0, 2);
        addScientificButton("e", () -> controller.useConstant(Math.E), 1, 2);
        addScientificButton("eˣ", () -> calculate(ScientificOperation.EXPONENTIAL), 2, 2);
        addScientificButton("xʸ", () -> calculate(StandardOperation.POWER), 3, 2);

        String[][] keys = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "x"},
                {"1", "2", "3", "-"},
                {"0", ".", "+/-", "+"}
        };

        for (int row = 0; row < keys.length; row++) {
            for (int column = 0; column < keys[row].length; column++) {
                addKey(keys[row][column], column, row + 3);
            }
        }

        Button push = createButton("PUSH");
        push.getStyleClass().add("push-button");
        push.setDefaultButton(true);
        push.setOnAction(event -> controller.push(accumulatorField.getText()));
        add(push, 0, 7, 2, 1);

        Button delete = createButton("⌫");
        delete.getStyleClass().add("function-button");
        delete.setOnAction(event -> backspace());
        add(delete, 2, 7);

        Button clear = createButton("CLEAR");
        clear.getStyleClass().add("clear-button");
        clear.setOnAction(event -> controller.clear());
        add(clear, 3, 7);
    }

    private void addScientificButton(String text, Runnable action, int column, int row) {
        Button button = createButton(text);
        button.getStyleClass().add("scientific-button");
        button.setOnAction(event -> action.run());
        add(button, column, row);
    }

    private void addKey(String key, int column, int row) {
        Button button = createButton(key);
        if (key.equals("+") || key.equals("-") || key.equals("x") || key.equals("/")) {
            button.getStyleClass().add("operator-button");
        } else if (key.equals("+/-")) {
            button.getStyleClass().add("function-button");
        } else {
            button.getStyleClass().add("number-button");
        }
        button.setMinHeight(48);
        configureAction(button, key);
        add(button, column, row);
        GridPane.setVgrow(button, Priority.ALWAYS);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setMinHeight(44);
        button.getStyleClass().add("calculator-button");
        GridPane.setHgrow(button, Priority.ALWAYS);
        return button;
    }

    private void configureAction(Button button, String key) {
        switch (key) {
            case "+" -> button.setOnAction(event -> calculate(StandardOperation.ADD));
            case "-" -> button.setOnAction(event -> calculate(StandardOperation.SUBTRACT));
            case "x" -> button.setOnAction(event -> calculate(StandardOperation.MULTIPLY));
            case "/" -> button.setOnAction(event -> calculate(StandardOperation.DIVIDE));
            case "+/-" -> button.setOnAction(event -> toggleSign());
            default -> button.setOnAction(event -> appendToEntry(key));
        }
    }

    private void calculate(StandardOperation operation) {
        controller.calculate(accumulatorField.getText(), operation);
    }

    private void calculate(ScientificOperation operation) {
        controller.calculate(accumulatorField.getText(), operation);
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

    private void backspace() {
        String value = accumulatorField.getText();
        if (!value.isEmpty()) {
            accumulatorField.setText(value.substring(0, value.length() - 1));
        }
        accumulatorField.positionCaret(accumulatorField.getText().length());
        accumulatorField.requestFocus();
    }
}
