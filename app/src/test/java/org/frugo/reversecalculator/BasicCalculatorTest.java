package org.frugo.reversecalculator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BasicCalculatorTest extends CalculatorContractTest {

    @Override
    protected CalculatorInterface createCalculator() {
        return new BasicCalculator();
    }

    @Test
    public void arithmeticRetainsDoublePrecision() {
        input("0.1");
        calc.enter();
        input("0.2");
        calc.calculate(Operator.ADD);

        Assert.assertEquals("0.30000000000000004", calc.getBuffer());
    }
}
