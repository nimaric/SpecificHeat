/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Test cases developed with assistance from ChatGPT (OpenAI).
 * Test cases developed with assistance from Gemini (Google).
 * Reviewed and maintained by the project author.
 */

package test.phase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.Materials;
import src.ThermalCalculator;

class PhaseTransitionTest {

  private static final double TOLERANCE = 0.001;
  private ThermalCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new ThermalCalculator(Materials.WATER);
    calculator.setMass(100.0); // Keep mass consistent at 100g for easy tracking
  }

  @Test
  @DisplayName(
    "Should calculate pure ice heating without triggering melting or liquid phases"
  )
  void pureIceHeating() {
    calculator.setInitialTemp(-30.0);
    calculator.setFinalTemp(-10.0);

    // Expected: m * C_ICE * deltaT = 100g * 2.09 * 20°C = 4180.0 J
    double energy = calculator.calculateEnergyRequired();
    assertEquals(4180.0, energy, TOLERANCE);
  }

  @Test
  @DisplayName(
    "Should calculate pure liquid heating without triggering phase shifts"
  )
  void pureLiquidHeating() {
    calculator.setInitialTemp(20.0);
    calculator.setFinalTemp(50.0);

    // Expected: m * C_WATER * deltaT = 100g * 4.184 * 30°C = 12552.0 J
    double energy = calculator.calculateEnergyRequired();
    assertEquals(12552.0, energy, TOLERANCE);
  }

  @Test
  @DisplayName(
    "Should calculate pure steam heating above the boiling threshold"
  )
  void pureSteamHeating() {
    calculator.setInitialTemp(110.0);
    calculator.setFinalTemp(140.0);

    // Expected: m * C_STEAM * deltaT = 100g * 2.01 * 30°C = 6030.0 J
    double energy = calculator.calculateEnergyRequired();
    assertEquals(6030.0, energy, TOLERANCE);
  }

  @Test
  @DisplayName(
    "Should accurately calculate latent heat of fusion when crossing the 0°C melting line"
  )
  void crossingMeltingPoint() {
    calculator.setInitialTemp(-10.0);
    calculator.setFinalTemp(20.0);

    // Expected parts:
    // 1. Ice heating (-10 to 0): 100g * 2.09 * 10°C = 2090 J
    // 2. Fusion/Melting:        100g * 334.0 = 33400 J
    // 3. Liquid heating (0 to 20): 100g * 4.184 * 20°C = 8368 J
    // Total = 2090 + 33400 + 8368 = 43858.0 J
    double energy = calculator.calculateEnergyRequired();
    assertEquals(43858.0, energy, TOLERANCE);
  }

  @Test
  @DisplayName(
    "Should accurately calculate latent heat of vaporization when crossing the 100°C boiling line"
  )
  void crossingBoilingPoint() {
    calculator.setInitialTemp(80.0);
    calculator.setFinalTemp(120.0);

    // Expected parts:
    // 1. Liquid heating (80 to 100): 100g * 4.184 * 20°C = 8368 J
    // 2. Vaporization/Boiling:       100g * 2260.0 = 226000 J
    // 3. Steam heating (100 to 120): 100g * 2.01 * 20°C = 4020 J
    // Total = 8368 + 226000 + 4020 = 238388.0 J
    double energy = calculator.calculateEnergyRequired();
    assertEquals(238388.0, energy, TOLERANCE);
  }

  @Test
  @DisplayName(
    "Should evaluate the full transition profile across all 5 heating and phase stages"
  )
  void completePhaseTransitionRun() {
    calculator.setInitialTemp(-50.0);
    calculator.setFinalTemp(150.0);

    double totalEnergy = calculator.calculateEnergyRequired();

    // Let's assert that a complete transition requires significantly more energy
    // due to both latent heat calculations combined (33,400 + 226,000 baseline for 100g)
    assertTrue(
      totalEnergy > 259400.0,
      "Total energy must absorb both phase changes fully"
    );
  }
}
