# SpecificHeat

A Java-based thermal energy calculator that calculates the energy absorbed or released when water changes temperature or transitions between solid, liquid, and gaseous states.

## Overview

SpecificHeat calculates thermal energy changes involving three phases of water:

- **Ice:** Calculates sensible heat changes using the specific heat capacity of ice.
- **Liquid water:** Calculates sensible heat changes using the specific heat capacity of liquid water.
- **Steam:** Calculates sensible heat changes using the specific heat capacity of steam.
- **Phase transitions:** Accounts for latent heat during melting, freezing, vaporization, and condensation.

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

Melting and vaporization absorb energy, while freezing and condensation release energy.

### Physical Constants

| Property | Value |
|---|---:|
| Specific heat capacity of ice | 2.108 J/(g·°C) |
| Specific heat capacity of liquid water | 4.184 J/(g·°C) |
| Specific heat capacity of steam | 1.996 J/(g·°C) |
| Latent heat of fusion | 334 J/g |
| Latent heat of vaporization | 2260 J/g |

## Temperature Boundaries

- **0°C:** Boundary between ice and liquid water; the calculator applies latent heat of fusion.
- **100°C:** Boundary between liquid water and steam; the calculator applies latent heat of vaporization.

## Project Structure

| Class | Responsibility |
|---|---|
| `Main` | Coordinates calculations based on mass and initial and final temperatures. |
| `Ice` | Calculates sensible heat changes for ice. |
| `WaterLiquid` | Calculates sensible heat changes for liquid water. |
| `Steam` | Calculates sensible heat changes for steam. |
| `HeatFusionWater` | Calculates latent heat associated with melting and freezing. |
| `HeatVaporizationWater` | Calculates latent heat associated with vaporization and condensation. |

## Usage

The main calculation method is:

```java
Main.calculateEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
```

### Example

```java
double energy = Main.calculateEnergy(250, 20, 80);
System.out.println(energy); // 62760.0 J
```

This example calculates the energy required to heat 250 grams of liquid water from 20°C to 80°C.

- A positive result indicates energy absorbed.
- A negative result indicates energy released.
- A result of zero indicates no net energy transfer.

## Input Validation

The calculator rejects invalid inputs, including:

- Negative mass.
- Temperatures below absolute zero (−273.15°C).
- `NaN` values.
- Positive or negative infinity.

Zero mass is not considered negative and may be used as a valid input.

## Testing

The project uses JUnit 5 for automated testing.

The test suite covers calculations and validation, including:

- Heating and cooling within individual phases.
- Transitions between phases.
- Energy absorbed and released.
- Reversibility of temperature and phase changes.
- Invalid numerical inputs.
- Phase-transition boundaries.

Additional tests are being developed to strengthen boundary coverage and verify phase-transition calculations.

## Technologies

- **Language:** Java
- **Runtime:** OpenJDK 17
- **Distribution:** Eclipse Temurin
- **Testing framework:** JUnit 5

Runtime environment:

```text
OpenJDK 17.0.16
Eclipse Temurin 17.0.16+8
64-Bit Server VM
```

## Project Status

**In development.**

The project is being refined through automated testing, input validation, and improvements to phase-transition calculations.

## Future Improvements

- Expand phase-transition boundary tests.
- Strengthen numerical edge-case handling.
- Improve the organization of phase-transition logic.
- Reduce duplicated conditional logic.
- Expand automated test coverage.

## Acknowledgments

The README documentation was generated with assistance from ChatGPT (OpenAI) and reviewed by the project author for accuracy. The project is an ongoing learning exercise in Java programming, object-oriented design, and automated testing.