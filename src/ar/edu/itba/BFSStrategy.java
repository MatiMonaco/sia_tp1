package ar.edu.itba;

import java.util.*;

public class BFSStrategy extends SearchStrategy {


    @Override
    public String findSolution(Board board)  {

        //init
        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new LinkedList<>();

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());

        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        if(board.isCompleted(root.baggs)){
            String solution =getSolutionPath(root);
            System.out.println("BFS Solution: " + solution);
            System.out.println("Solution length: "+solution.length());
            return solution;
        }
        frontier.add(root);

        while(!frontier.isEmpty()){

            StateNode vertex = frontier.poll();
            visited.add(vertex);
            List<StateNode> successors = vertex.getChildren(board);

            for(StateNode successor : successors){

                if(!visited.contains(successor) && !frontier.contains(successor)){
                    if(board.isCompleted(successor.baggs)){
                        String solution =getSolutionPath(successor);
                        System.out.println("BFS Solution: " + solution);
                        System.out.println("Solution length: "+solution.length());

                        return solution;
                    }

                    frontier.add(successor);

                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return null;


    }
}
