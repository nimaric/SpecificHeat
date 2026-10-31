package src;

public class HeatFusionWater {
    private final double mass;
    private static final double FUSION_JOULES_PER_GRAM = 334.0;

    public HeatFusionWater(double mass) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass cannot be negative.");
        }

        this.mass = mass;
    }

    public double totalEnergy() {
        return mass * FUSION_JOULES_PER_GRAM;
    }
}