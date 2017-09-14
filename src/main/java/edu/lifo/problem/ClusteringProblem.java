package edu.lifo.problem;

import com.google.common.collect.Lists;

import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;
import edu.lifo.solution.Sample;

import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.uma.jmetal.problem.Problem;

public class ClusteringProblem implements Problem<PartitionSolution> {

	private static final long serialVersionUID = 1L;

    private Patterns patterns;
	private int kMin;
	private int kMax;
	private int numberOfObjectives;
    private double L;

    EuclideanDistance distance = new EuclideanDistance();

    public ClusteringProblem(int kMin, int kMax, int numberOfObjectives, String dataSetPath, String filePatternsPath,
        double L) {
		this.kMin = kMin;
		this.kMax = kMax;
		this.numberOfObjectives = numberOfObjectives;
		
        this.patterns = new Patterns(dataSetPath, filePatternsPath);
        this.L = L;

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
                List<Double> coordinates = sample.getCoordinates();
                var +=
                    distance.compute(centroid.stream().mapToDouble(Double::doubleValue).toArray(), coordinates.stream()
                        .mapToDouble(Double::doubleValue).toArray());
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
                        solution.clusterOf(Pe.patterns->nnList[(*it1).patternNumber][j])) {
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
		
        return null;
		
	}

}
