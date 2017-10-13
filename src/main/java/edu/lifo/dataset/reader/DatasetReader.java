package edu.lifo.dataset.reader;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatasetReader {


    public static List<Map<Integer, List<String>>> readInitialPartitions(String initialPartitionsPath) {

        File dir = new File(initialPartitionsPath);
        if (!dir.isDirectory()) {
            throw new RuntimeException("The parameter should be a directory");
        }

        File[] filesOnDir = dir.listFiles();
        
        List<Map<Integer, List<String>>> initialPartitions = Lists.newArrayList();
        for (int i = 0; i < filesOnDir.length; i++) {
            if (!filesOnDir[i].getName().startsWith(".DS_")) {
                Map<Integer, List<String>> map = Maps.newHashMap();
                try (BufferedReader br = Files.newBufferedReader(Paths.get(filesOnDir[i].getAbsolutePath()))) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        String[] split = line.split("\\s");
                        Integer clusterId = Integer.valueOf(split[1]);
                        String sampleId = split[0];
                        if (map.get(clusterId) == null) {
                            List<String> listSamples = Lists.newArrayList();
                            listSamples.add(sampleId);
                            map.put(clusterId, listSamples);
                        } else {
                            map.get(clusterId).add(sampleId);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initialPartitions.add(map);
            }
        }
        return initialPartitions;
    }
    
    

    public static void main(String[] args) {

        DatasetReader datasetReader = new DatasetReader();      
        String initialPartitionsDir = "/home/lifo/Downloads/iris/partitions";
        List<Map<Integer, List<String>>> readInitialPartitions = datasetReader.readInitialPartitions(initialPartitionsDir);

        for (int i=0; i<readInitialPartitions.size(); i++) {
        	System.out.print("individual="+i+ " ");
        	Map<Integer, List<String>> map = readInitialPartitions.get(i);
        	for (Integer cluster: map.keySet()) {
        		System.out.print("Cluster="+cluster+" ");
        		List<String> list = map.get(cluster);
        		System.out.println(list.toString());
        	}
        }

        


    }

}
