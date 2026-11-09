package fr.imtminesales.calculette.model;

public enum ScientificOperation implements UnaryOperation {
    SQUARE(value -> value * value),
    SQUARE_ROOT(value -> {
        if (value < 0.0) {
            throw new CalculatorException("La racine carree exige une valeur positive.");
        }
        return Math.sqrt(value);
    }),
    RECIPROCAL(value -> {
        if (value == 0.0) {
            throw new CalculatorException("L'inverse de zero est impossible.");
        }
        return 1.0 / value;
    }),
    NATURAL_LOG(value -> logarithm(value, Math::log)),
    DECIMAL_LOG(value -> logarithm(value, Math::log10)),
    SINE(value -> normalizeTrigonometricResult(Math.sin(Math.toRadians(value)))),
    COSINE(value -> normalizeTrigonometricResult(Math.cos(Math.toRadians(value)))),
    TANGENT(value -> {
        double radians = Math.toRadians(value);
        if (Math.abs(Math.cos(radians)) < 1e-12) {
            throw new CalculatorException("La tangente n'est pas definie pour cet angle.");
        }
        return normalizeTrigonometricResult(Math.tan(radians));
    }),
    EXPONENTIAL(Math::exp);

    private final UnaryOperation operation;

    ScientificOperation(UnaryOperation operation) {
        this.operation = operation;
    }

    @Override
    public double apply(double value) {
        return operation.apply(value);
    }

    private static double logarithm(double value, UnaryOperation logarithm) {
        if (value <= 0.0) {
            throw new CalculatorException("Le logarithme exige une valeur strictement positive.");
        }
        return logarithm.apply(value);
    }

    private static double normalizeTrigonometricResult(double value) {
        if (Math.abs(value) < 1e-12) {
            return 0.0;
        }
        if (Math.abs(value - 1.0) < 1e-12) {
            return 1.0;
        }
        if (Math.abs(value + 1.0) < 1e-12) {
            return -1.0;
        }
        return value;
    }
}
