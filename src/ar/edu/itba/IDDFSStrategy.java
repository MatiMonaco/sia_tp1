package ar.edu.itba;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private long expandedNodes = 0;
    private Set<StateNode> visited;
    private StateNode endNode;

    @Override
    public SearchResult findSolution(Board board)  {

        board.restartLevel();
        visited = new HashSet<>();
        endNode = null;
        StateNode root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null, 0);
        boolean found = iddfs(root, board);

        if(found && endNode != null){

            String solution = getSolutionPath(endNode);
            System.out.println("Solution: " + solution);
            System.out.println("Solution length: " + solution.length());
            System.out.println("Expanded nodes: " + expandedNodes);
            return new SearchResult(endNode, expandedNodes, getSolutionPath(endNode));
        }
        System.out.println("NO SOLUTION FOUND");
        return null;
    }

    public boolean iddfs(StateNode root, Board board){

        int maxIter = Integer.MAX_VALUE;
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


        if (board.isCompleted(current.getBags())){
            endNode = current;
            return true;
        }

        if (depth == 0)
            return false;

        visited.add(current);
        expandedNodes++;
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

