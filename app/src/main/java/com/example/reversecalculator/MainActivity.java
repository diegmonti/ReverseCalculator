package com.example.reversecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView buffer;
    private CalculatorInterface calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calc = new BigDecimalCalculator();
        buffer = findViewById(R.id.buffer);
        updateBuffer();
    }

    private void updateBuffer() {
        buffer.setText(calc.getBuffer());
    }

    public void enter(View view) {
        calc.enter();
        updateBuffer();
    }

    public void cl(View view) {
        calc.resetBuffer();
        updateBuffer();
    }

    public void clr(View view) {
        calc.reset();
        updateBuffer();
    }

    public void div(View view) {
        calc.calculate(Operator.DIV);
        updateBuffer();
    }

    public void _7(View view) {
        calc.input('7');
        updateBuffer();
    }

    public void _8(View view) {
        calc.input('8');
        updateBuffer();
    }

    public void _9(View view) {
        calc.input('9');
        updateBuffer();
    }

    public void mul(View view) {
        calc.calculate(Operator.MUL);
        updateBuffer();
    }

    public void _4(View view) {
        calc.input('4');
        updateBuffer();
    }

    public void _5(View view) {
        calc.input('5');
        updateBuffer();
    }

    public void _6(View view) {
        calc.input('6');
        updateBuffer();
    }

    public void sub(View view) {
        calc.calculate(Operator.SUB);
        updateBuffer();
    }

    public void _1(View view) {
        calc.input('1');
        updateBuffer();
    }

    public void _2(View view) {
        calc.input('2');
        updateBuffer();
    }

    public void _3(View view) {
        calc.input('3');
        updateBuffer();
    }

    public void add(View view) {
        calc.calculate(Operator.ADD);
        updateBuffer();
    }

    public void _0(View view) {
        calc.input('0');
        updateBuffer();
    }

    public void sep(View view) {
        calc.input('.');
        updateBuffer();
    }

    public void cs(View view) {
        calc.changeSign();
        updateBuffer();
    }
}
