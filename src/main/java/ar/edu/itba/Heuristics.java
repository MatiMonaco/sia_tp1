package ar.edu.itba;

import ar.edu.itba.SearchStrategy.StateNode;



import java.util.*;

public  class Heuristics {


    public static int goalCount(StateNode node,Board board){

        List<Goal> goals = board.getGoals();

        Set<Baggage> baggageSet = node.getBags();

        int goalCount = 0;

        for(Baggage bagg :baggageSet){
            for (Goal goal: goals){
                if(bagg.getX() == goal.getX() && bagg.getY() == goal.getY()){
                    goalCount++;
                    break;
                }
            }
        }

        return goalCount;
    }

    public static int simpleManhattanDistances(StateNode node,Board board){

        List<Goal> goals = board.getGoals();

        Set<Baggage> baggageSet = node.getBags();

        int totalDistance = 0;

        for(Baggage bagg :baggageSet){

            Integer minDistance = null;
            for (Goal goal: goals){
                Integer distance = Math.abs(bagg.getX() - goal.getX()) + Math.abs(bagg.getY() - goal.getY());

                if( minDistance == null || minDistance > distance){
                    minDistance = distance/Board.SPACE;
                }
            }

            totalDistance+=minDistance;
        }
        return totalDistance;
    }

    public static int minimumMatchingLowerBound(StateNode node,Board board){

        List<Matching> matchings = getGoalMatchings(node,board);
        int totalDistance = 0;

        for(Matching matching : matchings){
            if(matching.getDistance() == Integer.MAX_VALUE){
                totalDistance = Integer.MAX_VALUE;
                break;

            }else{
                totalDistance +=matching.getDistance();
            }
        }
        return totalDistance;

    }

    private static List<Matching> getGoalMatchings(StateNode node,Board board){
        List<Goal> goals = board.getGoals();
        Set<Baggage> baggageSet = node.getBags();
        Queue<Matching> priorityQueue = new PriorityQueue<>(11,Comparator.comparingInt(Matching::getDistance));

        for(Goal goal: goals){
            for(Baggage bag : baggageSet){
                Matching matching = new Matching(goal,bag,board.getDistancesToGoal().get(goal).get(new Actor(bag.getX(),bag.getY())));
                priorityQueue.add(matching);
            }
        }
        Set<Baggage> matchedBaggs = new HashSet<>();
        Set<Goal> matchedGoals = new HashSet<>();
        List<Matching> matchings = new ArrayList<>();
        while(!priorityQueue.isEmpty()){
            Matching aux = priorityQueue.poll();
            if(!matchedBaggs.contains(aux.bag) && !matchedGoals.contains(aux.goal)){
                matchings.add(aux);
                matchedBaggs.add(aux.bag);
                matchedGoals.add(aux.goal);

            }
        }
        for(Baggage bag: baggageSet){
            if(!matchedBaggs.contains(bag)){
                Goal closestGoal = getClosestGoal(board,bag);
                matchings.add(new Matching(closestGoal,bag,board.getDistancesToGoal().get(closestGoal).get(new Actor(bag.getX(),bag.getY()))));

            }
        }

        return matchings;
    }

    private static Goal getClosestGoal(Board board,Baggage bagg){
        List<Goal> goals = board.getGoals();
        Integer minDistance = null;
        Actor position = new Actor(bagg.getX(), bagg.getY());
        Goal match = null;
        for(Goal goal :goals){
            int distance = board.getDistancesToGoal().get(goal).get(position);
            if(minDistance == null || minDistance > distance){
                minDistance = distance;
                match = goal;
            }
        }
        return match;
    }

    private static class Matching{
        private Goal goal;
        private Baggage bag;
        private int distance;
        public Matching(Goal goal, Baggage bag,int distance) {
            this.goal = goal;
            this.bag = bag;
            this.distance = distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Matching matching = (Matching) o;
            return Objects.equals(goal, matching.goal) &&
                    Objects.equals(bag, matching.bag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(goal, bag);
        }

        public int getDistance(){
            return distance;
        }
    }
}
