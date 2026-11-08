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
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class ThermalCalculator {
    private static final Logger LOGGER = Logger.getLogger(ThermalCalculator.class.getName());
    
    static {
        // By default, Java's console handler might block FINE logs. 
        // This static block configures the handler to allow verbose logs through.
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL); // Allow all levels of logs
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);   // Set the logger to capture everything
        LOGGER.setUseParentHandlers(false); // Prevent duplicate logging outputs
    }

    private final Materials material;

    private double mass;
    private double initialTemp;
    private double finalTemp;
    private double fusionTemp;
    private double vaporizationTemp;

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
    public ThermalCalculator(Materials material) {
        this.material = material;

        // Declares fusion and vaporization temps
        this.fusionTemp = material.fusionTemp;
        this.vaporizationTemp = material.vaporizationTemp;
    }

    private static final double EPSILON = 1e-6;

    private boolean approximatelyEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public double calculateSensibleHeat(double initialTemp, double finalTemp, Phase phase){
        LOGGER.fine("Calculating sensible heat -> Mass: " + mass + ", InitialT: " + initialTemp + ", FinalT: " + finalTemp + ", Phase: " + phase);

        double specificHeat;
        switch(phase) {
            case SOLID -> specificHeat = material.cSolid;
            case LIQUID -> specificHeat = material.cLiquid;
            case GAS -> specificHeat = material.cGas;
            default -> {
                LOGGER.warning("Unknown phase encountered: " + phase);
                throw new IllegalArgumentException("Unknown phase");
            }
        }
        double result = mass * specificHeat * (finalTemp - initialTemp);
        LOGGER.fine("Sensible heat result: " + result);
        return result;
    }

    public double calculateLatentHeat(double initialTemp, double finalTempt, Phase phase){
        LOGGER.fine("Calculating latent heat -> Mass: " + mass + ", InitialT: " + initialTemp + ", FinalT: " + finalTemp + ", Phase: " + phase);

        double latentHeat;
        switch(phase) {
            case FUSION -> latentHeat = material.latentFusion;
            case VAPORIZATION -> latentHeat = material.latentVaporization;
            default -> {
                LOGGER.warning("Unknown phase encountered: " + phase);
                throw new IllegalArgumentException("Unknown phase");
            }
        }

        if (initialTemp < finalTemp){ 
            LOGGER.fine("Calculating latent heat being absorbed");
            return mass * latentHeat; 
        }
        if (initialTemp > finalTemp){ 
            LOGGER.fine("Calculating latent heat being released");
            return mass * -latentHeat; 
        } else {
            LOGGER.severe("Unable to register phase: " + phase);
            System.exit(1);
            return 0.0;
        }
    }

    // Determines which phase is correct.
    public double detmineLatentHeat(double current, double target){
        double energy = 0.0;
        boolean phaseChange = approximatelyEquals(current, fusionTemp) || approximatelyEquals(current, vaporizationTemp);

        if (approximatelyEquals(current, target)) {
            LOGGER.fine("There is no need for calculateLatentHeat() because current = target.");
            return energy; 
        }

        if (phaseChange){
            if (approximatelyEquals(current, fusionTemp)) {
                LOGGER.fine("Using Phase.FUSION for calculateLatentHeat()");
                energy += calculateLatentHeat(current, target, Phase.FUSION);
            } else if (approximatelyEquals(current, vaporizationTemp)) {
                LOGGER.fine("Using Phase.VAPORIZATION for calculateLatentHeat()");
                energy += calculateLatentHeat(current, target, Phase.VAPORIZATION);
            }
        } else {
            LOGGER.fine("There is no need for calculateLatentHeat(). phaseChange: " + phaseChange);
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
                LOGGER.warning("Calculation Spiralled Out of Control (or hit the 22-bit integer limit)");
                System.exit(1);
            }

            if (approximatelyEquals(initialTemp, fusionTemp) || approximatelyEquals(initialTemp, vaporizationTemp)) {
                LOGGER.fine("[INITIAL] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += detmineLatentHeat(current, target);
            }

            if ((current < fusionTemp) || (current <= fusionTemp && target < current)) {
                LOGGER.fine("[SOLID] due to current: " + current + ", target: " + target);
                double endIce = Math.min(target, fusionTemp);
                energy += calculateSensibleHeat(current, endIce, Phase.SOLID);
                current = endIce;
                LOGGER.fine("[SOLID] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= fusionTemp && current < vaporizationTemp && target > current)) {
                LOGGER.fine("[LIQUID-HEATING] due to current: " + current + ", target: " + target);
                double endWater = Math.max(Math.min(target, vaporizationTemp), fusionTemp);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                current = endWater;
                LOGGER.fine("[LIQUID-HEATING] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += detmineLatentHeat(current, target);
            }

            if ((current > fusionTemp && current <= vaporizationTemp && target < current)) {
                LOGGER.fine("[LIQUID-COOLING] due to current: " + current + ", target: " + target);
                double endWater = Math.max(Math.min(target, vaporizationTemp), fusionTemp);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                current = endWater;
                LOGGER.fine("[LIQUID-COOLING] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += detmineLatentHeat(current, target);
            }

            if ((current >= vaporizationTemp && target > current) || (current > vaporizationTemp)) {
                LOGGER.fine("[GAS] due to current: " + current + ", target: " + target);
                double endSteam = Math.max(target, vaporizationTemp);
                energy += calculateSensibleHeat(current, endSteam, Phase.GAS);
                current = endSteam;
                LOGGER.fine("[GAS] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += detmineLatentHeat(current, target);
            } 
        }

        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        return energy;
    }

    static Scanner scanner = new Scanner(System.in); 
    public static void main(String[] args) {
        /*
        Turn ON: Level.FINE
        TURN OFF: Level.WARNING
        */
        Logger.getLogger("src.WaterThermalCalculator").setLevel(Level.WARNING);

        System.out.println("Enter mass (g), initial temp (°C), and final temp (°C):");
        double mass = scanner.nextDouble();
        double t_i = scanner.nextDouble();
        double t_f = scanner.nextDouble();

        ThermalCalculator calculateEnergy = new ThermalCalculator(Materials.WATER);
        calculateEnergy.setMass(mass);
        calculateEnergy.setInitialTemp(t_i);
        calculateEnergy.setFinalTemp(t_f);

        // Display results
        double totalEnergy = calculateEnergy.calculateEnergyRequired();
        System.out.printf("\nTotal Energy Required: %.2f Joules%n", totalEnergy);
    }
}