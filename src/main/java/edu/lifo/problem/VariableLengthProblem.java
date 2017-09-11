package edu.lifo.problem;

import edu.lifo.solution.VariableLengthSolution;

import org.uma.jmetal.problem.Problem;

/**
 * @author Giovani Guizzo
 * @param <T>
 */
public interface VariableLengthProblem<T> extends Problem<VariableLengthSolution<T>> {

    /**
     *
     * @return
     */
    int getMaxLength();

    /**
     *
     * @return
     */
    int getMinLength();
}
