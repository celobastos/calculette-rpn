package fr.imtminesales.calculette.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScientificOperationTest {
    private static final double DELTA = 1e-10;

    @Test
    void calculatesTrigonometricFunctionsInDegrees() {
        assertEquals(0.5, ScientificOperation.SINE.apply(30), DELTA);
        assertEquals(0.0, ScientificOperation.SINE.apply(180), DELTA);
        assertEquals(0.5, ScientificOperation.COSINE.apply(60), DELTA);
        assertEquals(-1.0, ScientificOperation.COSINE.apply(180), DELTA);
        assertEquals(1.0, ScientificOperation.TANGENT.apply(45), DELTA);
    }

    @Test
    void calculatesSquareRootReciprocalAndLogarithms() {
        assertEquals(144.0, ScientificOperation.SQUARE.apply(12), DELTA);
        assertEquals(9.0, ScientificOperation.SQUARE_ROOT.apply(81), DELTA);
        assertEquals(0.25, ScientificOperation.RECIPROCAL.apply(4), DELTA);
        assertEquals(1.0, ScientificOperation.NATURAL_LOG.apply(Math.E), DELTA);
        assertEquals(3.0, ScientificOperation.DECIMAL_LOG.apply(1000), DELTA);
        assertEquals(Math.E, ScientificOperation.EXPONENTIAL.apply(1), DELTA);
    }

    @Test
    void rejectsValuesOutsideScientificDomains() {
        assertThrows(CalculatorException.class, () -> ScientificOperation.SQUARE_ROOT.apply(-1));
        assertThrows(CalculatorException.class, () -> ScientificOperation.RECIPROCAL.apply(0));
        assertThrows(CalculatorException.class, () -> ScientificOperation.NATURAL_LOG.apply(0));
        assertThrows(CalculatorException.class, () -> ScientificOperation.DECIMAL_LOG.apply(-10));
        assertThrows(CalculatorException.class, () -> ScientificOperation.TANGENT.apply(90));
    }
}
