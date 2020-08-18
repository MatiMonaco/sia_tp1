package ar.edu.itba;


import java.util.function.BiFunction;


public abstract class InformedSearchStrategy extends SearchStrategy {

    protected BiFunction<StateNode,Board,Integer> heuristic;


    public InformedSearchStrategy(BiFunction<StateNode,Board,Integer> heuristic) {
        this.heuristic = heuristic;
    }
    public int getTotalCost(StateNode node,Board board){
        System.out.println("node: "+node);
        int cost = node.pathCost + heuristic.apply(node,board);
        System.out.println("cost: "+cost);
        return cost ;
    }


}
