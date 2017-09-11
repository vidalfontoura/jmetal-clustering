package edu.lifo.solution;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private Pattern centroid;
    private List<Pattern> samples;

    public Cluster () {

    }

    public Cluster(List<Pattern> coordinates) {

    }

    public Pattern getCentroid () {
        if (centroid == null) {
            centroid = new Pattern ();
        }
        return centroid;
    }

    public void setCentroid (Pattern point) {
        this.centroid = point.copy ();
    }

    public List <Pattern> getPoints () {

        if (samples == null) {
            samples = new ArrayList<Pattern>();
        }
        return samples;
    }

    public void setPoints(List<Pattern> sample) {

        this.samples = sample;
    }

    public boolean isEmpty () {
        return (getPoints (). isEmpty ()? true: false);
    }

    @Override
    public String toString () {

        return "TODO: to be implemented";
    }

    public void printCluster () {
        System.out.println ();
        System.out.println ("Centroid:" + centroid.toString ());
        if (samples != null) {
            System.out.println("Points size:" + samples.size());
            for (Pattern p : samples) {
                System.out.println (p.toString ());
            }
        }
        System.out.println ();
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((centroid == null) ? 0 : centroid.hashCode());
        result = prime * result + ((samples == null) ? 0 : samples.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cluster other = (Cluster) obj;
        if (centroid == null) {
            if (other.centroid != null)
                return false;
        } else if (!centroid.equals(other.centroid))
            return false;
        if (samples == null) {
            if (other.samples != null)
                return false;
        } else if (!samples.equals(other.samples))
            return false;
        return true;
    }

}
