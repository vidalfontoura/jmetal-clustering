package edu.lifo.migrated;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Pair;

public class Patterns {
    
    private String dataset;
    private List<PatternDescription> patternsDescription;
    
    private Map<Integer, Map<Integer, Double>> distanceMatrix;
    
    private Map<Integer, Map<Integer, Double> > correlationMatrix;

    private Map<Integer, List<Integer>> nnList, nnListPearson;
    
    private EuclideanDistance euclideanDistance = new EuclideanDistance();
    
    private PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    
    // class constructor
    public Patterns(){
        dataset = null;
        patternsDescription.clear();
        distanceMatrix.clear();
        nnList.clear();
        correlationMatrix.clear();
        nnListPearson.clear();
    }

    public Patterns(Patterns pat){

        dataset = pat.dataset;
        patternsDescription = pat.patternsDescription;
        distanceMatrix = pat.distanceMatrix;
        nnList = pat.nnList;
        correlationMatrix = pat.correlationMatrix;
        nnListPearson = pat.nnListPearson;

    }
    public Patterns(String ds, String filePatterns) {
        dataset = ds;
        
        patternsDescription = new ArrayList<>();
        
        Map<String, List<Double>> pat = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
        });
       	try (BufferedReader br = Files.newBufferedReader(Paths.get(dataset))) {
       		String line = null;
    		while ((line = br.readLine()) != null) {
    			 if (line.startsWith("ID")) continue;
    			 String[] split = line.split("\\s");
                 List<Double> coordinates = Lists.newArrayList();
                 for (int i = 1; i < split.length; i++) {
                     coordinates.add(Double.valueOf(split[i]));
                 }
                 pat.put(split[0], coordinates);
    		}
        } catch (IOException e) {
            e.printStackTrace();
        }
       	
       	
       	int number = 1;
    	try (BufferedReader br = Files.newBufferedReader(Paths.get(filePatterns))) {
    		String line = null;
    		while ((line = br.readLine()) != null) {
    			PatternDescription pattern = new PatternDescription();
    			
    			String[] split = line.split("\\s");
            	String clu = split[1];
            	String label = split[0];
                
            	
            	pattern.setPatternLabel(label);
            	pattern.setPatternNumber(number);
            	number++;
            	pattern.setPatternClass(Integer.valueOf(clu));
            	
            	pattern.setValues(pat.get(label));
            	patternsDescription.add(pattern);
            	
    		}
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    	 generateDistanceMatrix();
         generatennList();        
         generateCorrelationMatrix();
         generatennListPearson();        
    }
    
   
    public void generateDistanceMatrix() {
        double distance = 0.0;
        distanceMatrix = new TreeMap<>();
        
        for (PatternDescription it1: patternsDescription){        
            for (PatternDescription it2: patternsDescription) {
                distance = euclideanDistance.compute(it1.getValues(), it2.getValues());
             
                if (distanceMatrix.get(it1.getPatternNumber()) == null) {
                	Map<Integer,Double> map = new TreeMap<>();
                	
                	map.put(it2.getPatternNumber(), distance);
                	
                	distanceMatrix.put(it1.getPatternNumber(), map);
                } else {
                	distanceMatrix.get(it1.getPatternNumber()).put(it2.getPatternNumber(), distance);
                }
                
                if (distanceMatrix.get(it2.getPatternNumber()) == null) {
                	Map<Integer,Double> map = new TreeMap<>();
                	
                	map.put(it1.getPatternNumber(), distance);
                	
                	distanceMatrix.put(it2.getPatternNumber(), map);
                } else {
                	distanceMatrix.get(it2.getPatternNumber()).put(it1.getPatternNumber(), distance);
                }
               
            }    
        }    
       
    }
    
    
    public void generatennList(){
    	 
    	nnList = new TreeMap<Integer, List<Integer>>();
    	for (PatternDescription it1: patternsDescription){ 
    		
			List<Pair<Integer,Double>> distances = new ArrayList<Pair<Integer,Double>>();
			Set<Entry<Integer, Double>> entrySet = distanceMatrix.get(it1.getPatternNumber()).entrySet();
			for (Entry<Integer,Double> entry: entrySet) { 
				distances.add(new Pair<>(entry.getKey(), entry.getValue()));
			}
	    	
			Collections.sort(distances,new  Comparator<Pair<Integer,Double>>() {
					@Override
					public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
						return o1.getSecond().compareTo(o2.getSecond());
					}
			});
			
			for (Pair<Integer,Double> pair: distances) {
				if (pair.getFirst() != it1.getPatternNumber()) {
					
					if (nnList.get(it1.getPatternNumber()) == null) {
						List<Integer> insideList = new ArrayList<>();	
						insideList.add(pair.getFirst());
						nnList.put(it1.getPatternNumber(), insideList);
					} else {
						nnList.get(it1.getPatternNumber()).add(pair.getFirst());
					}
					
				}
			}
    		 
    	 }
    }   
    
    public void generatennListPearson() {
    	
    	nnListPearson = new TreeMap<Integer, List<Integer>>();
    	for (PatternDescription it1: patternsDescription){  
    		List<Pair<Integer,Double>> correlations = new ArrayList<Pair<Integer,Double>>();
    		
    		Set<Entry<Integer, Double>> entrySet = correlationMatrix.get(it1.getPatternNumber()).entrySet();
			for (Entry<Integer,Double> entry: entrySet) { 
				correlations.add(new Pair<>(entry.getKey(), entry.getValue()));
			}
			
			Collections.sort(correlations,new  Comparator<Pair<Integer,Double>>() {
				@Override
				public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
					return o1.getSecond().compareTo(o2.getSecond());
				}
			});
			
			for (Pair<Integer,Double> pair: correlations) {
				if (pair.getFirst() != it1.getPatternNumber()) {
					
					if (nnListPearson.get(it1.getPatternNumber()) == null) {
						List<Integer> insideList = new ArrayList<>();	
						insideList.add(pair.getFirst());
						nnListPearson.put(it1.getPatternNumber(), insideList);
					} else {
						nnListPearson.get(it1.getPatternNumber()).add(pair.getFirst());
					}
				}
			}
    	}
    }
    
    public void generateCorrelationMatrix() {
        double correlation = 0.0; 
        correlationMatrix = new TreeMap<>();
        for (PatternDescription it1: patternsDescription){ 
        	for (PatternDescription it2: patternsDescription){ 
        		  correlation = pearsonsCorrelation.correlation(it1.getValues(), it2.getValues());
        		  if (correlationMatrix.get(it1.getPatternNumber()) == null) {
                  	Map<Integer,Double> map = new TreeMap<>();
                  	
                  	map.put(it2.getPatternNumber(), correlation);
                  	
                  	correlationMatrix.put(it1.getPatternNumber(), map);
                  } else {
                	  correlationMatrix.get(it1.getPatternNumber()).put(it2.getPatternNumber(), correlation);
                  }
                  
                  if (correlationMatrix.get(it2.getPatternNumber()) == null) {
                  	Map<Integer,Double> map = new TreeMap<>();
                  	
                  	map.put(it1.getPatternNumber(), correlation);
                  	
                  	correlationMatrix.put(it2.getPatternNumber(), map);
                  } else {
                	correlationMatrix.get(it2.getPatternNumber()).put(it1.getPatternNumber(), correlation);
                  }
        	}
        }
    
    }   
    
    
    public double[] getCoordinatesByPatternLabel(String label) {
    	 for (PatternDescription it1: patternsDescription){ 
    		 if (it1.getPatternLabel().equals(label)) {
    			 return it1.getValues();
    		 }
    	 }
    	 throw new RuntimeException("Label: "+label+" not found in the PatternsDescription while looking for PatternNumber");
    	
    }
    
    public int getPatternNumberByPatternLabel(String label) {
   	 for (PatternDescription it1: patternsDescription){ 
   		 if (it1.getPatternLabel().equals(label)) {
   			 return it1.getPatternNumber();
   		 }
   	 }
   	 throw new RuntimeException("Label: "+label+" not found and PatternsDescription while looking for PatternNumber");
   	
   }
    
    public List<PatternDescription> getPatternsDescription() {
		return patternsDescription;
	}

	public void setPatternsDescription(List<PatternDescription> patternsDescription) {
		this.patternsDescription = patternsDescription;
	}

	public Map<Integer, Map<Integer, Double>> getDistanceMatrix() {
		return distanceMatrix;
	}

	public void setDistanceMatrix(Map<Integer, Map<Integer, Double>> distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public Map<Integer, List<Integer>> getNnList() {
		return nnList;
	}

	public void setNnList(Map<Integer, List<Integer>> nnList) {
		this.nnList = nnList;
	}

	public Map<Integer, List<Integer>> getNnListPearson() {
		return nnListPearson;
	}

	public void setNnListPearson(Map<Integer, List<Integer>> nnListPearson) {
		this.nnListPearson = nnListPearson;
	}

    public String getDataset() {

        return dataset;
    }

    public void setDataset(String dataset) {

        this.dataset = dataset;
    }

    public static void main(String[] args) {
		Patterns pat = new Patterns("/home/lifo/Downloads/iris/iris-dataset.txt", "/home/lifo/Downloads/iris/true partition/iris-truePartition.txt");
		
		
		int size = pat.getPatternsDescription().size();
		
		System.out.println(size);
		
		for (int i=0; i<size; i++) {
		
		System.out.println(pat.getPatternsDescription().get(i).getPatternNumber() + "," + pat.getPatternsDescription().get(i).getPatternLabel() + "," +pat.getPatternsDescription().get(i).getPatternClass()+", "+Arrays.toString(pat.getPatternsDescription().get(i).getValues()));
		}
		
		
		
		
	}
}


