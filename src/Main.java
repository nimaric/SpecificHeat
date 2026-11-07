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

import src.calculations.HeatFusionWater;
import src.calculations.HeatVaporizationWater;
import src.calculations.WaterLiquid;

public class Main{
    static Scanner scanner = new Scanner(System.in); 
    double mass = scanner.nextDouble();
    double t_i = scanner.nextDouble();
    double t_f = scanner.nextDouble();

    double message = calculateEnergy(mass, t_i, t_f);
    // Option 2: Save to a variable first
    public static void main(String[] args) {
        Main main = new Main();
    System.out.println("\n[SpecificHeat]: Q_total = " + main.message);
    }

    private static double iceEnergy(
        double mass,
        double start,
        double end
    ){
        return new Ice(mass, start, end).totalEnergy();
    }

    private static double heatFusionEnergy(
        double mass,
        double start,
        double end
    ){
        return new HeatFusionWater(mass, start, end).totalEnergy();
    }
   
    private static double liquidEnergy(
        double mass,
        double start,
        double end
    ){
        return new WaterLiquid(mass, start, end).totalEnergy();
    }

    private static double heatVaporizationEnergy(
        double mass,
        double start,
        double end
    ){
        return new HeatVaporizationWater(mass, start, end).totalEnergy();
    }

    private static double steamEnergy(
        double mass,
        double start,
        double end
    ){
        return new Steam(mass, start, end).totalEnergy();
    }

    public static double calculateEnergy(
        double massInGrams,
        double initialTemperatureCelsius,
        double finalTemperatureCelsius
    ){
        InputValidator.validate(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);

        System.out.println("[SpecificHeat]: total Mass: " + massInGrams);
        System.out.println("[SpecificHeat]: initial Temperature: " + initialTemperatureCelsius);
        System.out.println("[SpecificHeat]: final Temperature: " + finalTemperatureCelsius);

        double totalEnergyObj = 0.0;
        
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius <= 0){
            totalEnergyObj += iceEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Only Ice (Ice)");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius > 0){
            totalEnergyObj += iceEnergy(massInGrams, initialTemperatureCelsius, 0);
            System.out.println("[SpecificHeat]: Ice to Water (Ice)");
        }
        if (initialTemperatureCelsius > 0 && initialTemperatureCelsius < 100 && finalTemperatureCelsius <= 0){
            totalEnergyObj += iceEnergy(massInGrams, 0, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Water to Ice (Ice)");
        }

        if ((initialTemperatureCelsius <= 0 && finalTemperatureCelsius >= 0) || (finalTemperatureCelsius <= 0 & initialTemperatureCelsius >= 0)){
            totalEnergyObj += heatFusionEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Fusion");
        }

        if (initialTemperatureCelsius >= 0 && initialTemperatureCelsius < 100 && finalTemperatureCelsius <= 100 && finalTemperatureCelsius >= 0){
            totalEnergyObj += liquidEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Only Water (Water)");
        }
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius > 100){
            totalEnergyObj += liquidEnergy(massInGrams, initialTemperatureCelsius, 100);
            System.out.println("[SpecificHeat]: Water to Steam (Water)");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius > 100){
            totalEnergyObj += liquidEnergy(massInGrams, 0, 100);
            System.out.println("[SpecificHeat]: Water to Steam (Water) 0-100 Celsius");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius < 100 && finalTemperatureCelsius > 0){
            totalEnergyObj += liquidEnergy(massInGrams, 0, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Ice to Water (Water)");
        }
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius < 0){
            totalEnergyObj += liquidEnergy(massInGrams, initialTemperatureCelsius, 0);
            System.out.println("[SpecificHeat]: Water to Ice (Water)");
        }
        if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius < 100 && finalTemperatureCelsius > 0){
            totalEnergyObj += liquidEnergy(massInGrams, 100, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Steam to Water (Water)");
        }
        if (initialTemperatureCelsius > 100 && finalTemperatureCelsius < 0){
            totalEnergyObj += liquidEnergy(massInGrams, 100, 0);
            System.out.println("[SpecificHeat]: Steam to Water (Water) 100 - 0 Celsius");
        }

        if (initialTemperatureCelsius >= 100 || finalTemperatureCelsius >= 100){
            totalEnergyObj += heatVaporizationEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Vaporized");
        }

        if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius > 100){
            totalEnergyObj += steamEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Only Steam (Steam)");
        }
        if (initialTemperatureCelsius > 100 && finalTemperatureCelsius < 100){
            totalEnergyObj += steamEnergy(massInGrams, initialTemperatureCelsius, 100);
            System.out.println("[SpecificHeat]: Steam to Water (Steam)");
        }
        if (initialTemperatureCelsius < 100 && finalTemperatureCelsius > 100){
            totalEnergyObj += steamEnergy(massInGrams, 100, finalTemperatureCelsius);
            System.out.println("[SpecificHeat]: Water to Steam (Steam)");
        }

        return totalEnergyObj;
    }
}