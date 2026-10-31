/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Test cases developed with assistance from ChatGPT (OpenAI).
 * Reviewed and maintained by the project author.
 */

package test;

import org.junit.jupiter.api.Test;
import src.Main;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainValidationTest {

    private static final double TOLERANCE = 0.001;


    @Test
    void rejectsNegativeMass() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(-250.0, 25.0, 80.0)
        );
    }

    @Test
    void rejectsTemperatureBelowAbsoluteZero() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(250.0, -300.0, -200.0)
        );
    }

    @Test
    void rejectsNaNMass() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(Double.NaN, 20.0, 80.0)
        );
    }

    @Test
    void rejectsInfiniteMass() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(Double.POSITIVE_INFINITY, 20.0, 80.0)
        );
    }

    @Test
    void rejectsNaNInitialTemperature() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(250.0, Double.NaN, 80.0)
        );
    }

    @Test
    void rejectsInfiniteFinalTemperature() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Main.calculateEnergy(
                250.0, 20.0, Double.POSITIVE_INFINITY
            )
        );
    }

    @Test
    void acceptsZeroMass() {
        assertEquals(
            0.0,
            Main.calculateEnergy(0.0, 20.0, 80.0),
            TOLERANCE
        );
    }

    @Test
    void acceptsAbsoluteZeroAsInitialTemperature() {
        assertEquals(
            0.0,
            Main.calculateEnergy(250.0, -273.15, -273.15),
            TOLERANCE
        );
    }
}
