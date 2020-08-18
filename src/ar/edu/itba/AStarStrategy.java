package ar.edu.itba;

import java.util.*;
import java.util.function.BiFunction;

public class AStarStrategy extends InformedSearchStrategy {


    public AStarStrategy(BiFunction<StateNode,Board, Integer> heuristic) {
        super(heuristic);
    }

    @Override
    public String findSolution(Board board){

        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new PriorityQueue<>(5,Comparator.comparingInt(stateNode -> getTotalCost(stateNode,board)));

        Set<Baggage> set = new HashSet<>();
        set.addAll(board.getBaggs());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        frontier.add(root);
        visited.add(root);

        while(!frontier.isEmpty()){

            StateNode vertex = frontier.poll();
            System.out.println("head: "+vertex);
            if(board.isCompleted(vertex.baggs)){
                String solution =getSolutionPath(vertex);
                System.out.println("A* Solution: " + solution);
                System.out.println("Solution length: "+solution.length());
                return solution;
            }
            visited.add(vertex);
            List<StateNode> successors = vertex.getChildren(board);
            for(StateNode successor : successors){
                boolean inFrontier = false;
                if(!visited.contains(successor) && !(inFrontier = frontier.contains(successor))){
                    frontier.add(successor);
                }else if(inFrontier){

                    StateNode aux = null;
                    for(StateNode node : frontier){
                        if(node.equals(successor)){
                            if(getTotalCost(node,board) > getTotalCost(successor,board)){
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
        return null;


    }



}

