package ar.edu.itba;


import java.util.*;


public abstract class SearchStrategy {

    int[] dir_x = {-1, 0, 1, 0};
    int[] dir_y = {0, 1, 0, -1};

    public abstract  String findSolution(Board board) throws CloneNotSupportedException;

    public String getSolutionPath(StateNode node){
        StringBuilder sb = new StringBuilder();
        while(node.prev != null){
            StateNode parent = node.prev;
            sb.append(node.direction);
            node = parent;
        }
        return sb.reverse().toString();
    }

    class StateNode implements Cloneable{
        StateNode prev;
        Player player;
        Set<Baggage> baggs;
        char direction;


        StateNode(char direction,Player player,Set<Baggage> baggs,StateNode prev){
            this.direction = direction;
            this.player  = player;
            this.baggs = baggs;
            this.prev = prev;

        }

        public Set<Baggage> getBags() {
            return baggs;
        }

        public  List<StateNode> getChildren(Board board) throws CloneNotSupportedException {
            List<StateNode> children = new ArrayList<>();
            char[] directions = {'L','T','R','B'};

            for (char c : directions) {

                Set<Baggage> set = new HashSet<>();
                baggs.forEach(b -> {
                    set.add(new Baggage(b.getX(), b.getY()));
                });
                StateNode aux = new StateNode(' ', new Player(player.getX(), player.getY()), set, this);

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
            if(checkBagCollision(direction, board)) {

                return null;
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
                    ", baggs=" + baggs +

                    '}';
        }

        private boolean checkBagCollision(char direction, Board board) {

            Iterator<Baggage> it1 = baggs.iterator();
            List<Baggage> toAdd = new ArrayList<>();
            switch (direction) {

                case 'L':


                    while(it1.hasNext()) {
                        Baggage bag = it1.next();
                        if (player.isLeftCollision(bag)) {
                            for (Baggage item : baggs) {
                                {


                                    if (!bag.equals(item)) {

                                        if (bag.isLeftCollision(item)) {
                                            return true;
                                        }
                                    }

                                    if (checkWallCollision(bag, 'L', board)) {
                                        return true;
                                    }
                                }

                            }
                            it1.remove();
                            bag.move(-Board.SPACE,0 );
                            toAdd.add(bag);
                            break;
                        }
                    }
                    baggs.addAll(toAdd);


                        return false;

                        case 'R':


                            while(it1.hasNext()) {
                                Baggage bag = it1.next();
                                if (player.isRightCollision(bag)) {
                                    for (Baggage item : baggs) {
                                        {


                                            if (!bag.equals(item)) {

                                                if (bag.isRightCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(bag, 'R', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    bag.move(Board.SPACE,0 );
                                    toAdd.add(bag);
                                    break;
                                }
                            }
                            baggs.addAll(toAdd);


                            return false;

                        case 'T':

                            while(it1.hasNext()) {
                                Baggage bag = it1.next();
                                if (player.isTopCollision(bag)) {
                                    for (Baggage item : baggs) {
                                        {


                                            if (!bag.equals(item)) {

                                                if (bag.isTopCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(bag, 'T', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    bag.move(0,-Board.SPACE );
                                    toAdd.add(bag);
                                    break;
                                }
                            }
                            baggs.addAll(toAdd);


                            return false;

                        case 'B':


                            while(it1.hasNext()) {
                                Baggage bag = it1.next();
                                if (player.isBottomCollision(bag)) {
                                    for (Baggage item : baggs) {
                                        {


                                            if (!bag.equals(item)) {

                                                if (bag.isBottomCollision(item)) {
                                                    return true;
                                                }
                                            }

                                            if (checkWallCollision(bag, 'B', board)) {
                                                return true;
                                            }
                                        }

                                    }
                                    it1.remove();
                                    bag.move(0,Board.SPACE );
                                    toAdd.add(bag);
                                    break;
                                }
                            }
                            baggs.addAll(toAdd);


                            return false;


                        default:
                            break;
                    }

                    return false;
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



        /*     public ArrayList<StateNode> getChildren(Board board){
            if(children.isEmpty()){
                for(int i = 0; i < directions.length; i++){
                    if(board.canMove(player,baggs,i)){
                        StateNode child = new StateNode(directions[i],player,baggs);
                    }
                }
            }
        }
*/
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
                    baggs.equals(stateNode.baggs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, baggs);
        }

        public boolean checkRepeats() {
            StateNode grandparent = prev.prev;
            while (grandparent!=null){

                if (grandparent.equals(this)){
                    return true;
                }
                grandparent = grandparent.prev;
            }

            return false;
        }
    }
}
