package ar.edu.itba;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private long expandedNodes = 0;
    private StateNode root;
    private Set<StateNode> visited;

    @Override
    public SearchResult findSolution(Board board)  {

        board.restartLevel();
        root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null,0);
        visited = new HashSet<>();
        int maxIter = Integer.MAX_VALUE;
        for (int depth = 0; depth < maxIter;) {

            StateNode found = dls(root, depth, board);

            if(found != null){

                String solution = getSolutionPath(found);
                System.out.println("Solution: " + solution);
                System.out.println("Solution length: " + solution.length());
                System.out.println("Expanded nodes: " + expandedNodes);
                return new SearchResult(found, expandedNodes, getSolutionPath(found));
            }
            visited.clear();
            System.out.println(++depth);
        }
        System.out.println("NO SOLUTION FOUND");
        return null;
    }

    StateNode dls(StateNode current, int depth, Board board){


        if (board.isCompleted(current.getBags()))
            return current;

        if (depth == 0)
            return null;

        visited.add(current);
        expandedNodes++;
        StateNode found = null;
        for(StateNode successor : current.getChildren(board)) {

            if(!visited.contains(successor)){
                visited.add(successor);
                found = dls(successor, depth - 1, board);
                visited.remove(successor);
            }


            if (found != null)
                return found;
        }
        return null;
    }
}

