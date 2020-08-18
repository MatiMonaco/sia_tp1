package ar.edu.itba;

import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IDAStarStrategy extends InformedSearchStrategy {

    private Stack<StateNode> path;

    public IDAStarStrategy(BiFunction<StateNode, Board, Integer> heuristic) {
        super(heuristic);
    }


    @Override
    public String findSolution(Board board) {

        board.restartLevel();
        path = new Stack<>();
        StateNode root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null, 0);
        StateNode found = idaStar(root, board);

        if(found != null){

            String solution = getSolutionPath(found);
            System.out.println("Solution: " + solution);
            return solution;
        }
        return null;
    }

    public StateNode idaStar(StateNode root, Board board){

        int bound = heuristic.apply(root, board);
        int f = bound;
        StateNode found;
        path.push(root);

        while (true){
            found = search(0, bound, board);

            if (board.isCompleted(found.baggs)){
                return found;
            }

            bound = heuristic.apply(found, board) + found.pathCost;
        }
    }

    public StateNode search(int g, int bound, Board board){

        StateNode current = path.peek();
        int f = g + heuristic.apply(current, board);

        if (board.isCompleted(current.baggs) || f > bound){
            return current;
        }


        int min = -1;
        StateNode found = null;
        List<StateNode> successors = current.getChildren(board);
        for(StateNode successor : successors) {
            if (!path.contains(successor)){
                path.push(successor);
                current = search(current.pathCost, bound, board);
                if (board.isCompleted(current.baggs)){
                    return current;
                }
                if (current.pathCost < min){
                    min = current.pathCost;
                    found = current;
                }
                path.pop();
            }
        }

        return found;
    }
}
