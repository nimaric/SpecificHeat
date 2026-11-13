package src;

import java.util.Scanner;

// Assumption: Qa = -Qb or Qmetal = -Qwater

public class Calorimetry {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter material name: ");
    String inputMaterialName = scanner.nextLine();
    Materials knownMaterialProperty = Materials.getMaterial(inputMaterialName);

    System.out.println("Enter unknown material's mass: ");
    double unknownMass = scanner.nextDouble();

    System.out.println("Enter known material's mass: ");
    double knownMass = scanner.nextDouble();

    System.out.println("Enter known material's initial Temp: ");
    double knownInitalTemp = scanner.nextDouble();

    System.out.println("Enter unknown initial's temp: ");
    double unknownInitialTemp = scanner.nextDouble();

    System.out.println("Enter final temp: ");
    double bothMaterialFinalTemp = scanner.nextDouble();

    scanner.close();

    // Using a known material, we find the energy it gains or loses.
    ThermalCalculatorUtil thermalCalculatorUtil = new ThermalCalculatorUtil();
    thermalCalculatorUtil.setKnownMaterialProperty(knownMaterialProperty);
    double energyTotal = thermalCalculatorUtil.calculateEnergyRequired(
      knownMass,
      knownInitalTemp,
      bothMaterialFinalTemp
    );

    // Using the inverse of the energyTotal, we can find the specific heat of the unknown material if we know mc/deltaT
    double unknownSpecificHeat = findSpecificHeat(
      -energyTotal,
      unknownMass,
      bothMaterialFinalTemp,
      unknownInitialTemp
    );

    System.out.printf("Specific Heat: %.2f J/gc%n", unknownSpecificHeat);
  }

  public static double findSpecificHeat(
    double totalEnergy,
    double mass,
    double finalTemp,
    double initialTemp
  ) {
    double specificHeat = totalEnergy / (mass * (finalTemp - initialTemp));
    return specificHeat;
  }
}
