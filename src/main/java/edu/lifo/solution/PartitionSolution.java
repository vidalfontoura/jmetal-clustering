package edu.lifo.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;

import edu.lifo.migrated.Patterns;
import edu.lifo.problem.PartitionProblem;

public class PartitionSolution implements Solution<Cluster> {

	private static final long serialVersionUID = 1L;
	
	private double[] objectives;
	private List<Cluster> clusters;
	
	protected Map<Object, Object> attributes ;
	protected PartitionProblem problem;
	private Patterns patterns;
	
	public PartitionSolution(List<Cluster> clusters, PartitionProblem problem,  Patterns patterns) {
		this.attributes = new HashMap<>() ;
		this.clusters = new ArrayList<>(clusters);
		this.objectives = new double[problem.getNumberOfObjectives()] ;
		this.problem = problem;
		this.patterns =  patterns;
	}
	
	public Patterns getPatterns() {
		return this.patterns;
	}
	
	public int getNumberOfClusters() {
		return clusters.size();
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
	
	
	public void printPartition() {
		for (Cluster cluster: clusters) {
			System.out.print("Cluster "+cluster.getClusterId()+":");
			
			Collections.sort(cluster.getSamples(), new Comparator<Sample>() {

				@Override
				public int compare(Sample o1, Sample o2) {
					if (o1.getPatternId() > o2.getPatternId()) {
						return 1;
					}
					return -1;
				}
			
			});
			System.out.print(" ");
			for (Sample sample: cluster.getSamples()) {
				System.out.print(sample.getPatternId()+ "  ");
			}
			System.out.println();
			
		}
	}
	
	public List<Cluster> getClusters() {
		return clusters;
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
		this.attributes.put(id, value) ;
		
	}

	@Override
	public Object getAttribute(Object id) {
		return attributes.get(id) ;
	}

}
