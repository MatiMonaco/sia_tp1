package ar.edu.itba;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Goal extends Actor {

    public Goal(int x, int y) {
        super(x, y);
        
        initArea();
    }
    
    private void initArea() {

        ImageIcon iicon = new ImageIcon("src/resources/area.png");
        Image image = iicon.getImage();
        setImage(image);
    }
}
