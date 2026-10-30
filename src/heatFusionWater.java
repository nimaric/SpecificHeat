package src;

public class heatFusionWater {
    double mass;
    final double FUSIONJOULES = 334.0;
    final double FUSIONCALORIES = 79.72;

    public heatFusionWater(
        double mass
    ){
        this.mass = mass;
    }

    public double totalEnergy(){
        return mass * FUSIONJOULES;
    }
}
