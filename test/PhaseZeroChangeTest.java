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
import src.ThermalCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseZeroChangeTest {

    private static final double TOLERANCE = 0.001;
    private ThermalCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ThermalCalculator(Materials.WATER);
        calculator.setMass(100.0); // 100 grams
    }

    @Test
    @DisplayName("Should require zero energy when starting and ending at exactly absolute zero")
    void staticAtAbsoluteZero() {
        calculator.setInitialTemp(-273.15);
        calculator.setFinalTemp(-273.15);

        double energy = calculator.calculateEnergyRequired();
        assertEquals(0.0, energy, TOLERANCE);
    }

    @Test
    @DisplayName("Should require zero energy when starting and ending at exactly the melting point")
    void staticAtMeltingPoint() {
        calculator.setInitialTemp(0.0);
        calculator.setFinalTemp(0.0);

        // Without pushing past 0.0, no melting work occurs
        double energy = calculator.calculateEnergyRequired();
        assertEquals(0.0, energy, TOLERANCE);
    }

    @Test
    @DisplayName("Should require zero energy when starting and ending at exactly the boiling point")
    void staticAtBoilingPoint() {
        calculator.setInitialTemp(100.0);
        calculator.setFinalTemp(100.0);

        // Without pushing past 100.0, no vaporization work occurs
        double energy = calculator.calculateEnergyRequired();
        assertEquals(0.0, energy, TOLERANCE);
    }

    @Test
    @DisplayName("Should process melting immediately when starting at 0°C and heating slightly")
    void meltingBoundaryStep() {
        calculator.setInitialTemp(0.0);
        calculator.setFinalTemp(0.1);

        // Expected parts: 
        // 1. Latent Heat of Fusion: 100g * 334.0 = 33400.0 J
        // 2. Water Heating (0 to 0.1): 100g * 4.184 * 0.1 = 41.84 J
        // Total = 33441.84 Joules
        double energy = calculator.calculateEnergyRequired();
        assertEquals(33441.84, energy, TOLERANCE);
    }

    @Test
    @DisplayName("Should process vaporization immediately when starting at 100°C and heating slightly")
    void vaporizationBoundaryStep() {
        calculator.setInitialTemp(100.0);
        calculator.setFinalTemp(100.1);

        // Expected parts: 
        // 1. Latent Heat of Vaporization: 100g * 2260.0 = 226000.0 J
        // 2. Steam Heating (100 to 100.1): 100g * 2.01 * 0.1 = 20.1 J
        // Total = 226020.1 Joules
        double energy = calculator.calculateEnergyRequired();
        assertEquals(226020.1, energy, TOLERANCE);
    }

    @Test
    @DisplayName("Should correctly handle cooling down to exactly a boundary line from a high temperature")
    void coolingDownToExactBoundary() {
        // Utilizing our cooling calculation support to fall precisely onto the boiling line
        calculator.setInitialTemp(150.0);
        calculator.setFinalTemp(100.0);

        // Expected parts: 
        // 1. Steam cooling (150 down to 100): 100g * 2.01 * (100 - 150) = -10050.0 J
        // Total = -10050.0 Joules (No vaporization releases because it didn't cross *under* 100)
        double energy = calculator.calculateEnergyRequired();
        assertEquals(-10050.0, energy, TOLERANCE);
    }
}

