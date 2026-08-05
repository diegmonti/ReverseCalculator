package org.frugo.reversecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import org.frugo.reversecalculator.databinding.ActivityMainBinding;

import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_VISIBLE_STACK_VALUES = 3;

    private ActivityMainBinding binding;
    private CalculatorViewModel calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calc = new ViewModelProvider(this).get(CalculatorViewModel.class);
        bindActions();
        updateDisplay();
    }

    private void bindActions() {
        bindDigitButtons(
                binding.digit0Button,
                binding.digit1Button,
                binding.digit2Button,
                binding.digit3Button,
                binding.digit4Button,
                binding.digit5Button,
                binding.digit6Button,
                binding.digit7Button,
                binding.digit8Button,
                binding.digit9Button
        );

        binding.decimalButton.setOnClickListener(view -> runAndUpdate(() -> calc.input('.')));
        binding.signButton.setOnClickListener(view -> runAndUpdate(calc::changeSign));
        binding.enterButton.setOnClickListener(view -> runAndUpdate(calc::enter));
        binding.clearEntryButton.setOnClickListener(view -> runAndUpdate(calc::resetBuffer));
        binding.clearAllButton.setOnClickListener(view -> runAndUpdate(calc::reset));

        bindOperator(binding.divideButton, Operator.DIV);
        bindOperator(binding.multiplyButton, Operator.MUL);
        bindOperator(binding.subtractButton, Operator.SUB);
        bindOperator(binding.addButton, Operator.ADD);

        binding.helpButton.setOnClickListener(view ->
                new GuideBottomSheet().show(getSupportFragmentManager(), "guide"));
    }

    private void bindDigitButtons(MaterialButton... buttons) {
        View.OnClickListener listener = view -> {
            CharSequence label = ((MaterialButton) view).getText();
            runAndUpdate(() -> calc.input(label.charAt(0)));
        };
        for (MaterialButton button : buttons) {
            button.setOnClickListener(listener);
        }
    }

    private void bindOperator(MaterialButton button, Operator operator) {
        button.setOnClickListener(view -> runAndUpdate(() -> calc.calculate(operator)));
    }

    private void runAndUpdate(Runnable action) {
        action.run();
        updateDisplay();
    }

    private void updateDisplay() {
        String value = calc.getError() == CalculatorError.NONE
                ? localizeDecimalSeparator(calc.getBuffer())
                : getString(errorMessage(calc.getError()));
        binding.buffer.setText(value);
        binding.buffer.setContentDescription(getString(R.string.current_value_description, value));
        updateStackDisplay(calc.getBufferState());
    }

    private void updateStackDisplay(String rawState) {
        if (rawState.length() <= 2) {
            binding.state.setText(R.string.stack_empty);
            binding.state.setContentDescription(getString(R.string.stack_empty_description));
            return;
        }

        String[] values = rawState.substring(1, rawState.length() - 1).split(", ");
        int firstVisibleValue = Math.max(0, values.length - MAX_VISIBLE_STACK_VALUES);
        StringBuilder visibleValues = new StringBuilder();
        StringBuilder spokenValues = new StringBuilder();

        for (int index = firstVisibleValue; index < values.length; index++) {
            String localizedValue = localizeDecimalSeparator(values[index]);
            if (visibleValues.length() > 0) {
                visibleValues.append('\n');
                spokenValues.append(", ");
            }
            visibleValues.append(localizedValue);
            spokenValues.append(localizedValue);
        }

        binding.state.setText(visibleValues);
        binding.state.setContentDescription(
                getString(R.string.stack_values_description, spokenValues));
    }

    private String localizeDecimalSeparator(String value) {
        char separator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        return separator == '.' ? value : value.replace('.', separator);
    }

    @StringRes
    private int errorMessage(CalculatorError error) {
        return switch (error) {
            case INSUFFICIENT_OPERANDS -> R.string.error_insufficient_operands;
            case DIVISION_BY_ZERO -> R.string.error_division_by_zero;
            case ARITHMETIC_ERROR -> R.string.error_arithmetic;
            case NONE -> R.string.error_unknown;
        };
    }
}
