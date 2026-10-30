package test;
import org.junit.jupiter.api.Test;

import src.Main;

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

}