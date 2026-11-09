package fr.imtminesales.calculette.model;

public interface CalculatorModelInterface {
    void setAccumulator(double value);

    void calculate(BinaryOperation operation);

    void calculate(UnaryOperation operation);

    void push();

    void pop();

    void drop();

    void swap();

    void clear();

    void dropAll();

    CalculatorSnapshot snapshot();

    void addListener(CalculatorModelListener listener);

    void removeListener(CalculatorModelListener listener);
}
