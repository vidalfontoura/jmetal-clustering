package edu.lifo.problem;


import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

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
		
		
		solution.setObjective(0, calculateVarIntraCluster);
		
		solution.setObjective(1, calculateConnectivity);

	}
	
    public double calculateVarIntraCluster(PartitionSolution solution) {

        int numberOfVariables = solution.getNumberOfVariables();

        double var = -1;
        for (int i = 0; i < numberOfVariables; i++) {
            Cluster cluster = solution.getVariableValue(i);
            List<Double> centroid = cluster.getCentroidCoordinates();

            List<Integer> patternNumbers = cluster.getListPatternNumber();
            for (int j = 0; j < patternNumbers.size(); j++) {
            	
                Integer patternNumber = patternNumbers.get(j);
                double[] coordinates = patterns.getCoordinatesByPatternNumber(patternNumber);
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
				List<Integer> patternNumbers = Lists.newArrayList();
				for (String patternLabel: patternLabels) {
					int patternNumberByPatternLabel = patterns.getPatternNumberByPatternLabel(patternLabel);
					patternNumbers.add(patternNumberByPatternLabel);
				}
				Cluster cluster = new Cluster(patternNumbers, clusterId, this.patterns);
				clusterList.add(cluster);
			}
			PartitionSolution partitionSolution = new PartitionSolution(clusterList, this.patterns);
	        return partitionSolution;
		}
		throw new RuntimeException("Iterator hasNext return false. Throwing error");
		
		
	}
	
	
    public List<PartitionSolution> removeDominadas(List<PartitionSolution> result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).getNumberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
//                    System.out.println("--------------------------------------------");
//                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
//                    System.out.println("[" + i + "] " + result.get(i).toString());
//                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
//                    System.out.println("--------------------------------------------");
//                    System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
//                    System.out.println("[" + i + "] " + result.get(i).toString());
//                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
//    public List<PartitionSolution> removeRepetidas(List<PartitionSolution> result) {
//        String solucao;
//
//        for (int i = 0; i < result.size() - 1; i++) {
//            solucao = result.get(i).get[0].toString();
//            for (int j = i + 1; j < result.size(); j++) {
//                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
////                    System.out.println("--------------------------------------------");
////                    System.out.println("Solucao [" + i + "] e igual [" + j + "]");
////                    System.out.println(result.get(i).getDecisionVariables()[0].toString());
////                    System.out.println(result.get(j).getDecisionVariables()[0].toString());
//
//                    result.remove(j);
//                }
//            }
//        }
//
//        return result;
//    }

}
