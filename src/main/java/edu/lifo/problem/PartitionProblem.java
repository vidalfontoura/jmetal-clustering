package edu.lifo.problem;


import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;
import edu.lifo.solution.Sample;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.uma.jmetal.problem.Problem;

import com.google.common.collect.Lists;

public class PartitionProblem implements Problem<PartitionSolution> {

	private static final long serialVersionUID = 1L;

    private Patterns patterns;
	private int kMin;
	private int kMax;
	private int numberOfObjectives;
    private double L;

    EuclideanDistance distance = new EuclideanDistance();
    
    
    private Iterator<Map<Integer, List<String>>> iterator;

    public PartitionProblem(int kMin, int kMax, int numberOfObjectives, double L,
    		List<Map<Integer, List<String>>> initialPopulation, Patterns patterns) {
		this.kMin = kMin;
		this.kMax = kMax;
		this.numberOfObjectives = numberOfObjectives;
		
		this.patterns = patterns;
        this.L = L;
        this.iterator = initialPopulation.iterator();

	}

	@Override
	public int getNumberOfVariables() {
		return kMin;
	}

	@Override
	public int getNumberOfObjectives() {
		return this.numberOfObjectives;
	}

	@Override
	public int getNumberOfConstraints() {
		return 0;
	}

	@Override
	public String getName() {
		return "ClusteringProblem";
	}

	@Override
    public void evaluate(PartitionSolution solution) {
		
//		solution.printPartition();
		
		double calculateVarIntraCluster = calculateVarIntraCluster(solution);
		
		double calculateConnectivity = calculateConnectivity(solution);
		
		
		System.out.println("VAR:"+calculateVarIntraCluster);
		System.out.println("CON: "+calculateConnectivity);
		
		

	}
	
    public double calculateVarIntraCluster(PartitionSolution solution) {

        int numberOfVariables = solution.getNumberOfVariables();

        double var = -1;

        for (int i = 0; i < numberOfVariables; i++) {
            Cluster cluster = solution.getVariableValue(i);
            List<Double> centroid = cluster.getCentroidCoordinates();

            List<Sample> samples = cluster.getSamples();
            for (int j = 0; j < samples.size(); j++) {
                Sample sample = samples.get(j);
                double[] coordinates = sample.getCoordinates();
                var +=
                    distance.compute(centroid.stream().mapToDouble(Double::doubleValue).toArray(), coordinates);
            }
        }
        return var;
    }

    public double calculateConnectivity(PartitionSolution solution) {
		
            double conn = 0.0;
            int nPat = patterns.getPatternsDescription().size();
            double nn = Math.ceil(nPat * L/100); // number of nearest neighbors is L% of the size of the dataset
            int nNearestNeighbors = (int) nn;
            
//            for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
//                double somaNN = 0.0;
//                
//                System.out.println("it1.patternNumber: " + patternDescription.getPatternNumber());
//                System.out.println("Pe.clusterOf(*it1).patternNumber: " 
//                		+ solution.clusterOf(patternDescription.getPatternNumber()));
//
//
//                
//                for (int j = 0; j < nNearestNeighbors; j++){
//                    
//                	System.out.println("nnList[(*it1).patternNumber]["+j+"] = "+patterns.getNnList().get(patternDescription.getPatternNumber()).get(j));
//	                      
//                	System.out.println("Pe.clusterOf(Pe.patterns->nnList[(*it1).patternNumber]["+j+"] = " + solution.clusterOf(
//                    		patterns.getNnList().get(patternDescription.getPatternNumber()).get(j)));
//                    if (solution.clusterOf(patternDescription.getPatternNumber()) !=
//                        solution.clusterOf(
//                        		patterns.getNnList().get(patternDescription.getPatternNumber()).get(j))) {
//                     
//                    	System.out.println("Entrei no fi");
//                    }
//                }    
//            }    
       
            for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
                double somaNN = 0.0;
                for (int j = 0; j < nNearestNeighbors; j++){
                    
                    
                    if (solution.clusterOf(patternDescription.getPatternNumber()) !=
                        solution.clusterOf(
                        		patterns.getNnList().get(patternDescription.getPatternNumber()).get(j))) {
                        double jj = (j + 1);
                        somaNN += 1.0 / jj; // j + 1 porque j � 0 para o 1o vizinho... 
                    }
                }    
                conn += somaNN;
            }    
           return conn; 
	}



	@Override
    public PartitionSolution createSolution() {
		
		if (iterator.hasNext()) {
			Map<Integer, List<String>> next = iterator.next();
			List<Cluster> clusterList = Lists.newArrayList();
			for (Integer clusterId:  next.keySet()) {
				
				
				List<String> patternLabels = next.get(clusterId);
				List<Sample> samples = Lists.newArrayList();
				for (String patternLabel: patternLabels) {
					double[] coordinatesByPatternLabel = patterns.getCoordinatesByPatternLabel(patternLabel);
					int patternNumberByPatternLabel = patterns.getPatternNumberByPatternLabel(patternLabel);
					Sample sample = new Sample(coordinatesByPatternLabel, patternLabel, patternNumberByPatternLabel);
					samples.add(sample);
				}
				
				Cluster cluster = new Cluster(samples, clusterId);
				clusterList.add(cluster);
			}
			PartitionSolution partitionSolution = new PartitionSolution(clusterList, this, this.patterns);
	        return partitionSolution;
		}
		throw new RuntimeException("Iterator hasNext return false. Throwing error");
		
		
	}

}
