package fr.imtminesales.calculette;

import fr.imtminesales.calculette.model.CalculatorModel;

public final class ConsoleDemo {
    private ConsoleDemo() {
    }

    public static void main(String[] args) {
        CalculatorModel calculator = new CalculatorModel();
        calculator.addListener(snapshot -> System.out.println("Etat : " + snapshot));

        for (double value : new double[]{3, 2, 5, 4, 7}) {
            calculator.setAccumulator(value);
            calculator.push();
        }
        calculator.add();
        calculator.multiply();
        calculator.add();
        calculator.multiply();
        System.out.println("Resultat final : " + calculator.snapshot().accumulator().orElseThrow());
    }
}
