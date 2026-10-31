package test;

import org.junit.jupiter.api.Test;
import src.Ice;
import src.Steam;
import src.WaterLiquid;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhaseCalculationTest {

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
}
