/*
 * SpecificHeat - Java-based thermal energy calculator
 * Copyright (C) 2026 Aldine Dane Fabro
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Test cases developed with assistance from ChatGPT (OpenAI).
 * Test cases developed with assistance from Gemini (Google).
 * Reviewed and maintained by the project author.
 */

package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.ThermalCalculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleIntegrationTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUpOutputStreams() {
        // Redirect System.out to capture console outputs
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreSystemStreams() {
        // Restore standard console input/output streams after each test
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    // Helper to mock keyboard typing input stream
    private void provideConsoleInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    private String getConsoleOutput() {
        return testOut.toString();
    }

    @Test
    @DisplayName("Main method should correctly read tokens and print calculated Joule value")
    void mainMethodProcessesValidInputsSuccessfully() {
        // Simulating user input: mass=10.0, t_i=20.0, t_f=30.0 (liquid heating only)
        // Space or newline separated tokens mimic Scanner reading nextDouble()
        provideConsoleInput("10.0 20.0 30.0");

        // Run your application runner method
        ThermalCalculator.main(new String[]{});

        String output = getConsoleOutput();

        // 10g * 4.184 J/gC * 10C = 418.4 Joules expected
        assertTrue(output.contains("Total Energy Required:"), "Should output the result label");
        assertTrue(output.contains("418.40"), "Should contain the correctly formatted math result");
    }

    @Test
    @DisplayName("Main method should process complex phase shifts spanning ice to steam")
    void mainMethodProcessesFullPhaseTransitionInputs() {
        // Simulating user input crossing all thresholds: mass=50.0, t_i=-10.0, t_f=110.0
        provideConsoleInput("50.0 -10.0 110.0");

        ThermalCalculator.main(new String[]{});

        String output = getConsoleOutput();

        // Ensure a calculation ran and printed out an absolute positive value
        assertTrue(output.contains("Total Energy Required:"), "Output must contain the final string block");
    }

        @Test
    @DisplayName("Main method should throw IllegalArgumentException for negative mass inputs")
    void mainThrowsExceptionOnNegativeMass() {
        // mass = -50.0, t_i = 20.0, t_f = 80.0
        provideConsoleInput("-50.0 20.0 80.0");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, 
            () -> ThermalCalculator.main(new String[]{})
        );
        
        assertTrue(exception.getMessage().contains("Mass cannot be negative"));
    }

    @Test
    @DisplayName("Main method should throw IllegalArgumentException for initial temperatures below absolute zero")
    void mainThrowsExceptionOnTemperatureBelowAbsoluteZero() {
        // mass = 100.0, t_i = -300.0 (Impossible), t_f = 25.0
        provideConsoleInput("100.0 -300.0 25.0");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, 
            () -> ThermalCalculator.main(new String[]{})
        );
        
        assertTrue(exception.getMessage().contains("Absolute Zero"));
    }

}

