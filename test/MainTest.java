package test;
import org.junit.jupiter.api.Test;

import src.Main;
import src.ice;
import src.steam;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void iceOnly() {
        double energy = Main.calculateEnergy(250.0, -20.0, -5.0);
        assertTrue(energy > 0);
    }

    @Test
    void noTemperatureChange() {
        double energy = Main.calculateEnergy(250.0, 25.0, 25.0);
        assertEquals(0.0, energy, 1e-9);
    }

    @Test
    void waterOnly() {
        double energy = Main.calculateEnergy(250.0, 25.0, 80.0);
        assertTrue(energy > 0);
    }

    @Test
    void waterToSteam() {
    double energy = Main.calculateEnergy(250.0, 25.0, 150.0);
    assertTrue(energy > 0);
    }
    
    @Test
    void steamToWater() {
    double energy = Main.calculateEnergy(250.0, 150.0, 25.0);
    assertTrue(energy < 0);
    }

    @Test
    void waterToIce() {
        double energy = Main.calculateEnergy(250.0, 25.0, -20.0);
        assertTrue(energy < 0);
    }

    @Test
    void iceToMeltingPoint() {
        double energy = Main.calculateEnergy(250.0, -20.0, 0.0);
        assertTrue(energy > 0);
    }

    @Test
    void waterToBoilingPoint() {
        double energy = Main.calculateEnergy(250.0, 25.0, 100.0);
        assertTrue(energy > 0);
    }

    @Test
    void steamFromBoilingPoint() {
        double energy = Main.calculateEnergy(250.0, 100.0, 150.0);
        assertTrue(energy > 0);
    }

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
    void waterCooling() {
        double energy = Main.calculateEnergy(250.0, 80.0, 25.0);
        assertTrue(energy < 0);
    }

    @Test
    void iceCooling() {
        double energy = Main.calculateEnergy(250.0, -5.0, -25.0);
        assertTrue(energy < 0);
    }

    @Test
    void iceHeating() {
        double energy = Main.calculateEnergy(250.0, -25.0, -5.0);
        assertTrue(energy > 0);
    }

    @Test
    void steamCooling() {
        double energy = Main.calculateEnergy(250.0, 150.0, 120.0);
        assertTrue(energy < 0);
    }

    @Test
    void steamHeating() {
        double energy = Main.calculateEnergy(250.0, 120.0, 150.0);
        assertTrue(energy > 0);
    }
    
    @Test
    void zeroMass() {
        double energy = Main.calculateEnergy(0.0, 25.0, 80.0);
        assertEquals(0.0, energy, 0.0001);
    }

    @Test
    void waterHeatingExactResult() {
        // 250 g of water, 25°C → 80°C
        double expected = 250.0 * 4.184 * (80.0 - 25.0);

        assertEquals(expected,
            Main.calculateEnergy(250.0, 25.0, 80.0), 0.001);
    }

    @Test
    void waterCoolingExactResult() {
        // 250 g of water, 80°C → 25°C
        double expected = 250.0 * 4.184 * (25.0 - 80.0);

        assertEquals(expected,
            Main.calculateEnergy(250.0, 80.0, 25.0), 0.001);
    }

    @Test
    void iceHeatingExactResult() {
        // 100 g of ice, -20°C → -5°C
        double expected = 100.0 * 2.108 * (-5.0 - (-20.0));

        assertEquals(expected,
            Main.calculateEnergy(100.0, -20.0, -5.0), 0.001);
    }

    @Test
    void iceCoolingExactResult() {
        // 100 g of ice, -5°C → -20°C
        double expected = 100.0 * 2.108 * (-20.0 - (-5.0));

        assertEquals(expected,
            Main.calculateEnergy(100.0, -5.0, -20.0), 0.001);
    }

    @Test
    void steamHeatingExactResult() {
        // 100 g of steam, 120°C → 150°C
        double expected = 100.0 * 1.996 * (150.0 - 120.0);

        assertEquals(expected,
            Main.calculateEnergy(100.0, 120.0, 150.0), 0.001);
    }

    @Test
    void steamCoolingExactResult() {
        // 100 g of steam, 150°C → 120°C
        double expected = 100.0 * 1.996 * (120.0 - 150.0);

        assertEquals(expected,
            Main.calculateEnergy(100.0, 150.0, 120.0), 0.001);
    }

    @Test
    void iceCoolingClassOnly() {
        ice testIce = new ice(100.0, -5.0, -20.0);
        assertEquals(-3162.0, testIce.totalEnergy(), 0.001);
    }
    
    @Test
    void steamHeatingByOneDegree() {
        // 100 g of steam, 120°C → 121°C
        steam testSteam = new steam(100.0, 120.0, 121.0);

    assertEquals(199.6, testSteam.totalEnergy(), 0.001);
    }

    @Test
    void steamCoolingByOneDegree() {
        // 100 g of steam, 121°C → 120°C
        steam testSteam = new steam(100.0, 121.0, 120.0);

    assertEquals(-199.6, testSteam.totalEnergy(), 0.001);
    }

    @Test
    void steamNoTemperatureChange() {
        // 250 g of steam, 150°C → 150°C
        steam testSteam = new steam(250.0, 150.0, 150.0);

    assertEquals(0.0, testSteam.totalEnergy(), 0.001);
    }
}