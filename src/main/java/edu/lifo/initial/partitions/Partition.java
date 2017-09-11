package edu.lifo.initial.partitions;


public class Partition {
    
    private String sampleId;
    private String clusterId;

    public Partition(String sampleId, String clusterId) {

        this.clusterId = clusterId;
        this.sampleId = sampleId;
    }

    public String getSampleId() {

        return sampleId;
    }

    public String getClusterId() {

        return clusterId;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((clusterId == null) ? 0 : clusterId.hashCode());
        result = prime * result + ((sampleId == null) ? 0 : sampleId.hashCode());
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
        if (sampleId == null) {
            if (other.sampleId != null)
                return false;
        } else if (!sampleId.equals(other.sampleId))
            return false;
        return true;
    }

    @Override
    public String toString() {

        return "Partition [sampleId=" + sampleId + ", clusterId=" + clusterId + "]";
    }

}
