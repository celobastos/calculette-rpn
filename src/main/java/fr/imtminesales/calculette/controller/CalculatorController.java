package fr.imtminesales.calculette.controller;

import fr.imtminesales.calculette.CalculatorNumberFormatter;
import fr.imtminesales.calculette.model.CalculatorException;
import fr.imtminesales.calculette.model.BinaryOperation;
import fr.imtminesales.calculette.model.CalculatorModelInterface;
import fr.imtminesales.calculette.model.CalculatorSnapshot;
import fr.imtminesales.calculette.model.UnaryOperation;

import java.util.List;
import java.util.Objects;

public final class CalculatorController {
    private final CalculatorModelInterface model;
    private final CalculatorView view;

    public CalculatorController(CalculatorModelInterface model, CalculatorView view) {
        this.model = Objects.requireNonNull(model);
        this.view = Objects.requireNonNull(view);
        model.addListener(this::change);
        change(model.snapshot());
    }

    public void push(String input) {
        execute(() -> {
            model.setAccumulator(parseNumber(input));
            model.push();
        });
    }

    public void pop() {
        execute(model::pop);
    }

    public void drop() {
        execute(model::drop);
    }

    public void swap() {
        execute(model::swap);
    }

    public void clear() {
        execute(model::clear);
    }

    public void dropAll() {
        execute(model::dropAll);
    }

    public void calculate(String input, BinaryOperation operation) {
        execute(() -> {
            updateAccumulator(input);
            model.calculate(operation);
        });
    }

    public void calculate(String input, UnaryOperation operation) {
        execute(() -> {
            updateAccumulator(input);
            model.calculate(operation);
        });
    }

    public void useConstant(double value) {
        execute(() -> model.setAccumulator(value));
    }

    private void execute(Runnable command) {
        try {
            command.run();
        } catch (CalculatorException exception) {
            view.showError(exception.getMessage());
        }
    }

    private void updateAccumulator(String input) {
        if (input != null && !input.isBlank()) {
            model.setAccumulator(parseNumber(input));
        }
    }

    private double parseNumber(String input) {
        if (input == null || input.isBlank() || input.equals("-") || input.equals("+") || input.equals(".") || input.equals(",")) {
            throw new CalculatorException("Saisissez un nombre valide.");
        }
        try {
            double value = Double.parseDouble(input.trim().replace(',', '.'));
            if (!Double.isFinite(value)) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new CalculatorException("Nombre invalide : " + input);
        }
    }

    private void change(CalculatorSnapshot snapshot) {
        String accumulator = snapshot.accumulator().isPresent()
                ? CalculatorNumberFormatter.format(snapshot.accumulator().getAsDouble())
                : "";
        List<String> stack = snapshot.stack().stream()
                .map(CalculatorNumberFormatter::format)
                .toList();
        view.changeAccumulator(accumulator);
        view.changeStackData(stack);
    }
}
