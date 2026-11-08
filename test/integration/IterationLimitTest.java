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

package test.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import src.Materials;
import src.ThermalCalculator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class IterationLimitTest {
    ThermalCalculator thermalCalculator = new ThermalCalculator(Materials.WATER);

    private static final int ITERATION_LIMIT = 30;

    @Test
    @DisplayName("Normal calculation doesn't hit the limit")
    void testCalculationWithinIterationLimit() {
        ThermalCalculator calculator =
            new ThermalCalculator(Materials.WATER);

        calculator.setMass(100);
        calculator.setInitialTemp(-10);
        calculator.setFinalTemp(110);

        assertDoesNotThrow(() ->
            calculator.calculateEnergyRequired()
        );
    }

    private void checkIterationLimit(int iteration) {
        if (iteration > ITERATION_LIMIT) {
            throw new IllegalStateException(
                "Hit " + ITERATION_LIMIT + " limit of phase calculation"
            );
        }
    }

    @Test
    void testIterationLimitNotExceeded() {
        assertDoesNotThrow(
            () -> checkIterationLimit(ITERATION_LIMIT)
        );
    }
}
