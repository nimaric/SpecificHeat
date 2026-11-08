package src;

import java.util.Scanner;

public class WaterThermalCalculator {
    
    // Specific heats (J / g * °C)
    private static final double C_ICE = 2.09;
    private static final double C_WATER = 4.184;
    private static final double C_STEAM = 2.01;

    // Latent heats (J / g)
    private static final double LATENT_FUSION = 334.0;
    private static final double LATENT_VAPORIZATION = 2260.0;

    private static final double EPSILON = 1e-6;

    private double massGrams;
    private double initialTemp;
    private double finalTemp;

    public void setMass(double massGrams) {
        this.massGrams = massGrams;
    }

    public void setInitialTemp(double initialTemp) {
        this.initialTemp = initialTemp;
    }

    public void setFinalTemp(double finalTemp) {
        this.finalTemp = finalTemp;
    }

    public double calculateSteamToIce(){
        double energy = 0.0;
        double current = initialTemp;
        double target = finalTemp;

        // Phase 5: Steam heating (100°C and above)
        if (current > 100.0) {
            double endSteam = Math.max(target, 100);
            energy += calculateSpecificHeat(C_STEAM, current, endSteam);
            System.out.printf("[Steam] Current Energy:  %.2f Energy%n", energy);
            current = endSteam;
        }

        // Phase 4: Vaporization (At exactly 100°C if the target wants to go higher)
        if (Math.abs(current - 100.0) < EPSILON && target < 100.0) {
            energy -= calculateLatentHeat(LATENT_VAPORIZATION);
            System.out.printf("[Vapor] Current Energy:  %.2f Energy%n", energy);
        }

        // Phase 3: Liquid Water heating (0°C to 100°C)
        if (current > 0.0 && current <= 100.0 && target < current) {
            double endWater = Math.max(target, 0.0);
            energy += calculateSpecificHeat(C_WATER, current, endWater);
            System.out.printf("[Water] Current Energy:  %.2f Energy%n", energy);
            current = endWater;
        }

        // Phase 2: Melting (At exactly 0°C if the target wants to go higher)
        if (Math.abs(current) < EPSILON && target < 0.0) {
            energy -= calculateLatentHeat(LATENT_FUSION);
            System.out.printf("[Melt] Current Energy:  %.2f Energy%n", energy);
            // We don't advance 'current' past 0 yet because liquid water starts heating FROM 0°C
        }

        // Phase 1: Ice heating (from initial up to 0°C)
        if (current <= 0.0 && target < current) {
            double endIce = Math.min(target, 0.0);
            energy += calculateSpecificHeat(C_ICE, current, endIce);
            System.out.printf("[Ice] Current Energy:  %.2f Energy%n", energy);
            current = endIce;
        }

        return energy;
    }

    public double calculateIceToSteam(){
        double energy = 0.0;
        double current = initialTemp;
        double target = finalTemp;

        // Phase 1: Ice heating (from initial up to 0°C)
        if (current < 0.0) {
            double endIce = Math.min(target, 0.0);
            energy += calculateSpecificHeat(C_ICE, current, endIce);
            System.out.printf("Current Energy:  %.2f Energy%n", energy);
            current = endIce;
        }

        // Phase 2: Melting (At exactly 0°C if the target wants to go higher)
        if (Math.abs(current) < EPSILON && target > 0.0) {
            energy += calculateLatentHeat(LATENT_FUSION);
            System.out.printf("Current Energy:  %.2f Energy%n", energy);
            // We don't advance 'current' past 0 yet because liquid water starts heating FROM 0°C
        }

        // Phase 3: Liquid Water heating (0°C to 100°C)
        if (current >= 0.0 && current < 100.0 && target > current) {
            double endWater = Math.min(target, 100.0);
            energy += calculateSpecificHeat(C_WATER, current, endWater);
            System.out.printf("Current Energy:  %.2f Energy%n", energy);
            current = endWater;
        }

        // Phase 4: Vaporization (At exactly 100°C if the target wants to go higher)
        if (Math.abs(current - 100.0) < EPSILON && target > 100.0) {
            energy += calculateLatentHeat(LATENT_VAPORIZATION);
            System.out.printf("Current Energy:  %.2f Energy%n", energy);
        }

        // Phase 5: Steam heating (100°C and above)
        if (current >= 100.0 && target > current) {
            double endSteam = Math.max(target, 100);
            energy += calculateSpecificHeat(C_STEAM, current, endSteam);
            System.out.printf("Current Energy:  %.2f Energy%n", energy);
            current = endSteam;
        }
        return energy;
    }

    public double calculateEnergyRequired() {
        System.out.printf("\nTotal Mass: %.2f Grams%n", massGrams);
        System.out.printf("Initial Temperature: %.2f initialCelsius%n", initialTemp);
        System.out.printf("Final Temperature: %.2f finalCelsius%n", finalTemp);
        InputValidator.validate(massGrams, initialTemp, finalTemp);
        
        double energy = 0.0;

        if (initialTemp > finalTemp){
            energy = calculateSteamToIce();
        }
        if (finalTemp > initialTemp){
            energy = calculateIceToSteam();
        }

        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        return energy;
    }

    public double calculateSpecificHeat(double specificHeat, double initialTemp, double finalTemp){
        return massGrams * specificHeat * (finalTemp - initialTemp);
    }

    public double calculateLatentHeat(double latentHeat){
        return massGrams * latentHeat;
    }

    static Scanner scanner = new Scanner(System.in); 
    public static void main(String[] args) {
        System.out.println("Enter mass (g), initial temp (°C), and final temp (°C):");
        double mass = scanner.nextDouble();
        double t_i = scanner.nextDouble();
        double t_f = scanner.nextDouble();

        WaterThermalCalculator calculateEnergy = new WaterThermalCalculator();
        calculateEnergy.setMass(mass);
        calculateEnergy.setInitialTemp(t_i);
        calculateEnergy.setFinalTemp(t_f);

        // Display results
        double totalEnergy = calculateEnergy.calculateEnergyRequired();
        System.out.printf("\nTotal Energy Required: %.2f Joules%n", totalEnergy);
    }
}