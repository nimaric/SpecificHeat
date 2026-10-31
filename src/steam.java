package src;

public class Steam{
    private final double mass;
    private static final double SPECIFIC_HEAT = 1.996;
    private final double initialTemperature;
    private final double finalTemperature;

    public Steam(double mass, double initialTemperature, double finalTemperature) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }

        if (initialTemperature < -273.15 || finalTemperature < -273.15) {
            throw new IllegalArgumentException(
                "Temperature cannot be below absolute zero (-273.15)."
            );
        }

        this.mass = mass;
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
    }

    public double totalEnergy()
    {
        return this.mass * 
        SPECIFIC_HEAT * 
        (this.finalTemperature - this.initialTemperature);
    }
}
