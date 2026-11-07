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

    public double calculateEnergyRequired() {
        double energy = 0.0;
        double current = initialTemp;
        double target = finalTemp;

        // Phase 1: Ice heating (from initial up to 0°C)
        if (current < 0.0) {
            double endIce = Math.min(target, 0.0);
            energy += calculateSpecificHeat(C_ICE, current, endIce);
            current = endIce;
        }

        // Phase 2: Melting (At exactly 0°C if the target wants to go higher)
        if (current == 0.0 && target > 0.0) {
            energy += calculateLatentHeat(LATENT_FUSION);
            // We don't advance 'current' past 0 yet because liquid water starts heating FROM 0°C
        }

        // Phase 3: Liquid Water heating (0°C to 100°C)
        if (current >= 0.0 && current < 100.0 && target > current) {
            double endWater = Math.min(target, 100.0);
            energy += calculateSpecificHeat(C_WATER, current, endWater);
            current = endWater;
        }

        // Phase 4: Vaporization (At exactly 100°C if the target wants to go higher)
        if (current == 100.0 && target > 100.0) {
            energy += calculateLatentHeat(LATENT_VAPORIZATION);
        }

        // Phase 5: Steam heating (100°C and above)
        if (current >= 100.0 && target > current) {
            energy += calculateSpecificHeat(C_STEAM, current, target);
        }

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
        System.out.printf("Total Energy Required: %.2f Joules%n", totalEnergy);
    }
}