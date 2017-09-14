package edu.lifo.migrated;

import java.util.List;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class TCluster {

    private List<Integer> patterns;
    private int clusterLabel;

    public TCluster() {

        patterns.clear();
        clusterLabel = 0;
    }

    public TCluster(TCluster clu) {
        patterns = clu.patterns;
        clusterLabel = clu.clusterLabel;
    }

    public int nPatterns() {
        return patterns.size();
    }

    boolean insertPattern(int pat) {
        return patterns.add(pat);
    }

    Integer removePattern(int pat) {

        return patterns.remove(pat);
    }

    int randomPattern() {
        int max, pos;
        max = patterns.size(); // valor maximo para deslocar o iterator e ainda
                               // estar apontando para
        // um elemento dentro do conjunto
        pos = JMetalRandom.getInstance().nextInt(0, max - 1);
        return patterns.get(pos);
    }

    void showCluster(){

        System.out.print("Cluster " + clusterLabel + ": ");
        for (Integer pattern : patterns) {
            System.out.print(pattern + " ");
        }

    }

    public List<Integer> getPatterns() {

        return patterns;
    }

    public void setPatterns(List<Integer> patterns) {

        this.patterns = patterns;
    }

    public int getClusterLabel() {

        return clusterLabel;
    }

    public void setClusterLabel(int clusterLabel) {

        this.clusterLabel = clusterLabel;
    }


}
