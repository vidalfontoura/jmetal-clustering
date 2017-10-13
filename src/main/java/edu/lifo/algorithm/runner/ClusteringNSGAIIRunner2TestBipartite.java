package edu.lifo.algorithm.runner;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.jmetal.util.PrintFinalSolutionSetClustering;
import edu.lifo.migrated.Patterns;
import edu.lifo.mo.nsgaii.ClusteringNSGAII;
import edu.lifo.operators.HBGFCrossover;
import edu.lifo.operators.MCLACrossover;
import edu.lifo.problem.PartitionProblem;
import edu.lifo.solution.PartitionSolution;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

	/**
	 * Class to configure and run the implementation of the ClusteringNSGAII algorithm {@link ClusteringNSGAII}
	 *
	 * @author vfontoura
	 */
public class ClusteringNSGAIIRunner2TestBipartite extends AbstractAlgorithmRunner {
	  /**
	   * @param args Command line arguments.
	   * @throws JMetalException
	   * @throws FileNotFoundException
	   * Invoking command:
	  java org.uma.jmetal.runner.multiobjective.NSGAII45Runner problemName [referenceFront]
	   */
	  public static void main(String[] args) throws JMetalException, FileNotFoundException {
		  
		  
	    Problem<PartitionSolution> problem;
	    Algorithm<List<PartitionSolution>> algorithm;
	    CrossoverOperator<PartitionSolution> crossover;
	    SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
	    String referenceParetoFront = "" ;
	  
	    int minK = 6;
	    int maxK = 6;
	    int numberOfObjectives = 2;
         String datasetPath = "/home/lifo/Downloads/iris/iris-dataset.txt";
         String filePatternsPath =
         "/home/lifo/Downloads/iris/true partition/iris-truePartition.txt";
         String initialPartitionPath = "/home/lifo/Downloads/iris/partitions";
//	    
//		String datasetPath = "/home/lifo/Downloads/MOCLE/MOCLE-v3-Debug/iris-testes/iris-dataset.txt";
//		String filePatternsPath = "/home/lifo/Downloads/MOCLE/MOCLE-v3-Debug/iris-testes/true partition/iris-truePartition.txt";
//		String initialPartitionPath = "/home/lifo/Downloads/MOCLE/MOCLE-v3-Debug/iris-testes/partitions";
	    
//        String datasetPath = "/Users/vfontoura/MOCLE/iris-test/iris-dataset.txt";
//        String filePatternsPath = "/Users/vfontoura/MOCLE/iris-test/true partition/iris-truePartition.txt";
//        String initialPartitionPath = "/Users/vfontoura/MOCLE/iris-test/partitions";

	    double L = 5;
	    
	    List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
	    
	    int populationSize = readInitialPartitions.size();
	    int maxEvaluations = 10000;
	    
	    Patterns patterns = new Patterns(datasetPath, filePatternsPath);
	    
	    problem = new PartitionProblem(minK, maxK, numberOfObjectives, L, readInitialPartitions, patterns);

	    double crossoverProbability = 0.9 ;
        crossover = new HBGFCrossover(minK, maxK);

	    selection = new BinaryTournamentSelection<PartitionSolution>(new RankingAndCrowdingDistanceComparator<PartitionSolution>());

	    algorithm = new ClusteringNSGAII<PartitionSolution>(problem, maxEvaluations, populationSize, crossover,
	            selection, new SequentialSolutionListEvaluator<PartitionSolution>());
	    
	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
	            .execute() ;

	    List<PartitionSolution> population = algorithm.getResult() ;
	    
	    long computingTime = algorithmRunner.getComputingTime() ;

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

	    PrintFinalSolutionSetClustering.printFinalSolutionSet(population);
	    if (!referenceParetoFront.equals("")) {
	      printQualityIndicators(population, referenceParetoFront) ;
	    }
	  }
	  
}