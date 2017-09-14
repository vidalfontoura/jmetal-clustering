package edu.lifo.solution;


public class Sample {
	
    private int patternId;
	private String patternLabel;
    private double[] coordinates;

    public Sample(double[] coordinates, String patternLabel, int patternId) {
        this.coordinates = coordinates;
        this.patternLabel = patternLabel;
        this.patternId = patternId;
    }

    
    public int getPatternId() {

        return patternId;
    }

    public String getPatternLabel() {
		return patternLabel;
	}

    public double[] getCoordinates() {
		return coordinates;
	}

    @Override
    public String toString() {

        return patternLabel + "," + coordinates.toString();
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((patternLabel == null) ? 0 : patternLabel.hashCode());
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
        Sample other = (Sample) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (patternLabel == null) {
            if (other.patternLabel != null)
                return false;
        } else if (!patternLabel.equals(other.patternLabel))
            return false;
        return true;
    }



}
