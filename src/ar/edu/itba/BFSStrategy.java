package ar.edu.itba;

import java.util.*;

public class BFSStrategy extends SearchStrategy {

    private Set<StateNode> visited;
    private Queue<StateNode> vertices;
    @Override
    public void findSolution(Board board) throws CloneNotSupportedException {
        //init
        board.restartLevel();
        visited = new HashSet<>();
        vertices = new LinkedList<>();
        StateNode root = new StateNode(' ',board.getPlayer(),board.getBaggs(),null);
        vertices.add(root);
        int height = 0;
        while(!vertices.isEmpty()){
            StateNode vertex = vertices.poll();
            List<StateNode> successors = vertex.getChildren(board);
            System.out.println("succesors("+height+"): "+successors);
            for(StateNode succesor : successors){
                if(board.isCompleted(succesor.baggs)){
                    String solution =getSolutionPath(succesor);
                    System.out.println("Solution: " + solution);
                }
            }
        }

    }
}
