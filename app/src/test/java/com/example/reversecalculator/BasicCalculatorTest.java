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
}
