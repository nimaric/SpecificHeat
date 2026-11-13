package src;

public class ThermalCalculatorUtil {

  private static ThermalCalculator thermalCalculator;

  public void setKnownMaterialProperty(Materials knownMaterialProperty) {
    thermalCalculator = new ThermalCalculator(knownMaterialProperty);
  }

  public double calculateEnergyRequired(
    double mass,
    double initialTemp,
    double finalTemp
  ) {
    thermalCalculator.setMass(mass);
    thermalCalculator.setInitialTemp(initialTemp);
    thermalCalculator.setFinalTemp(finalTemp);
    return thermalCalculator.calculateEnergyRequired();
  }

  // Add other utility methods as needed
}
