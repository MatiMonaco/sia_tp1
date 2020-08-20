package ar.edu.itba.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Goal extends Actor {

    public Goal(int x, int y) {
        super(x, y);
        
        initArea();
    }
    
    private void initArea() {

        ImageIcon iicon = new ImageIcon(getClass().getClassLoader().getResource("area.png"));
        Image image = iicon.getImage();
        setImage(image);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
