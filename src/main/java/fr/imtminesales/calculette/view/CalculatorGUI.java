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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public final class CalculatorGUI implements CalculatorView {
    private final TextField accumulatorField = new TextField();
    private final ListView<String> stackView = new ListView<>();
    private CalculatorController controller;

    public void show(Stage stage, CalculatorController controller) {
        this.controller = controller;

        accumulatorField.setPromptText("Accumulateur");
        accumulatorField.setAlignment(Pos.CENTER_RIGHT);
        accumulatorField.getStyleClass().add("display-field");
        accumulatorField.setOnAction(event -> controller.push(accumulatorField.getText()));

        Label emptyStack = new Label("Pile vide");
        emptyStack.getStyleClass().add("empty-stack-label");
        stackView.setPlaceholder(emptyStack);
        stackView.setFocusTraversable(false);
        stackView.getStyleClass().add("stack-view");
        VBox.setVgrow(stackView, Priority.ALWAYS);

        Label brand = new Label("CASIO");
        brand.getStyleClass().add("brand-label");
        Label series = new Label("RPN SCIENTIFIC");
        series.getStyleClass().add("series-label");
        Region brandSpacer = new Region();
        HBox.setHgrow(brandSpacer, Priority.ALWAYS);
        HBox brandBar = new HBox(8, brand, brandSpacer, series);
        brandBar.setAlignment(Pos.CENTER_LEFT);

        Label stackLabel = new Label("Pile (sommet en haut)");
        stackLabel.getStyleClass().add("section-label");
        Label accumulatorLabel = new Label("Accumulateur");
        accumulatorLabel.getStyleClass().add("section-label");

        CalculatorKeypad keypad = new CalculatorKeypad(accumulatorField, controller);
        HBox stackCommands = new HBox(8,
                commandButton("POP", controller::pop),
                commandButton("DROP", controller::drop),
                commandButton("SWAP", controller::swap),
                commandButton("DROP ALL", controller::dropAll));
        stackCommands.setAlignment(Pos.CENTER);
        stackCommands.getChildren().forEach(node -> HBox.setHgrow(node, Priority.ALWAYS));

        VBox center = new VBox(10,
                brandBar,
                stackLabel, stackView,
                accumulatorLabel, accumulatorField,
                keypad, stackCommands);
        center.setPadding(new Insets(16));
        center.getStyleClass().add("calculator-body");

        BorderPane root = new BorderPane(center);
        root.getStyleClass().add("calculator-root");
        Scene scene = new Scene(root, 430, 720);
        scene.getStylesheets().add(Objects.requireNonNull(
                CalculatorGUI.class.getResource("calculator.css")).toExternalForm());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.clear();
            }
        });

        stage.setTitle("CASIO RPN - IMT Mines Ales");
        stage.setMinWidth(410);
        stage.setMinHeight(680);
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
        button.getStyleClass().addAll("calculator-button", "function-button");
        button.setOnAction(event -> command.run());
        return button;
    }
}
