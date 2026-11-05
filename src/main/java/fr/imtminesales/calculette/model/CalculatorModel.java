package fr.imtminesales.calculette.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

public final class CalculatorModel implements CalculatorModelInterface {
    private final Deque<Double> stack = new ArrayDeque<>();
    private final List<CalculatorModelListener> listeners = new ArrayList<>();
    private Double accumulator;

    @Override
    public void setAccumulator(double value) {
        if (!Double.isFinite(value)) {
            throw new CalculatorException("Le nombre doit etre fini.");
        }
        accumulator = value;
        notifyListeners();
    }

    @Override
    public void calculate(BinaryOperation operation) {
        Objects.requireNonNull(operation);
        BinaryOperands operands = readOperands();
        double result = operation.apply(operands.left(), operands.right());
        if (!Double.isFinite(result)) {
            throw new CalculatorException("Le resultat depasse la capacite de la calculatrice.");
        }
        commitOperation(operands, result);
    }

    @Override
    public void push() {
        requireAccumulator();
        stack.push(accumulator);
        accumulator = null;
        notifyListeners();
    }

    @Override
    public void pop() {
        requireStackSize(1, "La pile est vide.");
        accumulator = stack.pop();
        notifyListeners();
    }

    @Override
    public void drop() {
        requireStackSize(1, "La pile est vide.");
        stack.pop();
        notifyListeners();
    }

    @Override
    public void swap() {
        requireStackSize(2, "Deux valeurs sont necessaires pour SWAP.");
        double first = stack.pop();
        double second = stack.pop();
        stack.push(first);
        stack.push(second);
        notifyListeners();
    }

    @Override
    public void clear() {
        accumulator = null;
        notifyListeners();
    }

    @Override
    public void dropAll() {
        stack.clear();
        notifyListeners();
    }

    @Override
    public CalculatorSnapshot snapshot() {
        OptionalDouble accumulatorValue = accumulator == null
                ? OptionalDouble.empty()
                : OptionalDouble.of(accumulator);
        return new CalculatorSnapshot(accumulatorValue, List.copyOf(stack));
    }

    @Override
    public void addListener(CalculatorModelListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void removeListener(CalculatorModelListener listener) {
        listeners.remove(listener);
    }

    private BinaryOperands readOperands() {
        int requiredStackValues = accumulator == null ? 2 : 1;
        requireStackSize(requiredStackValues, "Deux operandes sont necessaires.");

        if (accumulator != null) {
            return new BinaryOperands(stack.peek(), accumulator, 1);
        }

        var iterator = stack.iterator();
        double right = iterator.next();
        double left = iterator.next();
        return new BinaryOperands(left, right, 2);
    }

    private void commitOperation(BinaryOperands operands, double result) {
        for (int index = 0; index < operands.stackValuesUsed(); index++) {
            stack.pop();
        }
        accumulator = result;
        notifyListeners();
    }

    private void requireAccumulator() {
        if (accumulator == null) {
            throw new CalculatorException("L'accumulateur est vide.");
        }
    }

    private void requireStackSize(int expected, String message) {
        if (stack.size() < expected) {
            throw new CalculatorException(message);
        }
    }

    private void notifyListeners() {
        CalculatorSnapshot current = snapshot();
        List.copyOf(listeners).forEach(listener -> listener.modelChanged(current));
    }

    private record BinaryOperands(double left, double right, int stackValuesUsed) {
    }
}
