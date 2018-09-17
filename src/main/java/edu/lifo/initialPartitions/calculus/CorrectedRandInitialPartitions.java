package edu.lifo.initialPartitions.calculus;

import com.google.common.collect.Lists;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.index.CorrectedRandCalculator;
import edu.lifo.migrated.Patterns;
import edu.lifo.problem.PartitionProblem;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CorrectedRandInitialPartitions {

    public static void main(String[] args) throws IOException, InterruptedException {

        String initialPartitionPath = "/Users/vfontoura/MOCLE/rebasededadosbpartificial/partitions";
        String datasetPath = "/Users/vfontoura/MOCLE/rebasededadosbpartificial/2sp2glob.txt";
        String filePatternsPath = "//Users/vfontoura/MOCLE/rebasededadosbpartificial/true partition/2sp2globGold.clu";
        Patterns patterns = new Patterns(datasetPath, filePatternsPath);

        List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
        Iterator<Map<Integer, List<String>>> iterator = readInitialPartitions.iterator();
        List<PartitionSolution> lists = Lists.newArrayList();

        PartitionProblem partitionProblem = new PartitionProblem(2, 6, 2, 5, readInitialPartitions, patterns);

        while (iterator.hasNext()) {
            Map<Integer, List<String>> next = iterator.next();
            List<Cluster> clusterList = Lists.newArrayList();
            for (Integer clusterId : next.keySet()) {

                List<String> patternLabels = next.get(clusterId);
                List<Integer> patternNumbers = Lists.newArrayList();
                for (String patternLabel : patternLabels) {
                    int patternNumberByPatternLabel = patterns.getPatternNumberByPatternLabel(patternLabel);
                    patternNumbers.add(patternNumberByPatternLabel);
                }
                Cluster cluster = new Cluster(patternNumbers, clusterId, patterns);
                clusterList.add(cluster);
            }
            PartitionSolution partitionSolution = new PartitionSolution(clusterList, patterns);
            lists.add(partitionSolution);
        }

            CorrectedRandCalculator correctedRandCalculator = new CorrectedRandCalculator();
            lists.stream().forEach(c -> {
                try{
                System.out.println("index:" + correctedRandCalculator.calculateIndexes(c));

                System.out.println(partitionProblem.calculateConnectivity(c) + " " + partitionProblem.calculateVarIntraCluster(c));

                } catch (Exception e) {
                    e.printStackTrace();
                };
            });
    }

}
