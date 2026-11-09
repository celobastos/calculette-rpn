package fr.imtminesales.calculette;

import fr.imtminesales.calculette.model.CalculatorModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelDemoTest {
    private static final double DELTA = 1e-10;

    @Test
    void executesEveryUnaryScientificCommand() {
        assertUnaryCommand("sin", 30, 0.5);
        assertUnaryCommand("cos", 60, 0.5);
        assertUnaryCommand("tan", 45, 1.0);
        assertUnaryCommand("square", 12, 144.0);
        assertUnaryCommand("sqrt", 81, 9.0);
        assertUnaryCommand("inverse", 4, 0.25);
        assertUnaryCommand("ln", Math.E, 1.0);
        assertUnaryCommand("log", 1000, 3.0);
        assertUnaryCommand("exp", 1, Math.E);
    }

    @Test
    void executesPowerCommand() {
        CalculatorModel model = new CalculatorModel();
        ModelDemo.execute(model, "2");
        ModelDemo.execute(model, "push");
        ModelDemo.execute(model, "3");

        ModelDemo.execute(model, "^");

        assertEquals(8.0, model.snapshot().accumulator().orElseThrow(), DELTA);
    }

    @Test
    void loadsScientificConstants() {
        CalculatorModel model = new CalculatorModel();

        ModelDemo.execute(model, "pi");
        assertEquals(Math.PI, model.snapshot().accumulator().orElseThrow(), DELTA);

        ModelDemo.execute(model, "e");
        assertEquals(Math.E, model.snapshot().accumulator().orElseThrow(), DELTA);
    }

    private void assertUnaryCommand(String command, double input, double expected) {
        CalculatorModel model = new CalculatorModel();
        ModelDemo.execute(model, Double.toString(input));
        ModelDemo.execute(model, command);

        assertEquals(expected, model.snapshot().accumulator().orElseThrow(), DELTA);
    }
}
