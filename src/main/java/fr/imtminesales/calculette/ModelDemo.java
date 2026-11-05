package fr.imtminesales.calculette;

import fr.imtminesales.calculette.model.CalculatorException;
import fr.imtminesales.calculette.model.CalculatorModel;
import fr.imtminesales.calculette.model.CalculatorSnapshot;
import fr.imtminesales.calculette.model.StandardOperation;

import java.math.BigDecimal;
import java.util.Scanner;

public final class ModelDemo {
    private ModelDemo() {
    }

    public static void main(String[] args) {
        CalculatorModel model = new CalculatorModel();

        try (Scanner scanner = new Scanner(System.in)) {
            showHelp();
            showState(model.snapshot());

            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String command = scanner.nextLine().trim().toLowerCase();
                if (command.equals("exit") || command.equals("quit")) {
                    break;
                }

                try {
                    execute(model, command);
                    showState(model.snapshot());
                } catch (CalculatorException | NumberFormatException exception) {
                    System.out.println("Erreur : " + exception.getMessage());
                }
            }
        }
    }

    private static void execute(CalculatorModel model, String command) {
        switch (command) {
            case "push" -> model.push();
            case "pop" -> model.pop();
            case "drop" -> model.drop();
            case "swap" -> model.swap();
            case "clear" -> model.clear();
            case "dropall", "drop all" -> model.dropAll();
            case "+" -> model.calculate(StandardOperation.ADD);
            case "-" -> model.calculate(StandardOperation.SUBTRACT);
            case "*", "x" -> model.calculate(StandardOperation.MULTIPLY);
            case "/" -> model.calculate(StandardOperation.DIVIDE);
            case "help", "?" -> showHelp();
            case "" -> {
            }
            default -> model.setAccumulator(parseNumber(command));
        }
    }

    private static double parseNumber(String input) {
        try {
            return Double.parseDouble(input.replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Commande inconnue : " + input);
        }
    }

    private static void showState(CalculatorSnapshot snapshot) {
        String accumulator = snapshot.accumulator().isPresent()
                ? format(snapshot.accumulator().getAsDouble())
                : "vide";
        String stack = snapshot.stack().stream()
                .map(ModelDemo::format)
                .toList()
                .toString();
        System.out.println("Accumulateur : " + accumulator);
        System.out.println("Pile : " + stack);
    }

    private static String format(double value) {
        if (value == 0.0) {
            return "0";
        }
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    private static void showHelp() {
        System.out.println("Saisissez un nombre ou une commande :");
        System.out.println("push, pop, drop, swap, clear, dropall");
        System.out.println("+, -, *, /, help, exit");
    }
}
