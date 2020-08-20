package ar.edu.itba;


import java.util.*;


public abstract class SearchStrategy {

    int[] dir_x = {-1, 0, 1, 0};
    int[] dir_y = {0, 1, 0, -1};
    String name;
    boolean deadlockCheck;
    public SearchStrategy(String name,boolean deadlockCheck) {
        this.name = name;
        this.deadlockCheck = deadlockCheck;
    }

    public String getName() {
        return name;
    }

    public Set<Box> deadlockedBoxes = new HashSet<>();

    public abstract  SearchResult findSolution(Board board) ;

    public String getSolutionPath(StateNode node){
        StringBuilder sb = new StringBuilder();
        while(node.prev != null){
            StateNode parent = node.prev;
            sb.append(node.direction);
            node = parent;
        }
        return sb.reverse().toString();
    }


    class StateNode {
        StateNode prev;
        Player player;
        Set<Box> boxes;
        char direction;
        int pathCost;

        StateNode(char direction, Player player, Set<Box> boxes, StateNode prev, int pathCost){
            this.direction = direction;
            this.player  = player;
            this.boxes = boxes;
            this.prev = prev;
            this.pathCost = pathCost;
        }

        public Set<Box> getBoxes() {
            return boxes;
        }


        public int getPathCost() {
            return pathCost;
        }

        public  List<StateNode> getChildren(Board board) {

            char[] directions = {'L','T','R','B'};
            List<StateNode> children = new ArrayList<>();

                for (char c : directions) {

                    Set<Box> set = new HashSet<>();
                    boxes.forEach(b -> set.add(new Box(b.getX(), b.getY())));
                    StateNode aux = new StateNode(' ', new Player(player.getX(), player.getY()), set, this,pathCost+1);

                    aux = aux.checkMove(c, board);

                    if (aux != null) {
                        aux.prev = this;
                        children.add(aux);
                    }

                }


            return children;
        }

        private StateNode checkMove(char direction,Board board) {

            if (checkWallCollision(player, direction, board)) {

                return null;
            }
            if(checkBoxCollision(direction, board)) {

                return null;
            }
            if(deadlockCheck){
                for (Box box: boxes) {
                    if (deadlockedBoxes.contains(box)) {

                        return null;
                    }
                }
            }

            switch(direction){
                case 'L':
                    player.move(-Board.SPACE,0);
                    break;
                case 'T':
                    player.move(0,-Board.SPACE);
                    break;
                case 'R':
                    player.move(Board.SPACE,0);
                    break;
                case 'B':
                    player.move(0,Board.SPACE);
                    break;
            }
            this.direction = direction;
            return this;

        }

        @Override
        public String toString() {
            return "StateNode{" +
                    " direction='" + direction + '\'' +
                    ", player=" + player +
                    ", baggs=" + boxes +

                    '}';
        }

        private boolean checkBoxCollision(char direction, Board board) {

            Iterator<Box> it1 = boxes.iterator();
            Box toAdd = null;
            switch (direction) {

                case 'L':


                    while(it1.hasNext()) {
                        Box box = it1.next();
                        if (player.isLeftCollision(box)) {
                            if (checkWallCollision(box, 'L', board)) {
                                return true;
                            }
                            for (Box item : boxes) {
                                {


                                    if (!box.equals(item)) {

                                        if (box.isLeftCollision(item)) {
                                            return true;
                                        }
                                    }


                                }

                            }
                            it1.remove();
                            box.move(-Board.SPACE,0 );
                            if(deadlockCheck){
                                checkDeadLock(direction, box, board);
                            }

                            toAdd = box;
                            break;
                        }
                    }
                    if(toAdd != null){
                        boxes.add(toAdd);
                    }



                        return false;

                        case 'R':


                            while(it1.hasNext()) {
                                Box box = it1.next();
                                if (player.isRightCollision(box)) {
                                    for (Box item : boxes) {
                                        {


                                            if (!box.equals(item)) {

                                                if (box.isRightCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(box, 'R', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    box.move(Board.SPACE,0 );
                                    if(deadlockCheck){
                                        checkDeadLock(direction, box, board);
                                    }
                                    toAdd = box;
                                    break;
                                }
                            }
                            if(toAdd != null){
                                boxes.add(toAdd);
                            }

                            return false;

                        case 'T':

                            while(it1.hasNext()) {
                                Box box = it1.next();
                                if (player.isTopCollision(box)) {
                                    for (Box item : boxes) {
                                        {


                                            if (!box.equals(item)) {

                                                if (box.isTopCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(box, 'T', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    box.move(0,-Board.SPACE );
                                    if(deadlockCheck){
                                        checkDeadLock(direction, box, board);
                                    }
                                    toAdd = box;
                                    break;
                                }
                            }
                            if(toAdd != null){
                                boxes.add(toAdd);
                            }


                            return false;

                        case 'B':


                            while(it1.hasNext()) {
                                Box box = it1.next();
                                if (player.isBottomCollision(box)) {
                                    for (Box item : boxes) {
                                        {


                                            if (!box.equals(item)) {

                                                if (box.isBottomCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(box, 'B', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    box.move(0,Board.SPACE );
                                    if(deadlockCheck){
                                        checkDeadLock(direction, box, board);
                                    }
                                    toAdd = box;
                                    break;
                                }
                            }
                            if(toAdd != null){
                                boxes.add(toAdd);
                            }

                            return false;


                        default:
                            break;
                    }

                    return false;
            }

        private boolean checkDeadLock(char direction, Box box, Board board) {
            boolean deadlocked = false;

            switch (direction){
                case 'L':
                    if ( (checkWallCollision(box, 'L', board) && checkWallCollision(box, 'T', board))
                            || (checkWallCollision(box, 'L', board) && checkWallCollision(box, 'B', board))){
                        deadlocked = !box.isInGoal(board.getGoals());
                        if (deadlocked){
                            deadlockedBoxes.add(box);
                        }
                    }
                    break;
                case 'R':
                    if ( (checkWallCollision(box, 'R', board) && checkWallCollision(box, 'T', board))
                            || (checkWallCollision(box, 'R', board) && checkWallCollision(box, 'B', board))){

                        deadlocked = !box.isInGoal(board.getGoals());
                        if (deadlocked){
                            deadlockedBoxes.add(box);
                        }
                    }
                    break;
                case 'T':
                    if ( (checkWallCollision(box, 'T', board) && checkWallCollision(box, 'L', board))
                            || (checkWallCollision(box, 'T', board) && checkWallCollision(box, 'R', board))){

                        deadlocked = !box.isInGoal(board.getGoals());
                        if (deadlocked)
                            deadlockedBoxes.add(box);
                    }
                    break;
                case 'B':
                    if ( (checkWallCollision(box, 'B', board) && checkWallCollision(box, 'L', board))
                            || (checkWallCollision(box, 'B', board) && checkWallCollision(box, 'R', board))){

                        deadlocked = !box.isInGoal(board.getGoals());
                        if (deadlocked)
                            deadlockedBoxes.add(box);
                    }
                    break;
            }
            return deadlocked;
        }


        private boolean checkWallCollision(Actor actor, char direction, Board board) {
            List<Wall> walls = board.getWalls();
            switch (direction) {

                case 'L':

                    for (Wall wall : walls) {

                        if (actor.isLeftCollision(wall)) {

                            return true;
                        }
                    }

                    return false;

                case 'R':

                    for (Wall wall : walls) {

                        if (actor.isRightCollision(wall)) {
                            return true;
                        }
                    }

                    return false;

                case 'T':

                    for (Wall wall : walls) {

                        if (actor.isTopCollision(wall)) {

                            return true;
                        }
                    }

                    return false;

                case 'B':

                    for (Wall wall : walls) {

                        if (actor.isBottomCollision(wall)) {

                            return true;
                        }
                    }

                    return false;

                default:
                    break;
            }

            return false;
        }


        public StateNode getPrev() {
            return prev;
        }

        public char getDirection() {
            return direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateNode stateNode = (StateNode) o;

            return player.equals(stateNode.player) &&
                    boxes.equals(stateNode.boxes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, boxes);
        }

    }
}
