package edu.lifo.index;

import edu.lifo.migrated.PatternDescription;
import edu.lifo.solution.PartitionSolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CorrectedRandCalculator {

	

	public double calculateIndexes(PartitionSolution partitionSolution) throws IOException, InterruptedException {
		
		File outputFile = File.createTempFile("output", ".R");
        outputFile.deleteOnExit();
       
		
		StringBuilder scriptBuilder = new StringBuilder();
		scriptBuilder.append("require(clues)\n");
		List<PatternDescription> patternsDescription = partitionSolution.getPatterns().getPatternsDescription();
		
		StringBuilder partition = new StringBuilder("cl1 <- c(");
		StringBuilder truePartition = new StringBuilder("cl2 <- c(");
		
		for (PatternDescription patternDescription: patternsDescription) {
			int patternNumber = patternDescription.getPatternNumber();
			int trueCluster = patternDescription.getPatternClass();
			int clusterOf = partitionSolution.clusterOf(patternNumber);
			
			
			partition.append(clusterOf+",");
			truePartition.append(trueCluster+",");
			
		}
		partition.deleteCharAt(partition.length()-1).append(")");
        truePartition.deleteCharAt(truePartition.length() - 1).append(")");
		
		scriptBuilder.append(partition.toString()+"\n");
		scriptBuilder.append(truePartition.toString()+"\n");
		
		scriptBuilder.append("adjustedRand(cl1,cl2)");
		
		
		File scriptFile = File.createTempFile("script", ".R");
		scriptFile.deleteOnExit();
		
		try (FileWriter scriptWriter = new FileWriter(scriptFile)) {
		    scriptWriter.append(scriptBuilder.toString());
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(
System.getProperty("os.name").contains("win") || System.getProperty("os.name").contains("Win") ? "R.exe"
                : "/Library/Frameworks/R.framework/Versions/3.2/Resources/Rscript",
 "--slave", scriptFile.getAbsolutePath());
		processBuilder.redirectOutput(outputFile);
		
		Process process = processBuilder.start();
		process.waitFor();
		
		double randIndex = 0.0;
		try (Scanner scanner = new Scanner(outputFile)) {
		     while (scanner.hasNextLine()) {
		    	String nextLine = scanner.nextLine().trim();
		    	if (!nextLine.contains("Rand")) {
		    		String[] split = nextLine.split(" ");
		    		randIndex = Double.valueOf(split[0]);
		    	}
		     }
		}
		outputFile.delete();
		return randIndex;
		
	}
	
	
	public double calculateIndexes(Map<String, Integer> partitionMap, Map<String, Integer> truePartitionMap) throws IOException, InterruptedException {
		
		File outputFile = File.createTempFile("output", ".R");
        outputFile.deleteOnExit();
       
		
		StringBuilder scriptBuilder = new StringBuilder();
		scriptBuilder.append("require(clues)\n");
		
		StringBuilder partition = new StringBuilder("cl1 <- c(");
		StringBuilder truePartition = new StringBuilder("cl2 <- c(");
		
		
		Set<String> keySet = truePartitionMap.keySet();
		for (String id: keySet) {
			Integer clusterPartition = partitionMap.get(id);
			Integer clusterTruePartition = truePartitionMap.get(id);
			
			partition.append(clusterPartition+",");
			truePartition.append(clusterTruePartition+",");
			
		}
		partition.deleteCharAt(partition.length()-1).append(")");
		truePartition.deleteCharAt(partition.length()-1).append(")");
		
		scriptBuilder.append(partition.toString()+"\n");
		scriptBuilder.append(truePartition.toString()+"\n");
		
		scriptBuilder.append("adjustedRand(cl1,cl2)");
		
		
		File scriptFile = File.createTempFile("script", ".R");
		scriptFile.deleteOnExit();
		
		try (FileWriter scriptWriter = new FileWriter(scriptFile)) {
		    scriptWriter.append(scriptBuilder.toString());
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(
		        System.getProperty("os.name").contains("win") || System.getProperty("os.name").contains("Win") ? "R.exe" : "R",
		    "--slave",
		    "-f", scriptFile.getAbsolutePath());
		processBuilder.redirectOutput(outputFile);
		
		Process process = processBuilder.start();
		process.waitFor();
		
		double randIndex = 0.0;
		try (Scanner scanner = new Scanner(outputFile)) {
		     while (scanner.hasNextLine()) {
		    	String nextLine = scanner.nextLine().trim();
		    	if (!nextLine.contains("Rand")) {
		    		String[] split = nextLine.split(" ");
		    		randIndex = Double.valueOf(split[0]);
		    	}
		     }
		}
		outputFile.delete();
		return randIndex;
		
	}
	
	
	
}
