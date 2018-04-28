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

public class MultiParentMCLACrossover implements CrossoverOperator<PartitionSolution> {

    private static final long serialVersionUID = 1L;
    
    private static final String GRAPH_DIR = "graphDir";
    
    private double crossoverProbability;
    
    private Patterns patterns;
    
    private int minK, maxK;

    public MultiParentMCLACrossover(double crossoverProbability, Patterns patterns, int minK, int maxK) {
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


        int nVertices = 0;
        int nEdges =  0;
        for(int i=0; i<source.size(); i++) {
            nVertices += source.get(i).getNumberOfClusters();
        }

        

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
        
        long tempoInicio = System.currentTimeMillis();

        int numberOfHyperClusters = 1;

        for (int parentI = 0; parentI < source.size(); parentI++) {
            PartitionSolution partitionSolution = source.get(parentI);
            List<Cluster> clusters = partitionSolution.getClusters();
            for (int clusterI = 0; clusterI < clusters.size(); clusterI++) {

                Map<Integer, Integer> map = Maps.newHashMap();
                Cluster cluster = clusters.get(clusterI);

                for (PatternDescription pat : patterns.getPatternsDescription()) {
                    if (cluster.getListPatternNumber().contains(pat.getPatternNumber())) {
                        map.put(pat.getPatternNumber(), 1);
                    } else {
                        map.put(pat.getPatternNumber(), 0);
                    }
                }

                h.put(numberOfHyperClusters, map);
                numberOfHyperClusters++;
            }
        }

        // Print the hypergraph
        System.out.print("object\t");
        for (int i = 1; i < h.size() + 1; i++)
            System.out.print("h " + i + "\t");
        System.out.println();
        for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
            System.out.print(patternDescription.getPatternNumber() + "\t");
            for (int i = 1; i < h.size() + 1; i++)
                System.out.print(h.get(i).get(patternDescription.getPatternNumber()) + "\t");
            System.out.println();
        }

        System.out.println("Tempo Total: " + (System.currentTimeMillis() - tempoInicio));
        
        
        Map<Integer, Map<Integer, Double>> w = Maps.newHashMap(); // pesos
                                                                    // das
                                                                    // arestas
        
        double[][] pesos = new double[numberOfHyperClusters][numberOfHyperClusters];

        nEdges = 0;
        for (int i = 1; i <= h.size(); i++) {
            for (int j = 1; j <= h.size(); j++) {

                double union = 0;
                double intersec = 0;
                double peso = 0;
                for (PatternDescription patternDescription : patterns.getPatternsDescription()) {
                    int patternNumber = patternDescription.getPatternNumber();
                    if (i == j)
                        continue;
                    if (h.get(i).get(patternNumber) == 1 && h.get(j).get(patternNumber) == 1) {
                        intersec++;
                    }
                    if (h.get(i).get(patternNumber) == 1) {
                        union++;
                    }
                    if (h.get(j).get(patternNumber) == 1) {
                        union++;
                    }
                }
                if (union > 0) {
                    peso = intersec / union;
                    pesos[i][j] = peso;
                    if (w.get(i) == null) {
                        Map<Integer, Double> map = Maps.newTreeMap();
                        map.put(j, (double) intersec / union);
                        w.put(i, map);
                    } else {
                        w.get(i).put(j, (double) intersec / union);
                    }
                }
            }
        }

        Set<Integer> keySet3 = w.keySet();
        for (Integer key : keySet3) {
            Set<Integer> keySet4 = w.get(key).keySet();
            for (Integer key2 : keySet4) {
                if (w.get(key).get(key2) > 0) {
                    nEdges++;
                }
            }
        }
        nEdges = nEdges / 2;
        
        Set<Integer> keySet = w.keySet();
        for (Integer key : keySet) {
            System.out.println(key);
            Map<Integer, Double> map = w.get(key);
            Set<Integer> keySet2 = map.keySet();
            for (Integer key2 : keySet2) {
                System.out.print(" " + key2 + "->" + map.get(key2));
            }
            System.out.println();
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(graphPath));) {
        
            fileWriter.write(nVertices + "\t" + nEdges + "\t1" + "\n");
            for (int i = 1; i <= h.size(); i++) {
                Map<Integer, Double> edges = w.get(i);
                Set<Integer> keys = edges.keySet();
                for (Integer key : keys) {
                    Double weight = edges.get(key);
                    if (weight > 0) {
                        int w_int = (int) Math.ceil(weight * 10000);
                        fileWriter.write(key + "\t" + w_int + "\t");
                    }
                    }
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
        int indH = 1;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(resultName))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                metaclu = Integer.valueOf(line);
                if (mc.get(metaclu) == null) {
                    List<Integer> list = Lists.newArrayList();
                    mc.put(metaclu, list);
                }
                mc.get(metaclu).add(indH);
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
      
        Map<Integer, Integer> higherAssociation = Maps.newTreeMap(); // Key ->
        // PatternNumber,
        // Value ->
        // mc
        // cluster
        for (PatternDescription patternsIt : patterns.getPatternsDescription()) {

            Set<Integer> keySet2 = association.keySet();
            Integer mcQualquer = keySet2.iterator().next();
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
            for (Integer mcIt : keySet2) {

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
        for (Integer clusterLabel : offspringAsMap.keySet()) {
            Cluster cluster = new Cluster(offspringAsMap.get(clusterLabel), clusterLabel, patterns);
            clusters.add(cluster);
        }

        PartitionSolution partitionSolution = new PartitionSolution(clusters, patterns);

        // partitionSolution.printPartition();
        List<PartitionSolution> newArrayList = Lists.newArrayList();
        newArrayList.add(partitionSolution);
        System.out.println("Child");
        partitionSolution.printPartition();
        return newArrayList;
      
	}

	@Override
	public int getNumberOfParents() {
		return 2;
	}

    public static void main(String[] args) {

        String datasetPath = "/Users/vfontoura/MOCLE/iris-test/iris-dataset.txt";
        String filePatternsPath = "/Users/vfontoura/MOCLE/iris-test/true partition/iris-truePartition.txt";
        String initialPartitionPath = "/Users/vfontoura/MOCLE/iris-test/partitions";

        Patterns patterns = new Patterns(datasetPath, filePatternsPath);

        MultiParentMCLACrossover mclaCrossover = new MultiParentMCLACrossover(1.0, patterns, 3, 4);

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
