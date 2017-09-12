package edu.lifo.solution;

import java.util.List;

public class Cluster {

    private List<Sample> samples;
    private String clusterId;

    public Cluster () {

    }
    public Cluster(List<Sample> samples, String clusterId) {
    	this.clusterId = clusterId;
    	this.samples = samples;
    }

    public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
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

	public void printCluster () {
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

}
