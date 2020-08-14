package ar.edu.itba;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private boolean found = false;
    private boolean remaining = true;
    private StateNode root;
    private Set<StateNode> visited;

    @Override
    public String findSolution(Board board) {

        board.restartLevel();
        root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null,0);
//        visited = new HashSet<>();

        while(!found || !remaining){

            StateNode found = dls(root, depth, board);

            if(found != null){

                String solution = getSolutionPath(found);
                System.out.println("Solution: " + solution);
                return solution;
            }
            depth++;
            visited.clear();
            System.out.println(depth);
        }
        System.out.println("NO SOLUTION FOUND");
        return null;
    }

    StateNode dls(StateNode current, int depth, Board board)  {
        StateNode found = null;


        if (board.isCompleted(current.getBags()))
            return current;

        if (depth == 0)
            return null;

        visited.add(current);
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

