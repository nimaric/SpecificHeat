/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src;

public class EnergyValues {

  private final double energy;
  private final double currentTemp;

  public EnergyValues(double energy, double currentTemp) {
    this.energy = energy;
    this.currentTemp = currentTemp;
  }

  // Getters
  public double getEnergy() {
    return energy;
  }

  public double getCurrentTemp() {
    return currentTemp;
  }
}
