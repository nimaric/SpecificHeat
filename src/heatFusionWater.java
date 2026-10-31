package src;

public class HeatFusionWater {
    double mass;
    final double FUSIONJOULES = 334.0;
    final double FUSIONCALORIES = 79.72;

    public HeatFusionWater(
        double mass
    ){
        this.mass = mass;
    }

    public double totalEnergy(){
        return mass * FUSIONJOULES;
    }
}
