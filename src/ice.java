package src;

public class Ice{
    private double mass;
    private final double SPECIFICHEAT = 2.108;
    private double initialTemperature;
    private double finalTemperature;

    public Ice(
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
