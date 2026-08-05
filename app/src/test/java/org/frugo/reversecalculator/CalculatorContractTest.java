package org.frugo.reversecalculator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class CalculatorContractTest {

    protected CalculatorInterface calc;

    protected abstract CalculatorInterface createCalculator();

    @Before
    public void setUp() {
        calc = createCalculator();
    }

    @Test
    public void initBuffer() {
        Assert.assertEquals("0", calc.getBuffer());
        Assert.assertEquals(CalculatorError.NONE, calc.getError());
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
        input("11");
        calc.enter();
        calc.input('1');
        calc.calculate(Operator.ADD);
        Assert.assertEquals("12", calc.getBuffer());
    }

    @Test
    public void subNumbers() {
        input("11");
        calc.enter();
        calc.input('1');
        calc.calculate(Operator.SUB);
        Assert.assertEquals("10", calc.getBuffer());
    }

    @Test
    public void mulNumbers() {
        input("11");
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.MUL);
        Assert.assertEquals("22", calc.getBuffer());
    }

    @Test
    public void divNumbers() {
        input("11");
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.DIV);
        Assert.assertEquals("5.5", calc.getBuffer());
    }

    @Test
    public void addEmptyStack() {
        input("11");
        calc.calculate(Operator.ADD);
        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.INSUFFICIENT_OPERANDS, calc.getError());
    }

    @Test
    public void reset() {
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.reset();

        Assert.assertEquals("0", calc.getBuffer());
        calc.calculate(Operator.ADD);
        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.INSUFFICIENT_OPERANDS, calc.getError());
    }

    @Test
    public void inputAfterCalculationReplacesResult() {
        calc.input('1');
        calc.enter();
        calc.input('2');
        calc.calculate(Operator.ADD);
        Assert.assertEquals("3", calc.getBuffer());

        calc.input('1');
        Assert.assertEquals("1", calc.getBuffer());
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
    public void inputAfterSignChangeAppendsDigit() {
        calc.input('1');
        calc.changeSign();
        calc.input('2');
        Assert.assertEquals("-12", calc.getBuffer());
    }

    @Test
    public void decimalInputAfterSignChangeAppendsDigit() {
        input("1.5");
        calc.changeSign();
        calc.input('2');
        Assert.assertEquals("-1.52", calc.getBuffer());
    }

    @Test
    public void changeSignEmptyBuffer() {
        calc.changeSign();
        Assert.assertEquals("0", calc.getBuffer());
    }

    @Test
    public void invalidInputIsIgnored() {
        calc.input('1');
        calc.input('x');
        Assert.assertEquals("1", calc.getBuffer());
    }

    @Test
    public void inputIsLimitedToOneHundredDigits() {
        for (int index = 0; index < 100; index++) {
            calc.input('9');
        }

        String firstHundredDigits = calc.getBuffer();
        Assert.assertEquals(100, firstHundredDigits.length());

        calc.input('8');
        Assert.assertEquals(firstHundredDigits, calc.getBuffer());
    }

    @Test
    public void mulEmptyBuffer() {
        calc.input('2');
        calc.enter();
        calc.calculate(Operator.MUL);

        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.INSUFFICIENT_OPERANDS, calc.getError());
        Assert.assertEquals("[2]", calc.getBufferState());
    }

    @Test
    public void enterWithoutCurrentValueDoesNotPushZero() {
        calc.enter();

        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.INSUFFICIENT_OPERANDS, calc.getError());
        Assert.assertEquals("[]", calc.getBufferState());
    }

    @Test
    public void divByZero() {
        calc.input('1');
        calc.enter();
        calc.input('0');
        calc.calculate(Operator.DIV);

        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.DIVISION_BY_ZERO, calc.getError());
        Assert.assertEquals("[1]", calc.getBufferState());

        calc.input('2');
        calc.calculate(Operator.DIV);
        Assert.assertEquals("0.5", calc.getBuffer());
        Assert.assertEquals(CalculatorError.NONE, calc.getError());
    }

    @Test
    public void divByZeroWithEnteredOperandsDoesNotConsumeStack() {
        calc.input('1');
        calc.enter();
        calc.input('0');
        calc.enter();

        calc.calculate(Operator.DIV);

        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals(CalculatorError.DIVISION_BY_ZERO, calc.getError());
        Assert.assertEquals("[1, 0]", calc.getBufferState());
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

    @Test
    public void decimalExpression() {
        input(".5");
        calc.enter();
        input(".5");
        calc.calculate(Operator.ADD);
        Assert.assertEquals("1", calc.getBuffer());
    }

    @Test
    public void smallResultUsesPlainNotation() {
        calc.input('1');
        calc.enter();
        input("10000000");
        calc.calculate(Operator.DIV);

        Assert.assertEquals("0.0000001", calc.getBuffer());
    }

    @Test
    public void calculateValuesAlreadyEntered() {
        calc.input('3');
        calc.enter();
        calc.input('4');
        calc.enter();

        calc.calculate(Operator.ADD);

        Assert.assertEquals("7", calc.getBuffer());
        Assert.assertEquals("[]", calc.getBufferState());
    }

    @Test
    public void enteredDecimalsUseCanonicalStackFormat() {
        input(".5");
        calc.enter();
        Assert.assertEquals("[0.5]", calc.getBufferState());
    }

    @Test
    public void enteredSmallDecimalsUsePlainStackFormat() {
        input("0.0000001");
        calc.enter();
        Assert.assertEquals("[0.0000001]", calc.getBufferState());
    }

    @Test
    public void insufficientOperandsDoNotConsumeStack() {
        calc.input('2');
        calc.enter();

        calc.calculate(Operator.MUL);

        Assert.assertEquals("ERROR", calc.getBuffer());
        Assert.assertEquals("[2]", calc.getBufferState());

        calc.input('3');
        calc.calculate(Operator.MUL);
        Assert.assertEquals("6", calc.getBuffer());
    }

    protected void input(String value) {
        for (char character : value.toCharArray()) {
            calc.input(character);
        }
    }
}
