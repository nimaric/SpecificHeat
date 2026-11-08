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
    // Specific heats (J / g * °C)
    private static final double C_ICE = 2.09;
    private static final double C_WATER = 4.184;
    private static final double C_STEAM = 2.01;

    // Latent heats (J / g)
    private static final double LATENT_FUSION = 334.0;
    private static final double LATENT_VAPORIZATION = 2260.0;

    private static final double EPSILON = 1e-6;

    private boolean approximatelyEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    private double massGrams;
    private double initialTemp;
    private double finalTemp;

        public double calculateSpecificHeat(double specificHeat, double initialTemp, double finalTemp){
        return massGrams * specificHeat * (finalTemp - initialTemp);
    }

    public double calculateLatentHeat(double latentHeat, double initialTemp, double finalTemp){
        if (initialTemp < finalTemp){ 
            return massGrams * latentHeat; 
        }
        if (initialTemp > finalTemp){ 
            return massGrams * -latentHeat; 
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
                energy += calculateLatentHeat(LATENT_FUSION, current, target);
            }

            if (approximatelyEquals(current, 100)) {
                energy += calculateLatentHeat(LATENT_VAPORIZATION, current, target);
            }
        }

        return energy;
    }

    // Determines SpecificHeat
    public double calculateEnergyRequired() {
        System.out.printf("\nTotal Mass: %.2f Grams%n", massGrams);
        System.out.printf("Initial Temperature: %.2f initialCelsius%n", initialTemp);
        System.out.printf("Final Temperature: %.2f finalCelsius%n", finalTemp);
        InputValidator.validate(massGrams, initialTemp, finalTemp);
        
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
                energy += calculateSpecificHeat(C_ICE, current, endIce);
                current = endIce;
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= 0.0 && current < 100.0 && target > current)) {
                double endWater = Math.max(Math.min(target, 100.0), 0);
                energy += calculateSpecificHeat(C_WATER, current, endWater);
                current = endWater;
                energy += detmineLatentHeat(current, target);
            }

            if ((current > 0.0 && current <= 100.0 && target < current)) {
                double endWater = Math.max(Math.min(target, 100.0), 0);
                energy += calculateSpecificHeat(C_WATER, current, endWater);
                current = endWater;
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= 100.0 && target > current) || (current > 100.0)) {
                double endSteam = Math.max(target, 100);
                energy += calculateSpecificHeat(C_STEAM, current, endSteam);
                current = endSteam;
                energy += detmineLatentHeat(current, target);
            } 
        }

        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        return energy;
    }

    public void setMass(double massGrams) {
        this.massGrams = massGrams;
    }

    public void setInitialTemp(double initialTemp) {
        this.initialTemp = initialTemp;
    }

    public void setFinalTemp(double finalTemp) {
        this.finalTemp = finalTemp;
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