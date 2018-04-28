package edu.lifo.operators;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

import java.util.List;
import java.util.Map;

import org.uma.jmetal.operator.CrossoverOperator;

public class DICLENS implements CrossoverOperator<PartitionSolution> {


    private int minK;
    private int maxK;

    public DICLENS(int minK, int maxK) {

        this.minK = minK;
        this.maxK = maxK;
    }

    @Override
    public List<PartitionSolution> execute(List<PartitionSolution> source) {

        PartitionSolution parent1 = source.get(0);
        PartitionSolution parent2 = source.get(1);

        List<Cluster> clusters1 = parent1.getClusters();
        List<Cluster> clusters2 = parent2.getClusters();


        return null;

    }

    @Override
    public int getNumberOfParents() {

        // TODO Auto-generated method stub
        return 0;
    }

    public static void main(String[] args) {

        String datasetPath = "/home/lifo/Downloads/iris/iris-dataset.txt";
        String filePatternsPath = "/home/lifo/Downloads/iris/true partition/iris-truePartition.txt";
        String initialPartitionPath = "/home/lifo/Downloads/iris/partitions";

        List<Map<Integer, List<String>>> readInitialPartitions =
            DatasetReader.readInitialPartitions(initialPartitionPath);

        int populationSize = readInitialPartitions.size();

        Patterns patterns = new Patterns(datasetPath, filePatternsPath);

    }


}
