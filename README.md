# SpecificHeat

SpecificHeat is a Java-based thermal energy calculator designed to compute the total energy (in Joules) required to change the temperature and/or phase of water across different states (ice, liquid water, and steam).

## Features
**Multi-Phase Calculations:** Handles continuous transitions across solid, liquid, and gas phases.
  * **Ice:**  $2.09 \text{ J/g}\cdot^\circ\text{C}$
  * **Water:** $4.184 \text{ J/g}\cdot^\circ\text{C}$
  * **Steam:** $2.01 \text{ J/g}\cdot^\circ\text{C}$

**Latent Heat Integration:** Automatically factors in latent heat during phase transitions:
  * Fusion (Melting/Freezing at $0^\circ\text{C}$): $334.0 \text{ J/g}$
  * Vaporization (Boiling/Condensation at $100^\circ\text{C}$): $2260.0 \text{ J/g}$

**Input Validation (`InputValidator.java`):**
  * Prevents negative masses and non-finite numbers ($\text{NaN}$ or $\infty$).
  * Ensures temperatures do not fall below Absolute Zero ($-273.15^\circ\text{C}$).

**Safe Calculation Loop:** Includes safeguards against infinite loops or calculations exceeding integer/numeric limits.

**Numerical Safeguards:** Precision tolerance ($\epsilon = 10^{-6}$) for float comparisons and infinite loop prevention.

---

## Physics & Formulas

The total energy change is calculated by summing the **sensible heat** required for temperature changes within a phase and the **latent heat** absorbed or released during phase transitions.

---

### Sensible Heat

Temperature changes within a single phase are calculated using:

$$Q = m \cdot c \cdot \Delta T$$

Where:
* $Q$ = thermal energy transferred ($\text{J}$)
* $m$ = mass ($\text{g}$)
* $c$ = specific heat capacity ($\text{J}/(\text{g}\cdot^\circ\text{C})$)
* $\Delta T = T_f - T_i$ = temperature change ($^\circ\text{C}$)

#### Specific Heat Values Used:
* **Ice ($c_{\text{ice}}$):** $2.09 \text{ J}/(\text{g}\cdot^\circ\text{C})$
* **Liquid Water ($c_{\text{water}}$):** $4.184 \text{ J}/(\text{g}\cdot^\circ\text{C})$
* **Steam ($c_{\text{steam}}$):** $2.01 \text{ J}/(\text{g}\cdot^\circ\text{C})$

---

### Latent Heat

Phase transitions occurring at constant temperatures ($0^\circ\text{C}$ and $100^\circ\text{C}$) are calculated using:

$$Q = m \cdot L$$

Where:
* $m$ = mass ($\text{g}$)
* $L$ = specific latent heat ($\text{J}/\text{g}$)

#### Latent Heat Values Used:
* **Latent Heat of Fusion ($L_f$ at $0^\circ\text{C}$):** $334.0 \text{ J}/\text{g}$ (Melting / Freezing)
* **Latent Heat of Vaporization ($L_v$ at $100^\circ\text{C}$):** $2260.0 \text{ J}/\text{g}$ (Vaporization / Condensation)

> **Note:** Energy is absorbed during melting and vaporization ($\Delta T > 0$), and released during freezing and condensation ($\Delta T < 0$).

---

### Physical Constants

| Property | Value |
|---|---:|
| Specific heat capacity of ice | 2.108 J/(g·°C) |
| Specific heat capacity of liquid water | 4.184 J/(g·°C) |
| Specific heat capacity of steam | 1.996 J/(g·°C) |
| Latent heat of fusion | 334 J/g |
| Latent heat of vaporization | 2260 J/g |

## Temperature Boundaries and Model Assumptions

The calculator uses 0°C and 100°C as phase-transition boundaries for water under a standard-pressure model.

- **0°C — Fusion and freezing:** When the temperature reaches 0°C, latent heat of fusion is added when transitioning from ice to liquid water and subtracted when transitioning from liquid water to ice.
- **100°C — Vaporization and condensation:** When the temperature reaches 100°C, latent heat of vaporization is added when transitioning from liquid water to steam and subtracted when transitioning from steam to liquid water.

The calculator determines energy contributions using the initial and final temperatures and applies latent heat according to the direction of the temperature transition. It does not currently accept the initial and final states of matter as explicit inputs. Therefore, exact-boundary cases follow the calculator's custom convention, which simplifies phase-transition calculations but may differ from the physical behavior of a substance whose phase is already known.

A future improvement may introduce explicit phase-state information to distinguish cases that cannot be determined from temperature alone.

## Project Structure

```text
SpecificHeat/
└── src/
    ├── InputValidator.java         # Validates input parameters
    └── WaterThermalCalculator.java # Core thermal engine & CLI driver
└── test/
    ├── ConsoleIntegrationTest.java # Tests command-line I/O streams and main runner execution
    ├── EnergyPropertiesTest.java   # Verifies physical laws (mass proportionality, energy sign, and symmetry)
    ├── PhaseCalculationTest.java   # Validates core sensible and latent heat formula calculations
    ├── PhaseTransitionTest.java    # Tests complete multi-phase heating and cooling paths across boundaries
    └── PhaseZeroChangeTest.java    # Validates exact-boundary states, micro-steps, and zero-energy checks
```

---

## Usage Example 
When you run the program, you will be prompted to enter the mass in grams, initial temperature in Celsius, and final temperature in Celsius:

```text
Enter mass (g), initial temp (°C), and final temp (°C):
50
-10
110

Total Mass: 50.00 Grams
Initial Temperature: -10.00 initialCelsius
Final Temperature: 110.00 finalCelsius

Current Energy Stored: 156695.00 Joules

Total Energy Required: 156695.00 Joules
```

- A positive result indicates energy absorbed.
- A negative result indicates energy released.
- A result of zero indicates no net energy transfer under the model.

## Input Validation

The calculator rejects invalid inputs, including:

- Negative mass.
- Temperatures below absolute zero (−273.15°C).
- `NaN` values.
- Positive or negative infinity.

Zero mass is permitted and produces zero energy for otherwise valid inputs.

## Testing

The project uses JUnit 5 for automated testing.

**Current test status: 27/27 tests passing.**

The test suite covers:

- Heating and cooling within individual phases.
- Transitions between phases.
- Energy absorbed and released.
- Reverse temperature and phase changes.
- Invalid numerical inputs.
- Phase-transition boundaries.

Passing tests confirm that the implementation matches the expectations encoded in the test suite. Additional review of overlapping conditions and exact-boundary behavior remains useful as the calculation logic evolves.

## Technologies

- **Language:** Java
- **Runtime:** OpenJDK 17
- **Distribution:** Eclipse Temurin
- **Testing framework:** JUnit 5

Runtime environment used during development:

```text
OpenJDK 17.0.16
Eclipse Temurin 17.0.16+8
64-Bit Server VM
```

## Project Status

**In development.**

The calculator currently includes phase-specific energy calculations, latent heat calculations, input validation, and an automated test suite. Development is focused on improving the clarity, maintainability, and reliability of the calculation logic.

## Future Improvements

* Testing & Quality Assurance: Implement unit tests (e.g., JUnit) to verify edge cases around phase change thresholds ($0^\circ\text{C}$ and $100^\circ\text{C}$).
* CLI Polish / UI Expansion: Enhance user prompts or consider expanding input mechanisms beyond the standard command-line scanner.

## License

SpecificHeat is licensed under the **GNU General Public License v3.0 or later (GPL-3.0-or-later)**.

You are free to use, study, modify, and redistribute this software under the terms of the license. If you distribute this software or derivative works covered by the GPL, you must comply with its applicable terms, including the requirements concerning source code availability.

See the [`LICENSE`](LICENSE) file for the full license text.

### AI Assistance

Parts of this project, including documentation and tests, were developed with assistance from ChatGPT & Gemini. The project author reviewed the generated content and remains responsible for the project's design, implementation, and maintenance.