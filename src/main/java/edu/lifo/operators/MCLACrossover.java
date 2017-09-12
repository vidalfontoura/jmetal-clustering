package edu.lifo.operators;


import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;

import edu.lifo.solution.ClusteringSolution;

public class MCLACrossover implements CrossoverOperator<ClusteringSolution> {

    private static final long serialVersionUID = 1L;

	@Override
	public List<ClusteringSolution> execute(List<ClusteringSolution> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

  

}
