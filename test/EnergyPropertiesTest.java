/*
 * Test cases developed with assistance from ChatGPT (OpenAI).
 * Reviewed and maintained by the project author.
 */

package test;

import org.junit.jupiter.api.Test;
import src.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyPropertiesTest {

    private static final double TOLERANCE = 0.001;

    @Test
    void reversingTemperaturesReversesEnergy() {
        double heating = Main.calculateEnergy(250.0, -20.0, 150.0);
        double cooling = Main.calculateEnergy(250.0, 150.0, -20.0);

        assertEquals(heating, -cooling, TOLERANCE);
    }

    @Test
    void doublingMassDoublesEnergy() {
        double energy250g = Main.calculateEnergy(250.0, 25.0, 150.0);
        double energy500g = Main.calculateEnergy(500.0, 25.0, 150.0);

        assertEquals(2.0 * energy250g, energy500g, TOLERANCE);
    }

    @Test
    void zeroTemperatureChangeReturnsZero() {
        assertEquals(
            0.0,
            Main.calculateEnergy(250.0, 25.0, 25.0),
            TOLERANCE
        );
    }

    @Test
    void multistageEnergyEqualsSumOfStages() {
        double direct = Main.calculateEnergy(250.0, -20.0, 150.0);

        double iceToWater = Main.calculateEnergy(250.0, -20.0, 20.0);
        double waterToSteam = Main.calculateEnergy(250.0, 20.0, 150.0);

        assertEquals(direct, iceToWater + waterToSteam, TOLERANCE);
    }
}
