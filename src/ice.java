package src;

public class ice{
    double mass;
    final double SPECIFICHEAT = 2.108;
    double initialTemperature;
    double finalTemperature;

    heatFusionWater heatFusionWaterObj;

    public ice(
        double mass, 
        double initialTemperature, 
        double finalTemperature
    ) {
        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;

        this.heatFusionWaterObj = new heatFusionWater(mass);
    }

    public double totalEnergy()
    {
        double energyToHeat = this.mass * this.SPECIFICHEAT * (this.finalTemperature - this.initialTemperature);
        if (finalTemperature == 0) {
            double energyToMelting = heatFusionWaterObj.totalEnergy();
            return energyToHeat + energyToMelting;
        }
        return energyToHeat;
    }
}
