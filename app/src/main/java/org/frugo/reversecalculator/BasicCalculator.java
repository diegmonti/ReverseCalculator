package org.frugo.reversecalculator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class BasicCalculator implements CalculatorInterface {
    private static final int MAX_INPUT_DIGITS = 100;

    private String entry = "0";
    private final Deque<Double> stack = new ArrayDeque<>();
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
        double o1;
        double o2;

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
            Iterator<Double> operands = stack.descendingIterator();
            o2 = operands.next();
            o1 = operands.next();
        }

        if (operator == Operator.DIV && o2 == 0.0d) {
            error = CalculatorError.DIVISION_BY_ZERO;
            return;
        }

        double res = 0.0;

        switch (operator) {
            case ADD:
                res = o1 + o2;
                break;
            case SUB:
                res = o1 - o2;
                break;
            case MUL:
                res = o1 * o2;
                break;
            case DIV:
                res = o1 / o2;
                break;
        }

        if (!Double.isFinite(res)) {
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
        Iterator<Double> values = stack.iterator();
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

    private double parseEntry() {
        String value = entry.endsWith(".") ? entry + "0" : entry;
        return Double.parseDouble(value);
    }

    private static String formatResult(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
