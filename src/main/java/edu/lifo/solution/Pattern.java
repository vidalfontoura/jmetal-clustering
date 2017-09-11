package edu.lifo.solution;


import java.util.ArrayList;
import java.util.List;

public class Pattern {

    private String patternLabel;
    private List<Double> coordinates;

    public Pattern() {
    }

    public Pattern(List<Double> coordinates, String patternLabel) {
        setCoordinates(coordinates);
        this.patternLabel = patternLabel;
    }



    public List<Double> getCoordinates() {

        if (coordinates == null) {
            coordinates = new ArrayList<Double>();
        }
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {

        this.coordinates = coordinates;
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
        Pattern other = (Pattern) obj;
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

    public Pattern copy() {

        Pattern point = new Pattern();
        point.coordinates = new ArrayList<Double>(coordinates);
        return point;
    }


}
