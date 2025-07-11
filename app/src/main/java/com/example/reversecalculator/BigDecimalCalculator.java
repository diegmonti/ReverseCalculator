package com.example.reversecalculator;

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
            switch (operator) {
                case ADD:
                    res = o1.add(o2);
                    break;
                case SUB:
                    res = o1.subtract(o2);
                    break;
                case MUL:
                    res = o1.multiply(o2);
                    break;
                case DIV:
                    res = o1.divide(o2, MathContext.DECIMAL32);
                    break;
            }
        } catch (ArithmeticException e) {
            error = true;
        }

        buffer = res;
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
}
