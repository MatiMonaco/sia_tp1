package ar.edu.itba;

import java.awt.EventQueue;
import java.util.Set;
import javax.swing.JFrame;

public class Sokoban extends JFrame {

    private final int OFFSET = 30;

    public Sokoban() {

        initUI();
    }

    private void initUI() {
        
        Board board = new Board();
        add(board);

        setTitle("Sokoban");
        
        setSize(board.getBoardWidth() + OFFSET,
                board.getBoardHeight() + 2 * OFFSET);
        
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



    public void caca(Set<Integer>asd){
        Integer[] array = asd.toArray(new Integer[0]);
        array[0] = 1;
    }
}
