package ar.edu.itba;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private boolean found = false;
    private boolean remaining = true;
    private int depth = 0;
    private StateNode root;
//    private Set<StateNode> visited;

    @Override
    public String findSolution(Board board) throws CloneNotSupportedException {

        board.restartLevel();
        root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null);
//        visited = new HashSet<>();

        while(!found || !remaining){

            StateNode found = dls(root, depth, board);

            if(found != null){

                String solution = getSolutionPath(found);
                System.out.println("Solution: " + solution);
                return solution;
            }
            depth++;
            System.out.println(depth);
//            visited.clear();
        }
        System.out.println("NO SOLUTION FOUND");
        return null;
    }

    StateNode dls(StateNode current, int depth, Board board) throws CloneNotSupportedException {
        StateNode found = null;

        if (depth == 0){

            if (board.isCompleted(current.getBags()))
                return current;
            else
                return null;
        }else if (depth > 0){

            boolean anyRemaining = false;
            List<StateNode> successors = current.getChildren(board);

            for(StateNode successor : successors){
                if (!successor.checkRepeats()){
                    found = dls(successor, depth-1, board);

                    if (found!=null)
                        return found;
                }
            }
        }

        return found;
    }
}
