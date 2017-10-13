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

public class PartitionSolution implements Solution<Cluster> {

	private static final long serialVersionUID = 1L;
	
	private double[] objectives;
	private List<Cluster> clusters;
	
	protected Map<Object, Object> attributes;
	private Patterns patterns;
	
	public PartitionSolution(List<Cluster> clusters,  Patterns patterns) {
		this.attributes = new HashMap<>() ;
		this.clusters = new ArrayList<>(clusters);
		this.objectives = new double[2];
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
            
            for (Integer patternNumber : it.getListPatternNumber()) {
                if (patternNumber == pattern) {
                    return it.getClusterId();
                }
            }
	    }    
	    return -1;
	}

    
    public PartitionSolution(PartitionSolution copy) {
		this.objectives = Arrays.copyOf(copy.objectives, copy.objectives.length);
		this.clusters = new ArrayList<>(copy.clusters);
	}
	
	public void printPartition() {
		for (Cluster cluster: clusters) {
			System.out.print("Cluster "+cluster.getClusterId()+":");
			
			Collections.sort(cluster.getListPatternNumber(), new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					if (o1 > o2) {
						return 1;
					}
					return -1;
				}
			
			});
			System.out.print(" ");
			for (Integer patternNumber: cluster.getListPatternNumber()) {
				System.out.print(patternNumber+ "  ");
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
		return clusters.get(index).toString() +"\n";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clusters == null) ? 0 : clusters.hashCode());
		result = prime * result + Arrays.hashCode(objectives);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartitionSolution other = (PartitionSolution) obj;
		if (clusters == null) {
			if (other.clusters != null)
				return false;
		} else if (!clusters.equals(other.clusters))
			return false;
		if (!Arrays.equals(objectives, other.objectives))
			return false;
		return true;
	}
	
	

}
