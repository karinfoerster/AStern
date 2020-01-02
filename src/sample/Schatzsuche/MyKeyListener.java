package sample.Schatzsuche;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MyKeyListener implements KeyListener{
    Wand wand;

    public MyKeyListener(Wand w) {
        wand = w;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
            System.exit(0);
        }

        if (KeyEvent.VK_SPACE == e.getKeyCode()) {
            wand.suche = true;
            wand.reset();
            wand.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}