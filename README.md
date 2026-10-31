# SpecificHeat

A Java-based thermal energy calculator that calculates the energy absorbed or released when water changes temperature or transitions between solid, liquid, and gaseous states.

## Overview

SpecificHeat calculates thermal energy changes involving three phases of water:

- **Ice:** Calculates sensible heat changes using the specific heat capacity of ice.
- **Liquid water:** Calculates sensible heat changes using the specific heat capacity of liquid water.
- **Steam:** Calculates sensible heat changes using the specific heat capacity of steam.
- **Phase transitions:** Accounts for latent heat associated with melting, freezing, vaporization, and condensation.

The calculator uses positive energy values for energy absorbed and negative values for energy released.

## Physics and Formulas

### Sensible Heat

Temperature changes within a single phase are calculated using:

\[
Q = mc\Delta T
\]

Where:

- \(Q\) = thermal energy transferred (J)
- \(m\) = mass (g)
- \(c\) = specific heat capacity (J/(g·°C))
- \(\Delta T = T_f - T_i\) = temperature change (°C)

### Latent Heat

Phase transitions are calculated using:

\[
Q = mL
\]

Where:

- \(m\) = mass (g)
- \(L\) = specific latent heat (J/g)

Melting and vaporization absorb energy, while freezing and condensation release energy. The sign of the energy contribution depends on the direction of the transition.

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

| Class | Responsibility |
|---|---|
| `Main` | Validates inputs, coordinates energy calculations, and combines the contributions from each phase and phase transition. |
| `Ice` | Calculates sensible heat changes for ice. |
| `WaterLiquid` | Calculates sensible heat changes for liquid water. |
| `Steam` | Calculates sensible heat changes for steam. |
| `HeatFusionWater` | Calculates latent heat associated with melting and freezing. |
| `HeatVaporizationWater` | Calculates latent heat associated with vaporization and condensation. |

## Usage

The main calculation method is:

```java
Main.calculateEnergy(
    massInGrams,
    initialTemperatureCelsius,
    finalTemperatureCelsius
);
```

### Example

```java
double energy = Main.calculateEnergy(250, 20, 80);
System.out.println(energy); // 62760.0 J
```

This example calculates the energy required to heat 250 grams of liquid water from 20°C to 80°C.

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

**Current test status: 42/42 tests passing.**

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

- Simplify overlapping conditional logic.
- Improve the organization and reuse of phase-transition calculations.
- Expand testing for exact-boundary and numerical edge cases.
- Review the model's treatment of starting and ending states at 0°C and 100°C.
- Improve diagnostic output and general code maintainability.

## License

SpecificHeat is licensed under the **GNU General Public License v3.0 or later (GPL-3.0-or-later)**.

You are free to use, study, modify, and redistribute this software under the terms of the license. If you distribute this software or derivative works covered by the GPL, you must comply with its applicable terms, including the requirements concerning source code availability.

See the [`LICENSE`](LICENSE) file for the full license text.

### AI Assistance

Parts of this project, including documentation and tests, were developed with assistance from ChatGPT. The project author reviewed the generated content and remains responsible for the project's design, implementation, and maintenance.