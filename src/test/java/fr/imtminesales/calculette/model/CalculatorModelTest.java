package fr.imtminesales.calculette.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorModelTest {
    private static final double DELTA = 1e-10;

    @Test
    void calculatesComplexRpnExpression() {
        CalculatorModel model = new CalculatorModel();
        push(model, 3, 2, 5, 4, 7);

        model.add();
        model.multiply();
        model.add();
        model.multiply();

        assertEquals(171.0, model.snapshot().accumulator().orElseThrow(), DELTA);
        assertTrue(model.snapshot().stack().isEmpty());
    }

    @Test
    void respectsOperandOrderForSubtractionAndDivision() {
        CalculatorModel model = new CalculatorModel();
        push(model, 20, 4);
        model.divide();
        assertEquals(5.0, model.snapshot().accumulator().orElseThrow(), DELTA);

        model.push();
        model.setAccumulator(2);
        model.subtract();
        assertEquals(3.0, model.snapshot().accumulator().orElseThrow(), DELTA);
    }

    @Test
    void supportsAllStackCommands() {
        CalculatorModel model = new CalculatorModel();
        push(model, 1, 2, 3);

        model.swap();
        assertEquals(List.of(2.0, 3.0, 1.0), model.snapshot().stack());

        model.drop();
        assertEquals(List.of(3.0, 1.0), model.snapshot().stack());

        model.pop();
        assertEquals(3.0, model.snapshot().accumulator().orElseThrow(), DELTA);
        assertEquals(List.of(1.0), model.snapshot().stack());

        model.clear();
        assertFalse(model.snapshot().accumulator().isPresent());
        model.dropAll();
        assertTrue(model.snapshot().stack().isEmpty());
    }

    @Test
    void doesNotChangeStateWhenDivisionByZeroFails() {
        CalculatorModel model = new CalculatorModel();
        push(model, 8, 0);
        CalculatorSnapshot before = model.snapshot();

        CalculatorException error = assertThrows(CalculatorException.class, model::divide);

        assertEquals("Division par zero impossible.", error.getMessage());
        assertEquals(before, model.snapshot());
    }

    @Test
    void rejectsOperationsWithoutEnoughOperands() {
        CalculatorModel model = new CalculatorModel();
        model.setAccumulator(4);
        CalculatorSnapshot before = model.snapshot();

        assertThrows(CalculatorException.class, model::add);
        assertEquals(before, model.snapshot());
        assertThrows(CalculatorException.class, model::swap);
    }

    @Test
    void notifiesListenersAfterChanges() {
        CalculatorModel model = new CalculatorModel();
        AtomicInteger notifications = new AtomicInteger();
        model.addListener(snapshot -> notifications.incrementAndGet());

        model.setAccumulator(10);
        model.push();
        model.pop();

        assertEquals(3, notifications.get());
    }

    private void push(CalculatorModel model, double... values) {
        for (double value : values) {
            model.setAccumulator(value);
            model.push();
        }
    }
}
