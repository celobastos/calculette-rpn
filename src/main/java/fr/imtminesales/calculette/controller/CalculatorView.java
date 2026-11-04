package fr.imtminesales.calculette.controller;

import java.util.List;

public interface CalculatorView {
    void changeAccumulator(String value);

    void changeStackData(List<String> values);

    void showError(String message);
}
