package ar.edu.itba;

import java.util.*;

public class BFSStrategy implements SearchStrategy {

    private Set<StateNode> visited;
    private Queue<StateNode> vertices;
    @Override
    public void findSolution(Board board) {
        //init
        board.restartLevel();
        visited = new HashSet<>();
        vertices = new LinkedList<>();
        StateNode root = new StateNode(null,board.getPlayer(),new HashSet<>(
                board.getBaggs()
        ),null);


    }
}
