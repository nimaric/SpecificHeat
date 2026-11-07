/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package src;

public class HeatFusionWater {
    private final double mass; // measured in grams
    private final double initialTemperature;
    private final double finalTemperature;
    private static final double FUSION_JOULES_PER_GRAM = 334.0;

    public HeatFusionWater(double mass, double initialTemperature, double finalTemperature) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }
        if (!Double.isFinite(mass)){
            throw new IllegalArgumentException("Mass cannot be NaN (Not a Number) or Infinity.");
        }

        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
    }

    public double totalEnergy() {
        if (initialTemperature <= finalTemperature) {
            double totalEnergy = mass * FUSION_JOULES_PER_GRAM;
            System.out.println("[SpecificHeat]: " + totalEnergy + " = Q_hf = (" + mass + ")" + "(" + FUSION_JOULES_PER_GRAM + ")");
            return totalEnergy;
        } else {
            double totalEnergy = mass * -FUSION_JOULES_PER_GRAM;
            System.out.println("[SpecificHeat]: " + totalEnergy + " = Q_hf = (" + mass + ")" + "(" + FUSION_JOULES_PER_GRAM + ")");
            return totalEnergy;
        }
    }
}