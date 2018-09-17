package edu.lifo.algorithm.runner;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.migrated.Patterns;
import edu.lifo.mo.nsgaii.ClusteringNSGAII;
import edu.lifo.operators.MCLAMultiParentCrossover;
import edu.lifo.printer.PartitionSolutionPrinter;
import edu.lifo.problem.PartitionProblem;
import edu.lifo.solution.PartitionSolution;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class NSGAIIMultiParentCrossoverRunner {

    public static void main(String[] args) throws IOException,
        InterruptedException {

        Integer crossoverType = Integer.valueOf(args[0]);
        Integer minK = Integer.valueOf(args[1]);
        Integer maxK = Integer.valueOf(args[2]);
        String datasetPath = args[3];
        String filePatternsPath = args[4];
        String initialPartitionPath = args[5];
        String resultsPath = args[6];
        Integer L = 5;
        if (args[7] != null) {
            L = Integer.valueOf(args[7]);
        }
        Integer numberOfGenerations = 100;
        if (args[8] != null) {
            numberOfGenerations = Integer.valueOf(args[8]);
        }

        Integer numberOfObjectives = 2;
        Integer numberOfParents = 2;
        if (args[9] != null) {
            numberOfParents = Integer.valueOf(args[9]);
        }

        Patterns patterns = new Patterns(datasetPath, filePatternsPath);

        CrossoverOperator<PartitionSolution> crossover =
            new MCLAMultiParentCrossover(1, patterns, minK, maxK);

        List<Map<Integer, List<String>>> initialPopulation =
            DatasetReader.readInitialPartitions(initialPartitionPath);

        PartitionProblem partitionProblem =
            new PartitionProblem(minK, maxK, numberOfObjectives, L,
                initialPopulation, patterns);

        SelectionOperator<List<PartitionSolution>, PartitionSolution> selection =
            new BinaryTournamentSelection<PartitionSolution>(
                new RankingAndCrowdingDistanceComparator<PartitionSolution>());

        Algorithm<List<PartitionSolution>> algorithm =
            new ClusteringNSGAII<PartitionSolution>(partitionProblem,
                numberOfGenerations, initialPopulation.size(), crossover,
                selection,
                new SequentialSolutionListEvaluator<PartitionSolution>(),
                numberOfParents);

        AlgorithmRunner algorithmRunner =
            new AlgorithmRunner.Executor(algorithm).execute();

        List<PartitionSolution> population = algorithm.getResult();

        PartitionSolutionPrinter.printSolutions(resultsPath, population);

    }
}
