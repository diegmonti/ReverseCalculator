package com.example.reversecalculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigDecimalCalculatorTest {

    private CalculatorInterface calc;

    @Before
    public void setUp() {
        calc = new BigDecimalCalculator();
    }

    @Test
    public void initBuffer() {
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void simpleInput() {
        calc.input('1');
        Assert.assertEquals("1", calc.getBuffer());
    }

    @Test
    public void complexInput() {
        calc.input('1');
        calc.input('2');
        Assert.assertEquals("12", calc.getBuffer());
    }

    @Test
    public void decimalInput() {
        calc.input('.');
        calc.input('1');
        calc.input('2');
        Assert.assertEquals("0.12", calc.getBuffer());
    }

    @Test
    public void multipleDecimalInput() {
        calc.input('1');
        calc.input('.');
        calc.input('1');
        calc.input('.');
        calc.input('2');
        Assert.assertEquals("1.12", calc.getBuffer());
    }

    @Test
    public void resetBuffer() {
        calc.input('1');
        calc.input('2');
        calc.resetBuffer();
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void addNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('1');
        calc.calculate(Operator.ADD);
        Assert.assertEquals("12", calc.getBuffer());
    }

    @Test
    public void subNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('1');
        calc.calculate(Operator.SUB);
        Assert.assertEquals("10", calc.getBuffer());
    }

    @Test
    public void mulNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.MUL);
        Assert.assertEquals("22", calc.getBuffer());
    }

    @Test
    public void divNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.DIV);
        Assert.assertEquals("5.5", calc.getBuffer());
    }

    @Test
    public void addEmptyStack() {
        calc.input('1');
        calc.input('1');
        calc.calculate(Operator.ADD);
        Assert.assertEquals("11", calc.getBuffer());
    }

    @Test
    public void reset() {
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.reset();
        Assert.assertEquals("0", calc.getBuffer());
        calc.calculate(Operator.ADD);
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void changeSign() {
        calc.input('1');
        calc.changeSign();
        Assert.assertEquals("-1", calc.getBuffer());
        calc.changeSign();
        Assert.assertEquals("1", calc.getBuffer());
    }

    @Test
    public void changeSignEmptyBuffer() {
        // change sign when no value has been entered
        calc.changeSign();
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void mulWithoutSecondOperand() {
        calc.input('2');
        calc.enter();
        calc.calculate(Operator.MUL);
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void divByZero() {
        calc.input('1');
        calc.enter();
        calc.input('0');
        calc.calculate(Operator.DIV);
        Assert.assertEquals("ERROR", calc.getBuffer());
    }

    @Test
    public void expression() {
        calc.input('4');
        calc.enter();
        calc.input('2');
        calc.enter();
        calc.input('3');
        calc.calculate(Operator.MUL);
        calc.calculate(Operator.ADD);
        Assert.assertEquals("10", calc.getBuffer());
    }

}
