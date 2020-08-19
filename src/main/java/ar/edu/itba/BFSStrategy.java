package ar.edu.itba;

import java.util.*;

public class BFSStrategy extends SearchStrategy {

    private long expandedNodes = 0;

    public BFSStrategy(boolean deadlockCheck) {
        super("BFS",deadlockCheck);
    }


    @Override
    public SearchResult findSolution(Board board)  {

        //init
        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new LinkedList<>();

        Set<Box> set = new HashSet<>(board.getBaggs());

        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        frontier.add(root);

        while(!frontier.isEmpty()){

            StateNode vertex = frontier.poll();
            expandedNodes++;
            visited.add(vertex);
            List<StateNode> successors = vertex.getChildren(board);

            for(StateNode successor : successors){

                if(!visited.contains(successor) && !frontier.contains(successor)){
                    if(board.isCompleted(successor.boxes)){
                        String solution =getSolutionPath(successor);
                        System.out.println("BFS Solution: " + solution);
                        System.out.println("Solution length: "+solution.length());
                        System.out.println("Expanded nodes: " + expandedNodes);
                        return new SearchResult(name,null,successor, expandedNodes,frontier.size(), getSolutionPath(successor));
                    }

                    frontier.add(successor);

                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return new SearchResult(name,null,null, expandedNodes,frontier.size(), null);


    }
}
