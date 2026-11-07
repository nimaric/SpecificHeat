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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import src.calculations.Ice;
import src.calculations.Steam;
import src.calculations.WaterLiquid;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseZeroChangeTest {

    @ParameterizedTest
    @CsvSource({
        "-20.0, -20.0",
        "-5.0, -5.0"
    })
    void iceHasZeroEnergyWhenTemperatureDoesNotChange(
        double initial, double finalTemp
    ) {
        Ice ice = new Ice(250.0, initial, finalTemp);

        assertEquals(0.0, ice.totalEnergy(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
        "20.0, 20.0",
        "80.0, 80.0"
    })
    void waterHasZeroEnergyWhenTemperatureDoesNotChange(
        double initial, double finalTemp
    ) {
        WaterLiquid water =
            new WaterLiquid(250.0, initial, finalTemp);

        assertEquals(0.0, water.totalEnergy(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
        "120.0, 120.0",
        "150.0, 150.0"
    })
    void steamHasZeroEnergyWhenTemperatureDoesNotChange(
        double initial, double finalTemp
    ) {
        Steam steam = new Steam(250.0, initial, finalTemp);

        assertEquals(0.0, steam.totalEnergy(), 0.001);
    }
}
