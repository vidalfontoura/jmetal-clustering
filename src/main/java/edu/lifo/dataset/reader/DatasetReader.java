package edu.lifo.dataset.reader;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.lifo.initial.partitions.Partition;
import edu.lifo.solution.Pattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatasetReader {



    public List<Pattern> readDataSetAsListPatterns(String datasetPath) {

        // read file into stream, try-with-resources
        List<Pattern> patterns = Lists.newArrayList();
        try (Stream<String> stream = Files.lines(Paths.get(datasetPath))) {
            patterns = stream.filter(line -> !line.startsWith("ID")).map(line -> {
                String[] split = line.split("\\s");
                List<Double> coordinates = Lists.newArrayList();
                for (int i = 1; i < split.length; i++) {
                    coordinates.add(Double.valueOf(split[i]));
                }
                Pattern pattern = new Pattern(coordinates, split[0]);
                return pattern;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return normalizeData(patterns);
    }



    public List<Pattern> normalizeData(List<Pattern> points) {

        Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;
        for (Pattern point : points) {
            Optional<Double> maxPoint = point.getCoordinates().stream().max((o1, o2) -> o1.compareTo(o2));
            Optional<Double> minPoint = point.getCoordinates().stream().max((o1, o2) -> o2.compareTo(o1));
            if (maxPoint.get() > max) {
                max = maxPoint.get();
            }
            if (minPoint.get() < min) {
                min = minPoint.get();
            }
        }

        final double maxf = max;
        final double minf = min;

        return points
            .stream()
            .map(
                p -> {
                    List<Double> coordinates =
                        p.getCoordinates().stream().map(c -> c = (c - minf) / (maxf - minf))
                            .collect(Collectors.toList());
                    p.setCoordinates(coordinates);
                    return p;
                }).collect(Collectors.toList());
    }

    public Set<Partition> readInitialPartitions(String initialPartitionsPath) {

        File dir = new File(initialPartitionsPath);
        if (!dir.isDirectory()) {
            throw new RuntimeException("The parameter should be a directory");
        }

        File[] filesOnDir = dir.listFiles();
        Set<Partition> content = Sets.newHashSet();
        for (int i = 0; i < filesOnDir.length; i++) {
            try (BufferedReader br = Files.newBufferedReader(Paths.get(filesOnDir[i].getAbsolutePath()))) {
                content.addAll(br.lines().map(line -> line.split("\\s"))
                    .map(split -> new Partition(split[0], split[1])).collect(Collectors.toList()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }


    public static void main(String[] args) {

        // String pathToDataSet =
        // "/Users/vfontoura/MOCLE/golub/golub-dataset.txt";

        // String pathToDataSet =
        // "/Users/vfontoura/MOCLE/iris/iris-dataset.txt";
        // DatasetReader datasetReader = new DatasetReader();
        //
        // List<Pattern> patterns =
        // datasetReader.readDataSetAsListPatterns(pathToDataSet);
        // System.out.println(patterns.toString());

        DatasetReader datasetReader = new DatasetReader();
        String initialPartitionsDir = "/Users/vfontoura/MOCLE/iris/partitions";
        Set<Partition> readInitialPartitions = datasetReader.readInitialPartitions(initialPartitionsDir);

        System.out.println(readInitialPartitions.size());


    }

}
