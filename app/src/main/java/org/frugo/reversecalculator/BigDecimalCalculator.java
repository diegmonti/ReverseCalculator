package org.frugo.reversecalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EmptyStackException;
import java.util.Stack;

public class BigDecimalCalculator implements CalculatorInterface {

    private static final int MAX_INPUT_DIGITS = 100;

    private String entry = "0";
    private Stack<BigDecimal> stack = new Stack<>();
    private boolean replaceEntryOnInput = false;
    private boolean error = false;

    @Override
    public void input(char value) {
        boolean isDigit = value >= '0' && value <= '9';
        if (!isDigit && value != '.') {
            return;
        }

        if (replaceEntryOnInput || error) {
            resetBuffer();
        }

        if (isDigit) {
            appendDigit(value);
        } else if (!entry.contains(".")) {
            entry += ".";
        }
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
        if (error) {
            return "ERROR";
        }
        return entry;
    }

    @Override
    public void enter() {
        stack.push(parseEntry());
        resetBuffer();
    }

    @Override
    public void calculate(Operator operator) {
        BigDecimal o1 = BigDecimal.ZERO;
        try {
            o1 = stack.pop();
        } catch (EmptyStackException e) {
            // Do nothing
        }
        BigDecimal o2 = parseEntry();

        BigDecimal res = BigDecimal.ZERO;

        try {
            res = switch (operator) {
                case ADD -> o1.add(o2);
                case SUB -> o1.subtract(o2);
                case MUL -> o1.multiply(o2);
                case DIV -> o1.divide(o2, MathContext.DECIMAL32);
            };
        } catch (ArithmeticException e) {
            error = true;
        }

        entry = formatResult(res);
        replaceEntryOnInput = true;
    }

    @Override
    public void changeSign() {
        if (error || isZeroEntry()) {
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
        error = false;
    }

    @Override
    public void reset() {
        resetBuffer();
        stack = new Stack<>();
    }

    @Override
    public String getBufferState() {
        return stack.toString();
    }

    private BigDecimal parseEntry() {
        String value = entry.endsWith(".") ? entry + "0" : entry;
        return new BigDecimal(value);
    }

    private String formatResult(BigDecimal value) {
        BigDecimal stripped = value.stripTrailingZeros();

        if (stripped.scale() <= 0) {
            return stripped.toBigInteger().toString();
        }
        return stripped.toString();
    }
}
