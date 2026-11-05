package fr.imtminesales.calculette.model;

public enum StandardOperation implements BinaryOperation {
    ADD((left, right) -> left + right),
    SUBTRACT((left, right) -> left - right),
    MULTIPLY((left, right) -> left * right),
    DIVIDE((left, right) -> {
        if (right == 0.0) {
            throw new CalculatorException("Division par zero impossible.");
        }
        return left / right;
    });

    private final BinaryOperation operation;

    StandardOperation(BinaryOperation operation) {
        this.operation = operation;
    }

    @Override
    public double apply(double left, double right) {
        return operation.apply(left, right);
    }
}
