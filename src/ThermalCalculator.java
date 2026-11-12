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
    private static final Level LOGGER_LEVEL = Constants.getLoggerLevel();
    private static final double EPSILON = Constants.getEpsilon();
    private static final int ITERATION_LIMIT = Constants.getIterationLimit();
    private double mass;
    private double initialTemp;
    private double finalTemp;
    private double fusionTemp;
    private double vaporizationTemp;
    
    private final Materials material;

    /**
     * Constructs a thermal calculator for a specific material.
     * 
     * @param material The material properties to use for calculations.
     */
    public ThermalCalculator(Materials material) {
        this.material = material;
        LOGGER.finest("Declare a new material: " + material.name);

        // Declares fusion and vaporization temps
        this.fusionTemp = material.fusionTemp;
        LOGGER.finest("Declare Fusion Temp: " + fusionTemp);
        this.vaporizationTemp = material.vaporizationTemp;
        LOGGER.finest("Declare Vaporization Temp: " + vaporizationTemp);

        // Declares material.name for logging
        LOGGER.finer("Generated Material: " + material.name + " Fusion Temp: " + fusionTemp + " Vaporization Temp: " + vaporizationTemp);
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

        int i = 0;
        while (!approximatelyEquals(current, target)) {
            // Checks if i does not exeed ITERATION_LIMIT, then iterate i
            checkIterationLimit(i);
            i++;

            if (approximatelyEquals(initialTemp, fusionTemp) || approximatelyEquals(initialTemp, vaporizationTemp)) {
                LOGGER.info("[INITIAL] calling determineLatentHeat using current: " + current + ", target: " + target);
                energy += determineLatentHeat(current, target);
            }

            if (!approximatelyEquals(current, target)){
                energy += determinePhaseChange(current, target).getEnergy();
                LOGGER.fine("determinePhaseChange energy: " + energy);
                current = determinePhaseChange(current, target).getCurrentTemp();
                LOGGER.fine("determinePhaseChange current: " + current);
            }
        }
        System.out.printf("\nCurrent Energy Stored: %.2f Joules%n", energy);
        LOGGER.finest("Sucessfully printed Energy Stored: " + energy);
        return energy;
    }

    public EnergyValues determinePhaseChange(double current, double target){
        double energy = 0.0;
        double specificHeat;

        if (checkSolidPhase(current, target)) {
            LOGGER.finest("[SOLID] due to current: " + current + ", target: " + target);
            double endIce = Math.min(target, fusionTemp);

            specificHeat = determineSpecificHeat(Phase.SOLID);
            energy += calculateSensibleHeat(specificHeat, current, endIce);
            LOGGER.finer("[SOLID] calculateSensibleHeat Current Energy: " + energy);

            current = endIce;
            LOGGER.finest("[SOLID] Declare Current Temp: " + endIce);

            LOGGER.info("[SOLID] calling determineLatentHeat using current: " + current + ", target: " + target);
            energy += determineLatentHeat(current, target);
            LOGGER.finer("[SOLID] determineLatentHeat Current Energy: " + energy);

            return new EnergyValues(energy, current);

        } else if (checkLiquidHeatingPhase(current, target)) {
            LOGGER.finest("[LIQUID-HEATING] due to current: " + current + ", target: " + target);
            double endWater = findMinimunWithMinimunCeiling(target, vaporizationTemp, fusionTemp);

            specificHeat = determineSpecificHeat(Phase.LIQUID);
            energy += calculateSensibleHeat(specificHeat, current, endWater);
            LOGGER.finer("[LIQUID-HEATING] calculateSensibleHeat Current Energy: " + energy);

            current = endWater;
            LOGGER.finest("[LIQUID-HEATING] Declare Current Temp: " + endWater);
            
            LOGGER.info("[LIQUID-HEATING] calling determineLatentHeat using current: " + current + ", target: " + target);
            energy += determineLatentHeat(current, target);
            LOGGER.finer("[LIQUID-HEATING] determineLatentHeat Current Energy: " + energy);

            return new EnergyValues(energy, current);

        } else if (checkLiquidCoolingPhase(current, target)) {
            LOGGER.finest("[LIQUID-COOLING] due to current: " + current + ", target: " + target);
            double endWater = findMinimunWithMinimunCeiling(target, vaporizationTemp, fusionTemp);
            
            specificHeat = determineSpecificHeat(Phase.LIQUID);
            energy += calculateSensibleHeat(specificHeat, current, endWater);
            LOGGER.finer("[LIQUID-COOLING] calculateSensibleHeat Current Energy: " + energy);

            current = endWater;
            LOGGER.finest("[LIQUID-COOLING] Declare Current Temp: " + endWater);

            LOGGER.info("[LIQUID-COOLING] calling determineLatentHeat using current: " + current + ", target: " + target);
            energy += determineLatentHeat(current, target);
            LOGGER.finer("[LIQUID-COOLING] determineLatentHeat Current Energy: " + energy);
            
            return new EnergyValues(energy, current);

        } else if (checkGasPhase(current, target)) {
            LOGGER.finest("[GAS] due to current: " + current + ", target: " + target);
            double endSteam = Math.max(target, vaporizationTemp);
            
            specificHeat = determineSpecificHeat(Phase.GAS);
            energy += calculateSensibleHeat(specificHeat, current, endSteam);
            LOGGER.finer("[GAS] calculateSensibleHeat Current Energy: " + energy);
            
            current = endSteam;
            LOGGER.finest("[GAS] Declare Current Temp: " + endSteam);
            
            LOGGER.info("[GAS] calling determineLatentHeat using current: " + current + ", target: " + target);
            energy += determineLatentHeat(current, target);
            LOGGER.finer("[GAS] determineLatentHeat Current Energy: " + energy);

            return new EnergyValues(energy, current);

        } else {
            throw new IllegalArgumentException("Can't determine correct phase");
        }
    }

    private boolean checkSolidPhase(double current, double target){
        return (current < fusionTemp) || (current <= fusionTemp && target < current);
    }

    private boolean checkLiquidHeatingPhase(double current, double target){
        return ((current >= fusionTemp && current < vaporizationTemp && target > current));
    }

    private boolean checkLiquidCoolingPhase(double current, double target){
        return (current > fusionTemp && current <= vaporizationTemp && target < current);
    }

    private boolean checkGasPhase(double current, double target){
        return (current >= vaporizationTemp && target > current) || (current > vaporizationTemp);
    }

    private void checkIterationLimit(int iteration) {
        if (iteration > ITERATION_LIMIT) {
            throw new IllegalStateException(
                "Hit " + ITERATION_LIMIT + " limit of phase calculation"
            );
        }
    }

    private double determineSpecificHeat(Phase phase){
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
        return specificHeat;
    }

    private double calculateSensibleHeat(double specificHeat, double initialTemp, double finalTemp){
        LOGGER.info("Calculating sensible heat -> Mass: " + mass + ", InitialT: " + initialTemp + ", FinalT: " + finalTemp);

        double result = mass * specificHeat * (finalTemp - initialTemp);
        LOGGER.fine("Sensible heat result: " + result);
        return result;
    }

    private double calculateLatentHeat(double initialTemp, double finalTemp, Phase phase){
        LOGGER.info("Calculating latent heat -> Mass: " + mass + ", InitialT: " + initialTemp + ", FinalT: " + finalTemp + ", Phase: " + phase);

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

static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL); 
        LOGGER.addHandler(handler);
        LOGGER.setLevel(LOGGER_LEVEL);
        LOGGER.setUseParentHandlers(false); 

        // Check if the current level is something other than INFO
        if (LOGGER.getLevel() != Level.WARNING) {
            LOGGER.warning("ThermalCalculator logger default level is set to " 
                + LOGGER.getLevel() + ", but WARNING was expected for production runs!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter material name: ");
        LOGGER.finest("Asking for: material");

        String inputMaterialName = scanner.nextLine();

        Materials materialProperty = Materials.getMaterial(inputMaterialName);
        LOGGER.finest("Created materialProperty with " + inputMaterialName);


        System.out.println("Enter mass (g), initial temp (°C), and final temp (°C):");
        LOGGER.finest("Moved on to asking for: mass");
        double mass = scanner.nextDouble();
        LOGGER.finest("Moved on to asking for: t_i");
        double t_i = scanner.nextDouble();
        LOGGER.finest("Moved on to asking for: t_f");
        double t_f = scanner.nextDouble();

        ThermalCalculator calculateEnergy = new ThermalCalculator(materialProperty);
        LOGGER.finest("Created calculator with material: " + "Materials.WATER");
        calculateEnergy.setMass(mass);
        LOGGER.finest("Set the material's mass: " + mass);
        calculateEnergy.setInitialTemp(t_i);
        LOGGER.finest("Set the material's initial Temp: " + t_i);
        calculateEnergy.setFinalTemp(t_f);
        LOGGER.finest("Set the material's final Temp: " + t_f);

        // Close scanner as parameters are all inputted
        scanner.close(); 

        // Calculate using provided parameters to display
        double totalEnergy = calculateEnergy.calculateEnergyRequired();
        LOGGER.finest("Received totalEnergy: " + totalEnergy);

        // Display results
        System.out.printf("\nTotal Energy Required: %.2f Joules%n", totalEnergy);
        LOGGER.finest("Printed out totalEnergy: " + totalEnergy);
    }

    private boolean approximatelyEquals(double a, double b) {
        boolean equal = Math.abs(a - b) < EPSILON;
        LOGGER.finest(a + " and " + b + " equality is " + equal);
        return equal;
    }

    /*
     findMinimunWithMinimunCeiling
     - find the minimun of a & b
     - compare the minimun with c
     - choose the maximun between the minimun and c
     - useful when choosing the absolute minimun breaks calculation due to sensible heat being calculated improperly
    */
    private double findMinimunWithMinimunCeiling(double a, double b, double c){
        double value = Math.max(Math.min(a, b), c);
        return value;
    }
}