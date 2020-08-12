package ar.edu.itba;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel {

    public static final int OFFSET = 30;
    public static final int SPACE = 20;
    private List<Wall> walls;
    private Set<Baggage> baggs;
    private List<Area> areas;
    private String solution;
    private Player player;
    private int w = 0;
    private int h = 0;
    
    private boolean isCompleted = false;
    private int i = 0;
    private  Timer timer = new Timer(250, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(i< solution.length()){
                char key = solution.charAt(i++);

                movePlayer(key);
                repaint();

            }else{
                ((Timer)e.getSource()).stop();
            }


        }
    });

/*    private String level
            = "    ######\n"
            + "    ##   #\n"
            + "    ##$  #\n"
            + "  ####  $##\n"
            + "  ##  $ $ #\n"
            + "#### # ## #   ######\n"
            + "##   # ## #####  ..#\n"
            + "## $  $          ..#\n"
            + "###### ### #@##  ..#\n"
            + "    ##     #########\n"
            + "    ########\n";*/

//    private String level =      "      ###\n"+
//                                "      #.#\n"+
//                                "  #####.#####\n"+
//                                " ##         ##\n"+
//                                "##  # # # #  ##\n"+
//                                "#  ##     ##  #\n"+
//                                "# ##  # #  ## #\n"+
//                                "#     $@$     #\n"+
//                                "####  ###  ####\n"+
//                                "   #### ####\n";

 private String level =         "  ####    \n" +
                                " ##  ##   \n" +
                                "## $  ####\n" +
                                "#  ..$  @#\n" +
                                "# $..#   #\n" +
                                "#   $#####\n" +
                                "##   #    \n" +
                                " ##  #    \n" +
                                "  #### ";

// private String level =         "#########\n" +
//                                "#.$ @ $.#\n" +
//                                "#       #\n" +
//                                "#    ####\n" +
//                                "#.$   $.#\n" +
//                                "#########\n";


    public Board()  {

        initBoard();

    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
        BFSStrategy bfs = new BFSStrategy();
        try {
            solution =  bfs.findSolution(this);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

   /*     DFSStrategy dfs = new DFSStrategy();
        try {
            solution =  dfs.findSolution(this);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
*/

    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    private void initWorld() {
        
        walls = new ArrayList<>();
        baggs = new HashSet<>();
        areas = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        Baggage b;
        Area a;

        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

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
                    x += SPACE;
                    break;

                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b);
                    x += SPACE;
                    break;

                case '.':
                    a = new Area(x, y);
                    areas.add(a);
                    x += SPACE;
                    break;

                case '@':
                    player = new Player(x, y);
                    x += SPACE;
                    break;

                case ' ':
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
                if (!solution.isEmpty()) {

                    showSolution();
                }
            }

        }
    }

        private void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
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
                
                Area area =  areas.get(j);
                
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

        areas.clear();
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

    public List<Area> getAreas() {
        return areas;
    }
}
