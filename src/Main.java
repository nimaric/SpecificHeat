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

        HeatFusionWater HeatFusionWaterObj;
        HeatVaporizationWater HeatVaporizationObj;

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
            Ice iceObject = new Ice(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            ); // Q_ice
            System.out.println("ICE CALCULATIONS: ONLY ICE (1)\n");

            return iceObject.totalEnergy();
        }

        if (initialTemperatureCelsius < 0 && finalTemperatureCelsius >= 0){
            Ice iceObject = new Ice(massInGrams, 
                initialTemperatureCelsius, 
                0
            ); // Q_ice
            HeatFusionWaterObj = new HeatFusionWater(massInGrams); // Q_hF
            WaterLiquid liquidObject = new WaterLiquid(
                massInGrams, 
                0, 
                finalTemperatureCelsius
            ); // Q_water
            System.out.println("ICE CALCULATIONS: ONLY ICE TO WATER (2)\n");

            return 
            iceObject.totalEnergy()
            + liquidObject.totalEnergy()
            + HeatFusionWaterObj.totalEnergy();
            
        } else if (initialTemperatureCelsius >= 0 && finalTemperatureCelsius < 0){
            WaterLiquid liquidObject = new WaterLiquid(
                massInGrams, 
                initialTemperatureCelsius,
                0
            ); // Q_water
            HeatFusionWaterObj = new HeatFusionWater(massInGrams); // Q_hF
            Ice iceObject = new Ice(
                massInGrams,
                0,
                finalTemperatureCelsius
            ); // Q_ice
            System.out.println("ICE CALCULATIONS: ONLY WATER TO ICE (3)\n");

            return 
            liquidObject.totalEnergy()
            + -HeatFusionWaterObj.totalEnergy()
            + iceObject.totalEnergy();
        }

        System.out.println("ICE CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 0 
            && initialTemperatureCelsius < 100 
            && finalTemperatureCelsius < 100
        ){
            WaterLiquid liquidObject = new WaterLiquid(
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
            WaterLiquid liquidObject = new WaterLiquid(
                massInGrams, 
                initialTemperatureCelsius, 
                100
            );
            HeatVaporizationObj = new HeatVaporizationWater(massInGrams);
            Steam steamObject = new Steam(
                massInGrams, 
                100, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY WATER TO STEAM (2)\n");

            return liquidObject.totalEnergy() + steamObject.totalEnergy() + HeatVaporizationObj.totalEnergy();

        } else if (finalTemperatureCelsius > 0 
            && finalTemperatureCelsius < 100 
            && initialTemperatureCelsius >= 100
        ){
            Steam steamObject = new Steam(
                massInGrams, 
                initialTemperatureCelsius, 
                100
            );
            HeatVaporizationObj = new HeatVaporizationWater(massInGrams);
            WaterLiquid liquidObject = new WaterLiquid(
                massInGrams, 
                100, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY STEAM TO WATER (3)\n");

            return  steamObject.totalEnergy() + -HeatVaporizationObj.totalEnergy() + liquidObject.totalEnergy();
        }

        System.out.println("WATER CALCULATIONS: INAPPLICABLE");

        if (initialTemperatureCelsius > 100 & finalTemperatureCelsius > 100){
            Steam steamObject = new Steam(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            );
            System.out.println("STEAM CALCULATIONS: ONLY STEAM (1)\n");
            
            return steamObject.totalEnergy();

        } else if (initialTemperatureCelsius >= 100 && finalTemperatureCelsius == 100){
            Steam steamObject = new Steam(
                massInGrams, 
                initialTemperatureCelsius, 
                finalTemperatureCelsius
            );
            HeatVaporizationObj = new HeatVaporizationWater(massInGrams);
            System.out.println("STEAM CALCULATIONS: STEAM CONDENCES TO WATER (2)\n");
            return steamObject.totalEnergy() + -HeatVaporizationObj.totalEnergy();
        }

        System.out.println("STEAM CALCULATIONS: INAPPLICABLE");
        System.out.println("calculateEnergy(): INAPPLICABLE\ncalculateEnergy() is not equipped to handle the given inputs!\n");
        throw new RuntimeException("calculateEnergy() is not equipped to handle the given inputs!");
    }
}