/*
 * Test cases developed with assistance from ChatGPT (OpenAI).
 * Reviewed and maintained by the project author.
 */

package test;

import org.junit.jupiter.api.Test;
import src.Ice;
import src.Main;
import src.Steam;
import src.WaterLiquid;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseCalculationTest {

    private static final double MASS = 250.0;
    private static final double TOLERANCE = 0.001;

    @Test
    void iceHeating() {
        Ice ice = new Ice(100.0, -20.0, -5.0);

        assertEquals(3162.0, ice.totalEnergy(), TOLERANCE);
    }

    @Test
    void iceCooling() {
        Ice ice = new Ice(100.0, -5.0, -20.0);

        assertEquals(-3162.0, ice.totalEnergy(), TOLERANCE);
    }

    @Test
    void iceToZero() {
        assertEquals(
            94040.0,
            Main.calculateEnergy(MASS, -20.0, 0.0),
            TOLERANCE
        );
    }

    @Test
    void waterHeating() {
        WaterLiquid water = new WaterLiquid(100.0, 20.0, 80.0);

        assertEquals(25104.0, water.totalEnergy(), TOLERANCE);
    }

    @Test
    void waterCooling() {
        WaterLiquid water = new WaterLiquid(100.0, 80.0, 20.0);

        assertEquals(-25104.0, water.totalEnergy(), TOLERANCE);
    }

    @Test
    void steamHeating() {
        Steam steam = new Steam(100.0, 120.0, 150.0);

        assertEquals(5988.0, steam.totalEnergy(), TOLERANCE);
    }

    @Test
    void steamCooling() {
        Steam steam = new Steam(100.0, 150.0, 120.0);

        assertEquals(-5988.0, steam.totalEnergy(), TOLERANCE);
    }

    void iceReachingZeroDegreesAppliesLatentHeatOfFusion() {
        // Ice: -20°C -> 0°C, including melting
        double expected = 10540.0 + 83500.0;

        double actual = Main.calculateEnergy(MASS, -20.0, 0.0);

        assertEquals(expected, actual, TOLERANCE);
    }

    @Test
    void waterReachingOneHundredDegreesAppliesLatentHeatOfVaporization() {
        // Water: 25°C -> 100°C, including vaporization
        double expected = 78450.0 + 565000.0;

        double actual = Main.calculateEnergy(MASS, 25.0, 100.0);

        assertEquals(expected, actual, TOLERANCE);
    }

    @Test
    void iceToWaterCrossingZeroDegreesIncludesFusion() {
        // Ice: -20°C -> liquid water: 20°C
        double expected = 10540.0 + 83500.0 + 20920.0;

        double actual = Main.calculateEnergy(MASS, -20.0, 20.0);

        assertEquals(expected, actual, TOLERANCE);
    }

    @Test
    void waterToSteamCrossingOneHundredDegreesIncludesVaporization() {
        // Water: 25°C -> steam: 150°C
        double expected = 78450.0 + 565000.0 + 24950.0;

        double actual = Main.calculateEnergy(MASS, 25.0, 150.0);

        assertEquals(expected, actual, TOLERANCE);
    }

    @Test
    void reversingIceToWaterTransitionReversesEnergySign() {
        double heating = Main.calculateEnergy(MASS, -20.0, 20.0);
        double cooling = Main.calculateEnergy(MASS, 20.0, -20.0);

        assertEquals(-heating, cooling, TOLERANCE);
    }

    @Test
    void reversingWaterToSteamTransitionReversesEnergySign() {
        double heating = Main.calculateEnergy(MASS, 25.0, 150.0);
        double cooling = Main.calculateEnergy(MASS, 150.0, 25.0);

        assertEquals(-heating, cooling, TOLERANCE);
    }

    @Test
    void zeroToWater() {
        assertEquals(
            104420.0,
            Main.calculateEnergy(MASS, 0.0, 20.0),
            TOLERANCE
        );
    }

    @Test
    void zeroToBoilingPoint() {
        assertEquals(
            753100.0,
            Main.calculateEnergy(MASS, 0.0, 100.0),
            TOLERANCE
        );
    }

    @Test
    void waterToZero() {
        assertEquals(
            -109650.0,
            Main.calculateEnergy(MASS, 25.0, 0.0),
            TOLERANCE
        );
    }

    @Test
    void boilingPointToWater() {
        assertEquals(
            -585920.0,
            Main.calculateEnergy(MASS, 100.0, 80.0),
            TOLERANCE
        );
    }

    @Test
    void sameTemperature() {
        assertEquals(
            0.0,
            Main.calculateEnergy(MASS, 25.0, 25.0),
            TOLERANCE
        );
    }

    @Test
    void boilingPointToSteam() {
        assertEquals(
            589950.0,
            Main.calculateEnergy(MASS, 100.0, 150.0),
            TOLERANCE
        );
    }

}
