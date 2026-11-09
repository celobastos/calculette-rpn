package fr.imtminesales.calculette.controller;

import fr.imtminesales.calculette.model.CalculatorModel;
import fr.imtminesales.calculette.model.ScientificOperation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorControllerTest {
    @Test
    void acceptsCommaAndRefreshesStack() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.push("2,5");

        assertEquals("", view.accumulator);
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

    @Test
    void executesScientificOperationAndConstants() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.calculate("30", ScientificOperation.SINE);
        assertEquals("0.5", view.accumulator);

        controller.useConstant(Math.PI);
        assertEquals("3.14159265359", view.accumulator);
    }

    @Test
    void roundsFloatingPointArtifactsForTheDisplay() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.calculate("60", ScientificOperation.COSINE);

        assertEquals("0.5", view.accumulator);
    }

    @Test
    void reportsScientificDomainErrorAndPreservesValue() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.calculate("-1", ScientificOperation.SQUARE_ROOT);

        assertEquals("-1", view.accumulator);
        assertEquals(List.of("La racine carree exige une valeur positive."), view.errors);
    }

    @Test
    void reportsEmptyScientificOperationWithoutCrashing() {
        FakeView view = new FakeView();
        CalculatorController controller = new CalculatorController(new CalculatorModel(), view);

        controller.calculate("", ScientificOperation.SQUARE);

        assertEquals(List.of("Une operande est necessaire."), view.errors);
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
