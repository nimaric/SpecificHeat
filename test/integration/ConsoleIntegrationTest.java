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

package test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.ThermalCalculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        InputStream originalIn = System.in;

        try {
            // Simulating user input: mass=10.0, t_i=20.0, t_f=30.0 (liquid heating only)
            // Space or newline separated tokens mimic Scanner reading nextDouble()
            provideConsoleInput("Water\n10.0\n20.0\n30.0\n");

            ThermalCalculator.main(new String[]{});

            String output = getConsoleOutput();

            assertTrue(
                output.contains("Total Energy Required:"), 
                "Should output the result label"
            );
            assertTrue(
                output.contains("418.40"), 
                "Should contain the correctly formatted math result"
            );

        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    @DisplayName("Main method should process complex phase shifts spanning ice to steam")
    void mainMethodProcessesFullPhaseTransitionInputs() {
        InputStream originalIn = System.in;

        try {
            // Simulating user input crossing all thresholds: mass=50.0, t_i=-10.0, t_f=110.0
            provideConsoleInput("Water\n50.0\n-10.0\n110.0\n");

            ThermalCalculator.main(new String[]{});

            String output = getConsoleOutput();

            assertTrue(
                output.contains("Total Energy Required:"), 
                "Output must contain the final string block"
            );
        } finally {
            System.setIn(originalIn);
        }
    }
        
    @Test
    @DisplayName("Main method should throw IllegalArgumentException for negative mass inputs")
    void mainThrowsExceptionOnNegativeMass() {
        InputStream originalIn = System.in;

        try {
            // mass = -50.0, t_i = 20.0, t_f = 80.0
            provideConsoleInput("Water\n-50.0\n-20.0\n80.0\n");

            assertThrows(
                IllegalArgumentException.class,
                () -> ThermalCalculator.main(new String[]{})
            );
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    @DisplayName("Main method should throw IllegalArgumentException for initial temperatures below absolute zero")
    void mainThrowsExceptionOnTemperatureBelowAbsoluteZero() {
        InputStream originalIn = System.in;

        try {
            provideConsoleInput("Water\n100.0\n-300.0\n25.0\n");

            assertThrows(
                IllegalArgumentException.class,
                () -> ThermalCalculator.main(new String[]{})
            );
        } finally {
            System.setIn(originalIn);
        }
    }

}

