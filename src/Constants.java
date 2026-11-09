package src;

import java.util.logging.Level;

public class Constants {
    public static final double EPSILON = 1e-6;
    public static final int ITERATION_LIMIT = 30;

    /**
     * Set the verbosity for logging
     * 
     * FINEST: Level.FINEST
     * FINER: Level.FINER
     * FINE: Level.FINE
     * INFO: Level.INFO
     * WARNING: Level.WARNING // Default
     * SEVERE: Level.SEVERE
     */
    public static final Level LOGGER_LEVEL = Level.WARNING; // Change this Variable for testing (DEFAULT: Level.WARNING)

    public static double getEpsilon(){
        return EPSILON;
    }

    public static int getIterationLimit(){
        return ITERATION_LIMIT;
    }

    public static Level getLoggerLevel(){
        return LOGGER_LEVEL;
    }

}
