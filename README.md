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

## Building

This project uses the Gradle build system. You can open the repository directly with **Android Studio** or build from the command line:

```bash
./gradlew assembleDebug
```

A recent Android SDK installation is required. If it is not automatically detected, set the `ANDROID_HOME` environment variable or create a `local.properties` file containing `sdk.dir=/path/to/sdk`.

## Running tests

Unit tests for both calculator implementations are located under `app/src/test`. They can be executed with:

```bash
./gradlew test
```

## License

Reverse Calculator is distributed under the terms of the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for the full text.
