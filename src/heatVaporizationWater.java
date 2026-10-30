package src;

public class heatVaporizationWater {
    double mass;
    final double VAPORIZATIONJOULES = 2260.0; // Joules

    public heatVaporizationWater(
        double mass
    ){
        this.mass = mass;
    }

    public double totalEnergy(){
        return mass * VAPORIZATIONJOULES;
    }
}
