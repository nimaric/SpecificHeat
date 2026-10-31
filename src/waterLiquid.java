package src;

public class WaterLiquid{
    private double mass;
    private final double SPECIFICHEAT = 4.184;
    private double initialTemperature;
    private double finalTemperature;

    public WaterLiquid(
        double mass, 
        double initialTemperature, 
        double finalTemperature
    ) {
        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
    }

    public double totalEnergy()
    {
        return this.mass * 
        this.SPECIFICHEAT * 
        (this.finalTemperature - this.initialTemperature);
    }
}
