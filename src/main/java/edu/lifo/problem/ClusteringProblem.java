package edu.lifo.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.collect.Lists;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.initial.partitions.ClusterAndSamples;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.ClusteringSolution;
import edu.lifo.solution.Sample;

public class ClusteringProblem implements Problem<ClusteringSolution>  {

	private static final long serialVersionUID = 1L;

	private int kMin;
	private int kMax;
	private int numberOfObjectives;
	private Map<String, List<Double>> dataset;
	private List<Map<String, List<String>>> initialPopulation;
	private int initialPopulationIndex;

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
	public void evaluate(ClusteringSolution solution) {
		
		
		solution.getNumberOfVariables();
		
	}
	
	public double calculateConnectivity(ClusteringSolution solution) {
		int numberOfVariables = solution.getNumberOfVariables();
		List<Cluster> clusters = Lists.newArrayList();
		for (int i=0; i<numberOfVariables; i++) {
			clusters.add(solution.getVariableValue(i));
		}
		
		
		return 0;
	}

	@Override
	public ClusteringSolution createSolution() {
		
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
		
		ClusteringSolution clusteringSolution = new ClusteringSolution(clusters);
		initialPopulationIndex++;
		return clusteringSolution;
		
	}

}
