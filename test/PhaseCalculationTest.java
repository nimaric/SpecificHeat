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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import src.Materials;
import src.Phase;
import src.WaterThermalCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseCalculationTest {

    private static final double TOLERANCE = 0.001;
    private WaterThermalCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new WaterThermalCalculator(Materials.WATER);
    }

    @Test
    @DisplayName("Specific heat calculation should accurately apply Q = m * c * ΔT")
    void specificHeatCalculatesCorrectly() {
        calculator.setMass(250.0); // 250 grams
        
        // Heating liquid water from 20°C to 60°C (ΔT = 40)
        // Expected: 250.0 * 4.184 * 40.0 = 41840.0 Joules
        double result = calculator.calculateSensibleHeat(20.0, 60.0, Phase.LIQUID);
        
        assertEquals(41840.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Specific heat calculation should handle negative temperature changes (cooling)")
    void specificHeatHandlesNegativeDeltaT() {
        calculator.setMass(100.0); // 100 grams
        
        // Cooling ice from -5°C to -15°C (ΔT = -10)
        // Expected: 100.0 * 2.09 * -10.0 = -2090.0 Joules
        double result = calculator.calculateSensibleHeat(-5.0, -15.0, Phase.SOLID);
        
        assertEquals(-2090.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Latent heat calculation should accurately apply Q = m * L")
    void latentHeatCalculatesCorrectly() {
        calculator.setMass(50.0); // 50 grams
        
        // Vaporizing 50g of water at 100°C
        // Expected: 50.0 * 2260.0 = 113000.0 Joules
        double result = calculator.calculateLatentHeat(0, 2, Phase.VAPORIZATION);
        
        assertEquals(113000.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Specific heat should yield zero when there is no temperature change")
    void specificHeatReturnsZeroForNoTemperatureChange() {
        calculator.setMass(500.0);
        
        // Initial and final temperatures match
        double result = calculator.calculateSensibleHeat( 25.0, 25.0, Phase.LIQUID);
        
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test
    @DisplayName("Both calculation methods should return zero if the mass is zero")
    void calculationMethodsReturnZeroForZeroMass() {
        calculator.setMass(0.0); // 0 grams
        
        double specificHeatResult = calculator.calculateSensibleHeat(10.0, 90.0, Phase.LIQUID);
        double latentHeatResult = calculator.calculateLatentHeat(0, 2, Phase.FUSION);
        
        assertEquals(0.0, specificHeatResult, TOLERANCE);
        assertEquals(0.0, latentHeatResult, TOLERANCE);
    }
}

