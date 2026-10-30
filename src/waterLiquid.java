package src;

// water (liquid) specific heat is 4.18 j/g°C
// Q = mc(delta T)

public class waterLiquid{
    double mass;
    final double SPECIFICHEAT = 4.184;
    double initialTemperature;
    double finalTemperature;

    heatVaporizationWater heatVaporizationObject;

    public waterLiquid(
        double mass, 
        double initialTemperature, 
        double finalTemperature
    ) {
        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;

        this.heatVaporizationObject = new heatVaporizationWater(mass);
    }

    public double totalEnergy()
    {
        double energyToHeat = this.mass * this.SPECIFICHEAT * (this.finalTemperature - this.initialTemperature);
        if (finalTemperature == 100) {
            double energyToBoil = heatVaporizationObject.totalEnergy();
            return energyToHeat + energyToBoil;
        }
        return energyToHeat;
    }
}
