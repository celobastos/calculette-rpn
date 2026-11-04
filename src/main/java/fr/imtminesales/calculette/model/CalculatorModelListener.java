package fr.imtminesales.calculette.model;

@FunctionalInterface
public interface CalculatorModelListener {
    void modelChanged(CalculatorSnapshot snapshot);
}
