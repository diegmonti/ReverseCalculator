package com.example.reversecalculator;

public interface CalculatorInterface {

    /**
     * Insert a new value in the buffer.
     *
     * @param value
     */
    void input(char value);

    /**
     * Return the current buffer.
     *
     * @return
     */
    String getBuffer();

    /**
     * Return the current buffer state, e.g. the stack contents.
     *
     * @return a textual representation of the stack
     */
    String getBufferState();

    /**
     * Push the buffer in the stack.
     */
    void enter();

    /**
     * Save the result in the buffer.
     *
     * @param operator
     */
    void calculate(Operator operator);

    /**
     * Change the sign of the buffer.
     */
    void changeSign();

    /**
     * Reset the buffer.
     */
    void resetBuffer();

    /**
     * Reset the buffer and the stack.
     */
    void reset();

}
