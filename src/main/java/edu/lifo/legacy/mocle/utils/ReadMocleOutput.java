package edu.lifo.legacy.mocle.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.lifo.index.CorrectedRandCalculator;



public class ReadMocleOutput {
	
	private static final String SOLUTION_POPULATION = "solutionPopulation";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String partitionsMocle = "/home/lifo/Downloads/MOCLE/MOCLE-v3/golub/saida";
		
//		String partitionsMocle = "/home/lifo/Downloads/MOCLE/MOCLE-v3/iris/saida";
		String truePartition = "/home/lifo/Downloads/bases/golub/partitions/TP/golubReal-2classes.clu";
		
		
		File truePartitionFile = new File(truePartition);
		File mocleOutput = new File(partitionsMocle);
		File[] listFiles = mocleOutput.listFiles();
		
		Map<String, Integer> truePartitionMap = Maps.newTreeMap();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(truePartitionFile.getAbsolutePath()))) {
       		String line = null;
       		while ((line = br.readLine()) != null) {
    			String[] split = line.split("\\s");
    			truePartitionMap.put(split[0], Integer.valueOf(split[1]));
    		}
        } catch (IOException e) {
            e.printStackTrace();
        }	
		
		
		
		for(File executionFile: listFiles) {
			
			Map<String,Double> maxCorrectedRandMap =  Maps.newTreeMap();
			Double maxCorrectedRand =  Double.MIN_VALUE;
			
			maxCorrectedRandMap.put(executionFile.getName(), maxCorrectedRand);
			
			if (executionFile.isDirectory()) {
				File file = new File(executionFile.getAbsolutePath() + File.separator + SOLUTION_POPULATION);
				File[] resultsFiles = file.listFiles();
			
			
				for (File partitionFile: resultsFiles) {
					
					Map<String,Integer> partition = Maps.newTreeMap();
					
					
					
					if (partitionFile.isFile()) {
						try (BufferedReader br = Files.newBufferedReader(Paths.get(partitionFile.getAbsolutePath()))) {
				       		String line = null;
				       		while ((line = br.readLine()) != null) {
				    			String[] split = line.split("\\s");
				    			partition.put(split[0], Integer.valueOf(split[1]));
				    		}
				        } catch (IOException e) {
				            e.printStackTrace();
				        }	
					}
					
					CorrectedRandCalculator correctedRandCalc = new CorrectedRandCalculator();
					double corretedRand = correctedRandCalc.calculateIndexes(partition, truePartitionMap);
					
					if (corretedRand > maxCorrectedRandMap.get(executionFile.getName())) {
						maxCorrectedRandMap.put(executionFile.getName(), corretedRand);
					}
					
				}
				
				for (String fileName : maxCorrectedRandMap.keySet()) {
					System.out.println(maxCorrectedRandMap.get(fileName));
				}
				
				
				
			}
		}
		
		
	
		
		
		
		
		
	}

}
