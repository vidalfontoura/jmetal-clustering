package edu.lifo.operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.lifo.solution.Cluster;
import edu.lifo.solution.PartitionSolution;

public class HBGFCrossover implements CrossoverOperator<PartitionSolution>  {

	 private static final String GRAPH_DIR = "graphDir";
	 
	 private int minK;
	 private int maxK;
	 
	 
	public HBGFCrossover(int minK, int maxK) {
		this.minK = minK;
		this.maxK = maxK;
	}

	@Override
	public List<PartitionSolution> execute(List<PartitionSolution> source) {
		
		File graphDirFile = new File(GRAPH_DIR);
		if (!graphDirFile.exists()) {
			graphDirFile.mkdir();
		}
		
		String graphPath = GRAPH_DIR + File.separator + new Date().getTime();

        PartitionSolution parent1 = source.get(0);
        PartitionSolution parent2 = source.get(1);
        
        System.out.println("Pai1");
        parent1.printPartition();
        System.out.println("Pai2");
        parent2.printPartition();
        
        int nVertices = parent1.getPatterns().getPatternsDescription().size() +
                parent1.getClusters().size() +
                parent2.getClusters().size();
       
        System.out.println(parent1.getPatterns().getPatternsDescription().size());
        System.out.println(parent1.getClusters().size());
        System.out.println(parent2.getClusters().size());
        System.out.println(nVertices);
        
        
        int nEdges = 0;    
        
        Map<Integer, List<Integer>> vertices = Maps.newTreeMap();
        int cluster = parent1.getPatterns().getPatternsDescription().size() + 1;
        //std::cout << cluster << std::endl;
        

        for (Cluster it: parent1.getClusters()) {
            for (Integer itP: it.getListPatternNumber()) {
//                std::cout << "cluster: " << cluster << " - pattern: " << (*itP) << std::endl;
            	
            	if (vertices.get(cluster) == null) {
            		List<Integer> values = Lists.newArrayList();
            		vertices.put(cluster, values);
            	}
                vertices.get(cluster).add(itP); 
                
                if (vertices.get(itP) == null) {
                	List<Integer> values = Lists.newArrayList();
                	vertices.put(itP, values);
                }
                vertices.get(itP).add(cluster);
            }    
            nEdges += it.getListPatternNumber().size();
            cluster++;
        }
        
        
        for (Cluster it: parent2.getClusters()) {
            for (Integer itP: it.getListPatternNumber()) {
            	
            	if (vertices.get(cluster) == null) {
            		List<Integer> values = Lists.newArrayList();
            		vertices.put(cluster, values);
            	}
                vertices.get(cluster).add(itP); 
                
                if (vertices.get(itP) == null) {
                	List<Integer> values = Lists.newArrayList();
                	vertices.put(itP, values);
                }
                vertices.get(itP).add(cluster);
            }    
            nEdges += it.getListPatternNumber().size();
            cluster++;
        }
        
        try (BufferedWriter fileWriter =
		         new BufferedWriter(new FileWriter(graphPath));) {
        	
        	 fileWriter.write(nVertices + "\t" + nEdges);
        	 fileWriter.newLine();
        	 System.out.println(nVertices + "\t" + nEdges);
             for(Integer itV: vertices.keySet()) {
             	List<Integer> second = vertices.get(itV);
             	for (int e = 0; e < second.size(); e++ ) {
             		Integer integer = second.get(e);
             		System.out.print(integer + "\t");
             		fileWriter.write(integer + "\t");
             	}
             	fileWriter.newLine();
             	System.out.println();
             }
        } catch (IOException e1) {
			e1.printStackTrace();
		}
        
        
        int nClustersParent1 = parent1.getClusters().size();
        int nClustersParent2 = parent2.getClusters().size();
        int nClustersChild = JMetalRandom.getInstance().nextInt(minK, maxK);
        
        
        File file = new File(graphPath);
        MetisExecutor.executeCommand(file, String.valueOf(nClustersChild));
   
        
        String resultName = graphPath + ".part." +nClustersChild;
        
        
       

//        int clu;
//        for (int id = 0; id <= parent1.getPatterns().getPatternsDescription().size(); id++)
//        //while (!res.eof())
//        {
//            res >> clu;
//            clu += 1; // para os rotulos dos clusters serem >= 1, porque no arquivo comeca em 0
////            std::cout << "id: " << id << " cluster: " << clu << std::endl;
//            if (!res.fail())
//            {           
//
//                tPartition::tClustersIt clustersIt = solutionPartition.clusters.begin();
//                bool found = false;
//                while (!found && (clustersIt != solutionPartition.clusters.end())) 
//                {
//                    if ((*clustersIt).clusterLabel == clu) found = true;
//                    else clustersIt++;
//                }    
//
//               if (found)
//               {
//                   (*clustersIt).insertPattern(id);                                          
//               }
//               else
//               {
//                   cCluster myCluster;
//                   myCluster.clusterLabel = clu;
//                   myCluster.insertPattern(id);
//                   solutionPartition.clusters.insert(solutionPartition.clusters.end(), myCluster);
//               }
//            }
//        }
//        res.close();
//        
       
        
        
        
        
//        for (std::map<int, vector<int> >::iterator itV = vertices.begin();
//        itV != vertices.end(); itV++) {
//       //std::cout << (*itV).first << "\t"; 
//       for (int e = 0; e < (*itV).second.size(); e++)
//       {
//           graph << (*itV).second[e] << "\t";
//           //std::cout << (*itV).second[e] << "\t";
//       }    
//       graph << std::endl;
//       //std::cout << std::endl;
        
        
		
		
		
		return null;
	}

	@Override
	public int getNumberOfParents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
