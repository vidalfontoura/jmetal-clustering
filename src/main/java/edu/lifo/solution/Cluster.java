package edu.lifo.solution;


import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import edu.lifo.migrated.Patterns;

public class Cluster {

    private List<Double> centroidCoordinates;
    private int clusterId;
    private List<Integer> listPatternNumber;
    
    private Patterns patterns;


    public Cluster(List<Integer> listPatternNumber, int clusterId, Patterns patterns) {
    	this.clusterId = clusterId;
    	this.listPatternNumber = listPatternNumber;
    	 this.patterns = patterns;
        this.updateCentroid();
       
    }
    
    public Cluster(Patterns patterns) {
    	this.patterns = patterns;
    }
    

    @Override
    public String toString () {

        return "TODO: to be implemented";
    }
    

    public int getClusterId() {
		return clusterId;
	}

    public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

    public List<Double> getCentroidCoordinates() {

        return centroidCoordinates;
    }

    public void setCentroidCoordinates(List<Double> centroidCoordinates) {

        this.centroidCoordinates = centroidCoordinates;
    }
    
    public List<Integer> getListPatternNumber() {
    	if (listPatternNumber == null) {
    		List<Integer> list = Lists.newArrayList();
    		this.listPatternNumber = list;
    	}
		return listPatternNumber;
	}
    
 

    public void updateCentroid() {
        
        int dimension = this.patterns.getDatasetDimension();
        double[] sumCoordinates = new double[dimension];

        for(Integer patternNumber: listPatternNumber) {
        	double[] coordinates = this.patterns.getCoordinatesByPatternNumber(patternNumber);
        	for (int j = 0; j < coordinates.length; j++) {
                double coordinate = coordinates[j];
                sumCoordinates[j] = sumCoordinates[j] + coordinate;
            }
        }
        
        centroidCoordinates = new ArrayList<Double>();
        for (int i = 0; i < sumCoordinates.length; i++) {
            double mean = sumCoordinates[i] / listPatternNumber.size();
            centroidCoordinates.add(mean);
        }
        
        
//        
//        for (int i = 0; i < samples.size(); i++) {
//            Sample sample = samples.get(i);
//            double[] coordinates = sample.getCoordinates();
//            for (int j = 0; j < coordinates.length; j++) {
//                double coordinate = coordinates[j];
//                sumCoordinates[j] = sumCoordinates[j] + coordinate;
//            }
//        }
//
//        centroidCoordinates = new ArrayList<Double>();
//        for (int i = 0; i < sumCoordinates.length; i++) {
//            double mean = sumCoordinates[i] / samples.size();
//            centroidCoordinates.add(mean);
//        }

    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((centroidCoordinates == null) ? 0 : centroidCoordinates.hashCode());
        result = prime * result + clusterId;
        result = prime * result + ((listPatternNumber == null) ? 0 : listPatternNumber.hashCode());
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
        if (centroidCoordinates == null) {
            if (other.centroidCoordinates != null)
                return false;
        } else if (!centroidCoordinates.equals(other.centroidCoordinates))
            return false;
        if (clusterId != other.clusterId)
            return false;
        if (listPatternNumber == null) {
            if (other.listPatternNumber != null)
                return false;
        } else if (!listPatternNumber.equals(other.listPatternNumber))
            return false;
        return true;
    }

    public static void main(String[] args) {



    }

}
