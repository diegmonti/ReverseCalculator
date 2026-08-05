package org.frugo.reversecalculator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigDecimalCalculatorTest extends CalculatorContractTest {

    @Override
    protected CalculatorInterface createCalculator() {
        return new BigDecimalCalculator();
    }

    @Test
    public void divisionUsesDecimal128Precision() {
        calc.input('1');
        calc.enter();
        calc.input('3');
        calc.calculate(Operator.DIV);

        Assert.assertEquals("0.3333333333333333333333333333333333", calc.getBuffer());
    }

    @Test
    public void decimalArithmeticIsExact() {
        input("0.1");
        calc.enter();
        input("0.2");
        calc.calculate(Operator.ADD);

        Assert.assertEquals("0.3", calc.getBuffer());
    }
}
