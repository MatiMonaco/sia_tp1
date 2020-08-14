package ar.edu.itba;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.function.Function;

public abstract class InformedSearchStrategy extends SearchStrategy {

    protected  Function<StateNode,Integer> heuristic;


    public InformedSearchStrategy(Function<StateNode, Integer> heuristic) {
        this.heuristic = heuristic;
    }
    public int getTotalCost(StateNode node){
        return node.pathCost + heuristic.apply(node);
    }


}
