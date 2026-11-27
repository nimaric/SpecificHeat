/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.util.Scanner;

// Assumption: Qa = -Qb or Qmetal = -Qwater
// Q = mc/deltaT
// Q = mc(t_f - t_i)
// Q / mc = (t_f - t_i)
// t_i = t_f - (Q / mc)

// TODO: Validate inputs given to Calorimetry
public class Calorimetry {

  public static double findSpecificHeat(
    double totalEnergy,
    double mass,
    double finalTemp,
    double initialTemp
  ) {
    double specificHeat =
      totalEnergy / (mass * deltaTemperature(finalTemp, initialTemp));
    return specificHeat;
  }

  public static double findUnknownMass(
    double totalEnergy,
    double finalTemp,
    double initialTemp,
    double specificHeat
  ) {
    double unknownMass =
      totalEnergy / (specificHeat * deltaTemperature(finalTemp, initialTemp));
    return unknownMass;
  }

  public static double findUnknownInitialTemp(
    double totalEnergy,
    double mass,
    double finalTemp,
    double specificHeat
  ) {
    double initialTemp = finalTemp - totalEnergy / (mass * specificHeat);
    return initialTemp;
  }

  public static Materials enterMaterialName(Scanner scanner) {
    System.out.print("Enter material name: ");
    String inputMaterialName = scanner.nextLine();
    return Materials.getMaterial(inputMaterialName);
  }

  public static double enterMass(Scanner scanner, String materialType) {
    System.out.print("Enter " + materialType + " material's mass: ");
    return scanner.nextDouble();
  }

  public static double enterInitialTemp(Scanner scanner, String materialType) {
    System.out.print("Enter " + materialType + " material's initial temp: ");
    return scanner.nextDouble();
  }

  public static double enterFinalTemp(Scanner scanner) {
    System.out.print("Enter final temp: ");
    return scanner.nextDouble();
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Materials knownMaterial = enterMaterialName(scanner);
    double unknownMass = enterMass(scanner, "unknown");
    double knownMass = enterMass(scanner, "known");
    double knownInitialTemp = enterInitialTemp(scanner, "known");
    double unknownInitialTemp = enterInitialTemp(scanner, "unknown");
    double finalTemp = enterFinalTemp(scanner);

    scanner.close();
    ThermalCalculator thermalCalculator = new ThermalCalculator(
      knownMaterial,
      knownMass,
      knownInitialTemp,
      finalTemp
    );
    double energyTotal = thermalCalculator.calculateEnergyRequired();

    // Using the inverse of the energyTotal, we can find the specific heat of the unknown material if we know mc/deltaT
    double unknownSpecificHeat = findSpecificHeat(
      -energyTotal,
      unknownMass,
      finalTemp,
      unknownInitialTemp
    );

    System.out.printf("Specific Heat: %.2f J/gc%n", unknownSpecificHeat);
  }

  public static double deltaTemperature(double finalTemp, double initialTemp) {
    return finalTemp - initialTemp;
  }
}
