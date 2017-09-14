package edu.lifo.migrated;

import java.util.List;
import java.util.Map;


public class Partition {
    
    String dataset;

    Patterns patterns;   
    List<TCluster> clusters;
    Map<Integer, List<Double>> centroids;
    

    boolean foundCluster(TCluster c, int clu) {
        return c.getClusterLabel() == clu;
    }

    Partition() {

        dataset = "";
        clusters.clear();
        centroids.clear();
    }

    Partition(Patterns pat) {

        dataset = pat.getDataset();
        clusters.clear();
        centroids.clear();
    }

    Partition(Partition part) {

        dataset = part.dataset;
        clusters = part.clusters;
        centroids = part.centroids;

    }
}
