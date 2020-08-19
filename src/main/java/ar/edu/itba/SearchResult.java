package ar.edu.itba;

public class SearchResult {

    private SearchStrategy.StateNode goalNode;
    private long expandedNodes;
    private long frontierNodes;
    private String solution;
    private String algorithm;
    private String heuristicName;

    public SearchResult(String algorithm,String heuristicName,SearchStrategy.StateNode goalNode, long expandedNodes, String solution) {
        this.goalNode = goalNode;
        this.expandedNodes = expandedNodes;
        this.solution = solution;
        this.algorithm = algorithm;
        this.heuristicName = heuristicName;
    }

    public long getFrontierNodes() {
        return frontierNodes;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getHeuristicName() {
        return heuristicName;
    }

    public SearchStrategy.StateNode getGoalNode() {
        return goalNode;
    }

    public long getExpandedNodes() {
        return expandedNodes;
    }

    public String getSolution(){
        return solution;
    }

    public int getLength(){
        return solution.length();
    }
}
