package edu.lifo.solution;

import edu.lifo.problem.VariableLengthProblem;

import java.util.List;

public class DefaultPartitionSolution extends VariableLengthSolution<Cluster> {

    public DefaultPartitionSolution(VariableLengthProblem<Cluster> problem) {
        super(problem);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public VariableLengthSolution<Cluster> copy() {

        List<Cluster> variablesCopy = this.getVariablesCopy();

        return null;
    }


}
