package org.frugo.reversecalculator;

import androidx.lifecycle.ViewModel;

public class CalculatorViewModel extends ViewModel implements CalculatorInterface {

    private final CalculatorInterface calculator = new BigDecimalCalculator();

    @Override
    public void input(char value) {
        calculator.input(value);
    }

    @Override
    public String getBuffer() {
        return calculator.getBuffer();
    }

    @Override
    public String getBufferState() {
        return calculator.getBufferState();
    }

    @Override
    public CalculatorError getError() {
        return calculator.getError();
    }

    @Override
    public void enter() {
        calculator.enter();
    }

    @Override
    public void calculate(Operator operator) {
        calculator.calculate(operator);
    }

    @Override
    public void changeSign() {
        calculator.changeSign();
    }

    @Override
    public void resetBuffer() {
        calculator.resetBuffer();
    }

    @Override
    public void reset() {
        calculator.reset();
    }
}
