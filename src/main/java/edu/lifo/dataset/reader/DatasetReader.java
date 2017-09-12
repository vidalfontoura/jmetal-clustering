package edu.lifo.dataset.reader;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.lifo.initial.partitions.ClusterAndSamples;
import edu.lifo.solution.Sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatasetReader {



    public Map<String, List<Double>> readDataSetAsListPatterns(String datasetPath) {

        Map<String, List<Double>> samplesAndCoordinates = Maps.newHashMap();
       	try (BufferedReader br = Files.newBufferedReader(Paths.get(datasetPath))) {
       		String line = null;
    		while ((line = br.readLine()) != null) {
    			 if (line.startsWith("ID")) continue;
    			 String[] split = line.split("\\s");
                 List<Double> coordinates = Lists.newArrayList();
                 for (int i = 1; i < split.length; i++) {
                     coordinates.add(Double.valueOf(split[i]));
                 }
                 samplesAndCoordinates.put(split[0], coordinates);
    		}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return normalizeData(samplesAndCoordinates);
    }


    //TODO: please check normalization
    public Map<String, List<Double>> normalizeData(Map<String, List<Double>> data) {
    	
    	Double max = Double.MIN_VALUE;
        Double min = Double.MAX_VALUE;
    	for (String cluster: data.keySet()) {
    		List<Double> coordinates = data.get(cluster);
    		Optional<Double> maxPoint = coordinates.stream().max((o1, o2) -> o1.compareTo(o2));
    		Optional<Double> minPoint = coordinates.stream().max((o1, o2) -> o2.compareTo(o1));
    		 if (maxPoint.get() > max) {
                 max = maxPoint.get();
             }
             if (minPoint.get() < min) {
                 min = minPoint.get();
             }
    	}

        final double maxf = max;
        final double minf = min;
        
        for (String cluster: data.keySet()) {
        	List<Double> coordinates = data.get(cluster);
        	List<Double> normalizedCoordinates =
        			coordinates.stream().map(c -> c = (c - minf) / (maxf - minf))
                        .collect(Collectors.toList());
        	data.put(cluster, normalizedCoordinates);
        }
        return data;
    }

    public static List<Map<String, List<String>>> readInitialPartitions(String initialPartitionsPath) {

        File dir = new File(initialPartitionsPath);
        if (!dir.isDirectory()) {
            throw new RuntimeException("The parameter should be a directory");
        }

        File[] filesOnDir = dir.listFiles();
        
        List<Map<String, List<String>>> initialPartitions = Lists.newArrayList();
        for (int i = 0; i < filesOnDir.length; i++) {
           
        	Map<String, List<String>> map = Maps.newHashMap();
        	try (BufferedReader br = Files.newBufferedReader(Paths.get(filesOnDir[i].getAbsolutePath()))) {
        		String line = null;
        		while ((line = br.readLine()) != null) {
        			String[] split = line.split("\\s");
                	String clusterId = split[1];
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
        return initialPartitions;
    }
    
    
//    public Map<String,String> readInitialPartitionsAsMap(String initialPartitionsPath) {
//
//        File dir = new File(initialPartitionsPath);
//        if (!dir.isDirectory()) {
//            throw new RuntimeException("The parameter should be a directory");
//        }
//
//        File[] filesOnDir = dir.listFiles();
//        Map<String,String> content = Maps.newHashMap();
//        for (int i = 0; i < filesOnDir.length; i++) {
//            try (BufferedReader br = Files.newBufferedReader(Paths.get(filesOnDir[i].getAbsolutePath()))) {
//            	content = br.lines().map(line -> line.split("\\s")).collect(Collectors.toMap(a-> a[0], a-> a[1]));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return content;
//    }



    public static void main(String[] args) {

        // String pathToDataSet =
        // "/Users/vfontoura/MOCLE/golub/golub-dataset.txt";

         String pathToDataSet =
         "/home/lifo/Downloads/iris/iris-dataset.txt";
         DatasetReader datasetReader = new DatasetReader();
        
        
         Map<String, List<Double>> data = datasetReader.readDataSetAsListPatterns(pathToDataSet);
         

         for (String sample: data.keySet()) {
        	 List<Double> list = data.get(sample);
        	 System.out.println("Sample="+sample+ " "+list.toString());
         }

       
    	
//    	
//        String initialPartitionsDir = "/home/lifo/Downloads/iris/partitions";
//        List<Map<String, List<String>>> readInitialPartitions = datasetReader.readInitialPartitions(initialPartitionsDir);
//
//        for (int i=0; i<readInitialPartitions.size(); i++) {
//        	System.out.print("individual="+i+ " ");
//        	Map<String, List<String>> map = readInitialPartitions.get(i);
//        	for (String cluster: map.keySet()) {
//        		System.out.print("Cluster="+cluster+" ");
//        		List<String> list = map.get(cluster);
//        		System.out.println(list.toString());
//        	}
//        }

        


    }

}
