package src;

public class Main{
    public static double calculateEnergy(
        double massInGrams,
        double initialTemperatureCelsius,
        double finalTemperatureCelsius
    ){
        System.out.println("calculateEnergy() initialized!");
        System.out.println("MASS: " + massInGrams + "g");
        System.out.println("T_i: " + initialTemperatureCelsius);
        System.out.println("T_f: " + finalTemperatureCelsius);

        heatFusionWater heatFusionWaterObj;
        heatVaporizationWater heatVaporizationObj;

        if (massInGrams < 0) {
            System.out.println("SYSTEM VALIDATION: FAILED (1) Negative Mass");
            throw new IllegalArgumentException("Mass cannot be negative!");
        }
        // Check if the temperature goes below Absolute Zero
        if (-273.15 > initialTemperatureCelsius || -273.15 > finalTemperatureCelsius){
            System.out.println("SYSTEM VALIDATION: FAILED (2) Absolute Zero");
            throw new IllegalArgumentException("Celsius cannot go below Absolute Zero (-273.15)!");
        }

        System.out.println("SYSTEM VALIDATION: PASSED");

        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius < 0){
            ice iceObject = new ice(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            ); // Q_ice
            System.out.println("ICE CALCULATIONS: ONLY ICE (1)\n");

            return iceObject.totalEnergy();
        }

        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius >= 0){
            ice iceObject = new ice(massInGrams, 
                initialTemperatureCelsius, 
                0
            ); // Q_ice
            heatFusionWaterObj = new heatFusionWater(massInGrams); // Q_hF
            waterLiquid liquidObject = new waterLiquid(
                massInGrams, 
                0, 
                finalTemperatureCelsius
            ); // Q_water
            System.out.println("ICE CALCULATIONS: ONLY ICE TO WATER (2)\n");

            return 
            iceObject.totalEnergy()
            + liquidObject.totalEnergy()
            + heatFusionWaterObj.totalEnergy();
            
        } else if (initialTemperatureCelsius >= 0 && finalTemperatureCelsius < 0){
            waterLiquid liquidObject = new waterLiquid(
                massInGrams, 
                initialTemperatureCelsius,
                0
            ); // Q_water
            heatFusionWaterObj = new heatFusionWater(massInGrams); // Q_hF
            ice iceObject = new ice(
                massInGrams,
                0,
                finalTemperatureCelsius
            ); // Q_ice
            System.out.println("ICE CALCULATIONS: ONLY WATER TO ICE (3)\n");

            return 
            liquidObject.totalEnergy()
            + -heatFusionWaterObj.totalEnergy()
            + iceObject.totalEnergy();
        }

        System.out.println("ICE CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius < 100 
            && finalTemperatureCelsius < 100
        ){
            waterLiquid liquidObject = new waterLiquid(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY WATER (1)\n");
            
            return liquidObject.totalEnergy();
        }

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius <= 100 
            && finalTemperatureCelsius >= 100
        ){
            waterLiquid liquidObject = new waterLiquid(
                massInGrams, 
                initialTemperatureCelsius, 
                100
            );
            heatVaporizationObj = new heatVaporizationWater(massInGrams);
            steam steamObject = new steam(
                massInGrams, 
                100, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY WATER TO STEAM (2)\n");

            return liquidObject.totalEnergy() + steamObject.totalEnergy() + heatVaporizationObj.totalEnergy();

        } else if (finalTemperatureCelsius > 0 
            && finalTemperatureCelsius < 100 
            && initialTemperatureCelsius >= 100
        ){
            steam steamObject = new steam(
                massInGrams, 
                initialTemperatureCelsius, 
                100
            );
            heatVaporizationObj = new heatVaporizationWater(massInGrams);
            waterLiquid liquidObject = new waterLiquid(
                massInGrams, 
                100, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY STEAM TO WATER (3)\n");

            return  steamObject.totalEnergy() + -heatVaporizationObj.totalEnergy() + liquidObject.totalEnergy();
        }

        System.out.println("WATER CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 100 & finalTemperatureCelsius > 100){
            steam steamObject = new steam(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY STEAM (1)\n");
            
            return steamObject.totalEnergy();

        } else if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius == 100){
            steam steamObject = new steam(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            );
            heatVaporizationObj = new heatVaporizationWater(massInGrams);
            System.out.println("STEAM CALCULATIONS: STEAM CONDENCES TO WATER (2)\n");
            return steamObject.totalEnergy() + -heatVaporizationObj.totalEnergy();
        }

        System.out.println("STEAM CALCULATIONS: INAPPLICABLE");
        System.out.println("calculateEnergy(): INAPPLICABLE\ncalculateEnergy() is not equipped to handle the given inputs!\n");
        throw new RuntimeException("calculateEnergy() is not equipped to handle the given inputs!");
    }
}