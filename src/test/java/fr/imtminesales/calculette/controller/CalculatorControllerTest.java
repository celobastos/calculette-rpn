package fr.imtminesales.calculette.controller;

import fr.imtminesales.calculette.model.CalculatorModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorControllerTest {
    @Test
    void acceptsCommaAndRefreshesView() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.push("2,5");
        controller.enterNumber("4");

        assertEquals("4", view.accumulator);
        assertEquals(List.of("2.5"), view.stack);
        assertTrue(view.errors.isEmpty());
    }

    @Test
    void reportsInvalidInputWithoutCrashing() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.push("abc");

        assertEquals(List.of("Nombre invalide : abc"), view.errors);
        assertTrue(view.stack.isEmpty());
    }

    private static final class FakeView implements CalculatorView {
        private String accumulator = "";
        private List<String> stack = List.of();
        private final List<String> errors = new ArrayList<>();

        @Override
        public void changeAccumulator(String value) {
            accumulator = value;
        }

        @Override
        public void changeStackData(List<String> values) {
            stack = List.copyOf(values);
        }

        @Override
        public void showError(String message) {
            errors.add(message);
        }
    }
}
