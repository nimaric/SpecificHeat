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

package test.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import src.Materials;
import src.ThermalCalculator;

class EnergyPropertiesTest {

  private static final double TOLERANCE = 0.001;

  // Helper method to keep tests clean and readable
  private double runCalculation(double mass, double t_i, double t_f) {
    ThermalCalculator calculator = new ThermalCalculator(Materials.WATER);
    calculator.setMass(mass);
    calculator.setInitialTemp(t_i);
    calculator.setFinalTemp(t_f);
    return calculator.calculateEnergyRequired();
  }

  @Test
  void reversingTemperaturesReversesEnergy() {
    double heating = runCalculation(250.0, -20.0, 150.0);
    double cooling = runCalculation(250.0, 150.0, -20.0);

    assertEquals(heating, -cooling, TOLERANCE);
  }

  @Test
  void zeroTemperatureChangeRequiresZeroEnergy() {
    double energyIce = runCalculation(100.0, -50.0, -50.0);
    double energyWater = runCalculation(100.0, 25.0, 25.0);
    double energySteam = runCalculation(100.0, 150.0, 150.0);

    assertEquals(0.0, energyIce, TOLERANCE);
    assertEquals(0.0, energyWater, TOLERANCE);
    assertEquals(0.0, energySteam, TOLERANCE);
  }

  @Test
  void energyIsLinearlyProportionalToMass() {
    double energyBase = runCalculation(100.0, -10.0, 110.0);
    double energyDoubleMass = runCalculation(200.0, -10.0, 110.0);

    // Doubling the mass should exactly double the required energy
    assertEquals(energyBase * 2.0, energyDoubleMass, TOLERANCE);
  }

  @Test
  void zeroMassRequiresZeroEnergy() {
    double energy = runCalculation(0.0, -50.0, 150.0);

    assertEquals(0.0, energy, TOLERANCE);
  }

  @Test
  void purePhaseChangeOnlyIncludesLatentHeat() {
    double mass = 10.0;
    double latentFusionConstant = 334.0;

    // Heating from 0°C (ice) to 0°C (water) should only be melting latent heat
    double meltingEnergy = runCalculation(mass, 0.0, 0.0001);
    // Note: Using a micro-step past 0.0 triggers the phase change block in the code

    double expectedLatentHeat = mass * latentFusionConstant;

    // Assert that the energy matches the expected latent heat within a small variance
    assertEquals(expectedLatentHeat, meltingEnergy, 0.01);
  }

  @Test
  void energySignMatchesTemperatureDirection() {
    double heatingEnergy = runCalculation(50.0, 20.0, 80.0);
    double coolingEnergy = runCalculation(50.0, 80.0, 20.0);

    assertTrue(
      heatingEnergy > 0,
      "Heating energy should be positive (absorbing heat)"
    );
    assertTrue(
      coolingEnergy < 0,
      "Cooling energy should be negative (releasing heat)"
    );
  }
}
