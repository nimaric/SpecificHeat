package src;

public class Main{
    public static double calculateEnergy(
        double massInGrams,
        double initialTemperatureCelsius,
        double finalTemperatureCelsisus
    ){
        ice iceObject = null;
        waterLiquid waterLiquidObject = null;
        steam steamObject = null;   

        if (massInGrams < 0) {
            // Throw an error here
            throw new IllegalArgumentException("Mass cannot be negative!");
        }
        // Check if the temperature goes below Absolute Zero
        if (-273.15 > initialTemperatureCelsius || -273.15 > finalTemperatureCelsisus){
            // Throw an error here
            throw new IllegalArgumentException("Celsius cannot go below Absolute Zero (-273.15)!");
        }
        System.out.println("PAIN");

        double objectTotalEnergy = 0.0;
        
        // Ice Only
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus < 0){
            iceObject = new ice (
                massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus
            );
            objectTotalEnergy += iceObject.totalEnergy();
            System.out.println("1: "+ objectTotalEnergy);
        }

        // Ice to Water
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus > 0){
            iceObject = new ice (
                massInGrams, initialTemperatureCelsius, 0
            );
            objectTotalEnergy += iceObject.totalEnergy();
            System.out.println("2: "+ objectTotalEnergy);

            // Water to Ice
        } else if (initialTemperatureCelsius > 0 && finalTemperatureCelsisus < 0){
                        iceObject = new ice (
                massInGrams, 0, finalTemperatureCelsisus
            );
            objectTotalEnergy += iceObject.totalEnergy();
            System.out.println("3: "+ objectTotalEnergy);
        }
        System.out.println("After Ice: "+ objectTotalEnergy);

        /*
        // Ice Phase: if initial or final temperature is less than 0 celsisus
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsisus < 0){
            iceObject = new ice (
                massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus
            );
            objectTotalEnergy += iceObject.totalEnergy();

            // Ice to melting point
        } else if(initialTemperatureCelsius <= 0 && finalTemperatureCelsisus >= 0 ){
            iceObject = new ice (
                massInGrams, initialTemperatureCelsius, 0
            );
            objectTotalEnergy += iceObject.totalEnergy();

            // Water to Ice
        } else if(initialTemperatureCelsius >= 0 && finalTemperatureCelsisus < 0){
            iceObject = new ice (
                massInGrams, 0, finalTemperatureCelsisus
            );
            objectTotalEnergy += iceObject.totalEnergy();
        }
        */

        // Water-only Phase
        if ((initialTemperatureCelsius < 100 && finalTemperatureCelsisus < 100) && (initialTemperatureCelsius > 0 && finalTemperatureCelsisus > 0)){
            waterLiquidObject = new waterLiquid (
                massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus
            );
            objectTotalEnergy += waterLiquidObject.totalEnergy();
            System.out.println("4: "+ objectTotalEnergy);

            //Water to steam
        } else if ((initialTemperatureCelsius < 0 && finalTemperatureCelsisus > 100)){ 
            waterLiquidObject = new waterLiquid (
                massInGrams, initialTemperatureCelsius, 100
            );
            objectTotalEnergy += waterLiquidObject.totalEnergy();
            System.out.println("5: "+ objectTotalEnergy);

            //Steam to water
        } else if ((initialTemperatureCelsius < 100 && finalTemperatureCelsisus > 0 && initialTemperatureCelsius > 0)){
            waterLiquidObject = new waterLiquid (
                massInGrams, 100, finalTemperatureCelsisus
            );
            objectTotalEnergy += waterLiquidObject.totalEnergy();
            System.out.println("6: "+ objectTotalEnergy);
        }

        System.out.println("After Water: "+ objectTotalEnergy);

        // Steam-only Phase
        if ((initialTemperatureCelsius > 100 && finalTemperatureCelsisus >= 100)){
            steamObject = new steam(
                massInGrams, initialTemperatureCelsius, finalTemperatureCelsisus
            );
            objectTotalEnergy += steamObject.totalEnergy();
            System.out.println("7: "+ objectTotalEnergy);

            // Steam to water
        } else if ((initialTemperatureCelsius > 100 && finalTemperatureCelsisus < 100)){
            steamObject = new steam(
                massInGrams, initialTemperatureCelsius, 100
            );
            objectTotalEnergy += steamObject.totalEnergy();
            System.out.println("8: "+ objectTotalEnergy);
            
            // Water to steam
        } else if ((initialTemperatureCelsius < 100 && finalTemperatureCelsisus >= 100)){ 
            waterLiquidObject = new waterLiquid (
                massInGrams, initialTemperatureCelsius, 100
            );
            objectTotalEnergy += waterLiquidObject.totalEnergy();
            System.out.println("9: "+ objectTotalEnergy);
        }
        System.out.println("After Steam: "+ objectTotalEnergy);
        return objectTotalEnergy;
    }
}