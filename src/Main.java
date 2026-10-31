package src;

public class Main{
    private static double iceEnergy(
        double mass,
        double start,
        double end
    ){
        return new Ice(mass, start, end).totalEnergy();
    }

    private static double heatFusionEnergy(
        double mass
    ){
        return new HeatFusionWater(mass).totalEnergy();
    }
   
    private static double liquidEnergy(
        double mass,
        double start,
        double end
    ){
        return new WaterLiquid(mass, start, end).totalEnergy();
    }

    private static double heatVaporizationEnergy(
        double mass
    ){
        return new HeatVaporizationWater(mass).totalEnergy();
    }

    private static double steamEnergy(
        double mass,
        double start,
        double end
    ){
        return new Steam(mass, start, end).totalEnergy();
    }

    public static double calculateEnergy(
        double massInGrams,
        double initialTemperatureCelsius,
        double finalTemperatureCelsius
    ){
        System.out.println("calculateEnergy() initialized!");
        System.out.println("MASS: " + massInGrams + "g");
        System.out.println("T_i: " + initialTemperatureCelsius);
        System.out.println("T_f: " + finalTemperatureCelsius);

        if (massInGrams < 0) {
            System.out.println("SYSTEM VALIDATION: FAILED (1) Negative Mass\n");
            throw new IllegalArgumentException("Mass cannot be negative.");
        }
        // Check if the temperature goes below Absolute Zero
        if (-273.15 > initialTemperatureCelsius || -273.15 > finalTemperatureCelsius){
            System.out.println("SYSTEM VALIDATION: FAILED (2) Absolute Zero\n");
            throw new IllegalArgumentException("Temperature cannot be below absolute zero (-273.15).");
        }
        if (!Double.isFinite(massInGrams)){
            System.out.println("SYSTEM VALIDATION: FAILED (3) NaN / Not a Number\n");
            throw new IllegalArgumentException("Mass cannot be NaN (Not a Number) or Infinity.");
        }
        if (!Double.isFinite(initialTemperatureCelsius) || !Double.isFinite(finalTemperatureCelsius)){
            System.out.println("SYSTEM VALIDATION: FAILED (3) NaN / Not a Number\n");
            throw new IllegalArgumentException("Temperature cannot be NaN (Not a Number) or Infinity.");
        }

        System.out.println("SYSTEM VALIDATION: PASSED");

        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius < 0){
            System.out.println("ICE CALCULATIONS: ONLY ICE (1)\n");

            return iceEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
        }

        if (initialTemperatureCelsius <= 0 && finalTemperatureCelsius >= 0 && finalTemperatureCelsius < 100){
            System.out.println("ICE CALCULATIONS: ONLY ICE TO WATER (2)\n");

            return iceEnergy(massInGrams, initialTemperatureCelsius, 0)
            + heatFusionEnergy(massInGrams)
            + liquidEnergy(massInGrams, 0, finalTemperatureCelsius);
            
        } else if (initialTemperatureCelsius >= 0 && initialTemperatureCelsius < 100 && finalTemperatureCelsius < 0){
            System.out.println("WATER CALCULATIONS: ONLY WATER TO ICE (ICE_UNIQUE_3)\n");

            return liquidEnergy(massInGrams, initialTemperatureCelsius, 0)
            - heatFusionEnergy(massInGrams)
            + iceEnergy(massInGrams, 0, finalTemperatureCelsius);
        }

        if (initialTemperatureCelsius <= 0 && finalTemperatureCelsius >= 100){
            System.out.println("ICE CALCULATIONS: ONLY ICE TO STEAM (4)\n");

            return iceEnergy(massInGrams, initialTemperatureCelsius, 0) // Q_ice
            + heatFusionEnergy(massInGrams) // Q_Hf
            + liquidEnergy(massInGrams, 0, 100) // Q_water
            + heatVaporizationEnergy(massInGrams) // Q_Hv
            + steamEnergy(massInGrams, 100, finalTemperatureCelsius); // Q_steam  

        } else if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius < 0){
            System.out.println("STEAM CALCULATIONS: ONLY STEAM TO ICE (ICE_UNIQUE_5)\n");

            return steamEnergy(massInGrams, initialTemperatureCelsius, 100) // Q_steam  
            - heatVaporizationEnergy(massInGrams)
            + liquidEnergy(massInGrams, 100, 0) // Q_water
            - heatFusionEnergy(massInGrams) // Q_Hf
            + iceEnergy(massInGrams, 0, finalTemperatureCelsius);
        }

        System.out.println("ICE CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius < 100 
            && finalTemperatureCelsius < 100
            && finalTemperatureCelsius > 0
        ){
            System.out.println("WATER CALCULATIONS: ONLY WATER (1)\n");
            
            return liquidEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);
        }

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius <= 100 
            && finalTemperatureCelsius >= 100
        ){
            System.out.println("WATER CALCULATIONS: ONLY WATER TO STEAM (2)\n");

            return liquidEnergy(massInGrams, initialTemperatureCelsius, 100) 
            + steamEnergy(massInGrams, 100, finalTemperatureCelsius)
            + heatVaporizationEnergy(massInGrams);

        } else if (finalTemperatureCelsius > 0 
            && finalTemperatureCelsius < 100 
            && initialTemperatureCelsius >= 100
        ){
            System.out.println("STEAM CALCULATIONS: ONLY STEAM TO WATER (WATER_UNIQUE_3)\n");

            return  steamEnergy(massInGrams, initialTemperatureCelsius, 100)
            - heatVaporizationEnergy(massInGrams) 
            + liquidEnergy(massInGrams, 100, finalTemperatureCelsius);
        }

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius < 100 
            && finalTemperatureCelsius < 100
            && finalTemperatureCelsius <= 0
        ){
            System.out.println("WATER CALCULATIONS: ONLY WATER TO ICE (4)\n");
            
            return liquidEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius)
            - heatFusionEnergy(massInGrams);
        }

        System.out.println("WATER CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 100 & finalTemperatureCelsius > 100){
            System.out.println("STEAM CALCULATIONS: ONLY STEAM (1)\n");
            
            return steamEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius);

        } else if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius == 100){
            System.out.println("STEAM CALCULATIONS: STEAM CONDENCES TO WATER (2)\n");
            return steamEnergy(massInGrams, initialTemperatureCelsius, finalTemperatureCelsius) 
            - heatVaporizationEnergy(massInGrams);
        }

        System.out.println("STEAM CALCULATIONS: INAPPLICABLE");
        System.out.println("calculateEnergy(): INAPPLICABLE\ncalculateEnergy() is not equipped to handle the given inputs.\n");
        throw new RuntimeException("calculateEnergy() is not equipped to handle the given inputs!");
    }
}