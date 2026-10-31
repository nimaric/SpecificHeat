package src;

public class HeatVaporizationWater {
    private final double mass;
    private static final double VAPORIZATION_JOULES_PER_GRAM = 2260.0;

    public HeatVaporizationWater(double mass) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }

        this.mass = mass;
    }

    public double totalEnergy() {
        return mass * VAPORIZATION_JOULES_PER_GRAM;
    }
}
