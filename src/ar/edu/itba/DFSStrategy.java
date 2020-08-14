package ar.edu.itba;

import java.util.*;

public class DFSStrategy extends SearchStrategy {

    private Set<StateNode> visited;
    private Stack<StateNode> vertices;
    @Override
    public String findSolution(Board board)  {
        //init
        board.restartLevel();
        visited = new HashSet<>();
        vertices = new Stack<>();

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        vertices.push(root);
        visited.add(root);
        int height = 0;
        while(!vertices.isEmpty()){
            StateNode vertex = vertices.pop();
            List<StateNode> successors = vertex.getChildren(board);

            for(StateNode successor : successors){
                if(board.isCompleted(successor.baggs)){
                    String solution =getSolutionPath(successor);
                    System.out.println("Solution: " + solution);

                    return solution;
                }

                if(!visited.contains(successor)){

                    vertices.add(successor);
                    visited.add(successor);

                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return null;


    }
}
