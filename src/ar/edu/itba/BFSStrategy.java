package ar.edu.itba;

import java.util.*;

public class BFSStrategy extends SearchStrategy {

    private Set<StateNode> visited;
    private Queue<StateNode> vertices;
    @Override
    public String findSolution(Board board) throws CloneNotSupportedException {
        //init
        board.restartLevel();
        visited = new HashSet<>();
        vertices = new LinkedList<>();

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null);
        vertices.add(root);
        visited.add(root);
        int height = 0;
        while(!vertices.isEmpty()){
            StateNode vertex = vertices.poll();
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
