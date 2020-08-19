package ar.edu.itba;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel {

    public static final int OFFSET = 0;
    public static final int SPACE = 20;
    private List<Wall> walls;
    private Set<Baggage> baggs;
    private List<Goal> goals;
    private SearchResult solution;
    private Player player;
    private int w = 0;
    private int h = 0;
    private List<Actor> positions;
    private Map<Goal,Map<Actor,Integer>> distancesToGoal;




    private boolean isCompleted = false;
    private int i = 0;
    private  Timer timer = new Timer(250, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(solution != null && i< solution.getLength()){
                char key = solution.getSolution().charAt(i++);

                movePlayer(key);
                repaint();

            }else{
                ((Timer)e.getSource()).stop();
            }


        }
    });



    public Board() {

        initBoard();
        preCompute();
        try {
            compute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
    private void compute() throws URISyntaxException {
        String algorithm = null;
        JSONParser parser = new JSONParser();
        URL res = getClass().getClassLoader().getResource("parameters.json");
        String  path = Paths.get(res.toURI()).toString();
        try (Reader reader = new FileReader(path)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            algorithm = (String) jsonObject.get("algorithm");
            if(algorithm == null){
                System.out.println("Algorithm can't be null");

            }
            System.out.println(algorithm);
            String heuristic;
            switch(algorithm){
                case "BFS":
                    BFSStrategy bfs = new BFSStrategy();
                    solution =  bfs.findSolution(this);
                    break;

                case "DFS":
                    DFSStrategy dfs = new DFSStrategy();
                    solution =  dfs.findSolution(this);
                    break;

                case "IDDFS":

                    IDDFSStrategy iddfs = new IDDFSStrategy();
                    solution =  iddfs.findSolution(this);
                    break;

                case "IDA*":
                    heuristic = (String) jsonObject.get("heuristic");
                    if(heuristic == null){
                        System.out.println("Heuristic can't be null");

                    }else {
                        BiFunction<SearchStrategy.StateNode, Board, Integer> func;
                        if((func =Heuristics.heuristicsMap.get(heuristic)) == null){
                            System.out.println("Heuristic '+"+heuristic+"' doesn't exists");
                        }else{
                            IDAStarStrategy idaStar = new IDAStarStrategy(func);
                            solution = idaStar.findSolution(this);

                        }
                    }

                    break;

                case "GGS":

                  heuristic = (String) jsonObject.get("heuristic");
                    if(heuristic == null){
                        System.out.println("Heuristic can't be null");

                    }else {
                        BiFunction<SearchStrategy.StateNode, Board, Integer> func;
                        if((func =Heuristics.heuristicsMap.get(heuristic)) == null){
                            System.out.println("Heuristic '+"+heuristic+"' doesn't exists");
                        }else{
                            GGSStrategy ggs = new GGSStrategy(func);
                            solution = ggs.findSolution(this);

                        }
                    }

                    break;

                case "A*":
                    heuristic = (String) jsonObject.get("heuristic");
                    if(heuristic == null){
                        System.out.println("Heuristic can't be null");

                    }else {
                        BiFunction<SearchStrategy.StateNode, Board, Integer> func;
                        if((func =Heuristics.heuristicsMap.get(heuristic)) == null){
                            System.out.println("Heuristic '+"+heuristic+"' doesn't exists");
                        }else{
                            AStarStrategy aStar = new AStarStrategy(func);
                            solution = aStar.findSolution(this);

                        }
                    }

                    break;


            }

            // loop array
//            JSONArray msg = (JSONArray) jsonObject.get("messages");
//            Iterator<String> iterator = msg.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    private void preCompute(){
        computeDistancesToGoal(positions);
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();




    }

    private void computeDistancesToGoal(List<Actor> positions){
        System.out.println("Starting pre-computations...");
        distancesToGoal = new HashMap<>();

        for(Goal goal :goals){
            distancesToGoal.put(goal,new HashMap<>());

            for(Actor pos : positions){
                distancesToGoal.get(goal).put(new Actor(pos.getX(),pos.getY()),Integer.MAX_VALUE);
            }
        }
        int[] dir_x = {-1, 0, 1, 0};
        int[] dir_y = {0, 1, 0, -1};

        for(Goal goal: goals){
            distancesToGoal.get(goal).put(new Actor(goal.getX(),goal.getY()),0);
            Queue<Actor> queue = new LinkedList<>();
            queue.add(new Actor(goal.getX(),goal.getY()));

            while(!queue.isEmpty()){

                Actor position = queue.poll();

                for(int i = 0; i < 4;i++){
                    Actor boxPosition = new Actor(position.getX()+dir_x[i]*Board.SPACE,position.getY() + dir_y[i]*Board.SPACE);
                    Actor playerPosition = new Actor(position.getX()+2*dir_x[i]*Board.SPACE,position.getY() +2*dir_y[i]*Board.SPACE);

                    if(distancesToGoal.get(goal).get(boxPosition) == Integer.MAX_VALUE){
                        boolean isWall = false;
                       for(int j = 0; j < walls.size() && !isWall;j++){
                           Wall wall = walls.get(j);
                            int wallX = wall.getX();
                            int wallY = wall.getY();
                            if((wallX == playerPosition.getX() && wallY == playerPosition.getY())|| (wallX == boxPosition.getX() && wallY == boxPosition.getY())){
                                isWall = true;
                            }
                        }
                       if(!isWall){

                           distancesToGoal.get(goal).put(boxPosition,distancesToGoal.get(goal).get(position)+1);
                           queue.add(boxPosition);
                       }

                    }
                }
            }



        }
        System.out.println("Pre-computations completed.");
        System.out.println(distancesToGoal);
    }

    public Map<Goal, Map<Actor, Integer>>  getDistancesToGoal() {
        return distancesToGoal;
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }


    private String getLevelData(){
        StringBuilder sb = new StringBuilder();
        try {
            URL res = getClass().getClassLoader().getResource("level.txt");
            File myObj = Paths.get(res.toURI()).toFile();

            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                sb.append(data);
                sb.append('\n');
            }
            myReader.close();
        } catch (FileNotFoundException | URISyntaxException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void initWorld() {
       positions = new ArrayList<>();
        walls = new ArrayList<>();
        baggs = new HashSet<>();
        goals = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        Baggage b;
        Goal a;
        String levelData = getLevelData();

        for (int i = 0; i < levelData.length(); i++) {

            char item = levelData.charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.w < x) {
                        this.w = x;
                    }

                    x = OFFSET;
                    break;

                case '#':
                    wall = new Wall(x, y);
                    walls.add(wall);
                    positions.add(new Actor(x,y));
                    x += SPACE;
                    break;

                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b);
                    positions.add(new Actor(x,y));
                    x += SPACE;
                    break;

                case '.':
                    a = new Goal(x, y);
                    goals.add(a);
                    positions.add(new Actor(x,y));
                    x += SPACE;
                    break;

                case '@':
                    player = new Player(x, y);
                    positions.add(new Actor(x,y));
                    x += SPACE;
                    break;

                case ' ':
                    positions.add(new Actor(x,y));
                    x += SPACE;
                    break;

                default:
                    break;
            }

            h = y;
        }
    }




    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int key = e.getKeyCode();
            if (key == KeyEvent.VK_S) {
                if (solution != null) {

                    showSolution();
                }else {
                    System.out.println("No solution found!");
                }
            }

        }
    }

    private void printResult(Graphics g){
        if(solution != null){
            boolean found = solution.getGoalNode() != null;

            g.setColor(found ? Color.black:Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,14));
            g.drawString("Resultados:",w + 5,20);
            g.setFont(new Font("Arial",Font.BOLD,12));
            g.drawString("Nodos expandidos: "+solution.getExpandedNodes(),w + 5,45);
            g.drawString("Nodos frontera al finalizar: "+solution.getExpandedNodes(),w + 5,65);
            g.drawString("Profundidad alcanzada: "+solution.getGoalNode().pathCost,w + 5,85);
            g.drawString(found ? "Solucion encontrada (Pulse 'S' para ver)":"Solucion no encontrada",w + 5,105);



        }
    }


        private void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, w, this.getHeight());
            g.setColor(Color.lightGray);
            g.fillRect(w, 0, this.getWidth()-w, this.getHeight());
        printResult(g);

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(goals);
        world.addAll(baggs);
        world.add(player);

            for (Actor item : world) {

                if (item instanceof Player || item instanceof Baggage) {

                    g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, this);
                } else {

                    g.drawImage(item.getImage(), item.getX(), item.getY(), this);
                }


            }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        buildWorld(g);
    }


      public void showSolution() {

          timer.start();


      }

      private void movePlayer(char direction){
          switch (direction) {

              case 'L':

                  if (checkWallCollision(player,
                          'L')) {
                      return;
                  }

                  if (checkBagCollision('L')) {
                      return;
                  }

                  player.move(-SPACE, 0);

                  break;

              case 'R':

                  if (checkWallCollision(player, 'R')) {
                      return;
                  }

                  if (checkBagCollision('R')) {
                      return;
                  }

                  player.move(SPACE, 0);

                  break;

              case 'T':

                  if (checkWallCollision(player, 'T')) {
                      return;
                  }

                  if (checkBagCollision('T')) {
                      return;
                  }

                  player.move(0, -SPACE);

                  break;

              case 'B':

                  if (checkWallCollision(player, 'B')) {
                      return;
                  }

                  if (checkBagCollision('B')) {
                      return;
                  }

                  player.move(0, SPACE);

                  break;


              default:
                  break;
          }





      }
    private boolean checkWallCollision(Actor actor, char direction) {

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

    private boolean checkBagCollision(char direction) {

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

                                if (checkWallCollision(bag, 'L')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        bag.move(-SPACE,0 );
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

                                if (checkWallCollision(bag, 'R')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        bag.move(SPACE,0 );
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

                                if (checkWallCollision(bag, 'T')) {
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

                                if (checkWallCollision(bag, 'B')) {
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



    public boolean isCompleted(Set<Baggage> baggsSet) {

        int nOfBags = baggsSet.size();
       Baggage[] baggsArray = baggsSet.toArray(new Baggage[0]);
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {
            
            Baggage bag = baggsArray[i];
            
            for (int j = 0; j < nOfBags; j++) {
                
                Goal area =  goals.get(j);
                
                if (bag.getX() == area.getX() && bag.getY() == area.getY()) {
                    
                    finishedBags += 1;
                }
            }
        }

        /* isCompleted = true;
            repaint();*/
        return finishedBags == nOfBags;
    }

    public void restartLevel() {

        goals.clear();
        baggs.clear();
        walls.clear();

        initWorld();

        if (isCompleted) {
            isCompleted = false;
        }
    }

    public Set<Baggage> getBaggs() {
        return baggs;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
