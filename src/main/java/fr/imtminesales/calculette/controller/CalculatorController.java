package fr.imtminesales.calculette.controller;

import fr.imtminesales.calculette.model.CalculatorException;
import fr.imtminesales.calculette.model.CalculatorModelInterface;
import fr.imtminesales.calculette.model.CalculatorSnapshot;

import java.math.BigDecimal;
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

    public void enterNumber(String input) {
        execute(() -> model.setAccumulator(parseNumber(input)));
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

    public void add(String input) {
        calculate(input, model::add);
    }

    public void subtract(String input) {
        calculate(input, model::subtract);
    }

    public void multiply(String input) {
        calculate(input, model::multiply);
    }

    public void divide(String input) {
        calculate(input, model::divide);
    }

    private void calculate(String input, Runnable operation) {
        execute(() -> {
            if (input != null && !input.isBlank()) {
                model.setAccumulator(parseNumber(input));
            }
            operation.run();
        });
    }

    private void execute(Runnable command) {
        try {
            command.run();
        } catch (CalculatorException exception) {
            view.showError(exception.getMessage());
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
                ? format(snapshot.accumulator().getAsDouble())
                : "";
        List<String> stack = snapshot.stack().stream().map(this::format).toList();
        view.changeAccumulator(accumulator);
        view.changeStackData(stack);
    }

    private String format(double value) {
        if (value == 0.0) {
            return "0";
        }
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
