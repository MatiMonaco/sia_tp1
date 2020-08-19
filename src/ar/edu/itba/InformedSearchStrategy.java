package ar.edu.itba;


import java.util.function.BiFunction;


public abstract class InformedSearchStrategy extends SearchStrategy {

    protected BiFunction<StateNode,Board,Integer> heuristic;


    public InformedSearchStrategy(BiFunction<StateNode,Board,Integer> heuristic) {
        this.heuristic = heuristic;
    }
    public int getTotalCost(StateNode node,Board board){

        int cost = node.pathCost + heuristic.apply(node,board);

        return cost ;
    }


}
