package ar.edu.itba;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Sokoban extends JFrame {

    private final int OFFSET = 60;

    public Sokoban() {
        initUI();
    }



    private void initUI() {
        
        Board board = new Board();
        add(board);
        setTitle("Sokoban");
        setSize(board.getBoardWidth() +250,
                board.getBoardHeight() +OFFSET+20);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {


        EventQueue.invokeLater(() -> {

            Sokoban game = null;
            game = new Sokoban();
            game.setVisible(true);
        });

 // set.sort((o1, o2) -> o1.getX() > o2.getX() ? 1: o1.getX() == o2.getX() ? o1.getY() > o2.getY() ? 1 : -1 : -1);



    }




}
