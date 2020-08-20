package ar.edu.itba.algorithms;

import ar.edu.itba.Board;
import ar.edu.itba.algorithms.SearchStrategy.StateNode;
import ar.edu.itba.entities.Actor;
import ar.edu.itba.entities.Box;
import ar.edu.itba.entities.Goal;


import java.util.*;
import java.util.function.BiFunction;

public class Heuristics {

    public static Map<String, BiFunction<StateNode, Board,Integer>> heuristicsMap = new HashMap<>(){{

        put("MML",Heuristics::minimumMatchingLowerBound);
        put("SMD",Heuristics::simpleManhattanDistances);
        put("goalCount",Heuristics::goalCount);}};


       public static int goalCount(StateNode node,Board board){

        List<Goal> goals = board.getGoals();

        Set<Box> boxSet = node.getBoxes();

        int goalCount = 0;

        for(Box box :boxSet){
            for (Goal goal: goals){
                if(box.getX() == goal.getX() && box.getY() == goal.getY()){
                    goalCount++;
                    break;
                }
            }
        }

        return goalCount;
    }

    public static int simpleManhattanDistances(StateNode node,Board board){

        List<Goal> goals = board.getGoals();

        Set<Box> boxSet = node.getBoxes();

        int totalDistance = 0;

        for(Box box :boxSet){

            Integer minDistance = null;
            for (Goal goal: goals){
                Integer distance = Math.abs(box.getX() - goal.getX()) + Math.abs(box.getY() - goal.getY());

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
        Set<Box> boxSet = node.getBoxes();
        Queue<Matching> priorityQueue = new PriorityQueue<>(11,Comparator.comparingInt(Matching::getDistance));

        for(Goal goal: goals){
            for(Box box : boxSet){
                Matching matching = new Matching(goal,box,board.getDistancesToGoal().get(goal).get(new Actor(box.getX(),box.getY())));
                priorityQueue.add(matching);
            }
        }
        Set<Box> matchedBaggs = new HashSet<>();
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
        for(Box box: boxSet){
            if(!matchedBaggs.contains(box)){
                Goal closestGoal = getClosestGoal(board,box);
                matchings.add(new Matching(closestGoal,box,board.getDistancesToGoal().get(closestGoal).get(new Actor(box.getX(),box.getY()))));

            }
        }

        return matchings;
    }

    private static Goal getClosestGoal(Board board, Box bagg){
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
        private Box bag;
        private int distance;
        public Matching(Goal goal, Box bag, int distance) {
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
