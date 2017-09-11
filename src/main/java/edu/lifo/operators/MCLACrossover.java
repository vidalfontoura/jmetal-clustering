package edu.lifo.operators;

import edu.lifo.solution.DefaultPartitionSolution;

import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;

public class MCLACrossover implements CrossoverOperator<DefaultPartitionSolution> {

    private static final long serialVersionUID = 1L;

    @Override
    public List<DefaultPartitionSolution> execute(List<DefaultPartitionSolution> source) {

        // Assuming only two parents
        DefaultPartitionSolution partitionSolution1 = source.get(0);
        DefaultPartitionSolution partitionSolution2 = source.get(1);

        return null;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    public void
        applyMetaClusteringAlgorithm(DefaultPartitionSolution partitionSolution1, DefaultPartitionSolution partitionSolution2) {


    }

}
