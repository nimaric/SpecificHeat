package src;

public class Steam{
    private double mass;
    private final double SPECIFICHEAT = 1.996;
    private double initialTemperature;
    private double finalTemperature;

    public Steam(
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
