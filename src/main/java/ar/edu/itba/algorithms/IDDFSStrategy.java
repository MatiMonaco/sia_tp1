package ar.edu.itba.algorithms;

import ar.edu.itba.Board;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private long expandedNodes = 0;
    private Set<StateNode> visited;
    private StateNode endNode;
    private int maxIter;

    public IDDFSStrategy(boolean deadlockCheck, int maxIter,int actionCost) {
        super("IDDFS",deadlockCheck,actionCost);
        this.maxIter = maxIter;
    }

    @Override
    public SearchResult findSolution(Board board)  {

        board.restartLevel();
        visited = new HashSet<>();
        endNode = null;
        StateNode root = new StateNode(' ', board.getPlayer(), board.getBoxes(), null, 0);
        boolean found = iddfs(root, board);

        if(found && endNode != null){

            String solution = getSolutionPath(endNode);
            System.out.println("Solution: " + solution);
            System.out.println("Solution length: " + solution.length());
            System.out.println("Expanded nodes: " + expandedNodes);
            return new SearchResult(name, null,endNode, actionCost,expandedNodes,0, getSolutionPath(endNode));
        }
        System.out.println("NO SOLUTION FOUND");
        return new SearchResult(name, null, null,actionCost, expandedNodes, 0,null);
    }

    public boolean iddfs(StateNode root, Board board){

        for (int depth = 0; depth < maxIter; depth++) {
            System.out.println(depth);
            boolean found = dls(root, depth, board);

            if(found){

                String solution = getSolutionPath(endNode);
                System.out.println("Solution: " + solution);
                System.out.println("Solution length: " + solution.length());
                System.out.println("Expanded nodes: " + expandedNodes);
                return true;
            }
            visited.clear();
        }
        return false;
    }

    boolean dls(StateNode current, int depth, Board board){


        if (board.isCompleted(current.getBoxes())){
            endNode = current;
            return true;
        }

        if (depth == 0)
            return false;

        visited.add(current);
        expandedNodes++;
        StateNode found = null;
        for(StateNode successor : current.getChildren(board)) {

            if(!visited.contains(successor)){
                visited.add(successor);
                if(dls(successor, depth - 1, board)){
                    return true;
                }
                visited.remove(successor);
            }
        }
        return false;
    }
}

