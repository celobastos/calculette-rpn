package fr.imtminesales.calculette;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CalculatorNumberFormatter {
    private static final MathContext DISPLAY_PRECISION = new MathContext(12, RoundingMode.HALF_UP);

    private CalculatorNumberFormatter() {
    }

    public static String format(double value) {
        if (value == 0.0) {
            return "0";
        }
        return BigDecimal.valueOf(value)
                .round(DISPLAY_PRECISION)
                .stripTrailingZeros()
                .toPlainString();
    }
}
