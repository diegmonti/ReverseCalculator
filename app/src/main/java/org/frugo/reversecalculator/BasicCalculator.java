package org.frugo.reversecalculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class BasicCalculator implements CalculatorInterface {
    private String buffer = "";
    private final Deque<String> stack = new ArrayDeque<>();
    private CalculatorError error = CalculatorError.NONE;

    @Override
    public void input(char value) {
        if (error != CalculatorError.NONE) {
            resetBuffer();
        }
        if (value == '.' && buffer.contains("."))
            return;
        buffer += Character.toString(value);
    }

    @Override
    public String getBuffer() {
        if (error != CalculatorError.NONE) {
            return "ERROR";
        }
        if (buffer.equals("")) {
            return "0";
        } else {
            return buffer;
        }
    }

    @Override
    public void enter() {
        if (buffer.isEmpty()) {
            error = CalculatorError.INSUFFICIENT_OPERANDS;
            return;
        }
        stack.addLast(buffer);
        resetBuffer();
    }

    @Override
    public void calculate(Operator operator) {
        error = CalculatorError.NONE;

        boolean usesBuffer = !buffer.isEmpty();
        double o1;
        double o2;

        if (usesBuffer) {
            if (stack.isEmpty()) {
                error = CalculatorError.INSUFFICIENT_OPERANDS;
                return;
            }
            o1 = Double.parseDouble(stack.peekLast());
            o2 = Double.parseDouble(buffer);
        } else {
            if (stack.size() < 2) {
                error = CalculatorError.INSUFFICIENT_OPERANDS;
                return;
            }
            Iterator<String> operands = stack.descendingIterator();
            o2 = Double.parseDouble(operands.next());
            o1 = Double.parseDouble(operands.next());
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
        if (!usesBuffer) {
            stack.removeLast();
        }
        buffer = String.valueOf(res);
    }

    @Override
    public void changeSign() {
        if (buffer.isEmpty() || error != CalculatorError.NONE) {
            return;
        }
        double value = Double.parseDouble(buffer);
        value *= -1;
        // Avoid "-0.0" when the buffer was empty or zero
        if (value == -0.0)
            value = 0.0;
        buffer = String.valueOf(value);
    }

    @Override
    public void resetBuffer() {
        buffer = "";
        error = CalculatorError.NONE;
    }

    @Override
    public void reset() {
        resetBuffer();
        stack.clear();
    }

    @Override
    public String getBufferState() {
        return stack.toString();
    }

    @Override
    public CalculatorError getError() {
        return error;
    }
}
