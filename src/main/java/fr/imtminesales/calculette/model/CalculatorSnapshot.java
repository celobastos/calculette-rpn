package fr.imtminesales.calculette.model;

import java.util.List;
import java.util.OptionalDouble;

public record CalculatorSnapshot(OptionalDouble accumulator, List<Double> stack) {
    public CalculatorSnapshot {
        stack = List.copyOf(stack);
    }
}
