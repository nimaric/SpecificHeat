package src;

public class Materials {
    // Specific Heats (J / g * °C) 
    double cSolid;
    double cLiquid;
    double cGas;

    // Phase Transition Temperatures (°C)
    double fusionTemp;
    double vaporizationTemp;

    // Latent Heats (J / g)
    double latentFusion;
    double latentVaporization;

    public Materials(
        double cSolid, 
        double cLiquid, 
        double cGas, 
        double fusionTemp,
        double vaporizationTemp,
        double latentFusion, 
        double latentVaporization
    ) {
        this.cSolid = cSolid;
        this.cLiquid = cLiquid;
        this.cGas = cGas;
        this.fusionTemp = fusionTemp;
        this.vaporizationTemp = vaporizationTemp;
        this.latentFusion = latentFusion;
        this.latentVaporization = latentVaporization;
    }

    public static final Materials WATER = new Materials(
        2.09, // cSolid (ice)
        4.184,// cLiquid (water)
        2.01, // cGas (steam)
        0.0,  // fusionTemp
        100.0,// vaporizationTemp
        334.0,// latentFusion
        2260.0// latentVaporization
    );

    public static final Materials ETHANOL = new Materials(
        2.44,  // cSolid (Specific heat of solid ethanol, J/g°C)
        2.46,  // cLiquid (Specific heat of liquid ethanol, J/g°C)
        1.43,  // cGas (Specific heat of gas ethanol, J/g°C)
        -114.1,// fusionTemp (°C)
        841.0, // latentHeatVaporization (J/g)
        108.8, // latentHeatFusion (J/g)
        78.4   // vaporizationTemp (°C)
    );
}
