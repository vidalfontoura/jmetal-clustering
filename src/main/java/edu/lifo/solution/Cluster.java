package edu.lifo.solution;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private List<Double> centroidCoordinates;
    private List<Sample> samples;
    private String clusterId;

    public Cluster () {

    }
    public Cluster(List<Sample> samples, String clusterId) {
    	this.clusterId = clusterId;
    	this.samples = samples;
        this.updateCentroid();
    }

    public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
        this.updateCentroid();
	}

	public boolean isEmpty () {
        return (getSamples().isEmpty ()? true: false);
    }

    @Override
    public String toString () {

        return "TODO: to be implemented";
    }
    

    public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

    public List<Double> getCentroidCoordinates() {

        return centroidCoordinates;
    }

    public void setCentroidCoordinates(List<Double> centroidCoordinates) {

        this.centroidCoordinates = centroidCoordinates;
    }

    public void updateCentroid() {
        int dimesion = samples.get(0).getCoordinates().size();
        double[] sumCoordinates = new double[dimesion];

        for (int i = 0; i < samples.size(); i++) {
            Sample sample = samples.get(i);
            List<Double> coordinates = sample.getCoordinates();
            for (int j = 0; j < coordinates.size(); j++) {
                Double coordinate = coordinates.get(j);
                sumCoordinates[j] = sumCoordinates[j] + coordinate;
            }
        }

        centroidCoordinates = new ArrayList<Double>();
        for (int i = 0; i < sumCoordinates.length; i++) {
            double mean = sumCoordinates[i] / samples.size();
            centroidCoordinates.add(mean);
        }

    }

    public void printCluster() {
        System.out.println ();
        System.out.println ("ClusterId:" + clusterId);
        if (samples != null) {
            System.out.println("Points size:" + samples.size());
            for (Sample p : samples) {
                System.out.println (p.toString ());
            }
        }
        System.out.println ();
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clusterId == null) ? 0 : clusterId.hashCode());
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
		if (clusterId == null) {
			if (other.clusterId != null)
				return false;
		} else if (!clusterId.equals(other.clusterId))
			return false;
		if (samples == null) {
			if (other.samples != null)
				return false;
		} else if (!samples.equals(other.samples))
			return false;
		return true;
	}

    public static void main(String[] args) {

        List<Sample> samples = new ArrayList<Sample>();
        Sample sampleX1 = new Sample();
        sampleX1.setCoordinates(Lists.newArrayList(25.0, 20.0, 10.0));
        sampleX1.setPatternLabel("x1");

        Sample sampleX2 = new Sample();
        sampleX2.setCoordinates(Lists.newArrayList(25.0, 40.0, 10.0));
        sampleX2.setPatternLabel("x2");

        Sample sampleX3 = new Sample();
        sampleX3.setCoordinates(Lists.newArrayList(70.0, 20.0, 20.0));
        sampleX3.setPatternLabel("x3");

        samples.add(sampleX1);
        samples.add(sampleX2);
        samples.add(sampleX3);

        Cluster cluster = new Cluster();
        cluster.setSamples(samples);

        System.out.println(cluster.getCentroidCoordinates());

    }

}
