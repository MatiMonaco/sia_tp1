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
            System.out.println("succesors("+height+"): "+successors);
            for(StateNode succesor : successors){
                if(board.isCompleted(succesor.baggs)){
                    String solution =getSolutionPath(succesor);
                    System.out.println("Solution: " + solution);

                    return solution;
                }
                System.out.println("visited: "+ visited);
                if(!visited.contains(succesor)){

                    vertices.add(succesor);
                    visited.add(succesor);
                    System.out.println("agrego a visited: "+ visited);
                }else{
                    System.out.println(succesor+ " es estado repetido");
                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return null;


    }
}
