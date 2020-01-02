package sample.Schatzsuche;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MyMouseListener implements MouseListener {

    Wand wand;

    MyMouseListener(Wand w) {
        this.wand = w;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / this.wand.ratio;
        int y = e.getY() / this.wand.ratio;
        this.wand.changeFieldAtPos(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
    

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void setWand(Wand w) {
        this.wand = w;
    }


}
