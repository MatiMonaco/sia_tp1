package ar.edu.itba;

import ar.edu.itba.SearchStrategy.StateNode;



import java.util.List;
import java.util.Set;

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

    public static int simpleGoalDistances(StateNode node,Board board){
        System.out.println("HEU NODE: "+node);
        List<Goal> goals = board.getGoals();

        Set<Baggage> baggageSet = node.getBags();

        int totalDistance = 0;

        for(Baggage bagg :baggageSet){
            System.out.println("bagg: "+bagg);
            Integer minDistance = null;
            for (Goal goal: goals){

                System.out.println("goal: "+goal);

                Integer distance = Math.abs(bagg.getX() - goal.getX()) + Math.abs(bagg.getY() - goal.getY());

                if( minDistance == null || minDistance > distance){
                    minDistance = distance;
                }

            }
            System.out.println("minDistance: "+minDistance);
            totalDistance+=minDistance;
        }
        System.out.println("totalDistance: "+totalDistance);
        return totalDistance;
    }
}