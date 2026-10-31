package src;

public class Main{
    public static double calculateEnergy(
        double massInGrams,
        double initialTemperatureCelsius,
        double finalTemperatureCelsius
    ){
        if (massInGrams < 0) {
            // Throw an error here
            throw new IllegalArgumentException("Mass cannot be negative!");
        }
        // Check if the temperature goes below Absolute Zero
        if (-273.15 > initialTemperatureCelsius || -273.15 > finalTemperatureCelsius){
            // Throw an error here
            throw new IllegalArgumentException("Celsius cannot go below Absolute Zero (-273.15)!");
        }

        // ONLY ICE
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius < 0){
            ice iceObject = new ice(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            return iceObject.totalEnergy();
        }

        // ONLY ICE TO WATER (2)
        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius >= 0){
            ice iceObject = new ice(massInGrams, initialTemperatureCelsius, 0);
            waterLiquid liquidObject = new waterLiquid(massInGrams, 0, finalTemperatureCelsius);
            return iceObject.totalEnergy()+ liquidObject.totalEnergy();
        }

        // ONLY WATER (2)
        if (initialTemperatureCelsius > 0 && finalTemperatureCelsius < 100){
            waterLiquid liquidObject = new waterLiquid(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            return liquidObject.totalEnergy();
        }

        // ONLY WATER TO STEAM (2)
        if (initialTemperatureCelsius > 0 && initialTemperatureCelsius < 100 && finalTemperatureCelsius >= 100){
            waterLiquid liquidObject = new waterLiquid(massInGrams, initialTemperatureCelsius, 100);
            steam steamObject = new steam(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            return liquidObject.totalEnergy() + steamObject.totalEnergy();
        }

        // ONLY STEAM
        if (initialTemperatureCelsius >= 100 & finalTemperatureCelsius >= 100){
            steam steamObject = new steam(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
            return steamObject.totalEnergy();
        }
        
        // ONLY ICE TO STEAM (Should include the second to 4th function)


        // REVERSE ORDERS


        return 0.0;
    }
}