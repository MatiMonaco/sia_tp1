package ar.edu.itba;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JPanel;

public class Board extends JPanel {

    public static final int OFFSET = 30;
    public static final int SPACE = 20;
    public static final int LEFT_COLLISION = 1;
    public static final int RIGHT_COLLISION = 2;
    public static final int TOP_COLLISION = 3;
    public static final int BOTTOM_COLLISION = 4;

    private List<Wall> walls;
    private Set<Baggage> baggs;
    private List<Area> areas;
    
    private Player soko;
    private int w = 0;
    private int h = 0;
    
    private boolean isCompleted = false;

    private String level
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
            + "    ########\n";

    public Board()  {

        initBoard();
        BFSStrategy bfs = new BFSStrategy();
        try {
            bfs.findSolution(this);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void initBoard() {

       // addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
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
                    soko = new Player(x, y);
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

    private void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(soko);

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);

            if (item instanceof Player || item instanceof Baggage) {
                
                g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, this);
            } else {
                
                g.drawImage(item.getImage(), item.getX(), item.getY(), this);
            }

            if (isCompleted) {
                
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
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

        if (finishedBags == nOfBags) {

            return true;
           /* isCompleted = true;
            repaint();*/
        }
        return false;
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
        return soko;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Area> getAreas() {
        return areas;
    }
}
