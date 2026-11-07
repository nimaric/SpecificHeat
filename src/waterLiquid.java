/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src;

public class WaterLiquid{
    private final double mass; // measured in grams
    private static final double SPECIFIC_HEAT = 4.184;
    private final double initialTemperature;
    private final double finalTemperature;

    public WaterLiquid(double mass, double initialTemperature, double finalTemperature) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }
        if (initialTemperature < -273.15 || finalTemperature < -273.15) {
            throw new IllegalArgumentException(
                "Temperature cannot be below absolute zero (-273.15)."
            );
        }
        if (!Double.isFinite(mass)){
            throw new IllegalArgumentException("Mass cannot be NaN (Not a Number) or Infinity.");
        }
        if (!Double.isFinite(initialTemperature) || !Double.isFinite(finalTemperature)){
            throw new IllegalArgumentException("Temperature cannot be NaN (Not a Number) or Infinity.");
        }

        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
    }

    public double totalEnergy()
    {
        double totalEnergy = this.mass * SPECIFIC_HEAT * 
        (this.finalTemperature - this.initialTemperature);
        System.out.println("[SpecificHeat]: " + totalEnergy + " = Q_liquid = " + this.mass + "(" + SPECIFIC_HEAT+ ")" + "(" + this.finalTemperature + "-" + this.initialTemperature + ")");
        return totalEnergy;
    }
}
