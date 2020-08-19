package ar.edu.itba;

public class SearchResult {

    private SearchStrategy.StateNode goalNode;
    private long expandedNodes;
    private String solution;

    public SearchResult(SearchStrategy.StateNode goalNode, long expandedNodes, String solution) {
        this.goalNode = goalNode;
        this.expandedNodes = expandedNodes;
        this.solution = solution;
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
