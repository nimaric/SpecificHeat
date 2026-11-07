/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src.calculations;

import src.InputValidator;

public class WaterLiquid{
    private final double mass; // measured in grams
    private static final double SPECIFIC_HEAT = 4.184;
    private final double initialTemperature;
    private final double finalTemperature;

    public WaterLiquid(double mass, double initialTemperature, double finalTemperature) {
        InputValidator.validate(mass, initialTemperature, finalTemperature);

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
