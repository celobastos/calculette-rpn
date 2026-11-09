package fr.imtminesales.calculette;

import fr.imtminesales.calculette.model.CalculatorException;
import fr.imtminesales.calculette.model.CalculatorModel;
import fr.imtminesales.calculette.model.CalculatorModelInterface;
import fr.imtminesales.calculette.model.CalculatorSnapshot;
import fr.imtminesales.calculette.model.ScientificOperation;
import fr.imtminesales.calculette.model.StandardOperation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public final class ModelDemo {
    private static final Map<String, Consumer<CalculatorModelInterface>> COMMANDS = createCommands();

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

                String command = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
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

    static void execute(CalculatorModelInterface model, String command) {
        if (command.isEmpty()) {
            return;
        }
        if (command.equals("help") || command.equals("?")) {
            showHelp();
            return;
        }

        Consumer<CalculatorModelInterface> action = COMMANDS.get(command);
        if (action != null) {
            action.accept(model);
            return;
        }

        model.setAccumulator(parseNumber(command));
    }

    private static Map<String, Consumer<CalculatorModelInterface>> createCommands() {
        Map<String, Consumer<CalculatorModelInterface>> commands = new HashMap<>();
        register(commands, CalculatorModelInterface::push, "push");
        register(commands, CalculatorModelInterface::pop, "pop");
        register(commands, CalculatorModelInterface::drop, "drop");
        register(commands, CalculatorModelInterface::swap, "swap");
        register(commands, CalculatorModelInterface::clear, "clear");
        register(commands, CalculatorModelInterface::dropAll, "dropall", "drop all");
        register(commands, model -> model.calculate(StandardOperation.ADD), "+");
        register(commands, model -> model.calculate(StandardOperation.SUBTRACT), "-");
        register(commands, model -> model.calculate(StandardOperation.MULTIPLY), "*", "x");
        register(commands, model -> model.calculate(StandardOperation.DIVIDE), "/");
        register(commands, model -> model.calculate(StandardOperation.POWER), "^", "pow", "power");
        register(commands, model -> model.calculate(ScientificOperation.SINE), "sin");
        register(commands, model -> model.calculate(ScientificOperation.COSINE), "cos");
        register(commands, model -> model.calculate(ScientificOperation.TANGENT), "tan");
        register(commands, model -> model.calculate(ScientificOperation.SQUARE), "square", "x2");
        register(commands, model -> model.calculate(ScientificOperation.SQUARE_ROOT), "sqrt", "root");
        register(commands, model -> model.calculate(ScientificOperation.RECIPROCAL), "inverse", "1/x");
        register(commands, model -> model.calculate(ScientificOperation.NATURAL_LOG), "ln");
        register(commands, model -> model.calculate(ScientificOperation.DECIMAL_LOG), "log");
        register(commands, model -> model.calculate(ScientificOperation.EXPONENTIAL), "exp", "e^x");
        register(commands, model -> model.setAccumulator(Math.PI), "pi");
        register(commands, model -> model.setAccumulator(Math.E), "e");
        return Map.copyOf(commands);
    }

    private static void register(Map<String, Consumer<CalculatorModelInterface>> commands,
                                 Consumer<CalculatorModelInterface> action,
                                 String... names) {
        for (String name : names) {
            commands.put(name, action);
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
                ? CalculatorNumberFormatter.format(snapshot.accumulator().getAsDouble())
                : "vide";
        String stack = snapshot.stack().stream()
                .map(CalculatorNumberFormatter::format)
                .toList()
                .toString();
        System.out.println("Accumulateur : " + accumulator);
        System.out.println("Pile : " + stack);
    }

    private static void showHelp() {
        System.out.println("Saisissez un nombre ou une commande :");
        System.out.println("push, pop, drop, swap, clear, dropall");
        System.out.println("+, -, *, /, ^ (ou pow)");
        System.out.println("sin, cos, tan, square, sqrt, inverse, ln, log, exp");
        System.out.println("pi, e, help, exit");
    }
}
