package ar.edu.itba;

        import java.util.List;
        import java.util.Stack;
        import java.util.function.BiFunction;

public class IDAStarStrategy extends InformedSearchStrategy {

    private Stack<StateNode> path;
    private StateNode endNode;

    public IDAStarStrategy(BiFunction<StateNode, Board, Integer> heuristic) {
        super(heuristic);
    }


    @Override
    public String findSolution(Board board) {

        board.restartLevel();
        path = new Stack<>();
        endNode = null;
        StateNode root = new StateNode(' ', board.getPlayer(), board.getBaggs(), null, 0);
        boolean found = idaStar(root, board);

        if(found && endNode != null){

            String solution = getSolutionPath(endNode);
            System.out.println("Solution: " + solution);
            return solution;
        }
        return null;
    }

    public boolean idaStar(StateNode root, Board board){

        int threshold = heuristic.apply(root, board);
        path.push(root);

        while (true){
            int temp = search(0, threshold, board);

            if (temp == Integer.MAX_VALUE){
                return false;
            }

            System.out.println("temp no es inf");

            if (temp == -1){
                endNode = path.peek();
                if (board.isCompleted(endNode.getBags())){
                    System.out.println("encontre");
                }
                return true;
            }

            System.out.println("actualice threshold");

            threshold = temp;
        }
    }

//    public int search(StateNode node, int g, int threshold, Board board){
//
//        int f = g + heuristic.apply(node, board);
//
//        if (f > threshold){
//            return f;
//        }
//        if (board.isCompleted(node.getBags())){
//            endNode = node;
//            return -1;
//        }
//
//        int min = Integer.MAX_VALUE;
//        List<StateNode> successors = node.getChildren(board);
//        for(StateNode successor : successors) {
//
//            int temp = search(successor, successor.pathCost, threshold, board);
//
//            if (board.isCompleted(successor.getBags())){
//                endNode = node;
//                return -1;
//            }
//
//            if (temp < min){
//                min = temp;
//                System.out.printf("actualice min: %d\n", min);
//            }
//        }
//
//        return min;
//    }


    public int search(int g, int bound, Board board){

        StateNode current = path.peek();
        int f = g + heuristic.apply(current, board);

        if (f > bound){
            return f;
        }
        if (board.isCompleted(current.getBags())){
            return -1;
        }


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
                    System.out.printf("actualice min: %d\n", min);
                }
                path.pop();
            }
        }

        return min;
    }


//    public StateNode search(int g, int bound, Board board){
//
//        StateNode current = path.peek();
//        int f = g + heuristic.apply(current, board);
//
//        if (board.isCompleted(current.baggs) || f > bound){
//            return current;
//        }
//
//
//        min = -1;
//        StateNode found = null;
//        List<StateNode> successors = current.getChildren(board);
//        for(StateNode successor : successors) {
//
//            if (!path.contains(successor)){
//
//                path.push(successor);
//
//                current = search(current.pathCost, bound, board);
//
//                if (board.isCompleted(current.baggs)){
//                    return current;
//                }
//
//                if (current.pathCost < min){
//                    min = current.pathCost;
//                    System.out.printf("actualice min: %d", min);
//                    found = current;
//                }
//                path.pop();
//            }
//        }
//
//        return found;
//    }
}
