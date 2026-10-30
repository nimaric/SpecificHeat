/* 
 * COMPLETELY AI-GENERATED CODE
*/

package test;

import src.ice;
import src.steam;
import src.waterLiquid;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== RUNNING THERMODYNAMICS TESTS ===\n");

        testIceOnly();
        testWaterOnly();
        testSteamOnly();
        testFullPhaseTransition();
    }

    // Scenario 1: Heating ice from -20C to -5C (No melting)
    public static void testIceOnly() {
        ice iceObj = new ice(100.0, -20.0, -5.0);
        // Specific heat of ice ~ 2.108 J/g°C. Energy = 100g * 2.108 * 15°C = 3162 Joules
        double expected = 3162.0; 
        double actual = iceObj.totalEnergy();
        
        printResult("Ice Only (-20°C to -5°C)", expected, actual);
    }

    // Scenario 2: Heating water from 20C to 70C (No boiling/melting)
    public static void testWaterOnly() {
        waterLiquid waterObj = new waterLiquid(100.0, 20.0, 70.0);
        // Specific heat of water = 4.184 J/g°C. Energy = 100g * 4.184 * 50°C = 20920 Joules
        double expected = 20920.0;
        double actual = waterObj.totalEnergy();

        printResult("Water Only (20°C to 70°C)", expected, actual);
    }

    // Scenario 3: Heating steam from 110C to 140C
    public static void testSteamOnly() {
        steam steamObj = new steam(100.0, 110.0, 140.0);
        // Specific heat of steam ~ 1.996 J/g°C. Energy = 100g * 1.996 * 30°C = 5988 Joules
        double expected = 5988.0;
        double actual = steamObj.totalEnergy();

        printResult("Steam Only (110°C to 140°C)", expected, actual);
    }

    // Scenario 4: Full cross-phase calculation (The main logic integration)
    public static void testFullPhaseTransition() {
        // This simulates your Main program's logic for 250g from -20C to 183C
        // 1. Ice: 250g * 2.108 * 20 = 10,540 J
        // 2. Fusion (Melt): 250g * 334 J/g = 83,500 J
        // 3. Water: 250g * 4.184 * 100 = 104,600 J
        // 4. Vaporization (Boil): 250g * 2260 J/g = 565,000 J
        // 5. Steam: 250g * 1.996 * 83 = 41,417 J
        // Total Expected = 805,057 Joules
        
        ice iceObj = new ice(250.0, -20.0, 0);
        waterLiquid waterObj = new waterLiquid(250.0, 0, 100);
        steam steamObj = new steam(250.0, 100, 183.0);

        // Assumes your sub-classes handle their own internal phase-change constants!
        double actualTotal = iceObj.totalEnergy() + waterObj.totalEnergy() + steamObj.totalEnergy();
        double expectedTotal = 805057.0;

        printResult("Full Transition (-20°C to 183°C)", expectedTotal, actualTotal);
    }

    // Helper method to format outputs nicely
    private static void printResult(String testName, double expected, double actual) {
        // Using a small delta (0.1) to account for minor double floating-point rounding differences
        boolean passed = Math.abs(expected - actual) < 0.1;
        
        if (passed) {
            System.out.println("✅ [PASS] " + testName);
        } else {
            System.out.println("❌ [FAIL] " + testName);
            System.out.println("   -> Expected: " + expected + " Joules");
            System.out.println("   -> Received: " + actual + " Joules\n");
        }
    }
}
