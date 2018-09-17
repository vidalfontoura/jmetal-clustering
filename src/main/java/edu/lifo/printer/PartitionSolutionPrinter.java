package edu.lifo.printer;

import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PartitionSolutionPrinter {

    public static void
        printSolutions(String path, List<PartitionSolution> partitionSolutions)
            throws IOException {

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (int i = 0; i < partitionSolutions.size(); i++) {
            StringBuilder fileName =
                new StringBuilder(path + File.separator + "partitionk");
            PartitionSolution solution = partitionSolutions.get(i);
            List<Cluster> clusters = solution.getClusters();
            fileName.append(clusters.size()).append("s").append(i)
                .append(".clu");
            try (FileWriter fileWriter = new FileWriter(fileName.toString())) {

                Patterns patterns = solution.getPatterns();

                for (PatternDescription pattern : patterns
                    .getPatternsDescription()) {
                    String patternLabel = pattern.getPatternLabel();
                    int patternNumber = pattern.getPatternNumber();
                    int cluster = solution.clusterOf(patternNumber);
                    fileWriter.write(patternLabel + " " + cluster);
                    fileWriter.write("\n");
                }

            }

        }

    }
}
