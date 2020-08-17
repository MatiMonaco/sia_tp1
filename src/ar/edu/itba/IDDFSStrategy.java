package ar.edu.itba;

import java.util.*;

public class IDDFSStrategy extends SearchStrategy {

    private boolean found = false;
    private boolean remaining = true;
    private StateNode root;
    private Set<StateNode> visited;

    @Override
    public String findSolution(Board board)  {

        board.restartLevel();
        root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null,0);
        visited = new HashSet<>();
        int maxIter = 100;
        for (int depth = 0; depth < maxIter; depth++) {

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

//    StateNode dls(StateNode current, int depth, Board board) throws CloneNotSupportedException {
//        StateNode found = null;
//
//        if (depth == 0){
//
//            if (board.isCompleted(current.getBags()))
//                return current;
//            else
//                return null;
//        }else if (depth > 0){
//
//            boolean anyRemaining = false;
//            List<StateNode> successors = current.getChildren(board);
//
//            for(StateNode successor : successors){
//                if (!successor.checkRepeats()){
//                    found = dls(successor, depth-1, board);
//
//                    if (found!=null)
//                        return found;
//                }
//            }
//        }
//
//        return found;
//    }

    StateNode dls(StateNode current, int depth, Board board){


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

