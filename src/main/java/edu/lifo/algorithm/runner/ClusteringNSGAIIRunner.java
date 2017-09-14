package edu.lifo.algorithm.runner;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII45;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.mo.nsgaii.ClusteringNSGAII;
import edu.lifo.operators.MCLACrossover;
import edu.lifo.problem.PartitionProblem;
import edu.lifo.solution.PartitionSolution;

	/**
	 * Class to configure and run the implementation of the NSGA-II algorithm included
	 * in {@link NSGAII45}
	 *
	 * @author Antonio J. Nebro <antonio@lcc.uma.es>
	 */
public class ClusteringNSGAIIRunner extends AbstractAlgorithmRunner {
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
	  
	    int minK = 3;
	    int maxK = 6;
	    int numberOfObjectives = 2;
	    String datasetPath = "/home/lifo/Downloads/iris/iris-dataset.txt";
	    String filePatternsPath = "/home/lifo/Downloads/iris/true partition/iris-truePartition.txt";
	    String initialPartitionPath = "/home/lifo/Downloads/iris/partitions";
	    double L = 5;
	    
	    List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
	    
	    int populationSize = readInitialPartitions.size();
	    int maxEvaluations = 25000;
	    
	    problem = new PartitionProblem(minK, maxK, numberOfObjectives, datasetPath, filePatternsPath, L, readInitialPartitions);

	    double crossoverProbability = 0.9 ;
	    crossover = new MCLACrossover(crossoverProbability) ;

	    selection = new BinaryTournamentSelection<PartitionSolution>(new RankingAndCrowdingDistanceComparator<PartitionSolution>());

	    algorithm = new ClusteringNSGAII<PartitionSolution>(problem, maxEvaluations, populationSize, crossover,
	            selection, new SequentialSolutionListEvaluator<PartitionSolution>());

	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
	            .execute() ;

	    List<PartitionSolution> population = algorithm.getResult() ;
	    long computingTime = algorithmRunner.getComputingTime() ;

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

	    printFinalSolutionSet(population);
	    if (!referenceParetoFront.equals("")) {
	      printQualityIndicators(population, referenceParetoFront) ;
	    }
	  }
	  
}