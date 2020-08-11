package ar.edu.itba;

import java.util.*;

public interface SearchStrategy {

    int[] dir_x = {-1, 0, 1, 0};
    int[] dir_y = {0, 1, 0, -1};

    void findSolution(Board board);

    class StateNode implements Cloneable{
        ArrayList<StateNode> children;
        StateNode prev;
        Player player;
        Set<Baggage> baggs;
        String direction;


        StateNode(String direction,Player player,Set<Baggage> baggs,StateNode prev){
            this.direction = direction;
            this.player  = player;
            this.baggs = baggs;
            this.prev = prev;
            children = new ArrayList<>();

        }



        private StateNode checkMove(char direction,Board board) {

            if (checkWallCollision(player, direction, board) || checkBagCollision(direction, board)) {

                return null;
            }
            return this;

        }

        private boolean checkBagCollision(char direction,Board board) {


            switch (direction) {

                case 'L':

                    for (Baggage bag : baggs) {

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
                            baggs.remove(bag);
                            bag.move(-board.SPACE,0 );
                            baggs.add(bag);
                        }
                    }
                        return false;

                        case 'R':

                            for (Baggage bag : baggs) {



                                if (player.isRightCollision(bag)) {

                                    for (Baggage item : baggs) {



                                        if (!bag.equals(item)) {

                                            if (bag.isRightCollision(item)) {
                                                return true;
                                            }
                                        }

                                        if (checkWallCollision(bag, 'R',board)) {
                                            return true;
                                        }
                                    }
                                    baggs.remove(bag);
                                    bag.move(board.SPACE,0 );
                                    baggs.add(bag);
                                }
                            }
                            return false;

                        case 'T':

                            for (Baggage bag : baggs) {



                                if (player.isTopCollision(bag)) {

                                    for (Baggage item : baggs) {



                                        if (!bag.equals(item)) {

                                            if (bag.isTopCollision(item)) {
                                                return true;
                                            }
                                        }

                                        if (checkWallCollision(bag, 'T',board)) {
                                            return true;
                                        }
                                    }
                                    baggs.remove(bag);
                                    bag.move(0,-board.SPACE );
                                    baggs.add(bag);
                                }
                            }

                            return false;

                        case 'B':

                            for (Baggage bag : baggs) {



                                if (player.isBottomCollision(bag)) {

                                    for (Baggage item : baggs) {



                                        if (!bag.equals(item)) {

                                            if (bag.isBottomCollision(item)) {
                                                return true;
                                            }
                                        }

                                        if (checkWallCollision(bag, 'B',board)) {

                                            return true;
                                        }
                                    }

                                    baggs.remove(bag);
                                    bag.move(0, board.SPACE);
                                    baggs.add(bag);

                                }
                            }

                            break;

                        default:
                            break;
                    }

                    return false;
            }


        private boolean checkWallCollision(Actor actor, char direction, Board board) {
            List<Wall> walls = board.getWalls();
            switch (direction) {

                case 'L':

                    for (int i = 0; i < walls.size(); i++) {

                        Wall wall = walls.get(i);

                        if (actor.isLeftCollision(wall)) {

                            return true;
                        }
                    }

                    return false;

                case 'R':

                    for (int i = 0; i < walls.size(); i++) {

                        Wall wall = walls.get(i);

                        if (actor.isRightCollision(wall)) {
                            return true;
                        }
                    }

                    return false;

                case 'T':

                    for (int i = 0; i < walls.size(); i++) {

                        Wall wall = walls.get(i);

                        if (actor.isTopCollision(wall)) {

                            return true;
                        }
                    }

                    return false;

                case 'B':

                    for (int i = 0; i < walls.size(); i++) {

                        Wall wall = walls.get(i);

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

        public String getDirection() {
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
    }
}
