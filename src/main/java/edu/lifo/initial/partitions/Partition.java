package edu.lifo.initial.partitions;

import java.util.List;

public class Partition {
    
    private List<String> samples;
    private String clusterId;

    public Partition(List<String> samples, String clusterId) {

        this.clusterId = clusterId;
        this.samples = samples;
    }

    public List<String> getSamples() {

        return samples;
    }

    public String getClusterId() {

        return clusterId;
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
        Partition other = (Partition) obj;
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

    @Override
    public String toString() {

        return "Partition [sampleId=" + samples + ", clusterId=" + clusterId + "]";
    }

}
