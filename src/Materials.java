/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package src;

/**
 * Represents the thermal properties of a material, including specific heats, 
 * phase transition temperatures, and latent heats.
 */
public class Materials {
    // Material's name
    String name;

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
        String name,
        double cSolid, 
        double cLiquid, 
        double cGas, 
        double fusionTemp,
        double vaporizationTemp,
        double latentFusion, 
        double latentVaporization
    ) {
        this.name = name;
        this.cSolid = cSolid;
        this.cLiquid = cLiquid;
        this.cGas = cGas;
        this.fusionTemp = fusionTemp;
        this.vaporizationTemp = vaporizationTemp;
        this.latentFusion = latentFusion;
        this.latentVaporization = latentVaporization;
    }

    public static final Materials WATER = new Materials(
        "Water",
        2.09, // cSolid (ice)
        4.184,// cLiquid (water)
        2.01, // cGas (steam)
        0.0,  // fusionTemp
        100.0,// vaporizationTemp
        334.0,// latentFusion
        2260.0// latentVaporization
    );

    public static final Materials ETHANOL = new Materials(
        "Ethanol",
        2.44,  // cSolid (Specific heat of solid ethanol, J/g°C)
        2.46,  // cLiquid (Specific heat of liquid ethanol, J/g°C)
        1.43,  // cGas (Specific heat of gas ethanol, J/g°C)
        -114.1,// fusionTemp (°C)
        841.0, // latentHeatVaporization (J/g)
        108.8, // latentHeatFusion (J/g)
        78.4   // vaporizationTemp (°C)
    );

    public static final Materials ALUMINUM = new Materials(
        "Aluminum",
        0.897,  // cSolid
        1.18,   // cLiquid (approx.)
        1.18,   // cGas (approx.)
        660.0,  // fusionTemp
        2516.0, // vaporizationTemp
        397.0,  // latentFusion
        10500.0 // latentVaporization
    );

    public static final Materials COPPER = new Materials(
        "Copper",
        0.385,  // cSolid
        0.50,   // cLiquid (approx.)
        0.50,   // cGas (approx.)
        1083.0, // fusionTemp
        2562.0, // vaporizationTemp
        134.0,  // latentFusion
        5069.0  // latentVaporization
    );

    public static final Materials IRON = new Materials(
        "Iron",
        0.449,  // cSolid
        0.82,   // cLiquid (approx.)
        0.82,   // cGas (approx.)
        1538.0, // fusionTemp
        2862.0, // vaporizationTemp
        247.0,  // latentFusion
        6340.0  // latentVaporization
    );

    public static final Materials LEAD = new Materials(
        "Lead",
        0.129,  // cSolid
        0.16,   // cLiquid
        0.16,   // cGas (approx.)
        327.5,  // fusionTemp
        1749.0, // vaporizationTemp
        23.0,   // latentFusion
        871.0   // latentVaporization
    );

    public static final Materials GOLD = new Materials(
        "Gold",
        0.129,  // cSolid
        0.15,   // cLiquid (approx.)
        0.15,   // cGas (approx.)
        1064.0, // fusionTemp
        2856.0, // vaporizationTemp
        64.5,   // latentFusion
        1578.0  // latentVaporization
    );

    public static final Materials SULFUR = new Materials(
        "Sulfur",
        0.71,   // cSolid (approx.)
        0.85,   // cLiquid (approx.)
        0.85,   // cGas (approx.)
        119.0,  // fusionTemp
        444.6,  // vaporizationTemp
        38.1,   // latentFusion
        326.0   // latentVaporization
    );
}
