# Reverse Calculator

Reverse Calculator is a simple Android application implementing a calculator that uses Reverse Polish Notation (RPN). Instead of the classical infix notation, operands are entered first and operations are applied afterwards. The project contains two calculator implementations:

* **BasicCalculator** – uses Java `double` values
* **BigDecimalCalculator** – provides higher precision using `BigDecimal`

## Features

* Add, subtract, multiply and divide
* ENTER key pushes the current number onto the stack
* CL clears the current entry
* CLR clears the entire stack
* Decimal separator and sign change support
* In-app help sheet describing RPN usage

The user interface is built with Material 3 components and is defined in `app/src/main/res/layout/activity_main.xml`.

## Architecture

`MainActivity` is responsible for rendering the calculator and forwarding button actions. Calculator state lives in `CalculatorViewModel`, so it survives activity recreation. The arithmetic implementations share the `CalculatorInterface` contract and report explicit errors without consuming stack operands when an operation cannot be completed.

## Precision

`BigDecimalCalculator` performs addition, subtraction and multiplication exactly. Division uses `MathContext.DECIMAL128`, which provides 34 significant digits with `HALF_EVEN` rounding. Results are displayed in plain decimal notation with unnecessary trailing zeros removed.

## Building

This project uses the Gradle build system, JDK 21 and Android SDK 36. You can open the repository directly with **Android Studio** or build from the command line:

```bash
./gradlew assembleDebug
```

A recent Android SDK installation is required. If it is not automatically detected, set the `ANDROID_HOME` environment variable or create a `local.properties` file containing `sdk.dir=/path/to/sdk`.

## Running tests

Unit tests for both calculator implementations are located under `app/src/test`. Instrumented UI tests are located under `app/src/androidTest`. Run the same checks used by continuous integration with:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Run instrumented tests on a connected device or emulator with:

```bash
./gradlew connectedDebugAndroidTest
```

Pull requests and pushes to `master` run unit tests, lint, debug builds and instrumented tests. Tags beginning with `v` additionally create signed APK and AAB artifacts for the corresponding GitHub Release.

## License

Reverse Calculator is distributed under the terms of the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for the full text.
