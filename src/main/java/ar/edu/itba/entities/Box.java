package ar.edu.itba.entities;

import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;

public class Box extends Actor {

    public Box(int x, int y) {
        super(x, y);
        
        initBox();
    }
    
    private void initBox() {
        
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("box.png"));
        Image image = icon.getImage();
        setImage(image);
    }

    public void move(int x, int y) {
        
        int dx = getX() + x;
        int dy = getY() + y;
        
        setX(dx);
        setY(dy);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isInGoal(List<Goal> areas) {
        for (Goal a: areas) {
            if ((getX() == a.getX() && getY() == a.getY())){

                return true;
            }
        }

        return false;
    }


}
