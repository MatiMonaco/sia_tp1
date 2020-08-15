package ar.edu.itba;

import java.util.*;
import java.util.function.Function;

public class GGSStrategy extends InformedSearchStrategy {
    public GGSStrategy(Function<StateNode, Integer> heuristic) {
        super(heuristic);
    }

    @Override
    public String findSolution(Board board){

        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new PriorityQueue<>(5, Comparator.comparingInt(informedStateNode -> heuristic.apply(informedStateNode)));

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        frontier.add(root);
        visited.add(root);

        while(!frontier.isEmpty()){
            StateNode vertex = frontier.poll();

            if(board.isCompleted(vertex.baggs)){
                String solution =getSolutionPath(vertex);
                System.out.println("GGS Solution: " + solution);

                return solution;
            }
            visited.add(vertex);
            List<StateNode> successors = vertex.getChildren(board);
            for(StateNode successor : successors){

                if(!visited.contains(successor) && !frontier.contains(successor)){

                    frontier.add(successor);

                }else if(getTotalCost(successor) > getTotalCost(frontier.peek())){
                    frontier.poll();
                    frontier.add(successor);
                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return null;


    }
}
