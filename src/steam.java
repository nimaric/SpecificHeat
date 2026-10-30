package src;

// water (liquid) specific heat is 4.18 j/g°C
// Q = mc(delta T)


public class steam{
    double mass;
    final double SPECIFICHEAT = 1.996;
    double initialTemperature;
    double finalTemperature;


    public steam(
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
        return this.mass * this.SPECIFICHEAT * (this.finalTemperature - this.initialTemperature);
    }
}
