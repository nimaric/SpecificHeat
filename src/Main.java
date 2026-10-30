package src;

public class Main{
    public static void main(String[] args){
        double massInGrams = 250.0;
        double initialTemperatureCelsius = -20.0;
        double finalTemperatureCelsisus = 183.0;

        ice iceObject = null;
        waterLiquid WaterLiquidObject = null;
        steam steamObject = null;


        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus >= 0){ // Ice -> Water
            iceObject = new ice(massInGrams, initialTemperatureCelsius, 0);
        } else {
            iceObject = new ice(massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus);
        }

        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus < 100){ // Ice -> Water
            WaterLiquidObject = new waterLiquid(massInGrams, 0, finalTemperatureCelsisus);
        } else if(initialTemperatureCelsius < 0 && finalTemperatureCelsisus >= 100) { // Ice -> Water -> Steam
            WaterLiquidObject = new waterLiquid(massInGrams, 0, 100);
        } else { // Water
            WaterLiquidObject = new waterLiquid(massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus);
        }

        if (finalTemperatureCelsisus >= 100 && initialTemperatureCelsius < 0 ){ // Steam
            steamObject = new steam(massInGrams, 100, finalTemperatureCelsisus);
        } else {
            steamObject = new steam(massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus);
        }

        
        
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus <= 0){ // Ice
            System.out.println("Total Energy Required: " + iceObject.totalEnergy());
        } else if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus < 100){ // Ice -> Water
            System.out.println("Total Energy Required: " + (iceObject.totalEnergy()+ WaterLiquidObject.totalEnergy()));
        } else if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus >= 100){  // Ice -> Steam
            System.out.println("Total Energy Required: " + (iceObject.totalEnergy() + WaterLiquidObject.totalEnergy() + steamObject.totalEnergy()));
        }




    }

}