package sample.Schatzsuche;

import javafx.scene.layout.VBox;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.*;

public class Fenster extends JFrame{
    static int color = 0;
    private final int HEIGHT = 600;
    private final int WIDTH = 600;



    public Fenster() {
        this.setTitle("FH-Games SCHATZSUCHE");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        int rahmenbreiteLR = this.getInsets().left + this.getInsets().right;
        int rahmenbreiteOU = this.getInsets().top + this.getInsets().bottom;
        this.setVisible(false);
        this.setSize(WIDTH + rahmenbreiteLR, HEIGHT + rahmenbreiteOU + 50);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.add(new Wand(WIDTH, HEIGHT));

        JTextArea anleitung = new JTextArea("Klicke mit der Maus um Steine zu entfernen oder zu plazieren.\n"
                + "Mit der Leertaste startest du die Schatzsuche.", 15, 2);
        anleitung.setBounds(0, HEIGHT, WIDTH, 50);


        anleitung.setEditable(false);
        panel.add(anleitung);
        this.add(panel);
        this.setVisible(true);
    }
}
