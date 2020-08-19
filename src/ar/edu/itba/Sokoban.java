package ar.edu.itba;

import java.awt.EventQueue;
import java.util.*;
import javax.swing.JFrame;

public class Sokoban extends JFrame {

    private final int OFFSET = 15;

    public Sokoban() {

        initUI("IDDFS");
    }

    private void initUI(String chosenAlgorithm) {
        
        Board board = new Board(chosenAlgorithm);
        add(board);

        setTitle("Sokoban - "+chosenAlgorithm);
        
        setSize(board.getBoardWidth() +OFFSET,
                board.getBoardHeight() +3* OFFSET);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {


        EventQueue.invokeLater(() -> {

            Sokoban game = new Sokoban();
            game.setVisible(true);
        });

 // set.sort((o1, o2) -> o1.getX() > o2.getX() ? 1: o1.getX() == o2.getX() ? o1.getY() > o2.getY() ? 1 : -1 : -1);



    }




}
