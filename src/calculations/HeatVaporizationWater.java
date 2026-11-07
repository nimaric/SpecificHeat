/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src.calculations;

import src.InputValidator;

public class HeatVaporizationWater {
    private final double mass; // measured in grams
    private final double initialTemperature;
    private final double finalTemperature;
    private static final double VAPORIZATION_JOULES_PER_GRAM = 2260.0;

    public HeatVaporizationWater(double mass, double initialTemperature, double finalTemperature) {
        InputValidator.validate(mass, initialTemperature, finalTemperature);

        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
    }

    public double totalEnergy() {
        if (initialTemperature <= finalTemperature) {
            double totalEnergy = mass * VAPORIZATION_JOULES_PER_GRAM;
            System.out.println("[SpecificHeat]: " + totalEnergy + " = Q_hv = (" + mass + ")" + "(" + VAPORIZATION_JOULES_PER_GRAM + ")");
            return totalEnergy;
        } else {
            double totalEnergy = mass * -VAPORIZATION_JOULES_PER_GRAM;
            System.out.println("[SpecificHeat]: " + totalEnergy + " = Q_hv = (" + mass + ")" + "(" + VAPORIZATION_JOULES_PER_GRAM + ")");
            return totalEnergy;
        }
    }
}
