package ar.edu.itba.algorithms;

import ar.edu.itba.algorithms.SearchStrategy;

public class SearchResult {

    private SearchStrategy.StateNode goalNode;
    private long expandedNodes;
    private long frontierNodes;
    private String solution;
    private String algorithm;
    private String heuristicName;
    private int actionCost;

    public SearchResult(String algorithm,String heuristicName,SearchStrategy.StateNode goalNode,int actionCost, long expandedNodes,long frontierNodes, String solution) {
        this.goalNode = goalNode;
        this.expandedNodes = expandedNodes;
        this.solution = solution;
        this.algorithm = algorithm;
        this.heuristicName = heuristicName;
        this.frontierNodes = frontierNodes;
        this.actionCost = actionCost;
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
    public long getTotalCost(){
        return actionCost * goalNode.getPathCost();
    }
}
