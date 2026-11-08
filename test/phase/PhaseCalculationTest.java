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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import src.Materials;
import src.ThermalCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseCalculationTest {

    private static final double TOLERANCE = 0.001;
    private ThermalCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ThermalCalculator(Materials.WATER);
    }

    @Test
    @DisplayName("Specific heat calculation should accurately apply Q = m * c * ΔT")
    void specificHeatCalculatesCorrectly() {
        calculator.setMass(250.0); // 250 grams
        calculator.setInitialTemp(20.0);
        calculator.setFinalTemp(60.0);
        
        // Heating liquid water from 20°C to 60°C (ΔT = 40)
        // Expected: 250.0 * 4.184 * 40.0 = 41840.0 Joules
        double result = calculator.calculateEnergyRequired();
        
        assertEquals(41840.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Specific heat calculation should handle negative temperature changes (cooling)")
    void specificHeatHandlesNegativeDeltaT() {
        calculator.setMass(100.0); // 100 grams
        calculator.setInitialTemp(-5.0);
        calculator.setFinalTemp(-15.0);
        
        // Cooling ice from -5°C to -15°C (ΔT = -10)
        // Expected: 100.0 * 2.09 * -10.0 = -2090.0 Joules
        double result = calculator.calculateEnergyRequired();
        
        assertEquals(-2090.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Calculations should return zero if the mass is zero")
    void calculationMethodsReturnZeroForZeroMass() {
        calculator.setMass(0.0); // 0 grams
        calculator.setInitialTemp(10.0);
        calculator.setFinalTemp(90.0);
        
        double result = calculator.calculateEnergyRequired();
        
        assertEquals(0.0, result, TOLERANCE);
    }
}

