package com.example.reversecalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EmptyStackException;
import java.util.Stack;

public class BigDecimalCalculator implements CalculatorInterface {

    private BigDecimal buffer = new BigDecimal(0);
    private Stack<BigDecimal> stack = new Stack<>();

    @Override
    public void input(char value) {

    }

    @Override
    public String getBuffer() {
        return buffer.toString();
    }

    @Override
    public void enter() {
        stack.push(buffer);
        resetBuffer();
    }

    @Override
    public void calculate(Operator operator) {
        BigDecimal o1 = new BigDecimal(0);
        try {
            o1 = stack.pop();
        } catch(EmptyStackException e) {
            // Do nothing
        }
        BigDecimal o2 = buffer;

        BigDecimal res = new BigDecimal(0);

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
            // Do nothing
        }

        buffer = res;
    }

    @Override
    public void changeSign() {
        buffer = buffer.negate();
    }

    @Override
    public void resetBuffer() {
        buffer = new BigDecimal(0);
    }

    @Override
    public void reset() {
        resetBuffer();
        stack = new Stack<>();
    }
}
