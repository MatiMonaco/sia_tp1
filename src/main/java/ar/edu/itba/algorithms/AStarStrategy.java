package ar.edu.itba.algorithms;

import ar.edu.itba.Board;
import ar.edu.itba.entities.Box;
import ar.edu.itba.entities.Player;

import java.util.*;
import java.util.function.BiFunction;

public class AStarStrategy extends InformedSearchStrategy {



    public AStarStrategy(String heuristicName,BiFunction<StateNode, Board, Integer> heuristic,int actionCost) {
        super("A*",heuristicName,heuristic,true,actionCost);
    }

    @Override
    public SearchResult findSolution(Board board){

        Set<StateNode> visited;
        Queue<StateNode> frontier;
        board.restartLevel();
        visited = new HashSet<>();
        frontier = new PriorityQueue<>(5, (o1, o2) -> {
            int h1 = heuristic.apply(o1,board);
            int h2 = heuristic.apply(o2,board);
            int total1 = o1.getPathCost() + h1;
            int total2 = o2.getPathCost() + h2;

            if(total1 > total2){
                return 1;
            }else if(total2 > total1){
                return -1;
            }else {
                if(h1 > h2){
                    return 1;
                }else if(h2>h1){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        Set<Box> set = new HashSet<>(board.getBoxes());
        StateNode root = new StateNode(' ',new Player(board.getPlayer().getX(),board.getPlayer().getY()),set,null,0);
        frontier.add(root);
        visited.add(root);

        while(!frontier.isEmpty()){

            StateNode vertex = frontier.poll();

            if(board.isCompleted(vertex.boxes)){
                String solution =getSolutionPath(vertex);
                System.out.println("A* Solution: " + solution);
                System.out.println("Solution length: "+solution.length());
                System.out.println("Expanded nodes: " + expandedNodes);
                System.out.println("Frontier nodes: " + frontier.size());
                return new SearchResult(name,heuristicName,vertex, actionCost,expandedNodes,frontier.size(), getSolutionPath(vertex));
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
        return  new SearchResult(name,heuristicName,null,actionCost, expandedNodes,frontier.size(), null);


    }



}

