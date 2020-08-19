package ar.edu.itba;

        import java.util.List;
        import java.util.Stack;
        import java.util.function.BiFunction;

public class IDAStarStrategy extends InformedSearchStrategy {

    private Stack<StateNode> path;
    private StateNode endNode;
    private long expandedNodes = 0;

    public IDAStarStrategy(BiFunction<StateNode, Board, Integer> heuristic) {
        super(heuristic);
    }


    @Override
    public SearchResult findSolution(Board board) {

        board.restartLevel();
        path = new Stack<>();
        endNode = null;
        StateNode root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null, 0);
        boolean found = idaStar(root, board);

        if(found && endNode != null){

            String solution = getSolutionPath(endNode);
            System.out.println("Solution: " + solution);
            System.out.println("Solution length: " + solution.length());
            System.out.println("Expanded nodes: " + expandedNodes);
            return new SearchResult(endNode, expandedNodes, getSolutionPath(endNode));
        }
        System.out.println("NO SOLUTION FOUND");
        return new SearchResult(null, expandedNodes, null);
    }

    public boolean idaStar(StateNode root, Board board){

        int threshold = heuristic.apply(root, board);
        path.push(root);

        while (true){
            int temp = search(0, threshold, board);

            if (temp == Integer.MAX_VALUE){
                return false;
            }

            if (temp == -1){
                endNode = path.peek();
                return true;
            }

            threshold = temp;
        }
    }

    public int search(int g, int bound, Board board){

        StateNode current = path.peek();
        int f = g + heuristic.apply(current, board);

        if (f > bound){
            return f;
        }
        if (board.isCompleted(current.getBags())){
            return -1;
        }


        expandedNodes++;
        int min = Integer.MAX_VALUE;
        List<StateNode> successors = current.getChildren(board);
        for(StateNode successor : successors) {

            if (!path.contains(successor)){

                path.push(successor);

                int temp = search(successor.pathCost, bound, board);

                if (temp == -1){
                    endNode = successor;
                    return -1;
                }

                if (temp < min){
                    min = temp;
                }
                path.pop();
            }
        }

        return min;
    }
}