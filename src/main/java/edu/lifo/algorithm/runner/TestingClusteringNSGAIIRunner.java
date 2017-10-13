package edu.lifo.algorithm.runner;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.index.CorrectedRandCalculator;
import edu.lifo.jmetal.util.PrintFinalSolutionSetClustering;
import edu.lifo.migrated.Patterns;
import edu.lifo.mo.nsgaii.ClusteringNSGAII;
import edu.lifo.operators.MCLACrossover;
import edu.lifo.problem.PartitionProblem;
import edu.lifo.solution.PartitionSolution;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import com.google.common.collect.Lists;


/**
 * Class to configure and run the implementation of the ClusteringNSGAII algorithm {@link ClusteringNSGAII}
 *
 * @author vfontoura
 */
public class TestingClusteringNSGAIIRunner extends AbstractAlgorithmRunner {
	  /**
	   * @param args Command line arguments.
	   * @throws JMetalException
	 * @throws InterruptedException 
	 * @throws IOException 
	   */
	  public static void main(String[] args) throws JMetalException, IOException, InterruptedException {
		  
		  
	    Problem<PartitionSolution> problem;
	    Algorithm<List<PartitionSolution>> algorithm;
	    CrossoverOperator<PartitionSolution> crossover;
	    SelectionOperator<List<PartitionSolution>, PartitionSolution> selection;
	    String referenceParetoFront = "" ;
	  
	    int minK = 3;
	    int maxK = 6;
	    int numberOfObjectives = 2;
         
	    String datasetPath = "/home/lifo/Downloads/iris/iris-dataset.txt";
         String filePatternsPath =
         "/home/lifo/Downloads/iris/true partition/iris-truePartition.txt";
         String initialPartitionPath = "/home/lifo/Downloads/iris/partitions";
//	    
//	    
//		String datasetPath = "/home/lifo/Downloads/bases/golub/dataset/golub.txt";
//		String filePatternsPath = "/home/lifo/Downloads/bases/golub/partitions/TP/golubReal-2classes.clu";
//		String initialPartitionPath = "/home/lifo/Downloads/bases/golub/partitions/BAlg";
	    
	    
//		String datasetPath = "/home/lifo/Downloads/golub/golub-dataset.txt";
//		String filePatternsPath = "/home/lifo/Downloads/golub/true partition/truePartitionBCell-TCell-AML.clu";
//		String initialPartitionPath = "/home/lifo/Downloads/golub/partitions";
	    
	    
//        String datasetPath = "/Users/vfontoura/MOCLE/iris-test/iris-dataset.txt";
//        String filePatternsPath = "/Users/vfontoura/MOCLE/iris-test/true partition/iris-truePartition.txt";
//        String initialPartitionPath = "/Users/vfontoura/MOCLE/iris-test/partitions";

	    double L = 5;
	    int maxEvaluations = 10000;
	    double crossoverProbability = 0.9 ;

	   
	    
	    List<Double> correctedRands = Lists.newArrayList();
	    List<PartitionSolution> partitionSolutions = Lists.newArrayList();
	    for (int i=0; i<30; i++) {
	    
		    List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
		    
		    int populationSize = readInitialPartitions.size();
		  
		    Patterns patterns = new Patterns(datasetPath, filePatternsPath);
		    
		    problem = new PartitionProblem(minK, maxK, numberOfObjectives, L, readInitialPartitions, patterns);
	
	        crossover = new MCLACrossover(crossoverProbability, patterns, minK, maxK);
	
		    selection = new BinaryTournamentSelection<PartitionSolution>(new RankingAndCrowdingDistanceComparator<PartitionSolution>());
	
		    algorithm = new ClusteringNSGAII<PartitionSolution>(problem, maxEvaluations, populationSize, crossover,
		            selection, new SequentialSolutionListEvaluator<PartitionSolution>());
		    
			    
		    new AlgorithmRunner.Executor(algorithm)
		            .execute();
		    
		    PartitionSolution partitionHigherCorrectedRand = null;
		    double higherCorrectedRand = Double.MIN_VALUE;
		    List<PartitionSolution> result = algorithm.getResult();
		    for (PartitionSolution partitionSolution: result) {
		    	 double calculateIndexes = new CorrectedRandCalculator().calculateIndexes(partitionSolution);
//		    	 System.out.println("index: "+calculateIndexes);
		    	 System.out.println(partitionSolution.getObjective(1)+ " " +partitionSolution.getObjective(0));
		    	 if (calculateIndexes > higherCorrectedRand ) {
		    		 higherCorrectedRand = calculateIndexes;
		    		 partitionHigherCorrectedRand = partitionSolution;
		    	 }
		    }
		    System.out.println();
		    correctedRands.add(higherCorrectedRand);
		    partitionSolutions.add(partitionHigherCorrectedRand);
	    }
	    
	    
//	    System.out.println("HigherCorrectedRand: "+higherCorrectedRand);
//	    partitionHigherCorrectedRand.printPartition();
	   
	    for (Double value: correctedRands) {
	    	System.out.println(value);
	    }
//	    for (PartitionSolution partitionSolution: partitionSolutions) {
//	    	partitionSolution.printPartition();
//	    	System.out.println();
//	    }
	    
	    
	   
	  }
	  
}