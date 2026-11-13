/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src;

/**
 * Validates input parameters for thermal energy calculations.
 */
public class InputValidator {

  public static boolean validate(
    double mass,
    double initialTemperature,
    double finalTemperature
  ) {
    if (mass < 0) {
      throw new IllegalArgumentException("Mass cannot be negative.");
    }
    if (!Double.isFinite(mass)) {
      throw new IllegalArgumentException(
        "Mass cannot be NaN (Not a Number) or Infinity."
      );
    }
    // Check if the temperature goes below Absolute Zero
    if (-273.15 > initialTemperature || -273.15 > finalTemperature) {
      throw new IllegalArgumentException(
        "Temperature cannot be below Absolute Zero (-273.15)."
      );
    }
    if (
      !Double.isFinite(initialTemperature) || !Double.isFinite(finalTemperature)
    ) {
      throw new IllegalArgumentException(
        "Temperature cannot be NaN (Not a Number) or Infinity."
      );
    }
    return true;
  }
}
