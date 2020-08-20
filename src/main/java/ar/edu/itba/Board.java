package ar.edu.itba;

import ar.edu.itba.algorithms.*;
import ar.edu.itba.entities.*;
import ar.edu.itba.entities.Box;
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
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel {

    public static final int OFFSET = 0;
    public static final int SPACE = 20;
    private List<Wall> walls;
    private Set<ar.edu.itba.entities.Box> boxes;
    private List<Goal> goals;
    private SearchResult solution;
    private Player player;
    private int w = 0;
    private int h = 0;
    private List<Actor> positions;
    private Map<Goal,Map<Actor,Integer>> distancesToGoal;
    private int i = 0;
    private Instant start, end;
    private Duration timeElapsed;
    private  Timer timer = new Timer(500, new ActionListener() {
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
        String algorithm;
        JSONParser parser = new JSONParser();
       String path = "./config.json";

        try (Reader reader = new FileReader(path)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            algorithm = (String) jsonObject.get("algorithm");
            if(algorithm == null){
                System.out.println("Se debe especificar el algoritmo a utilizar ['BFS','DFS','IDDFS','A*','GGS','IDA*']");

            }

            String heuristic;
            String deadlockCheck;

            switch(algorithm){
                case "BFS":
                    deadlockCheck = (String) jsonObject.get("deadlockCheck");

                    BFSStrategy bfs = new BFSStrategy(deadlockCheck != null && !deadlockCheck.equals("false"), 1);
                    start = Instant.now();

                    solution =  bfs.findSolution(this);
                    end = Instant.now();
                    timeElapsed = Duration.between(start, end);

                    break;

                case "DFS":
                    deadlockCheck = (String) jsonObject.get("deadlockCheck");
                    DFSStrategy dfs = new DFSStrategy(deadlockCheck != null && !deadlockCheck.equals("false"), 1);
                    start = Instant.now();
                    solution =  dfs.findSolution(this);
                    end = Instant.now();
                    timeElapsed = Duration.between(start, end);

                    break;

                case "IDDFS":
                    deadlockCheck = (String) jsonObject.get("deadlockCheck");
                    String maxIter = (String) jsonObject.get("maxIter");

                    if (maxIter == null){
                        System.out.println("Se debe especificar el limite de profundidad para IDDFS");
                        break;
                    }
                    IDDFSStrategy iddfs = new IDDFSStrategy(deadlockCheck != null && !deadlockCheck.equals("false"), Integer.parseInt(maxIter),1);
                    start = Instant.now();
                    solution =  iddfs.findSolution(this);
                    end = Instant.now();
                    timeElapsed = Duration.between(start, end);

                    break;

                case "IDA*":
                    heuristic = (String) jsonObject.get("heuristic");
                    if(heuristic == null){
                        System.out.println("Se debe especificar la heuristica a utilizar ['goalCount','SMD','MML']");

                    }else {
                        BiFunction<SearchStrategy.StateNode, Board, Integer> func;
                        if((func = Heuristics.heuristicsMap.get(heuristic)) == null){
                            System.out.println("Heuristic '+"+heuristic+"' doesn't exists");
                        }else{
                            IDAStarStrategy idaStar = new IDAStarStrategy(heuristic,func,1);
                            start = Instant.now();
                            solution =  idaStar.findSolution(this);
                            end = Instant.now();
                            timeElapsed = Duration.between(start, end);

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
                            GGSStrategy ggs = new GGSStrategy(heuristic,func,1);
                            start = Instant.now();
                            solution =  ggs.findSolution(this);
                            end = Instant.now();
                            timeElapsed = Duration.between(start, end);
                          
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
                            AStarStrategy aStar = new AStarStrategy(heuristic,func,1);
                            start = Instant.now();
                            solution =  aStar.findSolution(this);
                            end = Instant.now();
                            timeElapsed = Duration.between(start, end);

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

        } catch (IOException | ParseException e) {
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

            File myObj = new File("./level.txt");

            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                sb.append(data);
                sb.append('\n');
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void initWorld() {
       positions = new ArrayList<>();
        walls = new ArrayList<>();
        boxes = new HashSet<>();
        goals = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        ar.edu.itba.entities.Box b;
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
                    b = new ar.edu.itba.entities.Box(x, y);
                    boxes.add(b);
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
        repaint();
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
            if (key == KeyEvent.VK_R) {
                if (solution != null && !timer.isRunning()) {

                    restartLevel();
                }
            }

        }
    }

    private void printResult(Graphics g){
        if(solution != null){
            int space = 25;
            int i = 2;
            boolean found = solution.getGoalNode() != null;

            g.setColor(found ? Color.black:Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,12));
            g.drawString("Algoritmo: "+solution.getAlgorithm(),w + 5,15);

            if(solution.getHeuristicName() != null){
                g.drawString("Heuristica: "+solution.getHeuristicName(),w + 5,i++*space);
            }

            g.drawString("Nodos expandidos: "+solution.getExpandedNodes(),w + 5,i++*space);
            g.drawString("Nodos frontera al finalizar: "+solution.getFrontierNodes(),w + 5,i++*space);
            g.drawString("Profundidad alcanzada: "+solution.getGoalNode().getPathCost(),w + 5,i++*space);
            g.drawString("Costo total: "+solution.getTotalCost(),w + 5,i++*space);

            g.drawString("Tiempo de busqueda: "+(timeElapsed.toSeconds() > 1 ? timeElapsed.toSeconds()+"s":timeElapsed.toMillis())+"ms",w + 5,i++*space);
            g.setColor(found ? Color.BLUE:Color.RED);
            g.drawString(found ? "Solucion encontrada (Pulse 'S' para ver)":"Solucion no encontrada",w + 5,i++*space);



        }
    }


        private void buildWorld(Graphics g) {

        g.setColor(new Color(197, 131, 114));
        g.fillRect(0, 0, w, this.getHeight());
            g.setColor(Color.lightGray);
            g.fillRect(w, 0, this.getWidth()-w, this.getHeight());
        printResult(g);

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(goals);
        world.addAll(boxes);
        world.add(player);

            for (Actor item : world) {

                if (item instanceof Player || item instanceof ar.edu.itba.entities.Box) {

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

                  if (checkBoxCollision('L')) {
                      return;
                  }

                  player.move(-SPACE, 0);

                  break;

              case 'R':

                  if (checkWallCollision(player, 'R')) {
                      return;
                  }

                  if (checkBoxCollision('R')) {
                      return;
                  }

                  player.move(SPACE, 0);

                  break;

              case 'T':

                  if (checkWallCollision(player, 'T')) {
                      return;
                  }

                  if (checkBoxCollision('T')) {
                      return;
                  }

                  player.move(0, -SPACE);

                  break;

              case 'B':

                  if (checkWallCollision(player, 'B')) {
                      return;
                  }

                  if (checkBoxCollision('B')) {
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

    private boolean checkBoxCollision(char direction) {

        Iterator<ar.edu.itba.entities.Box> it1 = boxes.iterator();
        List<ar.edu.itba.entities.Box> toAdd = new ArrayList<>();
        switch (direction) {

            case 'L':


                while(it1.hasNext()) {
                    ar.edu.itba.entities.Box box = it1.next();
                    if (player.isLeftCollision(box)) {
                        for (ar.edu.itba.entities.Box item : boxes) {
                            {


                                if (!box.equals(item)) {

                                    if (box.isLeftCollision(item)) {
                                        return true;
                                    }
                                }

                                if (checkWallCollision(box, 'L')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        box.move(-SPACE,0 );
                        toAdd.add(box);
                        break;
                    }
                }
                boxes.addAll(toAdd);


                return false;

            case 'R':


                while(it1.hasNext()) {
                    ar.edu.itba.entities.Box box = it1.next();
                    if (player.isRightCollision(box)) {
                        for (ar.edu.itba.entities.Box item : boxes) {
                            {


                                if (!box.equals(item)) {

                                    if (box.isRightCollision(item)) {
                                        return true;
                                    }
                                }

                                if (checkWallCollision(box, 'R')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        box.move(SPACE,0 );
                        toAdd.add(box);
                        break;
                    }
                }
                boxes.addAll(toAdd);


                return false;

            case 'T':

                while(it1.hasNext()) {
                    ar.edu.itba.entities.Box box = it1.next();
                    if (player.isTopCollision(box)) {
                        for (ar.edu.itba.entities.Box item : boxes) {
                            {


                                if (!box.equals(item)) {

                                    if (box.isTopCollision(item)) {
                                        return true;
                                    }
                                }

                                if (checkWallCollision(box, 'T')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        box.move(0,-Board.SPACE );
                        toAdd.add(box);
                        break;
                    }
                }
                boxes.addAll(toAdd);


                return false;

            case 'B':


                while(it1.hasNext()) {
                    Box box = it1.next();
                    if (player.isBottomCollision(box)) {
                        for (ar.edu.itba.entities.Box item : boxes) {
                            {


                                if (!box.equals(item)) {

                                    if (box.isBottomCollision(item)) {
                                        return true;
                                    }
                                }

                                if (checkWallCollision(box, 'B')) {
                                    return true;
                                }
                            }

                        }
                        it1.remove();
                        box.move(0,Board.SPACE );
                        toAdd.add(box);
                        break;
                    }
                }
                boxes.addAll(toAdd);


                return false;


            default:
                break;
        }

        return false;
    }



    public boolean isCompleted(Set<Box> boxSet) {

        int nOfBoxes = boxSet.size();
       ar.edu.itba.entities.Box[] boxArray = boxSet.toArray(new ar.edu.itba.entities.Box[0]);
        int finishedBoxes = 0;

        for (int i = 0; i < nOfBoxes; i++) {
            
            ar.edu.itba.entities.Box bag = boxArray[i];
            
            for (int j = 0; j < nOfBoxes; j++) {
                
                Goal area =  goals.get(j);
                
                if (bag.getX() == area.getX() && bag.getY() == area.getY()) {

                    finishedBoxes += 1;
                }
            }
        }

        /* isCompleted = true;
            repaint();*/
        return finishedBoxes == nOfBoxes;
    }

    public void restartLevel() {

        goals.clear();
        boxes.clear();
        walls.clear();
        i = 0;
        initWorld();


    }

    public Set<Box> getBoxes() {
        return boxes;
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
