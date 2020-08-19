package ar.edu.itba;

import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;

public class Baggage extends Actor {

    public Baggage(int x, int y) {
        super(x, y);
        
        initBaggage();
    }
    
    private void initBaggage() {
        
        ImageIcon iicon = new ImageIcon(getClass().getClassLoader().getResource("baggage.png"));
        Image image = iicon.getImage();
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
