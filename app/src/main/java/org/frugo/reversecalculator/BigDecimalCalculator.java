package org.frugo.reversecalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EmptyStackException;
import java.util.Stack;

public class BigDecimalCalculator implements CalculatorInterface {

    private BigDecimal buffer = BigDecimal.ZERO;
    private Stack<BigDecimal> stack = new Stack<>();
    private int position = 1;
    private boolean enter = false;
    private boolean error = false;

    @Override
    public void input(char value) {
        if (enter) {
            resetBuffer();
        }
        if (value >= '0' && value <= '9') {
            if (position > 0) {
                // Integer part
                buffer = buffer.scaleByPowerOfTen(position);
                buffer = buffer.add(
                        new BigDecimal(Character.getNumericValue(value)));
            } else {
                // Decimal part
                BigDecimal decimal = new BigDecimal(
                        Character.getNumericValue(value));
                decimal = decimal.scaleByPowerOfTen(position);
                buffer = buffer.add(decimal);
                position -= 1;
            }
        } else if (value == '.' && position == 1) {
            position = -1;
        }
    }

    @Override
    public String getBuffer() {
        if (error) {
            return "ERROR";
        }
        return buffer.toString();
    }

    @Override
    public void enter() {
        stack.push(buffer);
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
        BigDecimal o2 = buffer;

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

        BigDecimal stripped = res.stripTrailingZeros();

        // Remove decimal part when it is zero
        if (stripped.scale() <= 0) {
            buffer = new BigDecimal(stripped.toBigInteger());
        } else {
            buffer = stripped;
        }

        enter = true;
    }

    @Override
    public void changeSign() {
        buffer = buffer.negate();
    }

    @Override
    public void resetBuffer() {
        buffer = BigDecimal.ZERO;
        position = 1;
        enter = false;
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
}
