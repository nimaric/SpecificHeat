package src;

// Assumption: Qa = -Qb or Qmetal = -Qwater 

public class Calorimetry {
    private static final Materials Q_A = Materials.WATER;
    // private static Materials Q_B = Materials.COPPER;

    public static void main(String[] args) {
        ThermalCalculator Q_AthermalCalculator = new ThermalCalculator(Q_A);
        double finalTemp = 64.7;

        double mass = 23.0;
        double initialTemp = 162.9;

        Q_AthermalCalculator.setMass(50);
        Q_AthermalCalculator.setInitialTemp(55);
        Q_AthermalCalculator.setFinalTemp(finalTemp);
        double energyTotal = Q_AthermalCalculator.calculateEnergyRequired();


        double specificHeat = findSpecificHeat(-energyTotal, mass, finalTemp, initialTemp);

        System.out.printf("Specific Heat: %.2f J/gc%n", specificHeat);

    }

    public static double findSpecificHeat(double totalEnergy, double mass, double finalTemp, double initialTemp){
        double specificHeat = totalEnergy / (mass * (finalTemp - initialTemp));
        return specificHeat;
    }
}

