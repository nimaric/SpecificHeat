/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 * 
 * This file is part of SpecificHeat.
 *
 * SpecificHeat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package src;

import java.util.Scanner;

public class WaterThermalCalculator {
    private final Materials material;
    private double mass;
    private double initialTemp;
    private double finalTemp;

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setInitialTemp(double initialTemp) {
        this.initialTemp = initialTemp;
    }

    public void setFinalTemp(double finalTemp) {
        this.finalTemp = finalTemp;
    }

    // Constructor injection: Pass any material you want!
    public WaterThermalCalculator(Materials material) {
        this.material = material;
    }

    private static final double EPSILON = 1e-6;

    private boolean approximatelyEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public double calculateSensibleHeat(double initialTemp, double finalTemp, Phase phase){
        double specificHeat;
        switch(phase) {
            case SOLID -> specificHeat = material.cSolid;
            case LIQUID -> specificHeat = material.cLiquid;
            case GAS -> specificHeat = material.cGas;
            default -> throw new IllegalArgumentException("Unknown phase");
        }
        return mass * specificHeat * (finalTemp - initialTemp);
    }

    public double calculateLatentHeat(double initialTemp, double finalTempt, Phase phase){
        double latentHeat;
        switch(phase) {
            case FUSION -> latentHeat = material.latentFusion;
            case VAPORIZATION -> latentHeat = material.latentVaporization;
            default -> throw new IllegalArgumentException("Unknown phase");
        }

        if (initialTemp < finalTemp){ 
            return mass * latentHeat; 
        }
        if (initialTemp > finalTemp){ 
            return mass * -latentHeat; 
        } else {
            System.out.println("Latent Heat can't be calculated");
            System.exit(1);
            return 0.0;
        }
    }

    // Determines which phase is correct.
    public double detmineLatentHeat(double current, double target){
        double energy = 0.0;
        boolean phaseChange = approximatelyEquals(current, 100) || approximatelyEquals(current, 0);

        if (phaseChange){
            if (approximatelyEquals(current, 0)) {
                energy += calculateLatentHeat(current, target, Phase.FUSION);
            }

            if (approximatelyEquals(current, 100)) {
                energy += calculateLatentHeat(current, target, Phase.VAPORIZATION);
            }
        }

        return energy;
    }

    // Determines SpecificHeat
    public double calculateEnergyRequired() {
        System.out.printf("\nTotal Mass: %.2f Grams%n", mass);
        System.out.printf("Initial Temperature: %.2f initialCelsius%n", initialTemp);
        System.out.printf("Final Temperature: %.2f finalCelsius%n", finalTemp);
        InputValidator.validate(mass, initialTemp, finalTemp);
        
        double energy = 0.0;
        double current = initialTemp;
        double target = finalTemp;

        while (!approximatelyEquals(current, target)) {
            if (approximatelyEquals(energy, 4194303)) {
                System.out.printf("\nCurrent Energy: %.2f Energy%n", energy);
                System.out.println("Calculation Spiralled Out of Control (or hit the 22-bit integer limit)");
                System.exit(1);
            }

            // Checks if material starts at phase changes
            energy += detmineLatentHeat(current, target);

            if ((current < 0.0) || (current <= 0.0 && target < current)) {
                double endIce = Math.min(target, 0.0);
                energy += calculateSensibleHeat(current, endIce, Phase.SOLID);
                current = endIce;
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= 0.0 && current < 100.0 && target > current)) {
                double endWater = Math.max(Math.min(target, 100.0), 0);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                current = endWater;
                energy += detmineLatentHeat(current, target);
            }

            if ((current > 0.0 && current <= 100.0 && target < current)) {
                double endWater = Math.max(Math.min(target, 100.0), 0);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                current = endWater;
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= 100.0 && target > current) || (current > 100.0)) {
                double endSteam = Math.max(target, 100);
                energy += calculateSensibleHeat(current, endSteam, Phase.GAS);
                current = endSteam;
                energy += detmineLatentHeat(current, target);
            } 
        }

        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        return energy;
    }

    static Scanner scanner = new Scanner(System.in); 
    public static void main(String[] args) {
        System.out.println("Enter mass (g), initial temp (°C), and final temp (°C):");
        double mass = scanner.nextDouble();
        double t_i = scanner.nextDouble();
        double t_f = scanner.nextDouble();

        WaterThermalCalculator calculateEnergy = new WaterThermalCalculator(Materials.WATER);
        calculateEnergy.setMass(mass);
        calculateEnergy.setInitialTemp(t_i);
        calculateEnergy.setFinalTemp(t_f);

        // Display results
        double totalEnergy = calculateEnergy.calculateEnergyRequired();
        System.out.printf("\nTotal Energy Required: %.2f Joules%n", totalEnergy);
    }
}