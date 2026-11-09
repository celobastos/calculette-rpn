package fr.imtminesales.calculette.model;

@FunctionalInterface
public interface UnaryOperation {
    double apply(double value);
}
