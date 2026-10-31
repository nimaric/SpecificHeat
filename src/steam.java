package src;

public class steam{
    double mass;
    final double SPECIFICHEAT = 1.996;
    double initialTemperature;
    double finalTemperature;

    heatVaporizationWater heatVaporizationWaterObj;

    public steam(
        double mass, 
        double initialTemperature, 
        double finalTemperature
    ) {
        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;

        this.heatVaporizationWaterObj = new heatVaporizationWater(mass);
    }

    public double totalEnergy()
    {
        double energyToHeat = this.mass * this.SPECIFICHEAT * (this.finalTemperature - this.initialTemperature);
        if (finalTemperature == 100){
            double energyToBoil = heatVaporizationWaterObj.totalEnergy();
            return energyToHeat + energyToBoil;
        }
        return energyToHeat;
    }
}
