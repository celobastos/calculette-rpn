package fr.imtminesales.calculette.model;

public interface CalculatorModelInterface {
    void setAccumulator(double value);

    void add();

    void subtract();

    void multiply();

    void divide();

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
