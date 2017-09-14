package edu.lifo.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public class PartitionSolution implements Solution<Cluster> {

	private static final long serialVersionUID = 1L;
	
	private double[] objectives;
	private List<Cluster> clusters;
	
	public PartitionSolution(List<Cluster> clusters) {
		this.clusters = new ArrayList<>(clusters);
	}
	
	
	public PartitionSolution(double[] objectives, List<Cluster> clusters) {
		this.clusters = new ArrayList<>(clusters);
		this.objectives = Arrays.copyOf(objectives, objectives.length);
	}
	
	
    // return the label of the cluster of the pattern "pattern"
    public int clusterOf(int pattern) {

        for (Cluster it : clusters) {
            List<Sample> samples = it.getSamples();
            for (Sample sample : samples) {
                if (sample.getPatternId() == pattern) {
                    return it.getClusterId();
                }
            }
	    }    
	    return -1;
	}
	/**
	 * Copy Constructor
	 * @param copy
	 */
	public PartitionSolution(PartitionSolution copy) {
		this.objectives = Arrays.copyOf(copy.objectives, copy.objectives.length);
		this.clusters = new ArrayList<>(copy.clusters);
	}
	
	
	@Override
	public void setObjective(int index, double value) {
		this.objectives[index] = value;
	}

	@Override
	public double getObjective(int index) {
		double objective = objectives[index];
		return objective;
	}

	@Override
	public Cluster getVariableValue(int index) {
		return clusters.get(index);
	}

	@Override
	public void setVariableValue(int index, Cluster value) {
		clusters.set(index, value);
	}

	@Override
	public String getVariableValueString(int index) {
		return clusters.get(index).toString();
	}

	@Override
	public int getNumberOfVariables() {
		return clusters.size();
	}

	@Override
	public int getNumberOfObjectives() {
		return this.objectives.length;
	}

	@Override
	public Solution<Cluster> copy() {
		return new PartitionSolution(this);
	}

	@Override
	public void setAttribute(Object id, Object value) {
		throw new RuntimeException("Not implemented");
		
	}

	@Override
	public Object getAttribute(Object id) {
		throw new RuntimeException("Not implemented");
	}

}
