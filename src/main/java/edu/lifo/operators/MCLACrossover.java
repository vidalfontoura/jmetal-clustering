package edu.lifo.operators;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.lifo.globals.ClusteringTypes;
import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;
import edu.lifo.solution.Sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class MCLACrossover implements CrossoverOperator<PartitionSolution> {

    private static final long serialVersionUID = 1L;
    
    private static final String GRAPH_DIR = "graphDir";
    
    
    private double crossoverProbability;
    
    private Patterns patterns;
    
    private int minK, maxK;

    public MCLACrossover(double crossoverProbability, Patterns patterns, int minK, int maxK) {
		this.crossoverProbability = crossoverProbability;
		this.patterns = patterns;
        this.minK = minK;
        this.maxK = maxK;
	}

	@Override
	public List<PartitionSolution> execute(List<PartitionSolution> source) {
		ClusteringTypes.passo++;
		
		File graphDirFile = new File(GRAPH_DIR);
		if (!graphDirFile.exists()) {
			graphDirFile.mkdir();
		}
		
		String graphPath = GRAPH_DIR + File.separator + new Date().getTime();

        PartitionSolution parent1 = source.get(0);
        PartitionSolution parent2 = source.get(1);

        System.out.println("Parent1");
        parent1.printPartition();

        System.out.println("Parent2");
        parent2.printPartition();

        int kP1 = parent1.getNumberOfClusters();
        int kP2 = parent2.getNumberOfClusters();

        int nVertices = kP1 + kP2;

        int nEdges = kP1 * kP2;

        Map<Integer, Map<Integer, Integer>> h = Maps.newTreeMap(); // int ->
                                                                   // indice do
                                                                   // h, int ->
                                                                   // objeto,
                                                                   // int -> 0
                                                                   // ou 1 se
                                                                   // objeto nao
                                                                   // pertence
                                                                   // ou
                                                                   // pertence
                                                                   // ao cluster
                                                                   // hi

        int indH = 1;
        int begin_hP1 = indH;
		try (BufferedWriter fileWriter =
		         new BufferedWriter(new FileWriter(graphPath));) {
		

			    
			for (indH = begin_hP1; indH <= nVertices; indH++) {
				  for (PatternDescription patternDescription : parent1.getPatterns().getPatternsDescription()) {
					  if (h.get(indH) == null) {
						  Map<Integer,Integer> map = Maps.newTreeMap();
						  map.put(patternDescription.getPatternNumber(), 0);
						  h.put(indH, map);
					  }else {
						  h.get(indH).put(patternDescription.getPatternNumber(), 0);
					  }
				  }
			}    
			
			indH = begin_hP1;        
		    for (Cluster c: parent1.getClusters()) {           
		        for (Sample it: c.getSamples()){
		        	 if (h.get(indH) == null) {
						  Map<Integer,Integer> map = Maps.newTreeMap();
						  map.put(it.getPatternId(), 1);
						  h.put(indH, map);
					  }else {
						  h.get(indH).put(it.getPatternId(), 1);
					  }
		        }    
		        indH++;
		    }
		    
		    int end_hP1 = indH - 1;
		    int begin_hP2 = indH;
		    for (Cluster c: parent2.getClusters()) {      
		    	for (Sample it: c.getSamples()){
		            if (h.get(indH) == null) {
						  Map<Integer,Integer> map = Maps.newTreeMap();
						  map.put(it.getPatternId(), 1);
						  h.put(indH, map);
					  }else {
						  h.get(indH).put(it.getPatternId(), 1);
					  }
		        }    
		        indH++;
		    }
		    int end_hP2 = indH - 1;
		    
//		    Print the hypergraph
		    System.out.print("object\t");
		    for (int i = begin_hP1; i <= end_hP2; i++)
		       System.out.print("h "+ i + "\t");
		    System.out.println();
		    for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
		        System.out.print(patternDescription.getPatternNumber() + "\t");
		        for (int i = begin_hP1; i <= end_hP2; i++)
		            System.out.print(h.get(i).get(patternDescription.getPatternNumber()) +"\t");
		        System.out.println();
		    }   
		    
		    Map<Integer, Map<Integer, Double> > w = Maps.newHashMap(); // pesos das arestas
		    int size1, size2, sizeInter;
		    
		    nEdges = 0;
		    for (int i = begin_hP1; i <= end_hP1; i++){
		    	size1 = 0;
		    	for (PatternDescription patternDescription : patterns.getPatternsDescription())
		    			if (h.get(i).get(patternDescription.getPatternNumber()) == 1) size1++;   
		    	
		    	for (int j = begin_hP2; j <= end_hP2; j++) {
		            size2 = 0;
		            sizeInter = 0; 
		            for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
		                
		            	
		            	if (h.get(j).get(patternDescription.getPatternNumber()) == 1) {
		                    size2++;
		                    if (h.get(i).get(patternDescription.getPatternNumber()) == 1) sizeInter++;
		                }        
		            }    
		            
		            if (w.get(i) == null) {
		            	Map<Integer,Double> map = Maps.newTreeMap();
		            	map.put(j, (double) sizeInter / (double)(size1 + size2 - sizeInter));
		            	w.put(i, map);
		            } else {
		            	w.get(i).put(j, (double) sizeInter / (double)(size1 + size2 - sizeInter));
		            }
		           
		            if (w.get(i).get(j) > 0) nEdges++;
		           
		        }    
		    }
		    
		    
		    int w_int;      
		    fileWriter.write(nVertices + "\t" + nEdges + "\t1" + "\n");
		    for (int i = begin_hP1; i <= end_hP1; i++)
		    {
		        boolean written = false;
		        for (int j = begin_hP2; j <= end_hP2; j++)
		        {
		            if (w.get(i).get(j) != 0)
		            {
		                w_int = (int) Math.ceil(w.get(i).get(j) * 10000);
		                fileWriter.write(j + "\t" + w_int + "\t");
		                written = true;
		            }    
		        }    
		        if (written) fileWriter.write("\n");    
		    }    
		    for (int j = begin_hP2; j <= end_hP2; j++)
		    {
		        boolean written = false;
		        for (int i = begin_hP1; i <= end_hP1; i++)
		        {
		            if (w.get(i).get(j) != 0)
		            {
		                w_int = (int) Math.ceil(w.get(i).get(j) * 10000);
		                fileWriter.write(i + "\t" + w_int + "\t");
		                written = true;
		            }    
		        }    
		              
		        if (written) fileWriter.write("\n"); 
		    }      
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("Problem writing graphFile");
			System.exit(-1);
		} 

        int nClustersChild = JMetalRandom.getInstance().nextInt(minK, maxK);
        File file = new File(graphPath);

        MetisExecutor.executeCommand(file, String.valueOf(nClustersChild));
        
        
        String resultName = graphPath + ".part." +nClustersChild;
        
     

        // depois de particionar:
            
        Map<Integer, List<Integer>> mc = Maps.newTreeMap(); // meta-clusters:
                                                            // int -> numero do
                                                            // mc,
                                        // vector<int> -> clusters que compoem
                                        // mc

        Map<Integer, Map<Integer, Double>> association = Maps.newTreeMap(); // level
                                                                            // of
                                                                            // association  // of each object to the // mc
        // int -> mc, int -> object, double -> association level

        int metaclu = 0;
        indH = begin_hP1;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(resultName))) {
            String line = null;
            while ((line = br.readLine()) != null) {

                if (mc.get(metaclu) == null) {
                    List<Integer> list= Lists.newArrayList();
                }
                mc..insert(mc[metaclu].end(), indH);
                //std::cout << "mc: " << metaclu << "\indH: " << indH << std::endl;
                indH++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
     
        
		return null;
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

	public static void main(String[] args) {
//		MCLACrossover mclaCrossover = new MCLACrossover(1.0);
//		
//		String datasetPath = "/home/lifo/Downloads/iris-testes/iris-dataset.txt";
//		String filePatternsPath = "/home/lifo/Downloads/iris-testes/true partition/iris-truePartition.txt";
//		String initialPartitionPath = "/home/lifo/Downloads/iris-testes/partitions";
//		
//		Patterns patterns = new Patterns(datasetPath, filePatternsPath);
//		
//		List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
//		 
//	    Iterator<Map<Integer, List<String>>> iterator = readInitialPartitions.iterator();
//	    List<PartitionSolution> solutions = Lists.newArrayList();
//		if (iterator.hasNext()) {
//			Map<Integer, List<String>> next = iterator.next();
//			List<Cluster> clusterList = Lists.newArrayList();
//			for (Integer clusterId:  next.keySet()) {
//				List<String> patternLabels = next.get(clusterId);
//				List<Sample> samples = Lists.newArrayList();
//				for (String patternLabel: patternLabels) {
//					double[] coordinatesByPatternLabel = patterns.getCoordinatesByPatternLabel(patternLabel);
//					int patternNumberByPatternLabel = patterns.getPatternNumberByPatternLabel(patternLabel);
//					Sample sample = new Sample(coordinatesByPatternLabel, patternLabel, patternNumberByPatternLabel);
//					samples.add(sample);
//				}
//				
//				Cluster cluster = new Cluster(samples, clusterId);
//				clusterList.add(cluster);
//			}
//			PartitionSolution partitionSolution = new PartitionSolution(clusterList, null, patterns);
//			solutions.add(partitionSolution);
//		}
//		
//		
//		mclaCrossover.execute(solutions);
		
		
	}

}
