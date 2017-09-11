package edu.lifo.problem;

import com.google.common.collect.Lists;

import edu.lifo.solution.Cluster;
import edu.lifo.solution.DefaultPartitionSolution;
import edu.lifo.solution.VariableLengthSolution;

import java.util.List;

public class ClusteringProblem implements VariableLengthProblem<DefaultPartitionSolution> {

    private static final long serialVersionUID = 1L;



    @Override
    public int getNumberOfVariables() {

        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int getNumberOfObjectives() {
        return 2;
    }


    @Override
    public int getNumberOfConstraints() {
        return 0;
    }


    @Override
    public String getName() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void evaluate(VariableLengthSolution<DefaultPartitionSolution> solution) {

        int numberOfVariables = solution.getNumberOfVariables();
        List<Cluster> clustersFromSolution = Lists.newArrayList();
        for (int i=0; i<numberOfVariables; i++) {
            DefaultPartitionSolution variableValue = solution.getVariableValue(i);
        }

    }


    @Override
    public VariableLengthSolution<DefaultPartitionSolution> createSolution() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxLength() {

        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int getMinLength() {

        // TODO Auto-generated method stub
        return 0;
    }

}
