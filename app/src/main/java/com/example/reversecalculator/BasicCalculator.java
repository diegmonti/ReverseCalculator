package com.example.reversecalculator;

import java.util.EmptyStackException;
import java.util.Stack;

public class BasicCalculator implements CalculatorInterface {
    private String buffer = "";
    private Stack<String> stack = new Stack<>();

    @Override
    public void input(char value) {
        buffer += Character.toString(value);
    }

    @Override
    public String getBuffer() {
        if (buffer.equals("")) {
            return "0";
        } else {
            return buffer;
        }
    }

    @Override
    public void enter() {
        stack.push(getBuffer());
        resetBuffer();
    }

    @Override
    public void calculate(Operator operator) {
        String stackValue = "0";
        try {
            stackValue = stack.pop();
        } catch (EmptyStackException e) {
            // Do nothing
        }
        double o1 = Double.parseDouble(stackValue);
        double o2 = Double.parseDouble(getBuffer());
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

        buffer = String.valueOf(res);
    }

    @Override
    public void resetBuffer() {
        buffer = "";
    }

    @Override
    public void reset() {
        resetBuffer();
        stack = new Stack<>();
    }
}
