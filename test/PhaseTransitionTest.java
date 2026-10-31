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

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseTransitionTest {

    private static final double TOLERANCE = 0.001;
    private static final double MASS = 250.0;

    @Test
    void iceToWater() {
        assertEquals(
            114960.0,
            Main.calculateEnergy(MASS, -20.0, 20.0),
            TOLERANCE
        );
    }

    @Test
    void waterToIce() {
        assertEquals(
            -114960.0,
            Main.calculateEnergy(MASS, 20.0, -20.0),
            TOLERANCE
        );
    }

    @Test
    void waterToSteam() {
        assertEquals(
            668400.0,
            Main.calculateEnergy(MASS, 25.0, 150.0),
            TOLERANCE
        );
    }

    @Test
    void steamToWater() {
        assertEquals(
            -668400.0,
            Main.calculateEnergy(MASS, 150.0, 25.0),
            TOLERANCE
        );
    }

    @Test
    void iceToSteam() {
        assertEquals(
            788590.0,
            Main.calculateEnergy(MASS, -20.0, 150.0),
            TOLERANCE
        );
    }

    @Test
    void steamToIce() {
        assertEquals(
            -788590.0,
            Main.calculateEnergy(MASS, 150.0, -20.0),
            TOLERANCE
        );
    }
}
