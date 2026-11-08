# SpecificHeat
*Thermal Energy Calculator*

A Java-based material-agnostic thermal energy calculator designed to compute the total energy (in Joules) required to change the temperature and/or phase of various substances across solid, liquid, and gas states.

## Features
**Multi-Phase Calculations:** Handles continuous transitions across solid, liquid, and gas phases using dynamic material properties.

**Material-Agnostic Architecture:** Supports arbitrary substances (e.g., Water, Ethanol, and more) via constructor injection of custom thermal properties, specific heat capacities, latent heats, and phase-transition temperatures.

**Latent Heat Integration:** Automatically factors in latent heat during phase transitions based on dynamically injected fusion and vaporization temperatures.

**Professional Logging:** Utilizes java.util.logging with custom diagnostic output to trace state transitions, sensible heat calculations, and latent heat determinations during complex heating/cooling cycles.

**Input Validation (InputValidator.java):**
* Prevents negative masses and non-finite numbers ($\text{NaN}$ or $\infty$).
* Ensures temperatures do not fall below Absolute Zero ($-273.15^\circ\text{C}$).

**Numerical Safeguards:** Precision tolerance ($\epsilon = 10^{-6}$) for float comparisons, exact-boundary checks, and infinite loop prevention.

---

## Physics & Formulas

The total energy change is calculated by summing the sensible heat required for temperature changes within a phase and the latent heat absorbed or released during phase transitions.

---

### Sensible Heat

Temperature changes within a single phase are calculated using:

$$Q = m \cdot c \cdot \Delta T$$

Where:
* $Q$ = thermal energy transferred ($\text{J}$)
* $m$ = mass ($\text{g}$)
* $c$ = specific heat capacity ($\text{J}/(\text{g}\cdot^\circ\text{C})$)
* $\Delta T = T_f - T_i$ = temperature change ($^\circ\text{C}$)

---

### Latent Heat

Phase transitions occurring at constant temperatures (defined by the material's dynamic `fusionTemp` and `vaporizationTemp`) are calculated using:

$$Q = m \cdot L$$

Where:
* $m$ = mass ($\text{g}$)
* $L$ = specific latent heat ($\text{J}/\text{g}$)

> **Note:** Energy is absorbed during melting and vaporization ($\Delta T > 0$), and released during freezing and condensation ($\Delta T < 0$).

---

## Temperature Boundaries and Model Assumptions

The calculator uses dynamically injected material thresholds (`fusionTemp` and `vaporizationTemp`) as phase-transition boundaries under a standard-pressure model.

  * **Fusion and freezing:** When the temperature reaches the material's fusion threshold, latent heat of fusion is added when transitioning from solid to liquid and subtracted when transitioning from liquid to solid.

  * **Vaporization and condensation:** When the temperature reaches the material's vaporization threshold, latent heat of vaporization is added when transitioning from liquid to gas and subtracted when transitioning from gas to liquid.

Exact-boundary handling applies smart short-circuiting to prevent unnecessary or erroneous phase transitions when a temperature cycle lands or terminates precisely on a boundary line.

## Project Structure

```text
SpecificHeat/
└── src/
    ├── InputValidator.java    # Validates input parameters
    ├── Materials.java         # Material data model and presets (Water, Ethanol, etc.)
    └── ThermalCalculator.java # Core thermal engine with dynamic boundaries and logging
└── test/
    ├── ConsoleIntegrationTest.java # Tests command-line I/O streams and main runner execution
    ├── EnergyPropertiesTest.java   # Verifies physical laws (mass proportionality, energy sign, and symmetry)
    ├── EthanolTest.java            # Verifies ThermalCalculator can handle non-water materials
    ├── PhaseCalculationTest.java   # Validates core sensible and latent heat formula calculations
    ├── PhaseTransitionTest.java    # Tests complete multi-phase heating and cooling paths across boundaries
    └── PhaseZeroChangeTest.java    # Validates exact-boundary states, micro-steps, and zero-energy checks
```

---

## Usage Example 
When you run the program, you will be prompted to enter the mass in grams, initial temperature in Celsius, and final temperature in Celsius:

```java
// Example using Ethanol with dynamic material properties
ThermalCalculator calculator = new ThermalCalculator(Materials.ETHANOL);
calculator.setMass(100.0);
calculator.setInitialTemp(-120.0);
calculator.setFinalTemp(50.0);

double energy = calculator.calculateEnergyRequired();
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

**Current test status:** All tests passing.

The test suite covers:
* Heating and cooling within individual phases across various materials.
* Multi-phase transitions using dynamic fusion and vaporization boundaries.
* Energy absorbed and released symmetry.
* Invalid numerical inputs and exact-boundary edge cases.

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

## Future Improvements

* **Testing & Quality Assurance:** Implement unit tests (e.g., JUnit) to verify edge cases around phase change thresholds ($0^\circ\text{C}$ and $100^\circ\text{C}$).
* **CLI Polish / UI Expansion:** Enhance user prompts or consider expanding input mechanisms beyond the standard command-line scanner.
* **Debug & Info:** Enhance existing debug/logging to accurately track and diagnose calculation process.

## License

SpecificHeat is licensed under the **GNU General Public License v3.0 or later (GPL-3.0-or-later)**.

You are free to use, study, modify, and redistribute this software under the terms of the license. If you distribute this software or derivative works covered by the GPL, you must comply with its applicable terms, including the requirements concerning source code availability.

See the [`LICENSE`](LICENSE) file for the full license text.

## AI Assistance

Parts of this project, including documentation and tests, were developed with assistance from ChatGPT & Gemini. The project author reviewed the generated content and remains responsible for the project's design, implementation, and maintenance.