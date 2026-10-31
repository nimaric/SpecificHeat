package src;

public class HeatVaporizationWater {
    double mass;
    final double VAPORIZATIONJOULES = 2260.0; // Joules

    public HeatVaporizationWater(
        double mass
    ){
        this.mass = mass;
    }

    public double totalEnergy(){
        return mass * VAPORIZATIONJOULES;
    }
}
