package edu.lifo.migrated;

import java.util.List;

public class PatternDescription {

    private int patternNumber;
    private String patternLabel;
    private int patternClass;
    private double[] values;

    public PatternDescription(PatternDescription patternDescription) {
        this.patternClass = patternDescription.getPatternClass();
        this.patternLabel = patternDescription.getPatternLabel();
        this.patternNumber = patternDescription.getPatternNumber();
        this.values = patternDescription.getValues();
    }

    public PatternDescription() {

        this.patternNumber = 0;
        this.patternLabel = "";
        this.patternClass = -1;
        this.values = new double[0];
    }


    public int getPatternNumber() {

        return patternNumber;
    }

    public void setPatternNumber(int patternNumber) {

        this.patternNumber = patternNumber;
    }

    public String getPatternLabel() {

        return patternLabel;
    }

    public void setPatternLabel(String patternLabel) {

        this.patternLabel = patternLabel;
    }

    public int getPatternClass() {

        return patternClass;
    }

    public void setPatternClass(int patternClass) {

        this.patternClass = patternClass;
    }

    public double[] getValues() {

        return values;
    }

    public void setValues(double[] values) {

        this.values = values;
    }
    
    public void setValues(List<Double> values) {
    	this.values = values.stream().mapToDouble(Double::doubleValue).toArray();
    }

}
