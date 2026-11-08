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

/**
 * Calculates thermal energy changes, sensible heat, and latent heat 
 * across multi-phase states for various materials using dynamic properties.
 */
public class ThermalCalculator {
    private static final Logger LOGGER = Logger.getLogger(ThermalCalculator.class.getName());
    private static final double EPSILON = 1e-6;
    private static final int BIT_22_LIMIT = 4194303;
    private static final String BIT_22_LIMIT_NAME = "22-bit integer";
    private double mass;
    private double initialTemp;
    private double finalTemp;
    private double fusionTemp;
    private double vaporizationTemp;

    private String materialName;
    
    private final Materials material;

    /**
     * Constructs a thermal calculator for a specific material.
     * 
     * @param material The material properties to use for calculations.
     */
    public ThermalCalculator(Materials material) {
        this.material = material;
        LOGGER.finest("Declare a new material: " + material);

        // Declares fusion and vaporization temps
        this.fusionTemp = material.fusionTemp;
        LOGGER.finest("Declare Fusion Temp: " + fusionTemp);
        this.vaporizationTemp = material.vaporizationTemp;
        LOGGER.finest("Declare Vaporization Temp: " + vaporizationTemp);

        // Declares material.name for logging
        this.materialName = material.name;
        LOGGER.finest("Declare a new name: " + materialName);
        LOGGER.finer("Generated Material: " + materialName + " Fusion Temp: " + fusionTemp + " Vaporization Temp: " + vaporizationTemp);
    }

    /**
     * Sets the mass of the material.
     * 
     * @param mass The mass in grams.
     */
    public void setMass(double mass) {
        this.mass = mass;
        LOGGER.finest("Declare mass: " + mass);
    }

    /**
     * Sets the initial temperature of the material.
     * 
     * @param initialTemp The initial temperature in Celsius.
     */
    public void setInitialTemp(double initialTemp) {
        this.initialTemp = initialTemp;
        LOGGER.finest("Declare Initial Temp: " + initialTemp);
    }

    /**
     * Sets the final temperature of the material.
     * 
     * @param finalTemp The final temperature in Celsius.
     */
    public void setFinalTemp(double finalTemp) {
        this.finalTemp = finalTemp;
        LOGGER.finest("Declare Final Temp: " + finalTemp);
    }

    /**
     * Calculates the total energy required to change the temperature of the material
     * from the initial temperature to the final temperature, considering phase changes.
     * 
     * @return The total energy in Joules.
     */
    public double calculateEnergyRequired() {
        System.out.printf("\nTotal Mass: %.2f Grams%n", mass);
        System.out.printf("Initial Temperature: %.2f Celsius%n", initialTemp);
        System.out.printf("Final Temperature: %.2f Celsius%n", finalTemp);
        LOGGER.finest("Printed out values of: mass: " + mass + " initialTemp: " + initialTemp + " finalTemp: " + finalTemp);
        
        InputValidator.validate(mass, initialTemp, finalTemp);
        LOGGER.finest("Sucessfully Validated: mass: " + mass + " initialTemp: " + initialTemp + " finalTemp: " + finalTemp);
        
        double energy = 0.0;
        LOGGER.finest("Initialied energy: " + energy);
        double current = initialTemp;
        LOGGER.finest("Initialied current: " + current);
        double target = finalTemp;
        LOGGER.finest("Initialied target: " + target);

        while (!approximatelyEquals(current, target)) {
            if (approximatelyEquals(energy, BIT_22_LIMIT)) {
                LOGGER.severe("Calculation Spiralled Out of Control (or hit the " + BIT_22_LIMIT_NAME + " limit)");
                System.exit(1);
            }
            LOGGER.finest("Current energy total does not exceed: the " + BIT_22_LIMIT_NAME);

            if (approximatelyEquals(initialTemp, fusionTemp) || approximatelyEquals(initialTemp, vaporizationTemp)) {
                LOGGER.finer("[INITIAL] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
            }

            if ((current < fusionTemp) || (current <= fusionTemp && target < current)) {
                LOGGER.fine("[SOLID] due to current: " + current + ", target: " + target);
                double endIce = Math.min(target, fusionTemp);
                energy += calculateSensibleHeat(current, endIce, Phase.SOLID);
                LOGGER.finest("[SOLID] calculateSensibleHeat Current Energy: " + energy);
                current = endIce;
                LOGGER.finer("[SOLID] Declare Current Temp: " + endIce);
                LOGGER.finer("[SOLID] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
                LOGGER.finest("[SOLID] determineLatentHeat Current Energy: " + energy);
            }

            if ((current >= fusionTemp && current < vaporizationTemp && target > current)) {
                LOGGER.fine("[LIQUID-HEATING] due to current: " + current + ", target: " + target);
                double endWater = Math.max(Math.min(target, vaporizationTemp), fusionTemp);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                LOGGER.finest("[LIQUID-HEATING] calculateSensibleHeat Current Energy: " + energy);
                current = endWater;
                LOGGER.finer("[LIQUID-HEATING] Declare Current Temp: " + endWater);
                LOGGER.finer("[LIQUID-HEATING] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
                LOGGER.finest("[LIQUID] determineLatentHeat Current Energy: " + energy);
            }

            if ((current > fusionTemp && current <= vaporizationTemp && target < current)) {
                LOGGER.fine("[LIQUID-COOLING] due to current: " + current + ", target: " + target);
                double endWater = Math.max(Math.min(target, vaporizationTemp), fusionTemp);
                energy += calculateSensibleHeat(current, endWater, Phase.LIQUID);
                LOGGER.finest("[LIQUID-COOLING] calculateSensibleHeat Current Energy: " + energy);
                current = endWater;
                LOGGER.finer("[LIQUID-COOLING] Declare Current Temp: " + endWater);
                LOGGER.finer("[LIQUID-COOLING] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
                LOGGER.finest("[LIQUID-COOLING] determineLatentHeat Current Energy: " + energy);
            }

            if ((current >= vaporizationTemp && target > current) || (current > vaporizationTemp)) {
                LOGGER.fine("[GAS] due to current: " + current + ", target: " + target);
                double endSteam = Math.max(target, vaporizationTemp);
                energy += calculateSensibleHeat(current, endSteam, Phase.GAS);
                LOGGER.finest("[GAS] calculateSensibleHeat Current Energy: " + energy);
                current = endSteam;
                LOGGER.finer("[GAS] Declare Current Temp: " + endSteam);
                LOGGER.finer("[GAS] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
                LOGGER.finest("[GAS] determineLatentHeat Current Energy: " + energy);
            } 
        }

        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        LOGGER.finest("Sucessfully printed Energy Stored: " + energy);
        return energy;
    }

    private double calculateSensibleHeat(double initialTemp, double finalTemp, Phase phase){
        LOGGER.fine("Calculating sensible heat -> Mass: " + mass + ", InitialT: " + initialTemp + ", FinalT: " + finalTemp + ", Phase: " + phase);

        double specificHeat;
        switch(phase) {
            case SOLID -> specificHeat = material.cSolid;
            case LIQUID -> specificHeat = material.cLiquid;
            case GAS -> specificHeat = material.cGas;
            default -> {
                LOGGER.severe("Unknown phase encountered: " + phase);
                throw new IllegalArgumentException("Unknown phase");
            }
        }
        double result = mass * specificHeat * (finalTemp - initialTemp);
        LOGGER.fine("Sensible heat result: " + result);
        return result;
    }

    private double calculateLatentHeat(double initialTemp, double finalTempt, Phase phase){
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
            LOGGER.severe("Unable to register phase transition of " + phase + " using initialTemp: " + initialTemp + " and finalTemp: " + finalTemp);
            throw new IllegalArgumentException("Unable to register phase transition");
        }
    }

    // Determines which phase is correct.
    private double determineLatentHeat(double current, double target){
        double energy = 0.0;
        LOGGER.finest("Initialied energy: " + energy);
        boolean phaseChange = approximatelyEquals(current, fusionTemp) || approximatelyEquals(current, vaporizationTemp);
        LOGGER.finest("Initialied phaseChange: " + phaseChange);
        
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

    /*
    FINEST: Level.FINEST
    FINER: Level.FINER
    FINE: Level.FINE
    INFO: Level.INFO
    WARNING: Level.WARNING // Default
    SEVERE: Level.SEVERE
    */

    // TODO create INFO logging for calculations
static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL); 
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.WARNING); // Change this Variable 
        LOGGER.setUseParentHandlers(false); 

        // Check if the current level is something other than WARNING
        if (LOGGER.getLevel() != Level.WARNING) {
            LOGGER.warning("WARNING: ThermalCalculator logger default level is set to " 
                + LOGGER.getLevel() + ", but WARNING was expected for production runs!");
        }
    }

    static Scanner scanner = new Scanner(System.in); 
    public static void main(String[] args) {
        

        System.out.println("Enter mass (g), initial temp (°C), and final temp (°C):");
        LOGGER.finest("Asking for: mass");
        double mass = scanner.nextDouble();
        LOGGER.finest("Moved on to asking for: t_i");
        double t_i = scanner.nextDouble();
        LOGGER.finest("Moved on to asking for: t_f");
        double t_f = scanner.nextDouble();

        // TODO allow user to choose Materials for calculation
        ThermalCalculator calculateEnergy = new ThermalCalculator(Materials.WATER);
        LOGGER.finest("Created calculator with material: " + "Materials.WATER");
        calculateEnergy.setMass(mass);
        LOGGER.finest("Set the material's mass: " + mass);
        calculateEnergy.setInitialTemp(t_i);
        LOGGER.finest("Set the material's initial Temp: " + t_i);
        calculateEnergy.setFinalTemp(t_f);
        LOGGER.finest("Set the material's final Temp: " + t_f);

        // Display results
        double totalEnergy = calculateEnergy.calculateEnergyRequired();
        LOGGER.finest("Received totalEnergy: " + totalEnergy);
        System.out.printf("\nTotal Energy Required: %.2f Joules%n", totalEnergy);
        LOGGER.finest("Printed out totalEnergy: " + totalEnergy);
    }

    private boolean approximatelyEquals(double a, double b) {
        LOGGER.finest("Received a request to determine equality of a: " + a + " and b: " + b);
        boolean equal = Math.abs(a - b) < EPSILON;
        LOGGER.finer(a + " and " + b + " equality is " + equal);
        return equal;
    }
}