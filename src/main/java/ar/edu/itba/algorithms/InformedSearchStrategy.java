package ar.edu.itba.algorithms;


import ar.edu.itba.Board;

import java.util.function.BiFunction;


public abstract class InformedSearchStrategy extends SearchStrategy {

    protected BiFunction<StateNode, Board,Integer> heuristic;
    protected String heuristicName;

    public InformedSearchStrategy(String name,String heuristicName,BiFunction<StateNode,Board,Integer> heuristic,boolean deadlockCheck,int actionCost) {
        super(name,deadlockCheck,actionCost);
        this.heuristic = heuristic;
        this.heuristicName = heuristicName;
    }
    public int getTotalCost(StateNode node,Board board){

        int cost = node.getPathCost() * actionCost + heuristic.apply(node,board);

        return cost ;
    }


}
