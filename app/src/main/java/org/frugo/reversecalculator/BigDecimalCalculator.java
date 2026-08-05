package org.frugo.reversecalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class BigDecimalCalculator implements CalculatorInterface {

    private static final int MAX_INPUT_DIGITS = 100;
    private static final MathContext DIVISION_CONTEXT = MathContext.DECIMAL128;

    private String entry = "0";
    private final Deque<BigDecimal> stack = new ArrayDeque<>();
    private boolean replaceEntryOnInput = false;
    private boolean hasCurrentValue = false;
    private CalculatorError error = CalculatorError.NONE;

    @Override
    public void input(char value) {
        boolean isDigit = value >= '0' && value <= '9';
        if (!isDigit && value != '.') {
            return;
        }

        if (replaceEntryOnInput || error != CalculatorError.NONE) {
            resetBuffer();
        }

        if (isDigit) {
            appendDigit(value);
        } else if (!entry.contains(".")) {
            entry += ".";
        }
        hasCurrentValue = true;
    }

    private void appendDigit(char value) {
        if (hasReachedDigitLimit()) {
            return;
        }

        if (entry.equals("0")) {
            entry = Character.toString(value);
        } else if (entry.equals("-0")) {
            entry = "-" + value;
        } else {
            entry += value;
        }
    }

    private boolean hasReachedDigitLimit() {
        int digitCount = 0;
        for (int index = 0; index < entry.length(); index++) {
            char character = entry.charAt(index);
            if (character >= '0' && character <= '9') {
                digitCount++;
            }
        }
        return digitCount >= MAX_INPUT_DIGITS;
    }

    @Override
    public String getBuffer() {
        if (error != CalculatorError.NONE) {
            return "ERROR";
        }
        return entry;
    }

    @Override
    public void enter() {
        if (!hasCurrentValue) {
            error = CalculatorError.INSUFFICIENT_OPERANDS;
            return;
        }
        stack.addLast(parseEntry());
        resetBuffer();
    }

    @Override
    public void calculate(Operator operator) {
        error = CalculatorError.NONE;

        boolean usesEntry = hasCurrentValue;
        BigDecimal o1;
        BigDecimal o2;

        if (usesEntry) {
            if (stack.isEmpty()) {
                error = CalculatorError.INSUFFICIENT_OPERANDS;
                return;
            }
            o1 = stack.peekLast();
            o2 = parseEntry();
        } else {
            if (stack.size() < 2) {
                error = CalculatorError.INSUFFICIENT_OPERANDS;
                return;
            }
            Iterator<BigDecimal> operands = stack.descendingIterator();
            o2 = operands.next();
            o1 = operands.next();
        }

        if (operator == Operator.DIV && o2.compareTo(BigDecimal.ZERO) == 0) {
            error = CalculatorError.DIVISION_BY_ZERO;
            return;
        }

        BigDecimal res = BigDecimal.ZERO;

        try {
            res = switch (operator) {
                case ADD -> o1.add(o2);
                case SUB -> o1.subtract(o2);
                case MUL -> o1.multiply(o2);
                case DIV -> o1.divide(o2, DIVISION_CONTEXT);
            };
        } catch (ArithmeticException e) {
            error = CalculatorError.ARITHMETIC_ERROR;
            return;
        }

        stack.removeLast();
        if (!usesEntry) {
            stack.removeLast();
        }

        entry = formatResult(res);
        replaceEntryOnInput = true;
        hasCurrentValue = true;
    }

    @Override
    public void changeSign() {
        if (!hasCurrentValue || error != CalculatorError.NONE || isZeroEntry()) {
            return;
        }

        if (entry.startsWith("-")) {
            entry = entry.substring(1);
        } else {
            entry = "-" + entry;
        }
    }

    private boolean isZeroEntry() {
        for (int index = 0; index < entry.length(); index++) {
            char character = entry.charAt(index);
            if (character >= '1' && character <= '9') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetBuffer() {
        entry = "0";
        replaceEntryOnInput = false;
        hasCurrentValue = false;
        error = CalculatorError.NONE;
    }

    @Override
    public void reset() {
        resetBuffer();
        stack.clear();
    }

    @Override
    public String getBufferState() {
        StringBuilder state = new StringBuilder("[");
        Iterator<BigDecimal> values = stack.iterator();
        while (values.hasNext()) {
            state.append(formatResult(values.next()));
            if (values.hasNext()) {
                state.append(", ");
            }
        }
        return state.append(']').toString();
    }

    @Override
    public CalculatorError getError() {
        return error;
    }

    private BigDecimal parseEntry() {
        String value = entry.endsWith(".") ? entry + "0" : entry;
        return new BigDecimal(value);
    }

    private static String formatResult(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
