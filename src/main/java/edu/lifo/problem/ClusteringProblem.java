package edu.lifo.problem;

import com.google.common.collect.Lists;

import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;
import edu.lifo.solution.Sample;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.uma.jmetal.problem.Problem;

public class ClusteringProblem implements Problem<PartitionSolution> {

	private static final long serialVersionUID = 1L;

	private int kMin;
	private int kMax;
	private int numberOfObjectives;
	private Map<String, List<Double>> dataset;
	private List<Map<String, List<String>>> initialPopulation;
	private int initialPopulationIndex;

    EuclideanDistance distance = new EuclideanDistance();

	public ClusteringProblem(int kMin, int kMax, int numberOfObjectives, List<Map<String, List<String>>> initialPopulation, Map<String, List<Double>> dataset) {
		this.kMin = kMin;
		this.kMax = kMax;
		this.numberOfObjectives = numberOfObjectives;
		this.dataset = dataset;
		this.initialPopulation = initialPopulation;
		initialPopulationIndex = 0;
		
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
		int numberOfVariables = solution.getNumberOfVariables();
		List<Cluster> clusters = Lists.newArrayList();
		for (int i=0; i<numberOfVariables; i++) {
			clusters.add(solution.getVariableValue(i));
		}
		
		
		return 0;
	}



	@Override
    public PartitionSolution createSolution() {
		
		//TODO: Checar se preciso ter esses objetos cluster ou poderia apenas usar map 
		Map<String, List<String>> clustersAndSamples = initialPopulation.get(initialPopulationIndex);
		List<Cluster> clusters = Lists.newArrayList();
		for (String clu: clustersAndSamples.keySet()) {
			
			List<String> samples = clustersAndSamples.get(clu);
			List<Sample> amostras = Lists.newArrayList();
			for (String sampleId: samples) {
				List<Double> coordinates = dataset.get(sampleId);
				Sample sample = new Sample(coordinates, sampleId);
				amostras.add(sample);
			}
			Cluster cluster = new Cluster();
			cluster.setClusterId(clu);
			cluster.setSamples(amostras);
			clusters.add(cluster);
		}
		
        PartitionSolution partitionSolution = new PartitionSolution(clusters);
		initialPopulationIndex++;
        return partitionSolution;
		
	}

}
