package ar.edu.itba;

import java.util.*;
import java.util.function.BiFunction;

public class GGSStrategy extends InformedSearchStrategy {

    private long expandedNodes = 0;

    public GGSStrategy(String heuristicName,BiFunction<StateNode,Board, Integer> heuristic) {
        super("GGS",heuristicName,heuristic);
    }

    @Override
    public SearchResult findSolution(Board board){

        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new PriorityQueue<>(5, Comparator.comparingInt(stateNode -> heuristic.apply(stateNode,board)));

        Set<Baggage> set = new HashSet<>(board.getBaggs());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        frontier.add(root);
        visited.add(root);

        while(!frontier.isEmpty()){
            StateNode vertex = frontier.poll();

            if(board.isCompleted(vertex.baggs)){
                String solution =getSolutionPath(vertex);
                System.out.println("GGS Solution: " + solution);
                System.out.println("Solution length: " + solution.length());
                System.out.println("Expanded nodes: " + expandedNodes);

                return new SearchResult(name,heuristicName,vertex, expandedNodes, getSolutionPath(vertex));
            }
            visited.add(vertex);
            expandedNodes++;
            List<StateNode> successors = vertex.getChildren(board);
            for(StateNode successor : successors){
                boolean inFrontier = false;
                if(!visited.contains(successor) && !(inFrontier = frontier.contains(successor))){
                    frontier.add(successor);
                }else if(inFrontier){

                    StateNode aux = null;
                    for(StateNode node : frontier){
                        if(node.equals(successor)){
                            if(heuristic.apply(node,board) > heuristic.apply(successor,board)){
                                aux = node;

                                break;
                            }
                        }
                    }
                    if(aux!= null){

                        frontier.remove(aux);
                        frontier.add(successor);
                    }

                }
            }
        }
        System.out.println("NO SOLUTION FOUND");
        return new SearchResult(name,heuristicName,null, expandedNodes, null);


    }
}
