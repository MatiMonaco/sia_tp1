package ar.edu.itba;

import java.util.*;

public class DFSStrategy extends SearchStrategy {

    private Set<StateNode> visited;
    private Stack<StateNode> vertices;
    @Override
    public String findSolution(Board board)  {

        //init
        Set<StateNode> visited;
        Stack<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new Stack<>();

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());

        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        if(board.isCompleted(root.baggs)){
            String solution =getSolutionPath(root);
            System.out.println("Solution: " + solution);

            return solution;
        }
        frontier.push(root);

        while(!frontier.isEmpty()){

            StateNode vertex = frontier.pop();
            visited.add(vertex);
            List<StateNode> successors = vertex.getChildren(board);

            for(StateNode successor : successors){

                if(!visited.contains(successor) && !frontier.contains(successor)){
                    if(board.isCompleted(successor.baggs)){
                        String solution =getSolutionPath(successor);
                        System.out.println("Solution: " + solution);

                        return solution;
                    }

                    frontier.push(successor);

                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return null;


    }
}
