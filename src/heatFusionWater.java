/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package src;

public class HeatFusionWater {
    private final double mass;
    private static final double FUSION_JOULES_PER_GRAM = 334.0;

    public HeatFusionWater(double mass) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }
        if (!Double.isFinite(mass)){
            throw new IllegalArgumentException("Mass cannot be NaN (Not a Number) or Infinity.");
        }

        this.mass = mass;
    }

    public double totalEnergy() {
        return mass * FUSION_JOULES_PER_GRAM;
    }
}