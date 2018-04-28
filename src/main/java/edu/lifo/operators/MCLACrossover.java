package edu.lifo.operators;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.lifo.dataset.reader.DatasetReader;
import edu.lifo.globals.ClusteringTypes;
import edu.lifo.migrated.PatternDescription;
import edu.lifo.migrated.Patterns;
import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
        PartitionSolution parent2 = source.get(3);

        int kP1 = parent1.getNumberOfClusters();
        int kP2 = parent2.getNumberOfClusters();

        int nVertices2 = 0;
        int nEdge = 0;
        for (int i = 0; i < source.size(); i++) {
            nVertices2 += source.get(i).getNumberOfClusters();
            nEdge *= source.get(i).getNumberOfClusters();
        }

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
        int indH2 = 1;
        int begin_hP1_2 = indH2;

        for (indH2 = begin_hP1_2; indH2 <= nVertices2; indH2++) {
            for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
                Map<Integer, Integer> map = Maps.newHashMap();
                map.put(patternDescription.getPatternNumber(), 0);
                h.put(indH2, map);
            }
        }

        for (int i = 0; i < source.size(); i++) {

        }

        int indH = 1;
        int begin_hP1 = indH;
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(graphPath));) {

            for (indH = begin_hP1; indH <= nVertices; indH++) {
                for (PatternDescription patternDescription : parent1.getPatterns().getPatternsDescription()) {
                    if (h.get(indH) == null) {
                        Map<Integer, Integer> map = Maps.newTreeMap();
                        map.put(patternDescription.getPatternNumber(), 0);
                        h.put(indH, map);
                    } else {
                        h.get(indH).put(patternDescription.getPatternNumber(), 0);
                    }
                }
            }

            indH = begin_hP1;
            for (Cluster c : parent1.getClusters()) {
                for (Integer patternNumber : c.getListPatternNumber()) {
                    if (h.get(indH) == null) {
                        Map<Integer, Integer> map = Maps.newTreeMap();
                        map.put(patternNumber, 1);
                        h.put(indH, map);
                    } else {
                        h.get(indH).put(patternNumber, 1);
                    }
                }
                indH++;
            }

            int end_hP1 = indH - 1;
            int begin_hP2 = indH;
            for (Cluster c : parent2.getClusters()) {
                for (Integer patternNumber : c.getListPatternNumber()) {
                    if (h.get(indH) == null) {
                        Map<Integer, Integer> map = Maps.newTreeMap();
                        map.put(patternNumber, 1);
                        h.put(indH, map);
                    } else {
                        h.get(indH).put(patternNumber, 1);
                    }
                }
                indH++;
            }
            int end_hP2 = indH - 1;
		    
//		    Print the hypergraph
            // System.out.print("object\t");
            // for (int i = begin_hP1; i <= end_hP2; i++)
            // System.out.print("h "+ i + "\t");
            // System.out.println();
            // for (PatternDescription patternDescription :
            // patterns.getPatternsDescription()) {
            // System.out.print(patternDescription.getPatternNumber() + "\t");
            // for (int i = begin_hP1; i <= end_hP2; i++)
            // System.out.print(h.get(i).get(patternDescription.getPatternNumber())
            // +"\t");
            // System.out.println();
            // }

            Map<Integer, Map<Integer, Double>> w = Maps.newHashMap(); // pesos
                                                                      // das
                                                                      // arestas
            int size1, size2, sizeInter;

            nEdges = 0;
            for (int i = begin_hP1; i <= end_hP1; i++) {
                size1 = 0;
                for (PatternDescription patternDescription : patterns.getPatternsDescription())
                    if (h.get(i).get(patternDescription.getPatternNumber()) == 1)
                        size1++;

                for (int j = begin_hP2; j <= end_hP2; j++) {
                    size2 = 0;
                    sizeInter = 0;
                    for (PatternDescription patternDescription : patterns.getPatternsDescription()) {

                        if (h.get(j).get(patternDescription.getPatternNumber()) == 1) {
                            size2++;
                            if (h.get(i).get(patternDescription.getPatternNumber()) == 1)
                                sizeInter++;
                        }
                    }

                    if (w.get(i) == null) {
                        Map<Integer, Double> map = Maps.newTreeMap();
                        map.put(j, (double) sizeInter / (double) (size1 + size2 - sizeInter));
                        w.put(i, map);
                    } else {
                        w.get(i).put(j, (double) sizeInter / (double) (size1 + size2 - sizeInter));
                    }

                    if (w.get(i).get(j) > 0)
                        nEdges++;

                }
            }

            int w_int;
            fileWriter.write(nVertices + "\t" + nEdges + "\t1" + "\n");
            for (int i = begin_hP1; i <= end_hP1; i++) {
                boolean written = false;
                for (int j = begin_hP2; j <= end_hP2; j++) {
                    if (w.get(i).get(j) != 0) {
                        w_int = (int) Math.ceil(w.get(i).get(j) * 10000);
                        fileWriter.write(j + "\t" + w_int + "\t");
                        written = true;
                    }
                }
                if (written)
                    fileWriter.write("\n");
            }
            for (int j = begin_hP2; j <= end_hP2; j++) {
                boolean written = false;
                for (int i = begin_hP1; i <= end_hP1; i++) {
                    if (w.get(i).get(j) != 0) {
                        w_int = (int) Math.ceil(w.get(i).get(j) * 10000);
                        fileWriter.write(i + "\t" + w_int + "\t");
                        written = true;
                    }
                }

                if (written)
                    fileWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem writing graphFile");
            System.exit(-1);
        }

        int nClustersChild = JMetalRandom.getInstance().nextInt(minK, maxK);
        File file = new File(graphPath);

        MetisExecutor.executeCommand(file, String.valueOf(nClustersChild));

        String resultName = graphPath + ".part." + nClustersChild;

        // depois de particionar:

        Map<Integer, List<Integer>> mc = Maps.newTreeMap(); // meta-clusters:
                                                            // int -> numero do
                                                            // mc,
        // vector<int> -> clusters que compoem
        // mc

        Map<Integer, Map<Integer, Double>> association = Maps.newTreeMap(); // level
                                                                            // of
                                                                            // association
                                                                            // //
                                                                            // of
                                                                            // each
                                                                            // object
                                                                            // to
                                                                            // the
                                                                            // //
                                                                            // mc
                                                                            // int
                                                                            // ->
                                                                            // mc,
                                                                            // int
                                                                            // ->
                                                                            // object,
                                                                            // double
                                                                            // ->
                                                                            // association
                                                                            // level

        int metaclu = 0;
        indH = begin_hP1;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(resultName))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                metaclu = Integer.valueOf(line);
                if (mc.get(metaclu) == null) {
                    List<Integer> list = Lists.newArrayList();
                    mc.put(metaclu, list);
                }
                mc.get(metaclu).add(indH); // TODO: Vidal ver a ordenacao pq no
                                           // codigo original estava
                                           // mc[metaclu].end()
                // System.out.println("mc: "+metaclu + "indH: "+ indH);
                indH++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (PatternDescription patternsIt : patterns.getPatternsDescription()) {
            for (Entry<Integer, List<Integer>> mcIt : mc.entrySet()) {
                if (association.get(mcIt.getKey()) == null) {
                    association.put(mcIt.getKey(), Maps.newTreeMap());
                }
                association.get(mcIt.getKey()).put(patternsIt.getPatternNumber(), 0.0);

                for (Integer iH : mcIt.getValue()) {

                    Double assoValue = association.get(mcIt.getKey()).get(patternsIt.getPatternNumber());
                    assoValue += h.get(iH).get(patternsIt.getPatternNumber());
                    association.get(mcIt.getKey()).put(patternsIt.getPatternNumber(), assoValue);
                }
                if (mcIt.getValue().size() != 0) {
                    Double assoValue = association.get(mcIt.getKey()).get(patternsIt.getPatternNumber());
                    assoValue /= mcIt.getValue().size();
                    association.get(mcIt.getKey()).put(patternsIt.getPatternNumber(), assoValue);
                }
            }
        }
        
//        for (PatternDescription patternsIt : patterns.getPatternsDescription()) {
//        	 for (Entry<Integer,  List<Integer>> mcIt: mc.entrySet()) {
//        		 Double associationLevel = association.get(mcIt.getKey()).get(patternsIt.getPatternNumber());
//        		 System.out.println("id: "+patternsIt.getPatternNumber()+ " mc: "+mcIt.getKey() + " lvl: "+associationLevel);
//        	 }
//        		 
//        		
//
//        }
        
        
//       Set<Integer> keySet = association.keySet();
//       for (Integer key: keySet) {
//    	   System.out.println("mc: "+key);
//    	   Map<Integer, Double> map = association.get(key);
//    	   for (PatternDescription description: patterns.getPatternsDescription()) {
//    		   Double levelOfAssociation = map.get(description.getPatternNumber());
//    		   
//    		   System.out.println("levelOfAssociation: "+levelOfAssociation+ " with object "+ description.getPatternNumber());
//    		   
//    		   
//    	   }
//       }
       
        Map<Integer, Integer> higherAssociation = Maps.newTreeMap(); // Key ->
                                                                     // PatternNumber,
                                                                     // Value ->
                                                                     // mc
                                                                     // cluster
        for (PatternDescription patternsIt : patterns.getPatternsDescription()) {

            Set<Integer> keySet = association.keySet();
            Integer mcQualquer = keySet.iterator().next();
            higherAssociation.put(patternsIt.getPatternNumber(), mcQualquer); // Coloca
                                                                              // higher
                                                                              // association
                                                                              // com
                                                                              // o
                                                                              // primeiro
                                                                              // mc,
                                                                              // apenas
                                                                              // para
                                                                              // pode
                                                                              // comparar
                                                                              // logo
                                                                              // embaixo
            for (Integer mcIt : keySet) {

                Integer mcAux = higherAssociation.get(patternsIt.getPatternNumber());
                Double assoLevelInHigherAsso = association.get(mcAux).get(patternsIt.getPatternNumber());
                Double associationLevelWithMcIt = association.get(mcIt).get(patternsIt.getPatternNumber());
                if (associationLevelWithMcIt > assoLevelInHigherAsso) {
                    higherAssociation.put(patternsIt.getPatternNumber(), mcIt);
                }
            }
        }
        
        Map<Integer, Integer> clusterCorrespondence = Maps.newTreeMap();

        int newLabel = 0;
        for (Integer metaCluster : mc.keySet()) {
            newLabel++;
            clusterCorrespondence.put(metaCluster, newLabel);
        }

        Map<Integer, List<Integer>> offspringAsMap = Maps.newTreeMap();
        for (PatternDescription patternsIt : patterns.getPatternsDescription()) {
            int patternNumber = patternsIt.getPatternNumber();
            Integer mcLabel = higherAssociation.get(patternNumber);
            Integer clusterLabel = clusterCorrespondence.get(mcLabel);

            List<Integer> list = offspringAsMap.get(clusterLabel);
            if (list == null) {
                list = Lists.newArrayList();
                offspringAsMap.put(clusterLabel, list);
            }
            list.add(patternNumber);
        }
      
      List<Cluster> clusters = Lists.newArrayList();
      for (Integer clusterLabel: offspringAsMap.keySet()) {
    	 Cluster cluster = new Cluster(offspringAsMap.get(clusterLabel), clusterLabel, patterns);
    	 clusters.add(cluster);
      }
      
      
      PartitionSolution partitionSolution = new PartitionSolution(clusters, patterns);
      
//      partitionSolution.printPartition();
      List<PartitionSolution> newArrayList = Lists.newArrayList();  
      newArrayList.add(partitionSolution);
      return newArrayList;
      
      
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

    public static void main(String[] args) {

        String datasetPath = "/Users/vfontoura/MOCLE/small-test/iris-dataset.txt";
        String filePatternsPath = "/Users/vfontoura/MOCLE/small-test/true partition/iris-truePartition.txt";
        String initialPartitionPath = "/Users/vfontoura/MOCLE/small-test/partitions";

        Patterns patterns = new Patterns(datasetPath, filePatternsPath);

        MCLACrossover mclaCrossover = new MCLACrossover(1.0, patterns, 3, 6);

        List<Map<Integer, List<String>>> readInitialPartitions = DatasetReader.readInitialPartitions(initialPartitionPath);
        Iterator<Map<Integer, List<String>>> iterator = readInitialPartitions.iterator();
        List<PartitionSolution> solutions = Lists.newArrayList();
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
            solutions.add(partitionSolution);
        }
        System.out.println(solutions.size());
        mclaCrossover.execute(solutions);

    }

}
