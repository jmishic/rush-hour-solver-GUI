package puzzles.common.solver;

import java.util.Collection;

/**
 * The representation of a single configuration for a puzzle.
 * The BFS common solver uses all these methods to solve a puzzle. Therefore,
 * all the puzzles must implement this interface.
 */
public interface Configuration {
    /**
     * Is the current configuration a solution?
     * @return true if the configuration is a puzzle's solution; false, otherwise
     */
    boolean isSolution();

    /**
     * Get the collection of neighbors from the current configuration.
     * @return All the neighbors
     */
    Collection<Configuration> getNeighbors();

    /**
     * returns true if two objects are equal and false otherwise
     * @param other object to compare to
     * @return true or false
     */
    boolean equals(Object other); //function to be overwritten
    int hashCode();               //function to be overwritten
    String toString();            //function to be overwritten
}