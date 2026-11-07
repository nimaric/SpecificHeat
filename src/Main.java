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
        if (massInGrams < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }
        // Check if the temperature goes below Absolute Zero
        if (-273.15 > initialTemperatureCelsius || -273.15 > finalTemperatureCelsius){
            throw new IllegalArgumentException("Temperature cannot be below absolute zero (-273.15).");
        }
        if (!Double.isFinite(massInGrams)){
            throw new IllegalArgumentException("Mass cannot be NaN (Not a Number) or Infinity.");
        }
        if (!Double.isFinite(initialTemperatureCelsius) || !Double.isFinite(finalTemperatureCelsius)){
            throw new IllegalArgumentException("Temperature cannot be NaN (Not a Number) or Infinity.");
        }

        System.out.println(massInGrams);
        System.out.println(initialTemperatureCelsius);
        System.out.println(finalTemperatureCelsius);

        double totalEnergyIce = 0.0;
        double totalEnergyFusion = 0.0;
        double totalEnergyWater = 0.0;
        double totalEnergyVaporization = 0.0;
        double totalEnergySteam = 0.0;

        double totalEnergyObj;
        
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius <= 0){
            totalEnergyIce = iceEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("Only Ice (Ice)");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius > 0){
            totalEnergyIce = iceEnergy(massInGrams, initialTemperatureCelsius, 0);
            System.out.println("Ice to Water (Ice)");
        }
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius <= 0){
            totalEnergyIce = iceEnergy(massInGrams, 0, finalTemperatureCelsius);
            System.out.println("Water to Ice (Ice)");
        }

        if ((initialTemperatureCelsius <= 0 && finalTemperatureCelsius >= 0) || (finalTemperatureCelsius <= 0 & initialTemperatureCelsius >= 0)){
            totalEnergyFusion = heatFusionEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("Fusion");
        }

        if (initialTemperatureCelsius >= 0 && initialTemperatureCelsius < 100 && finalTemperatureCelsius <= 100 && finalTemperatureCelsius >= 0){
            totalEnergyWater = liquidEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("Only Water (Water)");
        }
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius > 100){
            totalEnergyWater = liquidEnergy(massInGrams, initialTemperatureCelsius, 100);
            System.out.println("Water to Steam (Water)");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius > 100){
            totalEnergyWater = liquidEnergy(massInGrams, 0, 100);
            System.out.println("Water to Steam (Water) 0-100 Celsius");
        }
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius < 100 && finalTemperatureCelsius > 0){
            totalEnergyWater = liquidEnergy(massInGrams, 0, finalTemperatureCelsius);
            System.out.println("Ice to Water (Water)");
        }
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius < 0){
            totalEnergyWater = liquidEnergy(massInGrams, initialTemperatureCelsius, 0);
            System.out.println("Water to Ice (Water)");
        }
        if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius < 100 && finalTemperatureCelsius > 0){
            totalEnergyWater = liquidEnergy(massInGrams, 100, finalTemperatureCelsius);
            System.out.println("Steam to Water (Water)");
        }
        if (initialTemperatureCelsius > 100 && finalTemperatureCelsius < 0){
            totalEnergyWater = liquidEnergy(massInGrams, 100, 0);
            System.out.println("Steam to Water (Water) 100 - 0 Celsius");
        }

        if (initialTemperatureCelsius >= 100 || finalTemperatureCelsius >= 100){
            totalEnergyVaporization = heatVaporizationEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("Vaporized");
        }

        if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius > 100){
            totalEnergySteam = steamEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            System.out.println("Only Steam (Steam)");
        }
        if (initialTemperatureCelsius > 100 && finalTemperatureCelsius < 100){
            totalEnergySteam = steamEnergy(massInGrams, initialTemperatureCelsius, 100);
            System.out.println("Steam to Water (Steam)");
        }
        if (initialTemperatureCelsius < 100 && finalTemperatureCelsius > 100){
            totalEnergySteam = steamEnergy(massInGrams, 100, finalTemperatureCelsius);
            System.out.println("Water to Steam (Steam)");
        }

        if (initialTemperatureCelsius > finalTemperatureCelsius){
            totalEnergyObj = totalEnergyIce - totalEnergyFusion + totalEnergyWater - totalEnergyVaporization + totalEnergySteam;
            System.out.println("Reversed\n");
        } else {
            totalEnergyObj = totalEnergyIce + totalEnergyFusion + totalEnergyWater + totalEnergyVaporization + totalEnergySteam;
            System.out.println("Normal\n");
        }

        return totalEnergyObj;
    }
}