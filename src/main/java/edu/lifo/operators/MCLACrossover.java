package edu.lifo.operators;


import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;

import edu.lifo.solution.PartitionSolution;

public class MCLACrossover implements CrossoverOperator<PartitionSolution> {

    private static final long serialVersionUID = 1L;

	@Override
	public List<PartitionSolution> execute(List<PartitionSolution> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

  

}
