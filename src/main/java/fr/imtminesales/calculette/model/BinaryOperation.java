package fr.imtminesales.calculette.model;

@FunctionalInterface
public interface BinaryOperation {
    double apply(double left, double right);
}
