package com.example.reversecalculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BasicCalculatorTest {

    private CalculatorInterface calc;

    @Before
    public void setUp() {
        calc = new BasicCalculator();
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
        Assert.assertEquals("12.0", calc.getBuffer());
    }

    @Test
    public void subNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('1');
        calc.calculate(Operator.SUB);
        Assert.assertEquals("10.0", calc.getBuffer());
    }

    @Test
    public void mulNumbers() {
        calc.input('1');
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.MUL);
        Assert.assertEquals("22.0", calc.getBuffer());
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
    public void addOneNumber() {
        calc.input('1');
        calc.input('1');
        calc.calculate(Operator.ADD);
        Assert.assertEquals("11.0", calc.getBuffer());
    }

    @Test
    public void multipleSeparators() {
        calc.input('1');
        calc.input('.');
        calc.input('.');
        calc.input('2');
        Assert.assertEquals("1.2", calc.getBuffer());
    }

    @Test
    public void reset() {
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.reset();
        Assert.assertEquals("0", calc.getBuffer());
        calc.calculate(Operator.ADD);
        Assert.assertEquals("0.0", calc.getBuffer());
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
        Assert.assertEquals("10.0", calc.getBuffer());
    }

    @Test
    public void changeSign() {
        calc.input('1');
        calc.changeSign();
        Assert.assertEquals("-1.0", calc.getBuffer());
    }
}
