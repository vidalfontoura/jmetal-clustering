package edu.lifo.migrated;


public class Patterns {
    
    File dataset;
    std::vector <TPatternDescription> patternsDescription;
    typedef std::vector <TPatternDescription>::iterator tPatternsDescriptionIt;
    
    std::map<int, std::map<int, double> > distanceMatrix;
    typedef std::map<int, std::map<int, double> >::iterator tDistanceMatrixIt;
    
    std::map<int, std::map<int, double> > correlationMatrix;
    typedef std::map<int, std::map<int, double> >::iterator tcorrelationMatrixIt;

    std::map<int, std::vector<int> > nnList, nnListPearson;
    typedef std::map<int, std::vector<int> >::iterator tnnListIt;
    

}
