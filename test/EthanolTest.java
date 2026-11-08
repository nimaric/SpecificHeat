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

package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import src.Materials;
import src.ThermalCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EthanolTest {
    @Test
    @DisplayName("Should correctly calculate thermal energy for Ethanol using dynamic boundaries")
    void testEthanolThermalCalculation() {
        // Initialize your calculator with the new Ethanol preset
        ThermalCalculator ethanolCalculator = new ThermalCalculator(Materials.ETHANOL);
        
        ethanolCalculator.setMass(100.0); // 100 grams
        ethanolCalculator.setInitialTemp(-120.0); // Starting as solid ethanol
        ethanolCalculator.setFinalTemp(50.0);    // Heating up to liquid state

        double energy = ethanolCalculator.calculateEnergyRequired();

        // Expected calculation breakdown:
        // 1. Solid heating (-120 to -114.1): 100g * 2.44 * 5.9 = 1,439.6 J
        // 2. Fusion at -114.1°C: 100g * 108.8 = 10,880.0 J
        // 3. Liquid heating (-114.1 to 50): 100g * 2.46 * 164.1 = 40,368.6 J
        // Total Expected ≈ 52,688.2 Joules
        
        assertEquals(52688.2, energy, 1.0); // Allow a small tolerance for float precision
    }
}
